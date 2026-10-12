package com.example.myproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * 桌面小组件（四种尺寸）。
 *
 * 全部共用同一份数据与同一套筛选逻辑，只是展示粒度不同：
 *   ScheduleWidgetProvider       4×2   今天全部课程（最多 6 行，多了折叠）
 *   ScheduleWidgetMediumProvider 2×2   今天前 3 门
 *   ScheduleWidgetShortProvider  2×1   第几周 + 今天第一门
 *   ScheduleWidgetTinyProvider   1×1   最近一节课（节次 + 课名）
 *
 * 设计取舍：
 * 1. 只用系统自带的 RemoteViews（不用 Glance），不新增任何依赖，也不用动 module.toml。
 * 2. 数据直接复用 App 自己的 SharedPreferences（同一个包、同一个进程），
 *    调用现成的 loadCoursesFromLocal / loadStartDate，不建数据库、不要存储权限，
 *    筛选逻辑与主界面表格是同一句表达式，所以桌面和界面永远一致。
 * 3. 配色复用 App 里的 getCourseColor()，某门课在桌面和主界面是同一个颜色。
 * 4. 布局根节点都是 match_parent，卡片永远占满格子（不会因为课少就缩成一小条）；
 *    顶部对齐、底部用一条撑杆把剩余空间吃掉。
 *
 * 刷新时机：
 * · 系统每 30 分钟自动刷一次（updatePeriodMillis 的最小值）
 * · 组件被添加到桌面 / 被系统重建时（onUpdate）
 * · App 里课表变化时、以及 App 启动时（saveToLocal / onCreate 里调用）
 */
class ScheduleWidgetProvider : AppWidgetProvider() {
    override fun onReceive(context: Context, intent: Intent) {
        // 「下课闹钟」到点了：重画一次组件（同时会安排下一节课的闹钟）
        if (intent.action == ACTION_WIDGET_TICK) {
            refreshAll(context)
            return
        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (id in appWidgetIds) appWidgetManager.updateAppWidget(id, buildFullViews(context, id))
        // 预算好「下一节课下课」的时刻，到点自动重画（不然要等系统 30 分钟的周期）
        scheduleNextRefresh(context)
    }

    companion object {
        /** 刷新所有尺寸的组件；一个都没添加过就什么都不做。 */
        fun refreshScheduleWidgets(context: Context) {
            refreshAll(context)
        }

        /** 只重画一次：拖动滑块这种「每一下都要立刻生效」的场合用，不做延迟补刷 */
        fun refreshScheduleWidgetsQuick(context: Context) {
            drawAllWidgets(context.applicationContext)
        }
    }
}

/** 2×2：今天前 3 门 */
class ScheduleWidgetMediumProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (id in appWidgetIds) appWidgetManager.updateAppWidget(id, buildMediumViews(context, id))
        // 预算好「下一节课下课」的时刻，到点自动重画（不然要等系统 30 分钟的周期）
        scheduleNextRefresh(context)
    }
}

/** 2×1：第几周 + 今天第一门 */
class ScheduleWidgetShortProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (id in appWidgetIds) appWidgetManager.updateAppWidget(id, buildShortViews(context, id))
        // 预算好「下一节课下课」的时刻，到点自动重画（不然要等系统 30 分钟的周期）
        scheduleNextRefresh(context)
    }
}

/** 1×1：最近一节课 */
class ScheduleWidgetTinyProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (id in appWidgetIds) appWidgetManager.updateAppWidget(id, buildTinyViews(context, id))
        // 预算好「下一节课下课」的时刻，到点自动重画（不然要等系统 30 分钟的周期）
        scheduleNextRefresh(context)
    }
}

// ==================== 下面都是四种尺寸共用的部分 ====================

private val WEEK_CN = listOf("一", "二", "三", "四", "五", "六", "日")
private const val TRANSPARENT = 0x00000000

private val FULL_ROW_IDS = intArrayOf(
    R.id.widget_row_1, R.id.widget_row_2, R.id.widget_row_3,
    R.id.widget_row_4, R.id.widget_row_5, R.id.widget_row_6
)
private val FULL_TIME_IDS = intArrayOf(
    R.id.widget_time_1, R.id.widget_time_2, R.id.widget_time_3,
    R.id.widget_time_4, R.id.widget_time_5, R.id.widget_time_6
)
private val FULL_TEXT_IDS = intArrayOf(
    R.id.widget_text_1, R.id.widget_text_2, R.id.widget_text_3,
    R.id.widget_text_4, R.id.widget_text_5, R.id.widget_text_6
)

private val FULL_INFO_IDS = intArrayOf(
    R.id.widget_info_1, R.id.widget_info_2, R.id.widget_info_3,
    R.id.widget_info_4, R.id.widget_info_5, R.id.widget_info_6
)

private val FULL_CELL_IDS = intArrayOf(
    R.id.widget_cell_1, R.id.widget_cell_2, R.id.widget_cell_3,
    R.id.widget_cell_4, R.id.widget_cell_5, R.id.widget_cell_6
)

/** 四行布局里那条「蓝线」的 id（原来是没 id 的 TextView，本轮才加的） */
private val FULL_LINE_IDS = intArrayOf(
    R.id.widget_line_1, R.id.widget_line_2, R.id.widget_line_3,
    R.id.widget_line_4, R.id.widget_line_5, R.id.widget_line_6
)

/** 每一行里装文字的那一层（行高靠调它的内边距来实现） */
private val FULL_ROWIN_IDS = intArrayOf(
    R.id.widget_rowin_1, R.id.widget_rowin_2, R.id.widget_rowin_3,
    R.id.widget_rowin_4, R.id.widget_rowin_5, R.id.widget_rowin_6
)

/**
 * 20 张圆角格子底图，顺序与 App 的 colorPalette 一一对应
 * （下标就是 courseColorIndex(name) 的返回值）。
 */
private val CELL_DRAWABLES = intArrayOf(
    R.drawable.widget_cell_c0, R.drawable.widget_cell_c1, R.drawable.widget_cell_c2,
    R.drawable.widget_cell_c3, R.drawable.widget_cell_c4, R.drawable.widget_cell_c5,
    R.drawable.widget_cell_c6, R.drawable.widget_cell_c7, R.drawable.widget_cell_c8,
    R.drawable.widget_cell_c9, R.drawable.widget_cell_c10, R.drawable.widget_cell_c11,
    R.drawable.widget_cell_c12, R.drawable.widget_cell_c13, R.drawable.widget_cell_c14,
    R.drawable.widget_cell_c15, R.drawable.widget_cell_c16, R.drawable.widget_cell_c17,
    R.drawable.widget_cell_c18, R.drawable.widget_cell_c19
)

private val MEDIUM_ROW_IDS = intArrayOf(
    R.id.widget2_row_1, R.id.widget2_row_2, R.id.widget2_row_3
)
private val MEDIUM_TIME_IDS = intArrayOf(
    R.id.widget2_time_1, R.id.widget2_time_2, R.id.widget2_time_3
)
private val MEDIUM_TEXT_IDS = intArrayOf(
    R.id.widget2_text_1, R.id.widget2_text_2, R.id.widget2_text_3
)

private val MEDIUM_INFO_IDS = intArrayOf(
    R.id.widget2_info_1, R.id.widget2_info_2, R.id.widget2_info_3
)

private val MEDIUM_CELL_IDS = intArrayOf(
    R.id.widget2_cell_1, R.id.widget2_cell_2, R.id.widget2_cell_3
)

private val MEDIUM_LINE_IDS = intArrayOf(
    R.id.widget2_line_1, R.id.widget2_line_2, R.id.widget2_line_3
)

private val MEDIUM_ROWIN_IDS = intArrayOf(
    R.id.widget2_rowin_1, R.id.widget2_rowin_2, R.id.widget2_rowin_3
)

/**
 * 一行的内边距（dp，值与布局 XML 里写的一致）。
 * 字号放大时按同一倍率一起放大，行与行之间的高度就跟着字变大了。
 */
private data class RowPad(
    val viewId: Int,
    val leftDp: Float,
    val topDp: Float,
    val rightDp: Float,
    val bottomDp: Float
)

private val FULL_ROW_PADS: List<RowPad> = FULL_ROWIN_IDS.map { RowPad(it, 6f, 3f, 6f, 3f) }
private val MEDIUM_ROW_PADS: List<RowPad> = MEDIUM_ROWIN_IDS.map { RowPad(it, 4f, 2f, 4f, 2f) }
private val SHORT_ROOT_PAD: List<RowPad> = listOf(RowPad(R.id.widget1_root, 10f, 4f, 10f, 4f))
private val TINY_ROOT_PAD: List<RowPad> = listOf(RowPad(R.id.widget0_root, 4f, 4f, 4f, 4f))

/**
 * 某个尺寸要展示的「那一天」。
 *
 * 规则（按“上没上完”来定，而不是只看有没有课）：
 *   ① 今天的课还没上完 → 显示今天，只列「还没上完的课」（上过的课不再占位置）；
 *   ② 今天的课全上完了 → 显示明天的课，当作「明日课程预告」；
 *   ③ 明天也没课 → 不列课程，只留一句「今日课程已上完」。
 *
 * 第 49 批（按设计稿）：上面这套规则只管「课程那一块」。
 * 顶上的「周数」「日期」两个格子永远显示**今天**，不跟着课程一起跳到明天
 * （设计稿原文：周数只显示今天的周数 / 日期只显示今天日期，不要提前显示明天有课的）。
 * 所以下面额外带了一份「今天」的信息给顶栏用。
 */
private data class DayPlan(
    val hasData: Boolean,   // 本地有没有课表数据
    val isToday: Boolean,   // 展出的课程是不是今天的
    val allDone: Boolean,   // 今天本来有课、但都已经上完了
    val date: LocalDate,    // 课程那一块展示的那一天（可能是明天）
    val weekNo: Int,        // 那一天是第几周
    val courses: List<Course>,
    val todayDate: LocalDate, // 今天（顶栏「日期」永远用它）
    val todayWeekNo: Int      // 今天第几周（顶栏「周数」永远用它）
)

/**
 * 今天是不是已经到了「没课可上」的状态。
 *
 * 只看课程的结束时间：结束时间早于此刻就算上完了。
 * 取不到这节课的时间（用户没配节次时间 / 节次超出范围）时返回 false —— 当作还没上完，
 * 宁可多显示一门，也不要把课漏掉。
 */
private fun alreadyFinished(cfg: TimeConfig, c: Course): Boolean {
    val slot = cfg.times.getOrNull(c.endSection - 1) ?: return false
    val end = parseSlot(slot)?.second ?: return false
    val now = java.time.LocalTime.now()
    return now.hour * 60 + now.minute >= end
}

private fun planFor(context: Context): DayPlan {
    val startDate = loadStartDate(context)
    val today = LocalDate.now()
    val all = loadCoursesFromLocal(context)
    val cfg = loadTimeConfig(context, loadCurrentBookId(context))

    // ① 今天还有没上完的课：就用今天，并且只留「还没上完」的那些
    val todayWeek = weekOf(startDate, today)
    val todayCourses = coursesOn(all, today, todayWeek)
    val remaining = todayCourses.filter { !alreadyFinished(cfg, it) }
    if (remaining.isNotEmpty()) {
        return DayPlan(
            all.isNotEmpty(), true, false, today, todayWeek, remaining, today, todayWeek
        )
    }

    // ② 今天的课上完了（或今天本来就没课）：看明天有没有课，当作「明日课程预告」
    val tomorrow = today.plusDays(1L)
    val tomorrowWeek = weekOf(startDate, tomorrow)
    val tomorrowCourses = coursesOn(all, tomorrow, tomorrowWeek)
    if (tomorrowCourses.isNotEmpty()) {
        return DayPlan(
            all.isNotEmpty(), false, todayCourses.isNotEmpty(), tomorrow, tomorrowWeek,
            tomorrowCourses, today, todayWeek
        )
    }

    // ③ 明天也没课：不列课程，只留一句「今日课程已上完」
    return DayPlan(
        all.isNotEmpty(), false, todayCourses.isNotEmpty(), today, todayWeek,
        emptyList(), today, todayWeek
    )
}

/** 某一天有哪些课：与主界面表格同一套筛选逻辑（星期对得上 + 该周在课程的周次范围内） */
private fun coursesOn(all: List<Course>, date: LocalDate, weekNo: Int): List<Course> {
    val cn = WEEK_CN[date.dayOfWeek.value - 1]
    return all
        .filter { it.weekCn == cn && weekNo in it.weekRangeList }
        .sortedWith(compareBy({ it.startSection }, { it.endSection }))
}

/** 算某个日期是第几周（与 calcCurrentWeek 同一算法，只是允许传任意日期） */
private fun weekOf(startDateStr: String, date: LocalDate): Int = try {
    val start = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val diff = ChronoUnit.DAYS.between(start, date)
    if (diff < 0) 1 else (diff / 7).toInt() + 1
} catch (_: Exception) {
    1
}

private fun dayLabel(date: LocalDate): String = "周${WEEK_CN[date.dayOfWeek.value - 1]}"

private fun shortDate(date: LocalDate): String = "${date.monthValue}/${date.dayOfMonth}"

/**
 * 需要额外说明时返回一句话，正常显示课程时返回 null（那行就不占位置）
 */
private fun hintText(plan: DayPlan): String? = when {
    !plan.hasData -> "还没有课表数据：打开 App 导入或从教务获取"
    plan.courses.isNotEmpty() && !plan.isToday ->
        if (plan.allDone) "今日已上完｜明日课程预告"
        else "今天没课 🎉｜明天 " + dayLabel(plan.date) + " " + shortDate(plan.date)
    plan.courses.isEmpty() && plan.allDone -> "今日课程已上完 🎉"
    plan.courses.isEmpty() -> "今天没课 🎉"
    else -> null
}

private fun openAppIntent(context: Context): PendingIntent {
    val intent = Intent(context, MainActivity::class.java)
    val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    return PendingIntent.getActivity(context, 0, intent, flags)
}

/** 把课程填进门面完全相同的若干行里（4×2 用 6 行，2×2 用 3 行） */
private fun fillRows(
    views: RemoteViews,
    courses: List<Course>,
    cfg: TimeConfig,
    cellAlpha: Float,
    rowIds: IntArray,
    cellIds: IntArray,
    timeIds: IntArray,
    nameIds: IntArray,
    infoIds: IntArray,
cellTint: Int
) {
    val max = rowIds.size
    val overflow = courses.size > max
    val normalRows = if (overflow) max - 1 else courses.size

    for (i in 0 until max) {
        val isOverflowRow = overflow && i == max - 1
        if (i >= normalRows && !isOverflowRow) {
            views.setViewVisibility(rowIds[i], View.GONE)
            continue
        }
        views.setViewVisibility(rowIds[i], View.VISIBLE)

        if (isOverflowRow) {
            views.setTextViewText(timeIds[i], "")
            views.setTextViewText(nameIds[i], "…等共 ${courses.size} 门课")
            views.setTextViewText(infoIds[i], "")
            views.setViewVisibility(cellIds[i], View.GONE)
        } else {
            val c = courses[i]
            // 左边一列：上一行写起始时间、下一行写结束时间（没配时间就退回写节次）
            views.setTextViewText(timeIds[i], courseTimeText(cfg, c))
            // 右边两行：课程名 / 节数｜教室｜教师
            views.setTextViewText(nameIds[i], c.name)
            views.setTextViewText(infoIds[i], courseInfoLine(c))
            // 课程格子：四角圆角的底图。
            // setImageViewResource / setFloat("setAlpha") 都是系统允许小组件用的方法；
            // 用底图而不是 setBackgroundColor，就是为了要圆角；透明度只作用在底图上，文字不受影响。
            views.setViewVisibility(cellIds[i], View.VISIBLE)
            views.setImageViewResource(cellIds[i], CELL_DRAWABLES[courseColorIdx(c)])
            // 格子颜色：存的是 ARGB；0x00000000（原色）这个滤镜等于不生效，
            // 所以直接把底图原样透出；其它颜色则把整块圆角底图换成用户挑的色。
            views.setInt(cellIds[i], "setColorFilter", cellTint)
            views.setFloat(cellIds[i], "setAlpha", cellAlpha)
        }
    }
}

/** 课表格子颜色：存的是 ARGB（0 = 原色，不染色） */
private fun loadCellTint(context: Context, appWidgetId: Int): Int =
    loadWidgetColorForId(context, appWidgetId, WIDGET_COLOR_CELL, WIDGET_CELL_COLOR_AUTO)

/** 一个可以被「小部件颜色」改色的文字控件：控件 id + 属于哪一类 + 它在布局里的默认色 */
private data class ColorTarget(val viewId: Int, val slot: String, val def: Int)

private fun ct(viewId: Int, slot: String, def: Int): ColorTarget = ColorTarget(viewId, slot, def)

/**
 * 背景的平均亮度（0 = 黑，1 = 白）。只有「自定义背景图」这一种模式才需要算：
 * 白卡算 1（全亮），透明模式不参与判断（当作亮，保留用户自己挑的颜色）。
 * 采样的是缓存里的那张源图，一次刷新只多几百次取像素，可以忽略。
 */
private fun widgetBgLuma(context: Context, appWidgetId: Int): Float {
    if (loadWidgetBgModeForId(context, appWidgetId) != WIDGET_BG_IMAGE) return 1f
    val path = loadWidgetBgImageForId(context, appWidgetId)
    if (path.isBlank()) return 1f
    val src = loadBgSource(path) ?: return 1f
    return try {
        val stepX = (src.width / 24).coerceAtLeast(1)
        val stepY = (src.height / 24).coerceAtLeast(1)
        var sum = 0.0
        var n = 0
        var y = 0
        while (y < src.height) {
            var x = 0
            while (x < src.width) {
                val c = src.getPixel(x, y)
                val r = (c shr 16 and 0xFF) / 255f
                val g = (c shr 8 and 0xFF) / 255f
                val b = (c and 0xFF) / 255f
                sum += (0.299 * r + 0.587 * g + 0.114 * b).toDouble()
                n++
                x += stepX
            }
            y += stepY
        }
        if (n == 0) 1f else (sum / n).toFloat()
    } catch (_: Throwable) {
        1f
    }
}

/**
 * 第 44 批新增：深色背景图上的「自动提亮」。
 * 背景平均亮度低于 0.5 时，把本身就很暗的字往白里兑一点，保证桌面上看得见；
 * 背景是亮色 / 白卡 / 透明时原样返回 —— 用户挑什么色就是什么色。
 * （起因：① 周数 是纯黑，配上深色背景图后桌面「第 5 周」几乎看不见。）
 */
private fun readableOnBg(color: Int, bgLuma: Float): Int {
    if (bgLuma >= 0.5f) return color
    val a = color ushr 24 and 0xFF
    val r = color shr 16 and 0xFF
    val g = color shr 8 and 0xFF
    val b = color and 0xFF
    val luma = (0.299f * r + 0.587f * g + 0.114f * b) / 255f
    if (luma >= 0.55f) return color
    val k = ((0.8f - luma) / (1f - luma)).coerceIn(0f, 1f)
    val nr = (r + (255 - r) * k).toInt().coerceIn(0, 255)
    val ng = (g + (255 - g) * k).toInt().coerceIn(0, 255)
    val nb = (b + (255 - b) * k).toInt().coerceIn(0, 255)
    return (a shl 24) or (nr shl 16) or (ng shl 8) or nb
}

/**
 * 把「小部件颜色」里设的颜色写进 RemoteViews。
 * 没设过的项读出来就是它自己的默认色，写一遍等于白写，外观不会有任何变化。
 * 那条「蓝线」本身就是个 TextView，所以直接改它的背景色。
 * 第 44 批起：自定义背景图偏暗时，先把太暗的字往白里提一提（见 readableOnBg）。
 */
private fun applyWidgetColors(
    views: RemoteViews,
    context: Context,
    appWidgetId: Int,
    targets: List<ColorTarget>,
    lineIds: IntArray
) {
    // 深色背景图上自动把太暗的字提亮；背景亮 / 白卡 / 透明就不动
    val bgLuma = widgetBgLuma(context, appWidgetId)
    // 第 45 批：「统一颜色」打开时，所有文字与线都用同一个颜色
    val uniformColor = if (loadWidgetUniformForId(context, appWidgetId))
        loadWidgetUniformColorForId(context, appWidgetId) else null
    for (t in targets) {
        val base = uniformColor ?: loadWidgetColorForId(context, appWidgetId, t.slot, t.def)
        views.setTextColor(
            t.viewId,
            readableOnBg(base, bgLuma)
        )
    }
    val divider = uniformColor
        ?: loadWidgetColorForId(context, appWidgetId, WIDGET_COLOR_DIVIDER, WIDGET_DEF_DIVIDER)
    for (id in lineIds) views.setInt(id, "setBackgroundColor", divider)
}

/** 4×2：周数 / 日期 / 每行时间 / 课名 / 其他（节数·教室·教师，含「今天没课」「点一下打开课表」） */
private val FULL_COLOR_TARGETS: List<ColorTarget> =
    listOf(
        ct(R.id.widget_title, WIDGET_COLOR_TITLE, WIDGET_DEF_TITLE),
        ct(R.id.widget_date, WIDGET_COLOR_DATE, WIDGET_DEF_DATE),
        ct(R.id.widget_empty, WIDGET_COLOR_HINT, WIDGET_DEF_HINT),
        ct(R.id.widget_footer, WIDGET_COLOR_HINT, WIDGET_DEF_HINT)
    ) +
            FULL_TIME_IDS.map { ct(it, WIDGET_COLOR_TIME, WIDGET_DEF_TIME) } +
            FULL_TEXT_IDS.map { ct(it, WIDGET_COLOR_NAME, WIDGET_DEF_NAME) } +
            FULL_INFO_IDS.map { ct(it, WIDGET_COLOR_INFO, WIDGET_DEF_INFO) }

/** 2×2：同上，只是 3 行 */
private val MEDIUM_COLOR_TARGETS: List<ColorTarget> =
    listOf(
        ct(R.id.widget2_title, WIDGET_COLOR_TITLE, WIDGET_DEF_TITLE),
        ct(R.id.widget2_date, WIDGET_COLOR_DATE, WIDGET_DEF_DATE),
        ct(R.id.widget2_empty, WIDGET_COLOR_HINT, WIDGET_DEF_HINT),
        ct(R.id.widget2_footer, WIDGET_COLOR_HINT, WIDGET_DEF_HINT)
    ) +
            MEDIUM_TIME_IDS.map { ct(it, WIDGET_COLOR_TIME, WIDGET_DEF_TIME) } +
            MEDIUM_TEXT_IDS.map { ct(it, WIDGET_COLOR_NAME, WIDGET_DEF_NAME) } +
            MEDIUM_INFO_IDS.map { ct(it, WIDGET_COLOR_INFO, WIDGET_DEF_INFO) }

/** 2×1：只有一行 */
private val SHORT_COLOR_TARGETS: List<ColorTarget> = listOf(
    ct(R.id.widget1_title, WIDGET_COLOR_TITLE, WIDGET_DEF_TITLE),
    ct(R.id.widget1_date, WIDGET_COLOR_DATE, WIDGET_DEF_DATE),
    ct(R.id.widget1_msg, WIDGET_COLOR_HINT, WIDGET_DEF_HINT),
    ct(R.id.widget1_time, WIDGET_COLOR_TIME, WIDGET_DEF_TIME),
    ct(R.id.widget1_text, WIDGET_COLOR_NAME, WIDGET_DEF_NAME),
    ct(R.id.widget1_info, WIDGET_COLOR_INFO, WIDGET_DEF_INFO)
)

/** 1×1：顶行跟着日期色，中间那行（节数）跟着「其他」，下面课名跟着课名色 */
private val TINY_COLOR_TARGETS: List<ColorTarget> = listOf(
    ct(R.id.widget0_top, WIDGET_COLOR_DATE, WIDGET_DEF_DATE),
    ct(R.id.widget0_main, WIDGET_COLOR_INFO, WIDGET_DEF_NAME),
    ct(R.id.widget0_sub, WIDGET_COLOR_NAME, WIDGET_DEF_FADED)
)


/** 左边那一列时间：上「起始」下「结束」两行 */
private fun courseTimeText(cfg: TimeConfig, c: Course): String {
    val p = courseClock(cfg, c)
    return if (p.first.isBlank() || p.second.isBlank()) {
        "${c.startSection}-${c.endSection}节"
    } else {
        p.first + "\n" + p.second
    }
}

/** 取这门课的（起始时间, 结束时间），形如 "8:00" / "8:40"；取不到就给空串（退回写节次） */
private fun courseClock(cfg: TimeConfig, c: Course): Pair<String, String> {
    val startSlot = cfg.times.getOrNull(c.startSection - 1) ?: return "" to ""
    val endSlot = cfg.times.getOrNull(c.endSection - 1) ?: return "" to ""
    val si = startSlot.indexOf('-')
    val ei = endSlot.indexOf('-')
    val a = if (si > 0) startSlot.substring(0, si) else ""
    val b = if (ei > 0) endSlot.substring(ei + 1) else ""
    return shortClock(a) to shortClock(b)
}

/** 右边第二行：第几节｜教室｜教师（哪一项没有就不写） */
private fun courseInfoLine(c: Course): String {
    val parts = mutableListOf<String>()
    parts.add("第${c.startSection}-${c.endSection}节")
    if (c.location.isNotBlank()) parts.add(c.location)
    if (c.teacher.isNotBlank()) parts.add(c.teacher)
    return parts.joinToString("｜")
}

// ==================== 字体缩放（用户在设置里按尺寸调） ====================

/**
 * 字号 = 布局 XML 里的基础字号 × 用户倍率。
 * RemoteViews.setTextViewTextSize 是系统明确允许小组件用的方法；
 * 单位用 COMPLEX_UNIT_SP，与布局里的 sp 保持一致。
 * 倍率是 1.0 时也照样写一遍：这样「重置」一定能回到布局默认值，
 * 不会出现「调完回不去 / 看着没变化」的情况。
 */
private fun applyFontScale(views: RemoteViews, scale: Float, items: List<Pair<Int, Float>>) {
    for ((id, baseSp) in items) {
        views.setTextViewTextSize(id, TypedValue.COMPLEX_UNIT_SP, baseSp * scale)
    }
}

/**
 * 行高跟着字号一起放大。
 * RemoteViews 没有 setLineSpacing 这类接口，但 setViewPadding 是系统允许的方法：
 * 把每一行（1×1 / 2×1 就是整块）的上下内边距按同一倍率放大，
 * 字变大之后行与行之间也不会挤在一起。
 * 注意 setViewPadding 的单位是 px，所以这里用屏幕密度把 dp 换算过去。
 */
private fun applyRowSpacing(
    views: RemoteViews,
    scale: Float,
    context: Context,
    pads: List<RowPad>
) {
    val density = context.resources.displayMetrics.density
    for (p in pads) {
        views.setViewPadding(
            p.viewId,
            Math.round(p.leftDp * scale * density),
            Math.round(p.topDp * scale * density),
            Math.round(p.rightDp * scale * density),
            Math.round(p.bottomDp * scale * density)
        )
    }
}


// ==================== 各尺寸的渲染 ====================

/** 4×2：今天全部（最多 6 行） */
private fun buildFullViews(context: Context, appWidgetId: Int): RemoteViews {
    val views = RemoteViews(context.packageName, R.layout.widget_schedule)
    val plan = planFor(context)
    val cfg = loadTimeConfig(context, loadCurrentBookId(context))

    // 第 49 批：顶栏「周数」「日期」永远显示今天（课程区可能已是明日预告，顶栏不跟着跳）
    views.setTextViewText(R.id.widget_title, "第 ${plan.todayWeekNo} 周")
    views.setTextViewText(
        R.id.widget_date,
        "${dayLabel(plan.todayDate)} ${shortDate(plan.todayDate)}"
    )

    val hint = hintText(plan)
    if (hint == null) {
        views.setViewVisibility(R.id.widget_empty, View.GONE)
    } else {
        views.setViewVisibility(R.id.widget_empty, View.VISIBLE)
        views.setTextViewText(R.id.widget_empty, hint)
    }

    val cellAlpha = loadWidgetCellAlphaForId(context, appWidgetId)
    fillRows(
        views, plan.courses, cfg, cellAlpha,
        FULL_ROW_IDS, FULL_CELL_IDS, FULL_TIME_IDS, FULL_TEXT_IDS, FULL_INFO_IDS,
        loadCellTint(context, appWidgetId)
    )

    // 「小部件颜色」里设的颜色（没设过的项就是布局里的默认色）
    applyWidgetColors(views, context, appWidgetId, FULL_COLOR_TARGETS, FULL_LINE_IDS)
    // 字体缩放（基础字号见 widget_schedule.xml），行高同步跟着放大
    val scale = loadWidgetFontScaleForId(context, WIDGET_KEY_FULL, appWidgetId)
    applyFontScale(
        views, scale,
        listOf(
            R.id.widget_title to 13f,
            R.id.widget_date to 12f,
            R.id.widget_empty to 13f
        ) + FULL_TIME_IDS.map { it to 11f } + FULL_TEXT_IDS.map { it to 13f } +
                FULL_INFO_IDS.map { it to 10f }
    )
    applyRowSpacing(views, scale, context, FULL_ROW_PADS)
    applyWidgetBackground(context, views, appWidgetId)
    views.setOnClickPendingIntent(R.id.widget_root, openAppIntent(context))
    return views
}

/** 2×2：今天前 3 门 */
private fun buildMediumViews(context: Context, appWidgetId: Int): RemoteViews {
    val views = RemoteViews(context.packageName, R.layout.widget_schedule_medium)
    val plan = planFor(context)
    val cfg = loadTimeConfig(context, loadCurrentBookId(context))

    // 第 49 批：顶栏同 4×2，周数与日期只显示今天
    views.setTextViewText(R.id.widget2_title, "第 ${plan.todayWeekNo} 周")
    views.setTextViewText(
        R.id.widget2_date,
        "${dayLabel(plan.todayDate)} ${shortDate(plan.todayDate)}"
    )

    val hint = hintText(plan)
    if (hint == null) {
        views.setViewVisibility(R.id.widget2_empty, View.GONE)
    } else {
        views.setViewVisibility(R.id.widget2_empty, View.VISIBLE)
        views.setTextViewText(R.id.widget2_empty, hint)
    }

    val cellAlpha = loadWidgetCellAlphaForId(context, appWidgetId)
    fillRows(
        views, plan.courses, cfg, cellAlpha,
        MEDIUM_ROW_IDS, MEDIUM_CELL_IDS, MEDIUM_TIME_IDS, MEDIUM_TEXT_IDS, MEDIUM_INFO_IDS,
        loadCellTint(context, appWidgetId)
    )

    applyWidgetColors(views, context, appWidgetId, MEDIUM_COLOR_TARGETS, MEDIUM_LINE_IDS)
    // 字体缩放（基础字号见 widget_schedule_medium.xml），行高同步跟着放大
    val scale = loadWidgetFontScaleForId(context, WIDGET_KEY_MEDIUM, appWidgetId)
    applyFontScale(
        views, scale,
        listOf(
            R.id.widget2_title to 12f,
            R.id.widget2_date to 11f,
            R.id.widget2_empty to 11f
        ) + MEDIUM_TIME_IDS.map { it to 10f } + MEDIUM_TEXT_IDS.map { it to 12f } +
                MEDIUM_INFO_IDS.map { it to 9f }
    )
    applyRowSpacing(views, scale, context, MEDIUM_ROW_PADS)
    applyWidgetBackground(context, views, appWidgetId)
    views.setOnClickPendingIntent(R.id.widget2_root, openAppIntent(context))
    return views
}

/** 2×1：一行信息 */
private fun buildShortViews(context: Context, appWidgetId: Int): RemoteViews {
    val views = RemoteViews(context.packageName, R.layout.widget_schedule_short)
    val plan = planFor(context)
    val cfg = loadTimeConfig(context, loadCurrentBookId(context))

    // 第 49 批：顶栏同 4×2，周数与日期只显示今天
    views.setTextViewText(R.id.widget1_title, "第 ${plan.todayWeekNo} 周")
    views.setTextViewText(
        R.id.widget1_date,
        "${dayLabel(plan.todayDate)} ${shortDate(plan.todayDate)}"
    )

    val first = if (plan.hasData && plan.courses.isNotEmpty()) plan.courses.first() else null
    if (first == null) {
        // 没数据 / 最近这一天没课：课程那一块整体藏掉，只留一句提示
        views.setViewVisibility(R.id.widget1_course_row, View.GONE)
        views.setViewVisibility(R.id.widget1_msg, View.VISIBLE)
        views.setTextViewText(
            R.id.widget1_msg,
            if (!plan.hasData) "打开 App 导入课表"
            else if (plan.allDone) "今日课程已上完 🎉"
            else "今天没课 🎉"
        )
    } else {
        views.setViewVisibility(R.id.widget1_course_row, View.VISIBLE)
        views.setViewVisibility(R.id.widget1_msg, View.GONE)
        views.setTextViewText(R.id.widget1_time, courseTimeText(cfg, first))
        views.setTextViewText(R.id.widget1_text, first.name)
        // 第 49 批：顶栏改成只显示今天之后，这里如果是「明日预告」必须自己标一下，
        // 不然会让人以为这门课是今天的
        views.setTextViewText(
            R.id.widget1_info,
            (if (plan.isToday) "" else "明日 ") + courseInfoLine(first)
        )
        // 课程那一行也铺一张圆角底图（2×1 只有这一行课）
        views.setImageViewResource(
            R.id.widget1_cell,
            CELL_DRAWABLES[courseColorIdx(first)]
        )
        views.setInt(R.id.widget1_cell, "setColorFilter", loadCellTint(context, appWidgetId))
        views.setFloat(R.id.widget1_cell, "setAlpha", loadWidgetCellAlphaForId(context, appWidgetId))
    }

    applyWidgetColors(
        views, context, appWidgetId, SHORT_COLOR_TARGETS, intArrayOf(R.id.widget1_line)
    )
    // 字体缩放（基础字号见 widget_schedule_short.xml），整块内边距跟着放大当行高
    val scale = loadWidgetFontScaleForId(context, WIDGET_KEY_SHORT, appWidgetId)
    applyFontScale(
        views, scale,
        listOf(
            R.id.widget1_title to 12f,
            R.id.widget1_date to 11f,
            R.id.widget1_msg to 13f,
            R.id.widget1_time to 10f,
            R.id.widget1_text to 13f,
            R.id.widget1_info to 10f
        )
    )
    applyRowSpacing(views, scale, context, SHORT_ROOT_PAD)
    applyWidgetBackground(context, views, appWidgetId)
    views.setOnClickPendingIntent(R.id.widget1_root, openAppIntent(context))
    return views
}

/** 1×1：最近一节课 */
private fun buildTinyViews(context: Context, appWidgetId: Int): RemoteViews {
    val views = RemoteViews(context.packageName, R.layout.widget_schedule_tiny)
    val plan = planFor(context)

    val top: String
    val main: String
    val sub: String

    when {
        !plan.hasData -> {
            top = "芝士院课表"
            main = "无数据"
            sub = "打开 App 导入"
        }
        plan.courses.isEmpty() -> {
            top = dayLabel(plan.date)
            main = if (plan.allDone) "已上完" else "无课"
            sub = if (plan.allDone) "今日课程已上完" else "好好休息 🎉"
        }
        plan.isToday -> {
            val c = plan.courses.first()
            top = dayLabel(plan.date)
            main = "${c.startSection}-${c.endSection}节"
            sub = if (plan.courses.size > 1) "${c.name} 等${plan.courses.size}门" else c.name
        }
        else -> {
            val c = plan.courses.first()
            top = "明日预告"
            main = "${c.startSection}-${c.endSection}节"
            sub = "${dayLabel(plan.date)} ${c.name}"
        }
    }

    views.setTextViewText(R.id.widget0_top, top)
    views.setTextViewText(R.id.widget0_main, main)
    views.setTextViewText(R.id.widget0_sub, sub)
    applyWidgetColors(views, context, appWidgetId, TINY_COLOR_TARGETS, intArrayOf())
    // 字体缩放（基础字号见 widget_schedule_tiny.xml），整块内边距跟着放大当行高
    val scale = loadWidgetFontScaleForId(context, WIDGET_KEY_TINY, appWidgetId)
    applyFontScale(
        views, scale,
        listOf(
            R.id.widget0_top to 10f,
            R.id.widget0_main to 15f,
            R.id.widget0_sub to 10f
        )
    )
    applyRowSpacing(views, scale, context, TINY_ROOT_PAD)
    applyWidgetBackground(context, views, appWidgetId)
    views.setOnClickPendingIntent(R.id.widget0_root, openAppIntent(context))
    return views
}

// ==================== 小部件背景图（每个组件各存各的，互不影响） ====================

/** 原图缓存：同一张图不用反复解码 */
private object WidgetBgCache {
    var srcPath = ""
    var src: Bitmap? = null
    var outKey = ""
    var out: Bitmap? = null
}

/** 把原图按需下采样读进来（上限 1600px，避免大图 OOM） */
private fun loadBgSource(path: String): Bitmap? {
    if (WidgetBgCache.srcPath == path && WidgetBgCache.src != null) return WidgetBgCache.src
    if (path.isBlank()) return null
    if (!java.io.File(path).exists()) return null
    return try {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, bounds)
        var sample = 1
        while (bounds.outWidth / (sample * 2) >= 1600 || bounds.outHeight / (sample * 2) >= 1600) sample *= 2
        val opts = BitmapFactory.Options().apply { inSampleSize = sample }
        val bmp = BitmapFactory.decodeFile(path, opts) ?: return null
        WidgetBgCache.srcPath = path
        WidgetBgCache.src = bmp
        bmp
    } catch (_: Throwable) {
        null
    }
}

/**
 * 把背景图裁成「正好是这一块组件大小」的圆角图。
 * 直接做成组件尺寸就不用靠 scaleType 去拉，圆角也不会被裁掉；
 * 透明度是把整张图画淡，这样能透出桌面（和 App 里背景图一个道理）。
 */
private fun widgetBgBitmap(context: Context, appWidgetId: Int, path: String, alpha: Float): Bitmap? = try {
    val options = AppWidgetManager.getInstance(context).getAppWidgetOptions(appWidgetId)
    var wDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
    var hDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
    if (wDp <= 0) wDp = 250
    if (hDp <= 0) hDp = 110
    val density = context.resources.displayMetrics.density
    val w = (wDp * density).toInt().coerceIn(60, 1600)
    val h = (hDp * density).toInt().coerceIn(60, 1600)
    val key = path + "|" + w + "|" + h + "|" + alpha
    if (WidgetBgCache.outKey == key && WidgetBgCache.out != null) {
        WidgetBgCache.out
    } else {
        val src = loadBgSource(path) ?: return null
        val out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)
        paint.alpha = (alpha.coerceIn(0.2f, 1f) * 255f).toInt()
        val scale = maxOf(w.toFloat() / src.width.toFloat(), h.toFloat() / src.height.toFloat())
        val dw = src.width * scale
        val dh = src.height * scale
        val left = (w - dw) / 2f
        val top = (h - dh) / 2f
        val clip = Path()
        val radius = 14f * density
        clip.addRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), radius, radius, Path.Direction.CW)
        canvas.clipPath(clip)
        canvas.drawBitmap(src, null, RectF(left, top, left + dw, top + dh), paint)
        WidgetBgCache.outKey = key
        WidgetBgCache.out = out
        out
    }
} catch (_: Throwable) {
    null
}

/**
 * 按每个组件自己的背景模式铺背景：
 * white（默认）→ 用布局自带的白底圆角卡片
 * transparent → 连白底一起去掉，只剩文字和课程格子
 * image → 铺自定义图片（顺手把内容层白底去掉，否则图会被整块盖住）
 */
private fun applyWidgetBackground(
    context: Context,
    views: RemoteViews,
    appWidgetId: Int
) {
    // 每个组件用自己的那份设置：白色（默认·圆角白卡）/ 透明 / 自定义图片
    val mode = loadWidgetBgModeForId(context, appWidgetId)
    val path = loadWidgetBgImageForId(context, appWidgetId)
    val transparent = mode == WIDGET_BG_TRANSPARENT
    // 只有图片模式才去解码；解不出来就退回白卡
    var bmp: Bitmap? = null
    if (!transparent && mode == WIDGET_BG_IMAGE && path.isNotBlank()) {
        bmp = widgetBgBitmap(context, appWidgetId, path, 1f)
    }
    if (bmp == null) {
        // 白底圆角卡片：默认显示；透明模式连它也一起藏起来
        views.setViewVisibility(R.id.widget_card, if (transparent) View.GONE else View.VISIBLE)
        views.setViewVisibility(R.id.widget_bg_image, View.GONE)
        return
    }
    views.setViewVisibility(R.id.widget_card, View.GONE)
    views.setViewVisibility(R.id.widget_bg_image, View.VISIBLE)
    views.setImageViewBitmap(R.id.widget_bg_image, bmp)
}

// ==================== 刷新 ====================

/** 「下课闹钟」的 action：到点后由 ScheduleWidgetProvider.onReceive 接住 */
private const val ACTION_WIDGET_TICK = "com.example.myproject.action.WIDGET_TICK"

private fun tickIntent(context: Context): PendingIntent {
    val intent = Intent(context, ScheduleWidgetProvider::class.java).setAction(ACTION_WIDGET_TICK)
    val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    return PendingIntent.getBroadcast(context, 1001, intent, flags)
}

/**
 * 算好「今天下一节课下课」的时刻，到那一刻自动重画一次组件。
 *
 * 系统的组件刷新周期最短是 30 分钟，光靠它，一门课上完之后最多要等 30 分钟才会消失。
 * 这里用一个普通（非精确）闹钟补上：到点了就重画，于是「上完的课」几乎立刻就不显示了。
 * 没有下一节课（今天已上完 / 今天本来没课）时，就把闹钟取消掉。
 * 用 setAndAllowWhileIdle：不需要精确闹钟权限，省电模式下也能到点触发；
 * 万一系统不给设（厂商限制 / 权限），就直接放弃 —— 还有 30 分钟的周期刷新兜底。
 */
private fun scheduleNextRefresh(context: Context) {
    val app = context.applicationContext
    try {
        val am = app.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val cfg = loadTimeConfig(app, loadCurrentBookId(app))
        val today = LocalDate.now()
        val week = weekOf(loadStartDate(app), today)
        val now = java.time.LocalTime.now()
        val nowMin = now.hour * 60 + now.minute
        // 今天还没下课的那些课里，最早的一个下课时刻
        val next = coursesOn(loadCoursesFromLocal(app), today, week)
            .mapNotNull { c -> cfg.times.getOrNull(c.endSection - 1)?.let { s -> parseSlot(s)?.second } }
            .filter { it > nowMin }
            .minOrNull()
        if (next == null) {
            // 今天没有下一节课了。这里不能直接取消闹钟 ——
            // 有些桌面（MIUI / HyperOS）不会按时走 30 分钟的周期刷新，闹钟一取消，
            // 第二天桌面上还挂着「今天没课 / 今日课程已上完」这种旧文案（第 43 批实测发现）。
            // 改成把闹钟挪到明天 00:00:05，跨天那一刻重画一次，文案就跟着日期翻过来。
            val cal2 = java.util.Calendar.getInstance()
            cal2.add(java.util.Calendar.DAY_OF_YEAR, 1)
            cal2.set(java.util.Calendar.HOUR_OF_DAY, 0)
            cal2.set(java.util.Calendar.MINUTE, 0)
            cal2.set(java.util.Calendar.SECOND, 5)
            cal2.set(java.util.Calendar.MILLISECOND, 0)
            val midnight = cal2.timeInMillis
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, midnight, tickIntent(app))
            } else {
                am.set(AlarmManager.RTC_WAKEUP, midnight, tickIntent(app))
            }
            return
        }
        val cal = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.HOUR_OF_DAY, next / 60)
        cal.set(java.util.Calendar.MINUTE, next % 60)
        cal.set(java.util.Calendar.SECOND, 5)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        val at = cal.timeInMillis
        if (at <= System.currentTimeMillis()) return
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, tickIntent(app))
        } else {
            am.set(AlarmManager.RTC_WAKEUP, at, tickIntent(app))
        }
    } catch (_: Exception) {
    }
}

private fun refreshAll(context: Context) {
    val app = context.applicationContext
    drawAllWidgets(app)
    // 有些桌面（MIUI / HyperOS）会把「刚在设置里改完」的那一次刷新吞掉，隔 0.7 秒再补一次
    try {
        Handler(Looper.getMainLooper()).postDelayed({ drawAllWidgets(app) }, 700L)
    } catch (_: Exception) {
    }
}

/** 四种尺寸各画一遍；每个组件都单独读它自己的字号倍率 */
private fun drawAllWidgets(context: Context) {
    // 每次画之前都重新安排「下课闹钟」：课表/时间改了也能马上接到新的时刻
    scheduleNextRefresh(context)
    val manager = AppWidgetManager.getInstance(context)
    updateEach(context, manager, ScheduleWidgetProvider::class.java) { id -> buildFullViews(context, id) }
    updateEach(context, manager, ScheduleWidgetMediumProvider::class.java) { id -> buildMediumViews(context, id) }
    updateEach(context, manager, ScheduleWidgetShortProvider::class.java) { id -> buildShortViews(context, id) }
    updateEach(context, manager, ScheduleWidgetTinyProvider::class.java) { id -> buildTinyViews(context, id) }
}

private fun updateEach(
    context: Context,
    manager: AppWidgetManager,
    providerClass: Class<*>,
    build: (Int) -> RemoteViews
) {
    val ids = manager.getAppWidgetIds(ComponentName(context, providerClass))
    if (ids.isEmpty()) return
    for (id in ids) manager.updateAppWidget(id, build(id))
}