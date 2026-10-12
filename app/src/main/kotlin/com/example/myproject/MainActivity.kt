package com.example.myproject

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import android.content.Context
import android.content.Intent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Build
import android.net.Uri
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.key
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.material3.Slider
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.KeyFactory
import java.security.KeyStore
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.security.spec.RSAPublicKeySpec
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.time.LocalDateTime
import java.time.LocalTime
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLException
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// ==================== 课程数据模型 ====================
data class Course(
    val weekRangeList: List<Int>,
    val weekCn: String,
    val startSection: Int,
    val endSection: Int,
    val name: String,
    val location: String,
    val teacher: String,
    // -1 = 按课程名自动分配颜色；0 .. 19 = 用户自己挑的配色下标
    val colorIndex: Int = -1
)

// 一个「课表」= 名字 + 开学周一 + 学习周数 + 课程列表
// 每个课表的开学时间和周数各自独立，互不影响
data class ScheduleBook(
    val id: String,
    val name: String = "",
    val startDate: String = "",
    val totalWeeks: Int = 20,
    val courses: List<Course> = emptyList()
)

// 预定义柔和课程配色
// 课程底色：统一用更低饱和的浅色（太鲜艳会显得花）
val colorPalette = listOf(
    Color(0xFFFFCED2), Color(0xFFCFF1E5), Color(0xFFDBDFF1), Color(0xFFFFE7D7),
    Color(0xFFECF5DD), Color(0xFFFFD0CD), Color(0xFFC1EFEE), Color(0xFFFCF0C5),
    Color(0xFFE3C4C4), Color(0xFFCCD9EA), Color(0xFFFCE7E9), Color(0xFFD9EBE3),
    Color(0xFFFFEECE), Color(0xFFE3EFF6), Color(0xFFFBEFC1), Color(0xFFE4F8ED),
    Color(0xFFFCE8E6), Color(0xFFF0E7F5), Color(0xFFCAE4F6), Color(0xFFF8DDC6)
)

// 课程文字色（与 colorPalette 一一对应）：同色系、更饱和也更暗 —— 不是黑色
val colorPaletteText = listOf(
    Color(0xFF9B2730), Color(0xFF1A694D), Color(0xFF304AC0), Color(0xFF8C4D23),
    Color(0xFF4C6A1B), Color(0xFF992D26), Color(0xFF196463), Color(0xFF715F1C),
    Color(0xFF8B2323), Color(0xFF24528E), Color(0xFFAF2C38), Color(0xFF1A6745),
    Color(0xFF7B5B1F), Color(0xFF23648A), Color(0xFF705E1C), Color(0xFF1C7042),
    Color(0xFFA9362A), Color(0xFF892FBB), Color(0xFF215B84), Color(0xFF7F4B20)
)

// 地点 / 教师用的稍浅一档（同样同色系）
val colorPaletteInfo = listOf(
    Color(0xFFA93842), Color(0xFF28775B), Color(0xFF4057BF), Color(0xFF995C33),
    Color(0xFF5A7828), Color(0xFFA63E37), Color(0xFF267271), Color(0xFF7F6C2A),
    Color(0xFF993333), Color(0xFF33609A), Color(0xFFBC3F4B), Color(0xFF277552),
    Color(0xFF88682D), Color(0xFF327297), Color(0xFF7D6C2A), Color(0xFF2A7E50),
    Color(0xFFB6483D), Color(0xFF9240BF), Color(0xFF306990), Color(0xFF8C592F)
)

const val totalSections = 30   // 节次上限：上午/下午/晚上各最多 10 节，共 30 节
val sectionGap: Dp = 2.dp

// 课程名 → 固定下标（忽略“【调】”前缀），三套颜色共用同一个下标
fun courseColorIndex(name: String): Int {
    val pureName = name.removePrefix("【调】")
    val md5 = MessageDigest.getInstance("MD5")
    val hashBytes = md5.digest(pureName.toByteArray(Charsets.UTF_8))
    var hashInt = 0
    for (b in hashBytes.take(4)) {
        hashInt = hashInt shl 8 or (b.toInt() and 0xFF)
    }
    val idx = hashInt.rem(colorPalette.size)
    return if (idx < 0) idx + colorPalette.size else idx
}

/** 这门课实际用的配色下标：用户手动选过就用他选的，否则按课程名自动算 */
fun courseColorIdx(c: Course): Int =
    if (c.colorIndex in colorPalette.indices) c.colorIndex else courseColorIndex(c.name)

fun getCourseColor(c: Course): Color = colorPalette[courseColorIdx(c)]

// 课程块里的文字：与底色同色系、更深一档的彩色（不再是黑字）
fun getCourseTextColor(c: Course): Color = colorPaletteText[courseColorIdx(c)]

fun getCourseInfoColor(c: Course): Color = colorPaletteInfo[courseColorIdx(c)]

// 安全计算当前第几周
fun calcCurrentWeek(startDateStr: String): Int {
    return try {
        val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // 第 47 批：开学日一律按「那一周的周一」算，选到周日 / 周三也不会把周次算歪
        val startMonday = LocalDate.parse(startDateStr, fmt).with(
            java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)
        )
        val today = LocalDate.now()
        val dayDiff = ChronoUnit.DAYS.between(startMonday, today)
        if (dayDiff < 0) 1 else (dayDiff / 7).toInt() + 1
    } catch (_: DateTimeParseException) {
        1
    }
}

// 安全获取指定周次周一日期
fun getWeekMonday(startDateStr: String, targetWeek: Int): LocalDate {
    return try {
        val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val base = LocalDate.parse(startDateStr, fmt)
        base.plusDays((targetWeek - 1) * 7L)
    } catch (_: DateTimeParseException) {
        LocalDate.now()
    }
}

// 解析周次字符串（支持 "1-16周" "1,3,5周" "1-16周(单)" 等）
fun parseWeekRange(weekStr: String): List<Int> {
    val result = mutableListOf<Int>()
    // ★ 单双周是「整串」的属性（例如 "1-9周,单周"），必须在整串上判断。
    //   以前是按逗号分段后逐段判断，遇到 "1-9周,单周" 会因为 "1-9周" 这一段里
    //   没有「单」字，而把 1-9 周全部算进去（单双周等于没生效）。
    val isSingle = "单" in weekStr
    val isDouble = "双" in weekStr
    val onlyOdd = isSingle && !isDouble
    val onlyEven = isDouble && !isSingle

    val parts = weekStr.split(",", "，").map { it.trim() }.filter { it.isNotEmpty() }
    val rangeRegex = Regex("(\\d+)(?:\\s*-\\s*(\\d+))?")
    for (part in parts) {
        val match = rangeRegex.find(part) ?: continue
        val start = match.groupValues[1].toIntOrNull() ?: continue
        val end = match.groupValues[2].ifEmpty { start.toString() }.toIntOrNull() ?: start
        for (w in start..end) {
            when {
                onlyOdd -> if (w % 2 == 1) result.add(w)
                onlyEven -> if (w % 2 == 0) result.add(w)
                else -> result.add(w)
            }
        }
    }
    return result
}

/** 把周次列表压回一段文本，方便「编辑课程」时回填：连续 → "1-16"，隔周 → "1-15单" / "2-16双"，否则 "1,3,7" */
fun formatWeekList(weekList: List<Int>): String {
    if (weekList.isEmpty()) return ""
    val w = weekList.distinct().sorted()
    if (w.size == 1) return w[0].toString()
    val d = w.zipWithNext { a, b -> b - a }
    if (d.all { it == 1 }) return w.first().toString() + "-" + w.last()
    if (d.all { it == 2 }) {
        return w.first().toString() + "-" + w.last() + (if (w.first() % 2 == 1) "单" else "双")
    }
    return w.joinToString(",")
}

// 正方教务 HTML 课表解析（用于导入浏览器导出的 txt/html 课表文件）
fun parseScheduleHtml(html: String): List<Course> {
    val courseList = mutableListOf<Course>()
    val numToWeek = mapOf(1 to "一", 2 to "二", 3 to "三", 4 to "四", 5 to "五", 6 to "六", 7 to "日")

    val tableStart = html.indexOf("id=\"kbgrid_table_0\"")
    val tableEnd = html.indexOf("</tbody>", tableStart)
    if (tableStart < 0 || tableEnd < 0) return emptyList()
    val tableHtml = html.substring(tableStart, tableEnd)

    val tdList = tableHtml.split("<td")
    for (td in tdList) {
        if ("timetable_con text-left" !in td) continue

        val idMatch = Regex("id=\"(\\d+)-(\\d+)\"").find(td)
        val weekDayNum = idMatch?.groupValues?.get(1)?.toIntOrNull() ?: continue
        val weekCn = numToWeek[weekDayNum] ?: continue

        val courseBlocks = td.split("<div class=\"timetable_con text-left\">")
        for (block in courseBlocks.drop(1)) {
            val nameMatch = Regex("<font color=\"blue\">([^<]+)</font>").find(block)
            val name = nameMatch?.groupValues?.get(1)?.trim() ?: continue

            val sectionMatch = Regex("\\((\\d+)(?:-(\\d+))?节\\)([^<]+)").find(block)
            val startSec = sectionMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0
            val endSec = sectionMatch?.groupValues?.get(2)?.ifEmpty { startSec.toString() }?.toIntOrNull() ?: startSec
            val weekRaw = sectionMatch?.groupValues?.get(3) ?: ""
            val weekRange = parseWeekRange(weekRaw)

            val pList = block.split("<p>")
            val location = if (pList.size > 2) {
                Regex("<font color=\"blue\">([^<]+)</font>").find(pList[2])?.groupValues?.get(1)?.trim() ?: ""
            } else ""

            val teacher = if (pList.size > 3) {
                Regex("<font color=\"blue\">([^<]+)</font>").find(pList[3])?.groupValues?.get(1)?.trim() ?: ""
            } else ""

            val cleanLoc = location.replace("相思湖校区", "")
                .replace("(相思湖)", "")
                .replace(" ", "")

            if (startSec in 1..totalSections && endSec >= startSec && weekRange.isNotEmpty()) {
                courseList.add(Course(weekRange, weekCn, startSec, endSec, name, cleanLoc, teacher))
            }
        }
    }
    return courseList
}

// ==================== 教务系统抓取 ====================
private const val BASE_URL = "https://jw.gxvnu.edu.cn"
private const val LOGIN_PAGE = "$BASE_URL/jwglxt/xtgl/login_slogin.html"
private const val PUBKEY_API = "$BASE_URL/jwglxt/xtgl/login_getPublicKey.html"
private const val INDEX_URL =
    "$BASE_URL/jwglxt/kbcx/xskbcx_cxXskbcxIndex.html?gnmkdm=N253508&layout=default"
private const val KB_API = "$BASE_URL/jwglxt/kbcx/xskbcx_cxXsgrkb.html?gnmkdm=N253508"

private const val USER_AGENT =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

// 简单 Cookie 容器
private class CookieJar {
    private val map = linkedMapOf<String, String>()

    @Synchronized
    fun put(setCookie: String) {
        val kv = setCookie.substringBefore(";").trim()
        val idx = kv.indexOf('=')
        if (idx <= 0) return
        map[kv.substring(0, idx)] = kv.substring(idx + 1)
    }

    @Synchronized
    fun header(): String = map.entries.joinToString("; ") { "${it.key}=${it.value}" }
}

private data class HttpResult(val code: Int, val body: String)

// 「信任所有证书」的引擎（等价于 Python 的 verify=False）。
// 正常情况下用不到它：只有当系统 CA 校验握手失败、且目标确实是学校域名时，
// 才会降级使用（学校自建网关常缺中间证书）。
private val trustAllSsl: SSLContext by lazy {
    val tm = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })
    SSLContext.getInstance("TLS").apply { init(null, tm, SecureRandom()) }
}

// 允许「降级信任」的域名后缀：只有学校自己的服务器，其它域名一律走系统默认校验。
private val insecureHostSuffixes = listOf("gxvnu.edu.cn")

private fun isSchoolHost(url: String): Boolean {
    val host = try {
        URL(url).host
    } catch (_: Exception) {
        null
    } ?: return false
    return insecureHostSuffixes.any { host.equals(it, true) || host.endsWith(".$it", true) }
}

private fun openConn(
    url: String,
    method: String,
    headers: Map<String, String>,
    cookies: CookieJar,
    insecure: Boolean = false
): HttpURLConnection {
    val conn = URL(url).openConnection() as HttpURLConnection
    conn.requestMethod = method
    conn.connectTimeout = 45000
    conn.readTimeout = 45000
    conn.instanceFollowRedirects = false // 手动处理重定向
    conn.doInput = true
    // 默认交给系统 CA 校验；insecure 只在「握手失败 + 学校域名」时由 httpRequest 置为 true
    if (conn is HttpsURLConnection && insecure) {
        conn.sslSocketFactory = trustAllSsl.socketFactory
        conn.hostnameVerifier = HostnameVerifier { _, _ -> true }
    }
    for ((k, v) in headers) conn.setRequestProperty(k, v)
    val ck = cookies.header()
    if (ck.isNotEmpty()) conn.setRequestProperty("Cookie", ck)
    return conn
}

private fun collectCookies(conn: HttpURLConnection, cookies: CookieJar) {
    conn.headerFields?.forEach { (k, vs) ->
        if (k != null && k.equals("Set-Cookie", ignoreCase = true)) {
            vs?.forEach { cookies.put(it) }
        }
    }
}

private fun readBody(conn: HttpURLConnection, code: Int): String {
    val stream = if (code in 200..299) conn.inputStream else conn.errorStream
    return stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() } ?: ""
}

private fun httpRequest(
    url: String,
    method: String,
    form: Map<String, String>?,
    headers: Map<String, String>,
    cookies: CookieJar
): HttpResult {
    var currentUrl = url
    var currentMethod = method
    var currentForm = form
    var redirects = 0
    // 是否已降级为「不校验证书」。默认 false（走系统 CA），只对学校域名降级一次。
    var useInsecure = false

    while (true) {
        val conn = openConn(currentUrl, currentMethod, headers, cookies, insecure = useInsecure)
        try {
            if (currentForm != null && currentMethod == "POST") {
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                val body = currentForm.entries.joinToString("&") {
                    "${URLEncoder.encode(it.key, "UTF-8")}=${URLEncoder.encode(it.value, "UTF-8")}"
                }
                conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            }

            val code = conn.responseCode
            collectCookies(conn, cookies)

            // 处理重定向
            if (code in 300..399) {
                val location = conn.getHeaderField("Location")
                conn.disconnect()
                if (location.isNullOrBlank() || redirects >= 8) {
                    return HttpResult(code, "")
                }
                currentUrl = if (location.startsWith("http")) location
                else URL(URL(currentUrl), location).toString()
                if (code == 302 || code == 303) {
                    currentMethod = "GET"
                    currentForm = null
                }
                redirects++
                continue
            }

            val body = readBody(conn, code)
            conn.disconnect()
            return HttpResult(code, body)
        } catch (e: SSLException) {
            runCatching { conn.disconnect() } // 断开失败不该掩盖原始的握手异常
            // 学校自建网关常缺中间证书，系统 CA 校验会直接握手失败；
            // 只对学校域名降级重试一次，其它域名 / 再次失败都原样抛出（不静默放行）。
            if (!useInsecure && isSchoolHost(currentUrl)) {
                useInsecure = true
                continue
            }
            throw e
        }
    }
}

// RSA PKCS#1 v1.5 加密
private fun rsaEncrypt(password: String, modulusB64: String, exponentB64: String): String {
    val modulusBytes = Base64.decode(modulusB64, Base64.DEFAULT)
    val exponentBytes = Base64.decode(exponentB64, Base64.DEFAULT)
    val modulus = BigInteger(1, modulusBytes)
    val exponent = BigInteger(1, exponentBytes)
    val keySpec = RSAPublicKeySpec(modulus, exponent)
    val publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec)
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    val encrypted = cipher.doFinal(password.toByteArray(Charsets.UTF_8))
    return Base64.encodeToString(encrypted, Base64.NO_WRAP)
}

// 解析教务系统课表 JSON 到 Course 列表
private fun parseKbJson(json: String): List<Course> {
    val result = mutableListOf<Course>()
    val root = JSONObject(json)
    val kbList = root.optJSONArray("kbList") ?: return result
    val numToWeek = mapOf(1 to "一", 2 to "二", 3 to "三", 4 to "四", 5 to "五", 6 to "六", 7 to "日")

    for (i in 0 until kbList.length()) {
        val c = kbList.optJSONObject(i) ?: continue
        val xqj = c.optString("xqj", "").trim().toIntOrNull() ?: continue
        val weekCn = numToWeek[xqj] ?: continue

        // ★ 关键修复：优先使用调课后实际生效的 jc 字段解析节次，为空则回退到 jcs 或 jcor
        val rawJc = c.optString("jc", "").trim()
        val rawJcs = c.optString("jcs", "").trim()
        val rawJcor = c.optString("jcor", "").trim()
        val effectiveJc = when {
            rawJc.isNotBlank() -> rawJc
            rawJcs.isNotBlank() -> rawJcs
            else -> rawJcor
        }

        val secMatch = Regex("(\\d+)(?:\\s*-\\s*(\\d+))?").find(effectiveJc) ?: continue
        val startSec = secMatch.groupValues[1].toIntOrNull() ?: continue
        val endSec = secMatch.groupValues[2].ifEmpty { startSec.toString() }.toIntOrNull() ?: startSec

        val zcd = c.optString("zcd", "")
        val weeks = parseWeekRange(zcd)
        if (weeks.isEmpty()) continue

        // 处理课程名称和【调】前缀
        var name = c.optString("kcmc", "") + c.optString("xslxbj", "")
        if (c.optString("jxbsftkbj", "0") == "1") {
            name = "【调】$name"
        }

        val location = (c.optString("xqmc", "") + c.optString("cdmc", ""))
            .replace("相思湖校区", "")
            .replace("(相思湖)", "")
            .replace(" ", "")
        val teacher = c.optString("xm", "")

        if (startSec in 1..totalSections && endSec >= startSec) {
            result.add(Course(weeks, weekCn, startSec, endSec, name, location, teacher))
        }
    }
    return result
}

// 完整登录 + 抓取流程
private fun fetchScheduleFromJw(username: String, password: String): List<Course> {
    val cookies = CookieJar()
    val baseHeaders = mapOf(
        "User-Agent" to USER_AGENT,
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language" to "zh-CN,zh;q=0.9"
    )

    // [1] 获取登录页
    val loginPageResp = httpRequest(LOGIN_PAGE, "GET", null, baseHeaders, cookies)
    if (loginPageResp.body.isBlank()) {
        throw RuntimeException("无法打开登录页(code=${loginPageResp.code})")
    }

    // [2] 获取 RSA 公钥（加 Referer）
    val keyHeaders = baseHeaders + mapOf("Referer" to LOGIN_PAGE)
    val keyResp = httpRequest(PUBKEY_API, "GET", null, keyHeaders, cookies)
    if (keyResp.body.isBlank()) {
        throw RuntimeException("获取公钥失败(code=${keyResp.code})，请检查网络")
    }
    val keyObj = try {
        JSONObject(keyResp.body)
    } catch (e: Exception) {
        throw RuntimeException("公钥响应异常: ${keyResp.body.take(80)}")
    }
    val encrypted = rsaEncrypt(
        password,
        keyObj.getString("modulus"),
        keyObj.getString("exponent")
    )

    // [3] 提交登录
    val form = linkedMapOf(
        "yhm" to username, "mm" to encrypted, "yzm" to "",
        "dlmmsfknt" to "0", "ydappsfbldlzt" to "0", "yzcskz" to "3", "mmsfjm" to "1",
        "mmsrddcshkzfs" to "0", "dlsbsdsj" to "3", "sfxswechatlogin" to "0",
        "scene_id" to "", "authJwglxtLoginURL" to "", "rzzxxs" to "", "rzys" to "",
        "sfzywqh" to "0", "xxdm" to "14684", "userPolicyOn" to "0", "dxsyrz" to "0"
    )
    val postHeaders = baseHeaders + mapOf(
        "Referer" to LOGIN_PAGE,
        "Origin" to BASE_URL,
        "Content-Type" to "application/x-www-form-urlencoded"
    )
    val loginResp = httpRequest(
        "$LOGIN_PAGE?time=${System.currentTimeMillis()}",
        "POST", form, postHeaders, cookies
    )
    if (loginResp.body.contains("用户名或密码错误") ||
        loginResp.body.contains("用户名不存在")) {
        throw RuntimeException("账号或密码错误")
    }

    // [4] 访问课表页，读取 xnm / xqm
    val pageHeaders = baseHeaders + mapOf("Referer" to "$BASE_URL/jwglxt/xtgl/index_initMenu.html")
    val pageResp = httpRequest(INDEX_URL, "GET", null, pageHeaders, cookies)
    if (pageResp.body.isBlank()) {
        throw RuntimeException("访问课表页失败(code=${pageResp.code})")
    }
    val page = pageResp.body
    val dotAll = RegexOption.DOT_MATCHES_ALL
    val xnm = Regex(
        "id=\"xnm\"[^>]*>.*?<option[^>]*value=\"([^\"]*)\"[^>]*selected", dotAll
    ).find(page)?.groupValues?.get(1) ?: ""
    val xqm = Regex(
        "id=\"xqm\"[^>]*>.*?<option[^>]*value=\"([^\"]*)\"[^>]*selected", dotAll
    ).find(page)?.groupValues?.get(1) ?: ""

    // [5] 请求课表 AJAX 接口
    val kbHeaders = baseHeaders + mapOf(
        "Referer" to INDEX_URL,
        "X-Requested-With" to "XMLHttpRequest",
        "Origin" to BASE_URL,
        "Content-Type" to "application/x-www-form-urlencoded"
    )
    val kbResp = httpRequest(
        KB_API,
        "POST",
        linkedMapOf(
            "xnm" to xnm, "xqm" to xqm, "kzlx" to "ck",
            "xsdm" to "", "kclbdm" to "", "kclxdm" to ""
        ),
        kbHeaders, cookies
    )
    if (kbResp.body.isBlank()) {
        throw RuntimeException("课表接口返回空(code=${kbResp.code})")
    }
    if (!kbResp.body.trimStart().startsWith("{")) {
        throw RuntimeException("课表接口返回非JSON: ${kbResp.body.take(80)}")
    }

    return parseKbJson(kbResp.body)
}

// ==================== 读取文本文件 ====================
suspend fun readTextFromUri(context: Context, uri: Uri): String? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))
            val text = reader.readText()
            reader.close()
            inputStream?.close()
            text
        } catch (e: Exception) {
            null
        }
    }
}

// ==================== 本地持久化 ====================
private const val SP_NAME = "schedule_sp"
private const val KEY_START_DATE = "start_date"
private const val KEY_COURSES_JSON = "courses_json"
private const val KEY_LAST_WEEK = "last_week"
private const val KEY_FONT_SCALE = "font_scale"
private const val KEY_ROW_HEIGHT = "row_height"
private const val KEY_USERNAME = "jw_username"
private const val KEY_PASSWORD = "jw_password"
// 打开软件自动更新课表：开关 + 上次自动更新的日期（同一天只自动更新一次）
private const val KEY_AUTO_UPDATE = "auto_update_jw"
private const val KEY_LAST_AUTO_UPDATE = "last_auto_update"
private const val KEY_BOOKS_JSON = "books_json"
private const val KEY_CURRENT_BOOK = "current_book_id"
private const val KEY_TOTAL_WEEKS = "total_weeks"
private const val KEY_BG_IMAGE = "bg_image_path"
private const val KEY_CELL_ALPHA = "cell_alpha"
// 边界（表头 / 节次列 / 午休晚休三条带）底色透明度：0 = 全透明，1 = 不透明
private const val KEY_BORDER_ALPHA = "border_alpha"
// 节次列宽度（dp）；0 = 跟随屏幕自动（小屏 54 / 大屏 60）
private const val KEY_INDEX_COL_W = "index_col_width"
// 小部件背景是按“每个组件”存的：键名里带组件 id（和字体倍率一个思路）
private fun widgetBgImageIdKey(appWidgetId: Int): String = "widget_bg_image_" + appWidgetId
private fun widgetCellAlphaIdKey(appWidgetId: Int): String = "widget_cell_alpha_" + appWidgetId
private const val KEY_DISCLAIMER_ACCEPTED = "disclaimer_accepted"

// ==================== 课表节数与上课时间（每个课表各自一套，互不影响） ====================

private const val KEY_SECTION_COUNTS = "section_counts"   // "5,4,2"
private const val KEY_SECTION_TIMES = "section_times"     // "08:00-08:40|08:50-09:30|…"
private const val KEY_SECTION_STARTS = "section_starts"   // "08:00,13:00,19:00"
private const val KEY_SECTION_LEN = "section_len"         // 每节课多少分钟
private const val KEY_BREAK_LEN = "break_len"             // 课间休息多少分钟
private const val KEY_SAME_LEN = "same_len"               // 每节课时长是否相同

const val MAX_SECTION_PER_GROUP = 10
const val DEFAULT_SECTION_LEN = 40
const val DEFAULT_BREAK_LEN = 10
val DEFAULT_GROUP_STARTS = listOf("08:00", "14:30", "19:30")   // 上午 8:00 / 下午 14:30 / 晚上 19:30
val DEFAULT_SECTION_COUNTS = listOf(5, 4, 2)
val GROUP_NAMES = listOf("上午", "下午", "晚上")

/** 课表节数与时间：三段各几节、每节课时长、课间休息、三段开始时间、各节起止时间 */
data class TimeConfig(
    val counts: List<Int> = DEFAULT_SECTION_COUNTS,
    val sameLength: Boolean = true,
    val sectionLen: Int = DEFAULT_SECTION_LEN,
    val breakLen: Int = DEFAULT_BREAK_LEN,
    val groupStarts: List<String> = DEFAULT_GROUP_STARTS,
    val times: List<String> = emptyList()
)

/** "08:00" → 480（一天里的第几分钟）；解析不出来返回 -1 */
fun timeToMinutes(s: String): Int {
    val p = s.trim().split(":")
    if (p.size != 2) return -1
    val h = p[0].trim().toIntOrNull() ?: return -1
    val m = p[1].trim().toIntOrNull() ?: return -1
    if (h !in 0..23 || m !in 0..59) return -1
    return h * 60 + m
}

/** 480（分钟）→ "08:00" */
fun minutesToTime(minutes: Int): String {
    val v = ((minutes % 1440) + 1440) % 1440
    val h = v / 60
    val m = v % 60
    return (if (h < 10) "0" else "") + h + ":" + (if (m < 10) "0" else "") + m
}

/** "08:00-08:40" → 起止分钟；解析不出来返回 null */
fun parseSlot(slot: String): Pair<Int, Int>? {
    val p = slot.split("-")
    if (p.size < 2) return null
    val a = timeToMinutes(p[0])
    val b = timeToMinutes(p[1])
    if (a < 0 || b < 0) return null
    return a to b
}

/** "08:00" → "8:00"（格子里不补零，和 8:00-8:40 这种写法一致） */
fun shortClock(t: String): String {
    val s = t.trim()
    return if (s.length == 5 && s[0] == '0') s.substring(1) else s
}

/** "08:00-08:40" → "8:00-8:40"（需要写成一行的地方用，比如说明文字） */
fun prettySlot(slot: String): String {
    val p = prettySlotLines(slot)
    return if (p.second.isBlank()) p.first else p.first + "-" + p.second
}

/** "08:00-08:40" → ("8:00", "8:40")：课表格子里上一行写起时间、下一行写止时间 */
fun prettySlotLines(slot: String): Pair<String, String> {
    val i = slot.indexOf('-')
    if (i <= 0) return shortClock(slot) to ""
    return shortClock(slot.substring(0, i)) to shortClock(slot.substring(i + 1))
}

/** 按「三段开始时间 + 每节时长 + 课间休息」把各节时间自动排出来（顺序就是上午→下午→晚上） */
fun autoSectionTimes(cfg: TimeConfig): List<String> {
    val out = mutableListOf<String>()
    val len = cfg.sectionLen.coerceIn(5, 180)
    val brk = cfg.breakLen.coerceIn(0, 60)
    for (g in 0..2) {
        val n = cfg.counts.getOrElse(g) { 0 }
        if (n <= 0) continue
        var cur = timeToMinutes(cfg.groupStarts.getOrElse(g) { DEFAULT_GROUP_STARTS[g] })
        if (cur < 0) cur = timeToMinutes(DEFAULT_GROUP_STARTS[g])
        for (i in 0 until n) {
            out.add(minutesToTime(cur) + "-" + minutesToTime(cur + len))
            cur += len + brk
        }
    }
    return out
}

/** 让配置自洽：节数对不上、或不是「每节课时长相同」却没存过时间，就按自动排的那份来 */
fun normalizeTimeConfig(cfg: TimeConfig): TimeConfig {
    val total = cfg.counts.sum()
    val needAuto = cfg.times.size != total
    val t = if (cfg.sameLength || needAuto) autoSectionTimes(cfg) else cfg.times
    return cfg.copy(times = t)
}

/** 老版本默认过的三段开始时间：存的就是它，说明用户没自己改过，升级后换成新默认 */
private const val OLD_DEFAULT_STARTS_STR = "08:00,13:00,19:00"

/** 把「还是老默认值」的那一份换成新默认；用户改过的（不是老默认串）不会被覆盖 */
private fun migrateStartsIfOld(context: Context, key: String) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    if (sp.getString(key, null) == OLD_DEFAULT_STARTS_STR) {
        sp.edit().putString(key, DEFAULT_GROUP_STARTS.joinToString(",")).apply()
    }
}

/** 键名后面缀上课表 id：每个课表各存一套，互不影响 */
private fun tcKey(base: String, bookId: String?): String =
    if (bookId.isNullOrBlank()) base else base + "_" + bookId

fun loadTimeConfig(context: Context, bookId: String? = null): TimeConfig {
    // 升级上来、还留着老默认时间（8:00 / 13:00 / 19:00）的那一份，先换成新默认
    migrateStartsIfOld(context, KEY_SECTION_STARTS)
    if (!bookId.isNullOrBlank()) migrateStartsIfOld(context, tcKey(KEY_SECTION_STARTS, bookId))
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    // 这个课表还没单独存过（比如刚升级上来）→ 先沿用「公共」那一套，不让用户白设一遍
    val useOwn = bookId != null && sp.contains(tcKey(KEY_SECTION_COUNTS, bookId))
    val k: (String) -> String = { base -> if (useOwn) tcKey(base, bookId) else base }
    val counts = (sp.getString(k(KEY_SECTION_COUNTS), "") ?: "").split(",")
        .mapNotNull { it.trim().toIntOrNull() }
        .let {
            if (it.size == 3) it.map { v -> v.coerceIn(0, MAX_SECTION_PER_GROUP) }
            else DEFAULT_SECTION_COUNTS
        }
    val starts = (sp.getString(k(KEY_SECTION_STARTS), "") ?: "").split(",").map { it.trim() }
        .let {
            if (it.size == 3 && it.all { s -> timeToMinutes(s) >= 0 }) it else DEFAULT_GROUP_STARTS
        }
    val times = (sp.getString(k(KEY_SECTION_TIMES), "") ?: "").split("|").filter { it.isNotBlank() }
    return normalizeTimeConfig(
        TimeConfig(
            counts = counts,
            sameLength = sp.getBoolean(k(KEY_SAME_LEN), true),
            sectionLen = sp.getInt(k(KEY_SECTION_LEN), DEFAULT_SECTION_LEN).coerceIn(5, 180),
            breakLen = sp.getInt(k(KEY_BREAK_LEN), DEFAULT_BREAK_LEN).coerceIn(0, 60),
            groupStarts = starts,
            times = times
        )
    )
}

fun saveTimeConfig(context: Context, cfg: TimeConfig, bookId: String? = null) {
    val c = normalizeTimeConfig(cfg)
    val e = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
    e.putString(tcKey(KEY_SECTION_COUNTS, bookId), c.counts.joinToString(","))
    e.putString(tcKey(KEY_SECTION_STARTS, bookId), c.groupStarts.joinToString(","))
    e.putInt(tcKey(KEY_SECTION_LEN, bookId), c.sectionLen)
    e.putInt(tcKey(KEY_BREAK_LEN, bookId), c.breakLen)
    e.putBoolean(tcKey(KEY_SAME_LEN, bookId), c.sameLength)
    if (c.sameLength) e.remove(tcKey(KEY_SECTION_TIMES, bookId))
    else e.putString(tcKey(KEY_SECTION_TIMES, bookId), c.times.joinToString("|"))
    e.apply()
    // 节数/时间变了，桌面组件也跟着重画一次
    ScheduleWidgetProvider.refreshScheduleWidgets(context)
}

/** 新课表 / 点「恢复默认」时用的那套：上午 08:00、下午 14:30、晚上 19:30 起，每节 40 分钟、课间 10 分钟 */
fun defaultTimeConfig(): TimeConfig = normalizeTimeConfig(TimeConfig())

/** 删掉某个课表时，把它单独存的那套节数 / 时间一起清掉（「公共」那套不动） */
fun clearTimeConfig(context: Context, bookId: String) {
    if (bookId.isBlank()) return
    val e = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
    e.remove(tcKey(KEY_SECTION_COUNTS, bookId))
    e.remove(tcKey(KEY_SECTION_STARTS, bookId))
    e.remove(tcKey(KEY_SECTION_TIMES, bookId))
    e.remove(tcKey(KEY_SECTION_LEN, bookId))
    e.remove(tcKey(KEY_BREAK_LEN, bookId))
    e.remove(tcKey(KEY_SAME_LEN, bookId))
    e.apply()
}


fun saveToLocal(context: Context, startDate: String, courses: List<Course>, week: Int) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
    sp.putString(KEY_START_DATE, startDate)
    sp.putInt(KEY_LAST_WEEK, week)

    sp.putString(KEY_COURSES_JSON, coursesToJsonArray(courses).toString())
    sp.apply()

    // 课表数据变了，顺手刷新桌面小组件（没添加过组件时是空操作）
    ScheduleWidgetProvider.refreshScheduleWidgets(context)
}

// 开学周一：存盘固定用 yyyy-MM-dd（桌面小组件也按这个格式解析），界面上显示成 yyyy/MM/dd
val START_DATE_STORE_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
val START_DATE_SHOW_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

fun parseStartDateOrNull(s: String?): LocalDate? = try {
    if (s.isNullOrBlank()) null else LocalDate.parse(s.trim(), START_DATE_STORE_FMT)
} catch (_: Exception) {
    null
}

// 界面显示用：没设置过 / 存的值坏了 → 显示今天
fun startDateForDisplay(s: String?): String =
    (parseStartDateOrNull(s) ?: LocalDate.now()).format(START_DATE_SHOW_FMT)

fun loadStartDate(context: Context): String {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    val raw = sp.getString(KEY_START_DATE, null)
    // 没设置过就默认「今天」，不再硬编码某年某月某日
    return (parseStartDateOrNull(raw) ?: LocalDate.now()).format(START_DATE_STORE_FMT)
}

fun saveStartDate(context: Context, startDate: String) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putString(KEY_START_DATE, startDate).apply()
    // 开学日期变了，桌面小组件的周次要跟着刷新
    ScheduleWidgetProvider.refreshScheduleWidgets(context)
}

fun loadLastWeek(context: Context): Int {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getInt(KEY_LAST_WEEK, 1)
}

fun courseToJsonObject(c: Course): JSONObject {
    val obj = JSONObject()
    obj.put("name", c.name)
    obj.put("weekCn", c.weekCn)
    obj.put("start", c.startSection)
    obj.put("end", c.endSection)
    obj.put("location", c.location)
    obj.put("teacher", c.teacher)
    obj.put("weeks", JSONArray(c.weekRangeList))
    obj.put("color", c.colorIndex)
    return obj
}

fun coursesToJsonArray(courses: List<Course>): JSONArray {
    val arr = JSONArray()
    for (c in courses) arr.put(courseToJsonObject(c))
    return arr
}

fun coursesFromJsonArray(arr: JSONArray): List<Course> {
    val list = mutableListOf<Course>()
    for (i in 0 until arr.length()) {
        val obj = arr.getJSONObject(i)
        val weeksArr = obj.getJSONArray("weeks")
        val weeks = (0 until weeksArr.length()).map { weeksArr.getInt(it) }
        list.add(
            Course(
                weekRangeList = weeks,
                weekCn = obj.getString("weekCn"),
                startSection = obj.getInt("start"),
                endSection = obj.getInt("end"),
                name = obj.getString("name"),
                location = obj.getString("location"),
                teacher = obj.getString("teacher"),
                colorIndex = obj.optInt("color", -1)
            )
        )
    }
    return list
}

fun loadCoursesFromLocal(context: Context): List<Course> {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    val json = sp.getString(KEY_COURSES_JSON, "") ?: ""
    if (json.isBlank()) return emptyList()
    return try {
        coursesFromJsonArray(JSONArray(json))
    } catch (_: Exception) {
        emptyList()
    }
}

fun loadFontScale(context: Context): Float {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getFloat(KEY_FONT_SCALE, 1.0f)
}

fun saveFontScale(context: Context, scale: Float) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
    sp.putFloat(KEY_FONT_SCALE, scale)
    sp.apply()
}

fun loadRowHeight(context: Context): Int {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getInt(KEY_ROW_HEIGHT, 52)
}

fun saveRowHeight(context: Context, height: Int) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
    sp.putInt(KEY_ROW_HEIGHT, height)
    sp.apply()
}

// ==================== 多课表：读写 ====================
// 没命名的课表按顺序叫「课表1」「课表2」……
fun bookDisplayName(books: List<ScheduleBook>, index: Int): String {
    if (index < 0 || index >= books.size) return "课表"
    val n = books[index].name.trim()
    return if (n.isEmpty()) "课表" + (index + 1) else n
}

/** [1,2,3,5] → "1-3,5周"（课程列表里显示周次用） */
fun compressWeeksText(list: List<Int>): String {
    if (list.isEmpty()) return "无周次"
    val s = list.sorted().distinct()
    val parts = mutableListOf<String>()
    var i = 0
    while (i < s.size) {
        var j = i
        while (j + 1 < s.size && s[j + 1] == s[j] + 1) j++
        if (j - i >= 2) {
            parts.add(s[i].toString() + "-" + s[j])
        } else {
            var k = i
            while (k <= j) {
                parts.add(s[k].toString())
                k++
            }
        }
        i = j + 1
    }
    return parts.joinToString(",") + "周"
}

/** 一条课程的简要信息：周几 · 第几到第几节 · 周次 · 地点 · 老师 */
fun courseMainInfo(c: Course): String {
    val sb = StringBuilder()
    sb.append("周").append(c.weekCn)
    sb.append(" · 第 ").append(c.startSection).append("-").append(c.endSection).append(" 节")
    sb.append(" · ").append(compressWeeksText(c.weekRangeList))
    if (c.location.isNotBlank()) sb.append(" · ").append(c.location)
    if (c.teacher.isNotBlank()) sb.append(" · ").append(c.teacher)
    return sb.toString()
}

fun loadBooks(context: Context): List<ScheduleBook> {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    val raw = sp.getString(KEY_BOOKS_JSON, "") ?: ""
    if (raw.isNotBlank()) {
        try {
            val arr = JSONArray(raw)
            val out = mutableListOf<ScheduleBook>()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                out.add(
                    ScheduleBook(
                        id = o.getString("id"),
                        name = o.optString("name", ""),
                        startDate = o.optString("startDate", ""),
                        totalWeeks = o.optInt("totalWeeks", 20),
                        courses = coursesFromJsonArray(o.optJSONArray("courses") ?: JSONArray())
                    )
                )
            }
            if (out.isNotEmpty()) return out
        } catch (_: Exception) {
        }
    }
    // 第一次升级：把老的「单课表」数据搬成「课表1」
    val legacyStart = loadStartDate(context)
    val legacyWeeks = sp.getInt(KEY_TOTAL_WEEKS, 20)
    val migrated = listOf(
        ScheduleBook(
            id = "b1",
            name = "",
            startDate = legacyStart,
            totalWeeks = legacyWeeks,
            courses = loadCoursesFromLocal(context)
        )
    )
    saveBooks(context, migrated, "b1")
    return migrated
}

fun saveBooks(context: Context, books: List<ScheduleBook>, currentId: String) {
    val arr = JSONArray()
    for (b in books) {
        val o = JSONObject()
        o.put("id", b.id)
        o.put("name", b.name)
        o.put("startDate", b.startDate)
        o.put("totalWeeks", b.totalWeeks)
        o.put("courses", coursesToJsonArray(b.courses))
        arr.put(o)
    }
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putString(KEY_BOOKS_JSON, arr.toString())
        .putString(KEY_CURRENT_BOOK, currentId)
        .apply()
}

fun loadCurrentBookId(context: Context): String {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getString(KEY_CURRENT_BOOK, "") ?: ""
}

// ==================== 课表背景 ====================
fun loadBgImage(context: Context): String {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getString(KEY_BG_IMAGE, "") ?: ""
}

fun saveBgImage(context: Context, path: String) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putString(KEY_BG_IMAGE, path).apply()
}

fun loadCellAlpha(context: Context): Float {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getFloat(KEY_CELL_ALPHA, 1.0f)
}

fun saveCellAlpha(context: Context, alpha: Float) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putFloat(KEY_CELL_ALPHA, alpha).apply()
}

/**
 * 边界（表头、节次列、午休 / 晚休三条带）底色的不透明度（0 = 全透明，1 = 完全不透明）。
 *
 * 设置页里显示的是「透明度」= 1 - 这个值：
 *   透明度 0%   → 不透明度 1.0 → 米色实色，把背景图完全挡住；
 *   透明度 22%  → 不透明度 0.78 → 正好是升级前那层「白 0.78」（所以默认取 0.78，外观不变）；
 *   透明度 100% → 不透明度 0   → 一点底色都没有，直接看到照片。
 * 没设背景图时它只影响午休 / 晚休那条浅灰带。
 */
fun loadBorderAlpha(context: Context): Float {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getFloat(KEY_BORDER_ALPHA, 0.78f).coerceIn(0f, 1f)
}

fun saveBorderAlpha(context: Context, alpha: Float) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putFloat(KEY_BORDER_ALPHA, alpha.coerceIn(0f, 1f)).apply()
}

/** 节次列（最左边那一列）宽度；0 = 跟随屏幕自动 */
fun loadIndexColWidth(context: Context): Int {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getInt(KEY_INDEX_COL_W, 0).coerceIn(0, 140)
}

fun saveIndexColWidth(context: Context, w: Int) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putInt(KEY_INDEX_COL_W, w.coerceIn(0, 140)).apply()
}

// 小部件背景模式：white（默认，白底圆角卡片） / transparent（透明） / image（背景图）
// ==================== 切换周数的动画（设置项） ====================
const val WEEK_ANIM_NONE = "none"
const val WEEK_ANIM_SLIDE = "slide"
const val WEEK_ANIM_FADE = "fade"
const val WEEK_ANIM_FLIP = "flip"
const val WEEK_ANIM_DRAG = "drag"
const val WEEK_ANIM_DIGIT = "digit"
const val WEEK_ANIM_COMBO = "combo"
private const val KEY_WEEK_ANIM = "week_anim"

/** 动画时长（毫秒），推入 / 淡入 / 翻牌共用 */
const val WEEK_ANIM_DUR_MS = 220

/** 设置页里列出的可选项：key / 名字 / 一句说明 */
val WEEK_ANIM_OPTIONS: List<Triple<String, String, String>> = listOf(
    Triple(WEEK_ANIM_SLIDE, "推入翻页（默认）", "旧课表向左滑出、新课表从右边滑进来，最像翻书"),
    Triple(WEEK_ANIM_COMBO, "推入 + 周数跳动", "推入翻页的同时，周数数字也跳一下"),
    Triple(WEEK_ANIM_FADE, "淡入淡出（略缩放）", "新课表淡入并从小放大到 100%，旧课表淡出"),
    Triple(WEEK_ANIM_FLIP, "翻牌（3D 翻转）", "像翻日历纸一样绕竖轴翻转过去"),
    Triple(WEEK_ANIM_DIGIT, "只有周数跳动", "课表直接切，只让周数数字放大回弹一下"),
    Triple(WEEK_ANIM_DRAG, "跟手分页", "拖动时课表 1:1 跟着手指走，松手超过 1/8 屏宽就翻周，不够就弹回"),
    Triple(WEEK_ANIM_NONE, "关（直接切）", "没有任何过渡，点 / 滑之后立刻换周")
)

fun weekAnimLabel(key: String): String =
    WEEK_ANIM_OPTIONS.firstOrNull { it.first == key }?.second ?: "推入翻页（默认）"

fun loadWeekAnim(context: Context): String {
    val v = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .getString(KEY_WEEK_ANIM, WEEK_ANIM_SLIDE) ?: WEEK_ANIM_SLIDE
    return if (WEEK_ANIM_OPTIONS.any { it.first == v }) v else WEEK_ANIM_SLIDE
}

fun saveWeekAnim(context: Context, v: String) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putString(KEY_WEEK_ANIM, v).apply()
}

/** 是否有“两张课表同框”的过程（推入 / 淡入 / 翻牌） */
fun weekAnimHasSlide(kind: String): Boolean =
    kind == WEEK_ANIM_SLIDE || kind == WEEK_ANIM_COMBO ||
        kind == WEEK_ANIM_FADE || kind == WEEK_ANIM_FLIP

/** 是否带“周数数字跳动” */
fun weekAnimHasDigit(kind: String): Boolean =
    kind == WEEK_ANIM_DIGIT || kind == WEEK_ANIM_COMBO

/**
 * 切换动画的画面变换：old = true 是正在退场的旧课表，false 是正在进场的新课表。
 * t 是进度 0 ~ 1，dir = +1 表示新课表从右边进来。
 */
private fun GraphicsLayerScope.weekAnimLayer(
    old: Boolean,
    kind: String,
    t: Float,
    dir: Int,
    slidePx: Float
) {
    val p = t.coerceIn(0f, 1f)
    when (kind) {
        WEEK_ANIM_SLIDE, WEEK_ANIM_COMBO -> {
            translationX = if (old) -dir * p * slidePx else dir * (1f - p) * slidePx
        }
        WEEK_ANIM_FADE -> {
            alpha = (if (old) 1f - p * 1.15f else p * 1.15f - 0.15f).coerceIn(0f, 1f)
            if (!old) {
                val s = 0.96f + 0.04f * p
                scaleX = s
                scaleY = s
            }
        }
        WEEK_ANIM_FLIP -> {
            val half = p * 2f
            if (old) {
                rotationY = -90f * half.coerceAtMost(1f)
                alpha = if (p < 0.5f) 1f else 0f
            } else {
                rotationY = 90f * (1f - (half - 1f).coerceIn(0f, 1f))
                alpha = if (p < 0.5f) 0f else 1f
            }
            cameraDistance = 16f * density
        }
    }
}

const val WIDGET_BG_WHITE = "white"
const val WIDGET_BG_TRANSPARENT = "transparent"
const val WIDGET_BG_IMAGE = "image"

private fun widgetBgModeIdKey(appWidgetId: Int): String = "widget_bg_mode_" + appWidgetId

fun loadWidgetBgModeForId(context: Context, appWidgetId: Int): String {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getString(widgetBgModeIdKey(appWidgetId), WIDGET_BG_WHITE) ?: WIDGET_BG_WHITE
}

fun saveWidgetBgModeForId(context: Context, appWidgetId: Int, mode: String) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putString(widgetBgModeIdKey(appWidgetId), mode).apply()
    // 改完立刻反映到桌面
    ScheduleWidgetProvider.refreshScheduleWidgetsQuick(context)
}

// ==================== 小部件背景（每个组件单独一份，互不影响） ====================
fun loadWidgetBgImageForId(context: Context, appWidgetId: Int): String {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getString(widgetBgImageIdKey(appWidgetId), "") ?: ""
}

fun saveWidgetBgImageForId(context: Context, appWidgetId: Int, path: String) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    // 换成新图时把上一个文件删掉，别让私有目录越堆越多
    val old = sp.getString(widgetBgImageIdKey(appWidgetId), "") ?: ""
    if (old.isNotBlank() && old != path) {
        runCatching {
            val f = File(old)
            if (f.name.startsWith("widget_bg_") && f.parentFile == File(context.filesDir, "bg")) {
                f.delete()
            }
        }
    }
    sp.edit()
        .putString(widgetBgImageIdKey(appWidgetId), path)
        // 选了图 = 「背景图」模式；移除（空路径）就是回到「白色（默认）」模式
        .putString(
            widgetBgModeIdKey(appWidgetId),
            if (path.isBlank()) WIDGET_BG_WHITE else WIDGET_BG_IMAGE
        )
        .apply()
    // 换图 / 移除立刻反映到桌面（只重画一次）
    ScheduleWidgetProvider.refreshScheduleWidgetsQuick(context)
}

/** 移除某一个组件的背景图（只清这个组件的，别人的不动）；模式会跟着回到「白色（默认）」 */
fun clearWidgetBgImageForId(context: Context, appWidgetId: Int) {
    saveWidgetBgImageForId(context, appWidgetId, "")
}

/**
 * 「课程格子透明度」：和 App 里「背景设置 → 课程格子透明度」一个意思，
 * 只是按组件各存各的；作用在每一行课程格子的圆角底图上（文字不受影响）。
 */
fun loadWidgetCellAlphaForId(context: Context, appWidgetId: Int): Float {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getFloat(widgetCellAlphaIdKey(appWidgetId), 1.0f)
}

fun saveWidgetCellAlphaForId(context: Context, appWidgetId: Int, alpha: Float) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putFloat(widgetCellAlphaIdKey(appWidgetId), alpha.coerceIn(0.2f, 1f)).apply()
    ScheduleWidgetProvider.refreshScheduleWidgetsQuick(context)
}

// ==================== 小部件颜色（每个组件单独一份） ====================
// 7 个可调项：周数 / 周数日期 / 课表格子 / 分割线 / 时间 / 课程名 / 其他（节数·教室·教师）
const val WIDGET_COLOR_TITLE = "title"
const val WIDGET_COLOR_DATE = "date"
const val WIDGET_COLOR_CELL = "cell"
const val WIDGET_COLOR_DIVIDER = "divider"
const val WIDGET_COLOR_TIME = "time"
const val WIDGET_COLOR_NAME = "name"
const val WIDGET_COLOR_INFO = "info"

/** 课表格子选「原色」时存这个值：0x00000000 = 不染色，直接用课表自己分配的颜色 */
const val WIDGET_CELL_COLOR_AUTO = 0

// 下面几个色号与四个布局 XML 里写的默认色一一对应：
// 用户没单独设过的时候，写回去的就是这些值，所以外观不会有任何变化
const val WIDGET_DEF_TITLE = 0xFF303F9F.toInt()
const val WIDGET_DEF_DATE = 0x99000000.toInt()
const val WIDGET_DEF_DIVIDER = 0xFF4A6CF7.toInt()
const val WIDGET_DEF_TIME = 0xFF5F6368.toInt()
const val WIDGET_DEF_NAME = 0xFF1F1F1F.toInt()
// 第 49 批：「⑦其他」的默认色由深灰 #7A7A7A 换成原图的浅灰 #CFCFCF
// （颜色条第 4 格正好也是这个值，所以「恢复默认色」和手动点第 4 格得到同一个颜色）
const val WIDGET_DEF_INFO = 0xFFCFCFCF.toInt()
const val WIDGET_DEF_FADED = 0x99000000.toInt()
const val WIDGET_DEF_FOOTER = 0x80000000.toInt()

/** 哪一个组件、哪一项颜色：键名 = widget_color_<项>_<组件 id> */
private fun widgetColorKey(appWidgetId: Int, slot: String): String =
    "widget_color_" + slot + "_" + appWidgetId

fun loadWidgetColorForId(context: Context, appWidgetId: Int, slot: String, def: Int): Int {
    if (appWidgetId < 0) return def
    return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .getInt(widgetColorKey(appWidgetId, slot), def)
}

fun saveWidgetColorForId(context: Context, appWidgetId: Int, slot: String, color: Int) {
    if (appWidgetId < 0) return
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putInt(widgetColorKey(appWidgetId, slot), color).apply()
    // 改完立刻反映到桌面（只重画一次）
    ScheduleWidgetProvider.refreshScheduleWidgetsQuick(context)
}

/** 某一项恢复默认色：把这个键删掉，下次读到的就是布局里的默认值 */
fun resetWidgetColorForId(context: Context, appWidgetId: Int, slot: String) {
    if (appWidgetId < 0) return
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .remove(widgetColorKey(appWidgetId, slot)).apply()
    ScheduleWidgetProvider.refreshScheduleWidgetsQuick(context)
}

/** 第 49 批一次性迁移的标记：做过就不再重复做 */
private const val WIDGET_INFO_COLOR_MIGRATED = "widget_info_color_migrated_v49"

/**
 * 第 49 批：「⑦其他（节数 · 教室 · 教师）」的默认色由深灰 #7A7A7A 换成原图的浅灰 #CFCFCF。
 *
 * 但「存过的值」优先级高于默认值 —— 老版本里这个槽已经被存过色（第 43 批误存成深灰），
 * 光改默认值它不会变。所以这里做一次性清理：把所有组件「其他」槽存过的旧值删掉，
 * 让它回落到新的浅灰原色。只做一次（标记写进同一个 SharedPreferences），
 * 之后用户在界面里挑的颜色不会被碰。
 */
private fun migrateWidgetInfoColorOnce(context: Context) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    if (sp.getBoolean(WIDGET_INFO_COLOR_MIGRATED, false)) return
    val prefix = "widget_color_" + WIDGET_COLOR_INFO + "_"
    val stale = sp.all.keys.filter { it.startsWith(prefix) }
    val ed = sp.edit().putBoolean(WIDGET_INFO_COLOR_MIGRATED, true)
    stale.forEach { ed.remove(it) }
    ed.apply()
    if (stale.isNotEmpty()) {
        // 删完立刻按新颜色重画一次桌面
        ScheduleWidgetProvider.refreshScheduleWidgets(context)
    }
}

/** 色号显示成 #AARRGGBB，方便在设置页里核对 */
fun argbHex(color: Int): String {
    val digits = "0123456789ABCDEF"
    val sb = StringBuilder("#")
    var i = 28
    while (i >= 0) {
        sb.append(digits[(color ushr i) and 0xF])
        i -= 4
    }
    return sb.toString()
}

// 小部件背景图也拷进 App 私有目录；文件名带时间戳，所以几个组件各用各的、不会互相覆盖
fun importWidgetBgImage(context: Context, uri: Uri): String = try {
    val dir = File(context.filesDir, "bg")
    if (!dir.exists()) dir.mkdirs()
    val out = File(dir, "widget_bg_" + System.currentTimeMillis() + ".jpg")
    context.contentResolver.openInputStream(uri)?.use { input ->
        out.outputStream().use { output -> input.copyTo(output) }
    }
    out.absolutePath
} catch (_: Exception) {
    ""
}

// 把用户选的图片拷进 App 私有目录，避免 Uri 授权过期后读不到
fun importBgImage(context: Context, uri: Uri): String = try {
    val dir = File(context.filesDir, "bg")
    if (!dir.exists()) dir.mkdirs()
    val out = File(dir, "schedule_bg.jpg")
    context.contentResolver.openInputStream(uri)?.use { input ->
        out.outputStream().use { output -> input.copyTo(output) }
    }
    out.absolutePath
} catch (_: Exception) {
    ""
}

// 背景图解码（按需下采样，避免大图 OOM）
fun decodeBgImage(path: String, maxWidth: Int = 1080): ImageBitmap? {
    if (path.isBlank()) return null
    if (!File(path).exists()) return null
    return try {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, bounds)
        var sample = 1
        while (bounds.outWidth > 0 && bounds.outWidth / sample > maxWidth) sample *= 2
        val opts = BitmapFactory.Options().apply { inSampleSize = sample }
        BitmapFactory.decodeFile(path, opts)?.asImageBitmap()
    } catch (_: Exception) {
        null
    }
}

fun loadUsername(context: Context): String {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getString(KEY_USERNAME, "") ?: ""
}

fun saveUsername(context: Context, name: String) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
    sp.putString(KEY_USERNAME, name)
    sp.apply()
}

/** 「用教务系统账号自动更新课表」的开关 */
fun loadAutoUpdate(context: Context): Boolean {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getBoolean(KEY_AUTO_UPDATE, false)
}

fun saveAutoUpdate(context: Context, v: Boolean) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
    sp.putBoolean(KEY_AUTO_UPDATE, v)
    sp.apply()
}

/** 上一次「自动更新」发生在哪一天（yyyy-MM-dd），同一天不再重复登录教务 */
fun loadLastAutoUpdate(context: Context): String {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getString(KEY_LAST_AUTO_UPDATE, "") ?: ""
}

fun saveLastAutoUpdate(context: Context, day: String) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
    sp.putString(KEY_LAST_AUTO_UPDATE, day)
    sp.apply()
}

// ==================== 密码存储：Keystore + AES/GCM ====================
// 旧版用 Base64「编码」存密码，等于明文（解码即得）。现在改成：
// 密钥生成在 Android Keystore 里（不出安全硬件/系统密钥库），密码用 AES-256-GCM 加密后
// 再写进 SharedPreferences；即使 sp 文件被导出也解不出明文。
private const val ANDROID_KEYSTORE = "AndroidKeyStore"
private const val PWD_KEY_ALIAS = "cheese_schedule_pwd"
// 存储格式版本前缀：v2 = AES/GCM(IV:密文)；无前缀 = 旧版 Base64（读取后会自动迁移升级）
private const val PWD_ENC_PREFIX = "v2:"

private fun pwdSecretKey(): SecretKey {
    val ks = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
    (ks.getEntry(PWD_KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }

    val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
    generator.init(
        KeyGenParameterSpec.Builder(
            PWD_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
    )
    return generator.generateKey()
}

private fun encryptPassword(pwd: String): String? = try {
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, pwdSecretKey())
    val iv = cipher.iv
    val ct = cipher.doFinal(pwd.toByteArray(Charsets.UTF_8))
    PWD_ENC_PREFIX + Base64.encodeToString(iv, Base64.NO_WRAP) +
            ":" + Base64.encodeToString(ct, Base64.NO_WRAP)
} catch (_: Exception) {
    // Keystore 不可用（极少数设备）。返回 null 让调用方「宁可不存」也不落明文。
    null
}

private fun decryptPassword(enc: String): String = try {
    val parts = enc.removePrefix(PWD_ENC_PREFIX).split(":", limit = 2)
    val iv = Base64.decode(parts[0], Base64.DEFAULT)
    val ct = Base64.decode(parts[1], Base64.DEFAULT)
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, pwdSecretKey(), GCMParameterSpec(128, iv))
    String(cipher.doFinal(ct), Charsets.UTF_8)
} catch (_: Exception) {
    ""
}

fun loadPassword(context: Context): String {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    val stored = sp.getString(KEY_PASSWORD, "") ?: ""
    if (stored.isBlank()) return ""
    if (stored.startsWith(PWD_ENC_PREFIX)) return decryptPassword(stored)
    // 旧数据是 Base64（等同明文）：读出来交给界面，界面回写时会自动升级成加密格式。
    return try {
        String(Base64.decode(stored, Base64.DEFAULT), Charsets.UTF_8)
    } catch (_: Exception) {
        ""
    }
}

fun savePassword(context: Context, pwd: String) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
    if (pwd.isEmpty()) {
        sp.remove(KEY_PASSWORD).apply()
        return
    }
    val enc = encryptPassword(pwd)
    if (enc != null) {
        sp.putString(KEY_PASSWORD, enc).apply()
    } else {
        // 加密失败就干脆不保存（下次启动重新输入），绝不退化成明文/Base64。
        sp.remove(KEY_PASSWORD).apply()
    }
}

// ==================== 主界面 ====================
@Composable
fun ScheduleMainScreen() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // ---------- 多课表 ----------
    val books = remember { mutableStateListOf<ScheduleBook>() }
    var currentBookId by remember { mutableStateOf("") }
    var totalWeeks by remember { mutableIntStateOf(20) }

    var courseData = remember { mutableStateListOf<Course>() }
    var startDateText by remember { mutableStateOf(loadStartDate(ctx)) }
    var displayWeek by remember { mutableIntStateOf(loadLastWeek(ctx)) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var fontScale by remember { mutableStateOf(loadFontScale(ctx)) }
    var rowHeightDp by remember { mutableIntStateOf(loadRowHeight(ctx)) }
    var showSettings by remember { mutableStateOf(false) }
    var showWeekPicker by remember { mutableStateOf(false) }
    // 主界面整页滚动状态
    val mainScroll = rememberScrollState()
    // 左右滑切换周：整页跟手偏移（阻尼 0.35，最多 1/3 屏宽），松手回弹
    val swipeOffset = remember { mutableFloatStateOf(0f) }
    val swipeScope = rememberCoroutineScope()
    // ---------- 切换周数的动画 ----------
    var weekAnim by remember { mutableStateOf(loadWeekAnim(ctx)) }
    var animFromWeek by remember { mutableIntStateOf(-1) }   // 动画里的「旧周」，-1 = 没在动画
    var animDir by remember { mutableIntStateOf(1) }         // +1：新课表从右边进来
    var animT by remember { mutableFloatStateOf(1f) }        // 动画进度 0 → 1
    var animTick by remember { mutableIntStateOf(0) }        // 每自增一次跑一遍动画
    var digitT by remember { mutableFloatStateOf(1f) }        // 周数数字跳动进度
    var digitTick by remember { mutableIntStateOf(0) }

    LaunchedEffect(weekAnim) { saveWeekAnim(ctx, weekAnim) }

    // 换周统一入口：按钮 / 手势 / 周次面板 / 双击都走这里，动画按当前设置对号入座
    val goWeek: (Int) -> Unit = { raw ->
        val target = raw.coerceIn(1, totalWeeks.coerceAtLeast(1))
        if (target != displayWeek) {
            if (weekAnimHasSlide(weekAnim)) {
                animDir = if (target > displayWeek) 1 else -1
                animFromWeek = displayWeek
                displayWeek = target
                animT = 0f
                animTick++
            } else {
                displayWeek = target
            }
            if (weekAnimHasDigit(weekAnim)) {
                digitT = 0f
                digitTick++
            }
        }
    }

    // 推入 / 淡入 / 翻牌：把进度从 0 跑到 1，跑完把「旧周」撤掉
    LaunchedEffect(animTick) {
        if (animTick == 0) return@LaunchedEffect
        animT = 0f
        animate(0f, 1f, animationSpec = tween(WEEK_ANIM_DUR_MS)) { v, _ -> animT = v }
        animT = 1f
        animFromWeek = -1
    }

    // 周数数字跳动：0 → 1 走一遍，缩放用 sin 做出「放大再回来」
    LaunchedEffect(digitTick) {
        if (digitTick == 0) return@LaunchedEffect
        digitT = 0f
        animate(0f, 1f, animationSpec = tween(260)) { v, _ -> digitT = v }
        digitT = 1f
    }
    // 顶部「第 N 周」那一行：手指往上划要"顶住"一段距离才收起；手指往下拉也要"顶住"一段才滑出来。
    // 顶住期间整页一点都不滚（这就是阻塞感），那一行最多跟手偏移 6dp；松手 220ms 没接着划就弹回。
    var topBarWanted by remember { mutableStateOf(true) }
    val topBarH by animateDpAsState(targetValue = if (topBarWanted) 52.dp else 0.dp)
    val density = LocalDensity.current
    val collapseDistPx = with(density) { 120.dp.toPx() }   // 上划：要顶住这么远才收起
    val expandDistPx = with(density) { 150.dp.toPx() }     // 下拉：要顶住这么远才滑出
    val resistMaxPx = with(density) { 6.dp.toPx() }        // 顶住时那一行最多跟手偏移多少
    // 下面这几个每帧都在变，故意不进 composition：蓄力值用普通数组装，跟手偏移只在 layout 阶段读
    val resist = remember { FloatArray(1) }                // [0] 本次手势已顶住的距离(px)
    val resistAt = remember { longArrayOf(0L) }            // 最近一次顶住的时间（Long，不能塞进 FloatArray）
    val resistOffsetPx = remember { mutableFloatStateOf(0f) }  // 那一行的跟手偏移(px)
    val topBarNestedScroll = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val dy = available.y
                if (dy <= -0.5f) {
                    // 手指往上划：顶栏还挂着、页面也没到底 → 先把这段滚动全吃掉，顶住
                    if (!topBarWanted || mainScroll.value >= mainScroll.maxValue) {
                        resist[0] = 0f
                        resistOffsetPx.floatValue = 0f
                        return Offset.Zero
                    }
                    resistAt[0] = System.currentTimeMillis()
                    resist[0] += dy
                    if (resist[0] <= -collapseDistPx) {
                        topBarWanted = false
                        resist[0] = 0f
                        resistOffsetPx.floatValue = 0f
                    } else {
                        resistOffsetPx.floatValue = (-resistMaxPx * (-resist[0] / collapseDistPx)).coerceIn(-resistMaxPx, 0f)
                    }
                    return Offset(0f, dy)
                } else if (dy >= 0.5f) {
                    // 手指往下拉：顶栏已经收起、页面也已经在最顶上 → 先把这段滚动全吃掉，顶住
                    if (topBarWanted || mainScroll.value > 0f) {
                        resist[0] = 0f
                        resistOffsetPx.floatValue = 0f
                        return Offset.Zero
                    }
                    resistAt[0] = System.currentTimeMillis()
                    resist[0] += dy
                    if (resist[0] >= expandDistPx) {
                        topBarWanted = true
                        resist[0] = 0f
                        resistOffsetPx.floatValue = 0f
                    } else {
                        resistOffsetPx.floatValue = (resistMaxPx * (resist[0] / expandDistPx)).coerceIn(0f, resistMaxPx)
                    }
                    return Offset(0f, dy)
                }
                return Offset.Zero
            }
        }
    }
    // 松手 220ms 没有接着划 → 顶住的蓄力和跟手偏移弹回去（用轮询，避免每帧触发重组）
    LaunchedEffect(Unit) {
        while (true) {
            delay(60)
            if (resist[0] != 0f && System.currentTimeMillis() - resistAt[0] > 220L) {
                resist[0] = 0f
                resistOffsetPx.floatValue = 0f
            }
        }
    }
    // 课程数据版本号：只要课表内容被换过就 +1，用来驱动落盘（避免"条数没变就不保存"）
    var dataRev by remember { mutableIntStateOf(0) }
    var widgetFonts by remember { mutableStateOf(loadAllWidgetFontScales(ctx)) }
    // ---------- 课表节数与上课时间（当前课表自己那一套） ----------
    var timeCfg by remember { mutableStateOf(loadTimeConfig(ctx, loadCurrentBookId(ctx))) }

    // ---------- 课表背景 ----------
    var bgImagePath by remember { mutableStateOf(loadBgImage(ctx)) }
    var cellAlpha by remember { mutableStateOf(loadCellAlpha(ctx)) }
    var borderAlpha by remember { mutableStateOf(loadBorderAlpha(ctx)) }
    var indexColW by remember { mutableIntStateOf(loadIndexColWidth(ctx)) }
    // ---------- 小部件背景：正在给哪个组件选图 + 版本号（选完 / 移除后让设置页重画） ----------
    var widgetBgTargetId by remember { mutableIntStateOf(-1) }
    var widgetBgRev by remember { mutableIntStateOf(0) }
    var bgBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    var jwUsername by remember { mutableStateOf(loadUsername(ctx)) }
    var jwPassword by remember { mutableStateOf(loadPassword(ctx)) }
    var autoUpdateJw by remember { mutableStateOf(loadAutoUpdate(ctx)) }
    var isFetching by remember { mutableStateOf(false) }

    // ---------- 课表增删改切 ----------
    val snack: (String) -> Unit = { msg ->
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }

    // 把界面上的「当前课表」写回课表列表并落盘
    val syncCurrentBook: () -> Unit = {
        val i = books.indexOfFirst { it.id == currentBookId }
        if (i >= 0) {
            books[i] = books[i].copy(
                startDate = startDateText,
                totalWeeks = totalWeeks,
                courses = courseData.toList()
            )
        }
        if (books.isNotEmpty()) saveBooks(ctx, books.toList(), currentBookId)
    }

    // 把某个课表的数据装到界面上
    val applyBook: (ScheduleBook) -> Unit = { b ->
        currentBookId = b.id
        // 换成这个课表时，节数 / 上课时间也跟着换成它自己那套
        timeCfg = loadTimeConfig(ctx, b.id)
        courseData.clear()
        courseData.addAll(b.courses)
        dataRev++
        startDateText = if (b.startDate.isBlank()) LocalDate.now().format(START_DATE_STORE_FMT) else b.startDate
        totalWeeks = b.totalWeeks.coerceIn(1, 30)
        if (displayWeek > totalWeeks) displayWeek = totalWeeks
        if (displayWeek < 1) displayWeek = 1
        saveStartDate(ctx, startDateText)
        saveToLocal(ctx, startDateText, b.courses, displayWeek)
    }

    val selectBook: (String) -> Unit = { id ->
        syncCurrentBook()
        books.firstOrNull { it.id == id }?.let { b -> applyBook(b) }
    }

    val addBook: () -> Unit = {
        syncCurrentBook()
        val today = LocalDate.now().format(START_DATE_STORE_FMT)
        val nb = ScheduleBook(
            id = "b" + System.currentTimeMillis().toString(),
            name = "",
            startDate = today,
            totalWeeks = 20,
            courses = emptyList()
        )
        books.add(nb)
        applyBook(nb)
        // 新课表给一套默认的课时时间（上午 5 节 / 下午 4 节 / 晚上 2 节，8:00 起、每节 40 分钟、课间 10 分钟）
        val dft = defaultTimeConfig()
        timeCfg = dft
        saveTimeConfig(ctx, dft, nb.id)
        displayWeek = 1
        saveBooks(ctx, books.toList(), nb.id)
        snack("已添加新课表（没命名时按顺序叫「课表" + books.size + "」）")
    }

    val renameBook: (String, String) -> Unit = { id, newName ->
        val i = books.indexOfFirst { it.id == id }
        if (i >= 0) {
            books[i] = books[i].copy(name = newName.trim())
            saveBooks(ctx, books.toList(), currentBookId)
        }
    }

    val deleteBook: (String) -> Unit = { id ->
        if (books.size <= 1) {
            snack("至少要保留一个课表")
        } else {
            val i = books.indexOfFirst { it.id == id }
            if (i >= 0) {
                val wasCurrent = id == currentBookId
                books.removeAt(i)
                // 顺手清掉这个课表单独存的节数 / 时间
                clearTimeConfig(ctx, id)
                if (wasCurrent) {
                    books.firstOrNull()?.let { b -> applyBook(b) }
                } else {
                    saveBooks(ctx, books.toList(), currentBookId)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val loaded = loadBooks(ctx)
        books.clear()
        books.addAll(loaded)
        val savedId = loadCurrentBookId(ctx)
        val cur = loaded.firstOrNull { it.id == savedId } ?: loaded.firstOrNull()
        if (cur != null) applyBook(cur)
        displayWeek = loadLastWeek(ctx).coerceIn(1, totalWeeks.coerceAtLeast(1))
    }

    // 当前课表状态一变 → 写回课表列表；同时刷新「镜像」数据（桌面小组件读的就是镜像）
    LaunchedEffect(currentBookId, startDateText, totalWeeks, dataRev, displayWeek) {
        if (currentBookId.isBlank()) return@LaunchedEffect
        syncCurrentBook()
        saveToLocal(ctx, startDateText, courseData.toList(), displayWeek)
        // 第 44 批新增：课表 / 开学时间 / 周数一变，上课提醒也跟着重排（镜像刚写，读到的是新数据）
        scheduleClassReminder(ctx)
        // 第 45 批：上课静音 / 下课恢复的闹钟同样跟着重排
        scheduleClassMute(ctx)
    }

    // 学习周数调小了 → 当前周次跟着收回来
    LaunchedEffect(totalWeeks) {
        if (displayWeek > totalWeeks) displayWeek = totalWeeks
        if (displayWeek < 1) displayWeek = 1
    }

    // 背景图：路径一变就存盘并重新解码
    LaunchedEffect(bgImagePath) {
        saveBgImage(ctx, bgImagePath)
        bgBitmap = if (bgImagePath.isBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) { decodeBgImage(bgImagePath) }
        }
    }

    LaunchedEffect(cellAlpha) { saveCellAlpha(ctx, cellAlpha) }
    LaunchedEffect(borderAlpha) { saveBorderAlpha(ctx, borderAlpha) }
    LaunchedEffect(indexColW) { saveIndexColWidth(ctx, indexColW) }

    LaunchedEffect(jwUsername) { saveUsername(ctx, jwUsername) }
    LaunchedEffect(jwPassword) { savePassword(ctx, jwPassword) }
    LaunchedEffect(autoUpdateJw) { saveAutoUpdate(ctx, autoUpdateJw) }
    LaunchedEffect(fontScale) { saveFontScale(ctx, fontScale) }
    LaunchedEffect(rowHeightDp) { saveRowHeight(ctx, rowHeightDp) }

    // 打开软件自动更新课表：开关打开 + 已存账号密码 + 今天还没自动更新过 → 静默拉一次
    LaunchedEffect(Unit) {
        if (!loadAutoUpdate(ctx)) return@LaunchedEffect
        val u = loadUsername(ctx)
        val p = loadPassword(ctx)
        if (u.isBlank() || p.isBlank()) return@LaunchedEffect
        val today = java.time.LocalDate.now().toString()
        if (loadLastAutoUpdate(ctx) == today) return@LaunchedEffect
        val r = withContext(Dispatchers.IO) { runCatching { fetchScheduleFromJw(u, p) } }
        r.onSuccess { list ->
            if (list.isNotEmpty()) {
                courseData.clear()
                courseData.addAll(list)
                dataRev++
                saveToLocal(ctx, startDateText, list, displayWeek)
                saveLastAutoUpdate(ctx, today)
                Toast.makeText(ctx, "课表已自动更新，共" + list.size + "条", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 开学日期一变就存盘（没课表数据时也能保存）
    LaunchedEffect(startDateText) { saveStartDate(ctx, startDateText) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                selectedFileUri = uri
                Toast.makeText(ctx, "正在解析课表...", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // 选一张图当课表背景（拷进 App 私有目录，长期有效）
    val bgPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                val p = importBgImage(ctx, uri)
                if (p.isBlank()) {
                    Toast.makeText(ctx, "图片读取失败，换一张试试", Toast.LENGTH_SHORT).show()
                } else {
                    bgImagePath = p
                    Toast.makeText(ctx, "背景已设置", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    // 给「某一个」组件选背景图（拷进私有目录，文件名带时间戳）
    val widgetBgPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            val target = widgetBgTargetId
            if (uri != null && target >= 0) {
                val p = importWidgetBgImage(ctx, uri)
                if (p.isBlank()) {
                    Toast.makeText(ctx, "图片读取失败，换一张试试", Toast.LENGTH_SHORT).show()
                } else {
                    saveWidgetBgImageForId(ctx, target, p)
                    widgetBgRev++
                    Toast.makeText(ctx, "这个组件的背景已设置", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    LaunchedEffect(selectedFileUri) {
        val uri = selectedFileUri ?: return@LaunchedEffect
        val text = readTextFromUri(ctx, uri)
        if (text.isNullOrBlank()) {
            Toast.makeText(ctx, "读取文件失败", Toast.LENGTH_LONG).show()
        } else {
            val parsed = parseScheduleHtml(text)
            courseData.clear()
            courseData.addAll(parsed)
            dataRev++
            Toast.makeText(ctx, "解析完成，共${parsed.size}条课程，已自动保存", Toast.LENGTH_LONG).show()
        }
    }

    // ==================== 设置页：独立页面 ====================
    if (showSettings) {
        SettingsScreen(
            startDateText = startDateText,
            onStartDateChange = { startDateText = it },
            fontScale = fontScale,
            onFontScaleChange = { fontScale = it },
            rowHeightDp = rowHeightDp,
            onRowHeightChange = { rowHeightDp = it },
            jwUsername = jwUsername,
            onUsernameChange = { jwUsername = it },
            jwPassword = jwPassword,
            onPasswordChange = { jwPassword = it },
            books = books,
            currentBookId = currentBookId,
            onSelectBook = { selectBook(it) },
            onAddBook = { addBook() },
            onRenameBook = { id, name -> renameBook(id, name) },
            onDeleteBook = { deleteBook(it) },
            totalWeeks = totalWeeks,
            onTotalWeeksChange = { totalWeeks = it.coerceIn(1, 30) },
            bgImagePath = bgImagePath,
            cellAlpha = cellAlpha,
            onCellAlphaChange = { cellAlpha = it },
            borderAlpha = borderAlpha,
            onBorderAlphaChange = { borderAlpha = it },
            indexColWidthDp = indexColW,
            onIndexColWidthChange = { indexColW = it },
            weekAnim = weekAnim,
            onWeekAnimChange = { weekAnim = it },
            onPickBgImage = { bgPicker.launch("image/*") },
            onClearBgImage = { bgImagePath = "" },
            widgetBgRev = widgetBgRev,
            onWidgetCellAlphaChange = { id, v -> saveWidgetCellAlphaForId(ctx, id, v) },
            onPickWidgetBgImage = { id ->
                widgetBgTargetId = id
                widgetBgPicker.launch("image/*")
            },
            onClearWidgetBgImage = { id ->
                clearWidgetBgImageForId(ctx, id)
                widgetBgRev++
            },
            onWidgetBgModeChange = { id, mode ->
                saveWidgetBgModeForId(ctx, id, mode)
                widgetBgRev++
            },
            onWidgetColorChange = { id, slot, c -> saveWidgetColorForId(ctx, id, slot, c) },
            onWidgetColorReset = { id, slot -> resetWidgetColorForId(ctx, id, slot) },
            // 第 44 批新增：分享码导入（另存为新课表 / 并入当前课表）
            onImportBook = { st ->
                syncCurrentBook()
                val nb = ScheduleBook(
                    id = "b" + System.currentTimeMillis().toString(),
                    name = st.name,
                    startDate = st.startDate.ifBlank { LocalDate.now().format(START_DATE_STORE_FMT) },
                    totalWeeks = st.totalWeeks,
                    courses = st.courses
                )
                books.add(nb)
                applyBook(nb)
                displayWeek = 1
                saveBooks(ctx, books.toList(), nb.id)
                Toast.makeText(
                    ctx,
                    "已导入为新课表：" + bookDisplayName(books, books.indexOfFirst { it.id == nb.id }),
                    Toast.LENGTH_LONG
                ).show()
                // 第 45 批：设计稿要求「冲突检测改成导入课表时顺便检测」
                val cfsImport = findCourseConflicts(st.courses)
                if (cfsImport.isNotEmpty()) {
                    Toast.makeText(
                        ctx,
                        "提醒：新课表里有 " + cfsImport.size + " 处时间冲突（同一时间有两门课）",
                        Toast.LENGTH_LONG
                    ).show()
                }
                scheduleClassReminder(ctx)
            },
            onMergeCourses = { list ->
                if (list.isEmpty()) {
                    Toast.makeText(ctx, "没有新课程可并入", Toast.LENGTH_SHORT).show()
                } else {
                    courseData.addAll(list)
                    dataRev++
                    // 立刻把镜像数据写一遍，下面重排提醒时才找得到刚并进来的课
                    saveToLocal(ctx, startDateText, courseData.toList(), displayWeek)
                    // 第 45 批：并入后顺便检测一次冲突
                    val cfsMerge = findCourseConflicts(courseData.toList())
                    Toast.makeText(
                        ctx,
                        "已并入 " + list.size + " 门课" +
                                (if (cfsMerge.isEmpty()) "" else "，检测到 " + cfsMerge.size + " 处时间冲突"),
                        if (cfsMerge.isEmpty()) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
                    ).show()
                    scheduleClassReminder(ctx)
                }
            },
            isFetching = isFetching,
            onImportFile = { filePicker.launch(arrayOf("text/plain", "text/html")) },
            autoUpdateJw = autoUpdateJw,
            onAutoUpdateChange = { autoUpdateJw = it },
            onFetchFromJw = {
                if (!isFetching) {
                    if (jwUsername.isBlank() || jwPassword.isBlank()) {
                        Toast.makeText(ctx, "请先填写学号和密码", Toast.LENGTH_SHORT).show()
                    } else {
                        isFetching = true
                        Toast.makeText(ctx, "正在登录教务系统...", Toast.LENGTH_SHORT).show()
                        scope.launch {
                            val result = withContext(Dispatchers.IO) {
                                runCatching { fetchScheduleFromJw(jwUsername, jwPassword) }
                            }
                            isFetching = false
                            result.onSuccess { list ->
                                if (list.isEmpty()) {
                                    Toast.makeText(ctx, "获取失败或课表为空，请检查账号密码", Toast.LENGTH_LONG).show()
                                } else {
                                    courseData.clear()
                                    courseData.addAll(list)
                                    dataRev++
                                    saveToLocal(ctx, startDateText, list, displayWeek)
                                    Toast.makeText(ctx, "获取成功，共${list.size}条课程", Toast.LENGTH_LONG).show()
                                }
                            }.onFailure { e ->
                                val msg = when {
                                    e.message?.contains("Failed to connect", true) == true ||
                                            e.message?.contains("Unable to resolve host", true) == true ->
                                        "无法连接教务服务器，请切换 WiFi/流量后重试"
                                    e.message?.contains("timeout", true) == true ->
                                        "连接超时，请检查网络"
                                    e.message?.contains("No address associated", true) == true ->
                                        "DNS 解析失败，请检查网络"
                                    else -> e.message ?: "未知错误"
                                }
                                Toast.makeText(ctx, "错误: $msg", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            },
            onBack = { showSettings = false },
            widgetFonts = widgetFonts,
            timeCfg = timeCfg,
            onTimeCfgChange = { cfg ->
                val n = normalizeTimeConfig(cfg)
                timeCfg = n
                saveTimeConfig(ctx, n, currentBookId)
                // 第 44 批新增：课时时间改了，提醒时刻也跟着变
                scheduleClassReminder(ctx)
            },
            onWidgetFontChange = { key, id, v ->
                if (id >= 0) {
                    saveWidgetFontScaleForId(ctx, key, id, v)
                } else {
                    widgetFonts = widgetFonts + (key to v)
                    saveWidgetFontScale(ctx, key, v)
                }
            },
            onAddCourse = { c ->
                courseData.add(c)
                dataRev++
                // 第 44 批新增：加完课顺手报一下有没有跟别的课撞时间
                val hint = conflictHintText(conflictsWith(courseData.toList(), c), c)
                Toast.makeText(
                    ctx,
                    if (hint.isBlank()) "已添加：" + c.name else hint,
                    Toast.LENGTH_LONG
                ).show()
                scheduleClassReminder(ctx)
            },
            onUpdateCourse = { idx, c ->
                if (idx in courseData.indices) {
                    courseData[idx] = c
                    dataRev++
                    val others = courseData.filterIndexed { i, _ -> i != idx }
                    val hint = conflictHintText(conflictsWith(others, c), c)
                    Toast.makeText(
                        ctx,
                        if (hint.isBlank()) "已修改：" + c.name else hint,
                        Toast.LENGTH_LONG
                    ).show()
                    scheduleClassReminder(ctx)
                }
            },
            courses = courseData,
            onDeleteCourse = { idx ->
                if (idx in courseData.indices) {
                    val nm = courseData[idx].name
                    courseData.removeAt(idx)
                    dataRev++
                    Toast.makeText(ctx, "已删除：" + nm, Toast.LENGTH_SHORT).show()
                    scheduleClassReminder(ctx)
                }
            }
        )
        return
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(topBarNestedScroll)
                // 左右滑切换周：普通模式滑过约 1/12 屏宽（或快速轻甩）就翻周；
                // 「跟手分页」模式下课表 1:1 跟手指走，松手超 1/8 屏宽就翻周、不够就弹回
                .pointerInput(Unit) {
                    var acc = 0f
                    var startAt = 0L
                    val easeBack: () -> Unit = {
                        swipeScope.launch {
                            animate(swipeOffset.floatValue, 0f, animationSpec = tween(140)) { v, _ ->
                                swipeOffset.floatValue = v
                            }
                        }
                    }
                    detectHorizontalDragGestures(
                        onDragStart = {
                            acc = 0f
                            startAt = System.currentTimeMillis()
                        },
                        onDragEnd = {
                            if (weekAnim == WEEK_ANIM_DRAG) {
                                val w = size.width.toFloat()
                                val thr = w / 8f
                                if (acc <= -thr) {
                                    swipeScope.launch {
                                        animate(swipeOffset.floatValue, -w, animationSpec = tween(110)) { v, _ ->
                                            swipeOffset.floatValue = v
                                        }
                                        displayWeek = (displayWeek + 1).coerceAtMost(totalWeeks)
                                        swipeOffset.floatValue = w
                                        animate(w, 0f, animationSpec = tween(170)) { v, _ ->
                                            swipeOffset.floatValue = v
                                        }
                                    }
                                } else if (acc >= thr) {
                                    swipeScope.launch {
                                        animate(swipeOffset.floatValue, w, animationSpec = tween(110)) { v, _ ->
                                            swipeOffset.floatValue = v
                                        }
                                        displayWeek = (displayWeek - 1).coerceAtLeast(1)
                                        swipeOffset.floatValue = -w
                                        animate(-w, 0f, animationSpec = tween(170)) { v, _ ->
                                            swipeOffset.floatValue = v
                                        }
                                    }
                                } else {
                                    easeBack()
                                }
                                acc = 0f
                            } else {
                                val threshold = size.width / 12f
                                val flick = size.width / 25f
                                val quick = System.currentTimeMillis() - startAt < 220L
                                if (acc <= -threshold || (quick && acc <= -flick)) {
                                    goWeek(displayWeek + 1)
                                } else if (acc >= threshold || (quick && acc >= flick)) {
                                    goWeek(displayWeek - 1)
                                }
                                acc = 0f
                                easeBack()
                            }
                        },
                        onDragCancel = {
                            acc = 0f
                            easeBack()
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            acc += dragAmount
                            swipeOffset.floatValue = if (weekAnim == WEEK_ANIM_DRAG) {
                                acc.coerceIn(-size.width.toFloat(), size.width.toFloat())
                            } else {
                                (acc * 0.35f).coerceIn(-size.width / 3f, size.width / 3f)
                            }
                        }
                    )
                }
                .graphicsLayer { translationX = swipeOffset.floatValue }
                .verticalScroll(mainScroll)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            val screenWidth = maxWidth
            val isSmallScreen = screenWidth < 380.dp

            // 界面控件字号：固定值，不随「课表字体」缩放
            val uiTitleTextSize = if (isSmallScreen) 13.sp else 18.sp

            // 课表字号：只有这 4 个跟着「课表字体」缩放
            val scaledCourseNameSize = (if (isSmallScreen) 9.sp else 10.sp) * fontScale
            val scaledCourseInfoSize = (if (isSmallScreen) 8.sp else 9.sp) * fontScale
            val scaledSectionTextSize = (if (isSmallScreen) 12.sp else 13.sp) * fontScale
            val scaledHeaderTextSize = (if (isSmallScreen) 10.sp else 11.sp) * fontScale

            Column {
                // 顶部这一行：手指往上划要顶住 120dp 才收起（把高度让给课表），往下拉要顶住 150dp 才滑出来；
                // 左上角的周数数字（红色）一直都在
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topBarH)
                        .clipToBounds()
                ) {
                    Column(modifier = Modifier.offset { IntOffset(0, resistOffsetPx.floatValue.toInt()) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { goWeek(displayWeek - 1) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF2F2F2),
                                    contentColor = Color(0xFF263238)
                                ),
                                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                                modifier = Modifier.height(44.dp)
                            ) {
                                Text("←", fontSize = 18.sp)
                            }
                            Text(
                                "第 $displayWeek 周",
                                fontSize = uiTitleTextSize,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .graphicsLayer {
                                        // 周数跳动：放大到 +28% 再回来（只在选了带跳动的动画时生效）
                                        if (weekAnimHasDigit(weekAnim)) {
                                            val q = digitT.coerceIn(0f, 1f)
                                            val s = 1f + 0.28f * (if (q < 0.5f) q * 2f else (1f - q) * 2f)
                                            scaleX = s
                                            scaleY = s
                                        }
                                    }
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF2F2F2))
                                    // 单击：弹出周次面板（1 ~ 学习周数）；双击：切回本周
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = { showWeekPicker = !showWeekPicker },
                                            onDoubleTap = {
                                                goWeek(calcCurrentWeek(startDateText))
                                                showWeekPicker = false
                                            }
                                        )
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                            Button(
                                onClick = { goWeek(displayWeek + 1) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF2F2F2),
                                    contentColor = Color(0xFF263238)
                                ),
                                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                                modifier = Modifier.height(44.dp)
                            ) {
                                Text("→", fontSize = 18.sp)
                            }
                            IconButton(
                                onClick = { showSettings = true },
                                modifier = Modifier.height(44.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_settings),
                                    contentDescription = "设置",
                                    tint = Color(0xFF37474F)
                                )
                            }
                        }
                    Spacer(Modifier.height(8.dp))
                    }
                }

                if (showWeekPicker) {
                    val pickerRows = (totalWeeks + 4) / 5
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            for (row in 0 until pickerRows) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    for (col in 0 until 5) {
                                        val wk = row * 5 + col + 1
                                        if (wk > totalWeeks) {
                                            Spacer(Modifier.weight(1f))
                                        } else Button(
                                            onClick = {
                                                goWeek(wk)
                                                showWeekPicker = false
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(40.dp),
                                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (wk == displayWeek) Color(0xFF37474F) else Color(0xFFECEFF1),
                                                contentColor = if (wk == displayWeek) Color.White else Color(0xFF263238)
                                            )
                                        ) {
                                            Text("$wk", fontSize = 15.sp)
                                        }
                                    }
                                }
                                if (row < pickerRows - 1) Spacer(Modifier.height(6.dp))
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "点数字切到对应周次（共 " + totalWeeks + " 周）；双击顶部「第 N 周」直接切回本周",
                                fontSize = 11.sp,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                if (displayWeek > totalWeeks) {
                    Text(
                        "第 $displayWeek 周已超过本课表的学习周数（共 $totalWeeks 周），这周没有课",
                        fontSize = 12.sp,
                        color = Color(0xFFB00020)
                    )
                    Spacer(Modifier.height(6.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    val bmp = bgBitmap
                    if (bmp != null) {
                        Image(
                            bitmap = bmp,
                            contentDescription = null,
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    // 切换周数的动画：动画进行时同时画「旧周」「新周」两张表，各走各的画面变换
                    val animRunning = animFromWeek >= 0 && weekAnimHasSlide(weekAnim)
                    val slidePx = with(density) { screenWidth.toPx() }
                    if (animRunning) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    weekAnimLayer(true, weekAnim, animT, animDir, slidePx)
                                }
                        ) {
                            ScheduleTable(
                                targetWeek = animFromWeek,
                                startDateStr = startDateText,
                                courses = courseData,
                                totalWeeks = totalWeeks,
                                sectionCounts = timeCfg.counts,
                                sectionTimes = timeCfg.times,
                                cellAlpha = cellAlpha,
                                borderAlpha = borderAlpha,
                                hasBackground = (bmp != null),
                                indexColWidth = if (indexColW > 0) indexColW.dp
                                    else if (isSmallScreen) 54.dp else 60.dp,
                                isSmallScreen = isSmallScreen,
                                fontScale = fontScale,
                                rowHeight = rowHeightDp.dp,
                                courseNameSize = scaledCourseNameSize,
                                courseInfoSize = scaledCourseInfoSize,
                                sectionTextSize = scaledSectionTextSize,
                                sectionTimeSize = (if (isSmallScreen) 7.sp else 8.sp) * fontScale,
                                headerTextSize = scaledHeaderTextSize,
                                onResetToCurrentWeek = {}
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    weekAnimLayer(false, weekAnim, animT, animDir, slidePx)
                                }
                        ) {
                            ScheduleTable(
                                targetWeek = displayWeek,
                                startDateStr = startDateText,
                                courses = courseData,
                                totalWeeks = totalWeeks,
                                sectionCounts = timeCfg.counts,
                                sectionTimes = timeCfg.times,
                                cellAlpha = cellAlpha,
                                borderAlpha = borderAlpha,
                                hasBackground = (bmp != null),
                                indexColWidth = if (indexColW > 0) indexColW.dp
                                    else if (isSmallScreen) 54.dp else 60.dp,
                                isSmallScreen = isSmallScreen,
                                fontScale = fontScale,
                                rowHeight = rowHeightDp.dp,
                                courseNameSize = scaledCourseNameSize,
                                courseInfoSize = scaledCourseInfoSize,
                                sectionTextSize = scaledSectionTextSize,
                                sectionTimeSize = (if (isSmallScreen) 7.sp else 8.sp) * fontScale,
                                headerTextSize = scaledHeaderTextSize,
                                onResetToCurrentWeek = { goWeek(calcCurrentWeek(startDateText)) }
                            )
                        }
                    } else {
                        ScheduleTable(
                            targetWeek = displayWeek,
                            startDateStr = startDateText,
                            courses = courseData,
                            totalWeeks = totalWeeks,
                            sectionCounts = timeCfg.counts,
                            sectionTimes = timeCfg.times,
                            cellAlpha = cellAlpha,
                            borderAlpha = borderAlpha,
                            hasBackground = (bmp != null),
                            indexColWidth = if (indexColW > 0) indexColW.dp
                                else if (isSmallScreen) 54.dp else 60.dp,
                            isSmallScreen = isSmallScreen,
                            fontScale = fontScale,
                            rowHeight = rowHeightDp.dp,
                            courseNameSize = scaledCourseNameSize,
                            courseInfoSize = scaledCourseInfoSize,
                            sectionTextSize = scaledSectionTextSize,
                            sectionTimeSize = (if (isSmallScreen) 7.sp else 8.sp) * fontScale,
                            headerTextSize = scaledHeaderTextSize,
                            onResetToCurrentWeek = { goWeek(calcCurrentWeek(startDateText)) }
                        )
                    }
                }

                Spacer(Modifier.height(200.dp))
            }
        }
    }
}

@Composable
fun ScheduleTable(
    targetWeek: Int,
    startDateStr: String,
    courses: List<Course>,
    totalWeeks: Int,
    sectionCounts: List<Int>,
    sectionTimes: List<String>,
    cellAlpha: Float,
    borderAlpha: Float,
    hasBackground: Boolean,
    indexColWidth: Dp,
    isSmallScreen: Boolean,
    fontScale: Float,
    rowHeight: Dp,
    courseNameSize: androidx.compose.ui.unit.TextUnit,
    courseInfoSize: androidx.compose.ui.unit.TextUnit,
    sectionTextSize: androidx.compose.ui.unit.TextUnit,
    sectionTimeSize: androidx.compose.ui.unit.TextUnit,
    headerTextSize: androidx.compose.ui.unit.TextUnit,
    // 双击表头左上角那个红色周数 → 回到本周
    onResetToCurrentWeek: () -> Unit = {}
) {
    val mondayDate = getWeekMonday(startDateStr, targetWeek)
    val today = LocalDate.now()
    val weekIndexMap = mapOf("一" to 0, "二" to 1, "三" to 2, "四" to 3, "五" to 4, "六" to 5, "日" to 6)

    // 超过设定的学习周数 → 整个课表都是空的
    val weekValid = targetWeek <= totalWeeks
    val saturdayCourses = weekValid && courses.any { it.weekCn == "六" && targetWeek in it.weekRangeList }
    val sundayCourses = weekValid && courses.any { it.weekCn == "日" && targetWeek in it.weekRangeList }

    val visibleWeekCns = mutableListOf("一", "二", "三", "四", "五")
    if (saturdayCourses) visibleWeekCns.add("六")
    if (sundayCourses) visibleWeekCns.add("日")

    val weekTitleList = visibleWeekCns.map {
        when (it) {
            "一" -> "周一"; "二" -> "周二"; "三" -> "周三"; "四" -> "周四"
            "五" -> "周五"; "六" -> "周六"; "日" -> "周日"; else -> it
        }
    }

    val weekDateObjList = visibleWeekCns.map { cn ->
        val offset = weekIndexMap[cn] ?: 0
        mondayDate.plusDays(offset.toLong())
    }
    val weekDateList = weekDateObjList.map { "${it.monthValue}-${it.dayOfMonth}" }

    val headerHeight = if (isSmallScreen) 48.dp else 56.dp
    // 骨架色：表头（周数 / 日期）、节次列、午休 / 晚休三条带共用同一个值。
    // 设了背景图时用半透明白：能透出背景，同时保证压在照片上的字看得清。
    // 之所以能做到三处同色，是因为午休 / 晚休不再压在节次列上（见下方 sepTops 的写法），
    // 三处都是「一层」半透明，不会再出现叠两层、左边偏白的情况。
    // 没设背景图时保持透明（就是原来的白底）。
    // 「边界」底色：不透明度 1.0 → 米色实色（把背景图挡住），0.78 → 白 0.78（升级前的样子），0 → 完全透明。
    // 越接近完全不透明，颜色越往米色走（纯白实色压在照片上太扎眼）。
    val borderT = (1f - borderAlpha).coerceIn(0f, 1f)
    val borderBase =
        if (borderT >= 0.22f) Color.White
        else androidx.compose.ui.graphics.lerp(Color(0xFFF7F3EA), Color.White, borderT / 0.22f)
    val frameColor = if (hasBackground) borderBase.copy(alpha = borderAlpha) else Color.Transparent
    // 午休 / 晚休横条的底色：有背景图时就等于表头 / 节次列那个颜色；
    // 没背景图时给一层很淡的灰，免得整条消失。
    val sepBarColor = if (hasBackground) frameColor else Color(0xFFF5F5F5).copy(alpha = borderAlpha)
    // 午休 / 晚休带上下两端那条线：有背景图时用白线，没背景图时用淡灰线（白底上白线看不见）
    val sepLineColor = if (hasBackground) Color.White else Color(0xFFD9D9D9)

    val gridLine = Color(0xFFE6E6E6)
    // 整张课表是「一个整体」：上午 / 下午 / 晚休的节次连续排下来，
    // 中间只插入「午休」「晚休」两条分隔行；这样跨段的课（比如 1~11 节）能显示成完整的一块。
    // 每段几节由设置里的「课表节数」决定；某段 0 节就整段不出现
    val gMorning = sectionCounts.getOrElse(0) { 5 }.coerceIn(0, MAX_SECTION_PER_GROUP)
    val gAfternoon = sectionCounts.getOrElse(1) { 4 }.coerceIn(0, MAX_SECTION_PER_GROUP)
    val gEvening = sectionCounts.getOrElse(2) { 2 }.coerceIn(0, MAX_SECTION_PER_GROUP)
    // 一条「午休 / 晚休」分隔行的总占高：整条都铺底色，不再上下留 6dp 缝隙。
    // （留缝的话，有背景图时缝里会透出照片，节次列看着就和午休条「断开了」）
    val sepTotal = 38.dp
    // 每一节在整张表里的顶部位置，以及两条分隔行的位置
    val sectionTop = mutableMapOf<Int, Dp>()
    val sepTops = mutableListOf<Pair<Dp, String>>()
    var cursor = 0.dp
    var secNo = 1
    repeat(gMorning) { sectionTop[secNo] = cursor; cursor += rowHeight; secNo++ }
    if (gAfternoon > 0) {
        if (cursor > 0.dp) { sepTops.add(cursor to "午休"); cursor += sepTotal }
        repeat(gAfternoon) { sectionTop[secNo] = cursor; cursor += rowHeight; secNo++ }
    }
    if (gEvening > 0) {
        if (cursor > 0.dp) { sepTops.add(cursor to "晚休"); cursor += sepTotal }
        repeat(gEvening) { sectionTop[secNo] = cursor; cursor += rowHeight; secNo++ }
    }
    val lastSection = secNo - 1
    val gridTotalHeight = cursor
    // 「上午 / 下午 / 晚上」三段各自的节次范围：用来把跨段的课（比如 1~11 节）
    // 拆成两三个独立的圆角格子；被拆开的交界处正好留给「午休 / 晚休」灰条。
    val groupRanges = mutableListOf<IntRange>()
    if (gMorning > 0) groupRanges.add(1..gMorning)
    if (gAfternoon > 0) groupRanges.add((gMorning + 1)..(gMorning + gAfternoon))
    if (gEvening > 0) groupRanges.add((gMorning + gAfternoon + 1)..lastSection)

    Column(modifier = Modifier.fillMaxWidth()) {
        // ---------------- 表头：左上角只写一个阿拉伯数字（就是第几周），右边是星期 + 日期
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(indexColWidth)
                    .height(headerHeight)
                    .background(frameColor)
                    // 双击这个红色数字 → 回到本周（单击不管它）
                    .pointerInput(Unit) {
                        detectTapGestures(onDoubleTap = { onResetToCurrentWeek() })
                    },
                contentAlignment = Alignment.Center
            ) {
                // 第 5 周就写 5；顶部那一行收起来时，靠这个也知道现在第几周
                Text(
                    "$targetWeek",
                    color = Color(0xFFE53935),
                    fontWeight = FontWeight.Bold,
                    fontSize = sectionTextSize * 1.2f,
                    lineHeight = sectionTextSize * 1.2f
                )
            }
            weekTitleList.forEachIndexed { idx, _ ->
                val isToday = weekDateObjList[idx] == today
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(headerHeight)
                        .background(frameColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${weekTitleList[idx]}\n${weekDateList[idx]}",
                        // 今天不做下划线，直接把「周几 + 日期」整段换成浅蓝色
                        color = if (isToday) Color(0xFF64B5F6) else Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = headerTextSize,
                        lineHeight = (12.sp * fontScale)
                    )
                }
            }
        }

        // 表头下面不再留缝：表头 / 节次列 / 午休晚休灰条连成一片
        // 整张表：先铺「节次列 + 每一天一列」的大网格（所有节次连续排布），
        // 最后再把「午休 / 晚休」两条分隔行画到最上层，压住格线和课程格子。
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(gridTotalHeight)
        ) {
            // ---------- 上层：整张表 ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(gridTotalHeight)
            ) {
                // 节次列：从第 1 节到最后一节连续排
                Box(
                    modifier = Modifier
                        .width(indexColWidth)
                        .height(gridTotalHeight)
                ) {
                    // 底色「一节一块」：只垫在每一节那一行里（每节都是整行高，所以节次列本身是连着的）。
                    // 分隔带那一段由午休 / 晚休条自己整宽铺满（它现在占满整个分隔行高度），
                    // 两处颜色相同、又都只叠一层，所以整根节次列从上到下一气贯通，不会断开。
                    for (sec in 1..lastSection) {
                        val bgY = sectionTop[sec] ?: continue
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = bgY)
                                .height(rowHeight)
                                .background(frameColor)
                        )
                    }
                    // 左边界竖线：分节画，午休 / 晚休那条带里不画（底色半透明时不会透出横线的残影）
                    for (sec in 1..lastSection) {
                        val lineY = sectionTop[sec] ?: continue
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(top = lineY)
                                .width(0.5.dp)
                                .height(rowHeight)
                                .background(gridLine)
                        )
                    }
                    for (sec in 1..lastSection) {
                        val secY = sectionTop[sec] ?: continue
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = secY)
                                .height(rowHeight)
                        ) {
                            Column(
                                // 让「节号 + 起止时间」整体在节次列里水平居中
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "$sec",
                                    color = Color.Black,
                                    fontSize = sectionTextSize,
                                    lineHeight = sectionTextSize * 0.92f,
                                    fontWeight = FontWeight.Bold
                                )
                                // 这一节的起止时间（跟着「课时时间」设置走）
                                val slot = sectionTimes.getOrNull(sec - 1)
                                if (!slot.isNullOrBlank()) {
                                    val clock = prettySlotLines(slot)
                                    Text(
                                        clock.first,
                                        color = Color(0xFF7A7A7A),
                                        fontSize = sectionTimeSize,
                                        lineHeight = sectionTimeSize * 0.92f,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    if (clock.second.isNotBlank()) {
                                        Text(
                                            clock.second,
                                            color = Color(0xFF7A7A7A),
                                            fontSize = sectionTimeSize,
                                            lineHeight = sectionTimeSize * 0.92f,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                            }
                        }
                        // 上一节和这一节挨着（中间没有分隔行）才画格线
                        val prevY = if (sec > 1) sectionTop[sec - 1] else null
                        if (prevY != null && secY == prevY + rowHeight) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = secY)
                                    .height(1.dp)
                                    .background(gridLine)
                            )
                        }
                    }
                }

                // 每一天一列
                visibleWeekCns.forEach { weekCnKey ->
                    // 只要跟显示范围有交叠就算这节课要画（跨段的课也能画成完整一块）
                    val dayCourses = courses.filter {
                        weekValid &&
                                it.weekCn == weekCnKey &&
                                targetWeek in it.weekRangeList &&
                                it.startSection <= lastSection &&
                                it.endSection >= 1
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(gridTotalHeight)
                    ) {
                        // 竖直分隔线：也分节画，午休 / 晚休那条带里不画；
                        // 最后一列再补一条右边界线，这样整表左右都封口
                        for (sec in 1..lastSection) {
                            val lineY = sectionTop[sec] ?: continue
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(top = lineY)
                                    .width(0.5.dp)
                                    .height(rowHeight)
                                    .background(gridLine)
                            )
                            if (weekCnKey == visibleWeekCns.last()) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = lineY)
                                        .width(0.5.dp)
                                        .height(rowHeight)
                                        .background(gridLine)
                                )
                            }
                        }

                        // 行分隔线：被课程块跨过的边界不画（格子内部不要横线）；
                        // 中间夹着分隔行的边界也不画（那边已经有灰条了）
                        val coveredBoundaries = mutableSetOf<Int>()
                        dayCourses.forEach { c ->
                            val cEnd = minOf(c.endSection, lastSection)
                            for (s in c.startSection until cEnd) {
                                if (s >= 1) coveredBoundaries.add(s)
                            }
                        }
                        for (s in 1 until lastSection) {
                            if (s in coveredBoundaries) continue
                            val upper = sectionTop[s] ?: continue
                            val lower = sectionTop[s + 1] ?: continue
                            if (lower != upper + rowHeight) continue
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = lower)
                                    .height(1.dp)
                                    .background(gridLine)
                            )
                        }

                        for (c in dayCourses) {
                            val cStart = c.startSection.coerceAtLeast(1)
                            val cEnd = c.endSection.coerceAtMost(lastSection)
                            val blockColor = getCourseColor(c).copy(alpha = cellAlpha)

                            // 按「上午 / 下午 / 晚上」分段画：跨段的课（比如 1~11 节）会被「午休 / 晚休」
                            // 切成两三个独立的圆角格子，每个格子各自有圆角和上下留白。
                            groupRanges.forEach rangeLoop@{ range ->
                                val from = maxOf(cStart, range.first)
                                val to = minOf(cEnd, range.last)
                                if (from > to) return@rangeLoop
                                val topY = sectionTop[from] ?: return@rangeLoop
                                val bottomY = sectionTop[to] ?: return@rangeLoop
                                val blockHeight = (bottomY + rowHeight) - topY

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = topY)
                                        .height(blockHeight)
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(blockColor)
                                        .padding(3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(ScrollState(0)),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            c.name,
                                            color = getCourseTextColor(c),
                                            fontSize = courseNameSize,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            lineHeight = (11.sp * fontScale)
                                        )
                                        Spacer(Modifier.height(2.dp))
                                        Text(c.location, color = getCourseInfoColor(c), fontSize = courseInfoSize)
                                        Text(c.teacher, color = getCourseInfoColor(c), fontSize = courseInfoSize)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 表头与第 1 节之间补一条横线：不然第 1 节看着跟周次连在一起了
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(gridLine)
            )
            // 整表底边线：原来靠每列的边框提供，现在边框改成分节竖线了，这里补回一条
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(gridLine)
            )
            // ---------- 最上层：午休 / 晚休灰条（压住格线与课程格子，把跨段课程切成 3 块） ----------
            sepTops.forEach { sep ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = sep.first)
                        .height(sepTotal)
                ) {
                    // 【底色层】整宽、整高铺满：条子现在占满整个分隔行（38dp），
                    // 节次列在分隔带里不垫色，所以这里铺上去刚好是「一层」，
                    // 左右两段同色、同高、严丝合缝 —— 不再有上下高低差，也没有断口。
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(sepBarColor)
                    )
                    // 【上沿线】横贯整行（连最左边的节次列也算进去）
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(sepLineColor)
                    )
                    // 【下沿线】
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(sepLineColor)
                    )
                    // 【文字层】单独一层铺满整宽，让「午休 / 晚休」仍然落在整张课表的正中间
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // 固定 12sp：不随课表字体缩放，避免被裁掉
                        Text(sep.second, fontSize = 12.sp, color = Color(0xFF808080))
                    }
                }
            }
        }
    }
}

// ==================== 免责声明（首次启动阅读 8 秒 + 勾选一次） ====================
private const val DISCLAIMER_READ_SECONDS = 10

fun isDisclaimerAccepted(context: Context): Boolean =
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .getBoolean(KEY_DISCLAIMER_ACCEPTED, false)

fun setDisclaimerAccepted(context: Context, accepted: Boolean) {
    // 只勾一次：同意之后永久记住，升级版本也不再弹出
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putBoolean(KEY_DISCLAIMER_ACCEPTED, accepted).apply()
}

// ==================== 桌面小部件：四种尺寸的字体缩放 ====================
const val WIDGET_KEY_FULL = "full"
const val WIDGET_KEY_MEDIUM = "medium"
const val WIDGET_KEY_SHORT = "short"
const val WIDGET_KEY_TINY = "tiny"
const val WIDGET_FONT_MIN = 0.7f
// 上限 300%：刻度就是倍率，200% 的 1.5 倍就是 300%
const val WIDGET_FONT_MAX = 3.0f

// 桌面组件的尺寸由系统在添加时决定，这里只分别记每种尺寸的字体缩放
val WIDGET_SIZE_ORDER = listOf(WIDGET_KEY_TINY, WIDGET_KEY_SHORT, WIDGET_KEY_MEDIUM, WIDGET_KEY_FULL)

private fun widgetFontKey(sizeKey: String): String = "widget_font_" + sizeKey

fun loadWidgetFontScale(context: Context, sizeKey: String): Float =
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .getFloat(widgetFontKey(sizeKey), 1f)
        .coerceIn(WIDGET_FONT_MIN, WIDGET_FONT_MAX)

fun saveWidgetFontScale(context: Context, sizeKey: String, scale: Float) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putFloat(widgetFontKey(sizeKey), scale.coerceIn(WIDGET_FONT_MIN, WIDGET_FONT_MAX)).apply()
    // 字体改了，桌面上的组件也要跟着重画（拖动滑块要立刻见效，所以只重画一次）
    ScheduleWidgetProvider.refreshScheduleWidgetsQuick(context)
}

fun loadAllWidgetFontScales(context: Context): Map<String, Float> = mapOf(
    WIDGET_KEY_FULL to loadWidgetFontScale(context, WIDGET_KEY_FULL),
    WIDGET_KEY_MEDIUM to loadWidgetFontScale(context, WIDGET_KEY_MEDIUM),
    WIDGET_KEY_SHORT to loadWidgetFontScale(context, WIDGET_KEY_SHORT),
    WIDGET_KEY_TINY to loadWidgetFontScale(context, WIDGET_KEY_TINY)
)

fun widgetSizeLabel(sizeKey: String): String = when (sizeKey) {
    WIDGET_KEY_TINY -> "1×1 迷你"
    WIDGET_KEY_SHORT -> "2×1 短条"
    WIDGET_KEY_MEDIUM -> "2×2 中号"
    else -> "4×2 完整"
}

/** 名字只写大小，不再带括号里的说明；这个别名留给老调用点 */
fun widgetSizeShortLabel(sizeKey: String): String = widgetSizeLabel(sizeKey)


// ==================== 小部件：看已添加几个 / 一键放到桌面 ====================

/** 某个尺寸对应的组件类 */
private fun widgetProviderClass(sizeKey: String): Class<*> = when (sizeKey) {
    WIDGET_KEY_MEDIUM -> ScheduleWidgetMediumProvider::class.java
    WIDGET_KEY_SHORT -> ScheduleWidgetShortProvider::class.java
    WIDGET_KEY_TINY -> ScheduleWidgetTinyProvider::class.java
    else -> ScheduleWidgetProvider::class.java
}

/** 桌面上已经放了几个这种尺寸的组件 */
fun countWidgets(context: Context, sizeKey: String): Int = try {
    AppWidgetManager.getInstance(context)
        .getAppWidgetIds(ComponentName(context, widgetProviderClass(sizeKey)))
        .size
} catch (_: Exception) {
    0
}

/**
 * 一键把组件放到桌面（Android 8.0 起支持）：系统会弹一个确认框，用户点「添加」才真正放上去。
 * 返回 false 表示这个系统 / 桌面不支持，需要用户自己长按桌面空白处添加。
 */
fun pinWidgetToHome(context: Context, sizeKey: String): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false
    return try {
        AppWidgetManager.getInstance(context)
            .requestPinAppWidget(ComponentName(context, widgetProviderClass(sizeKey)), null, null)
    } catch (_: Exception) {
        false
    }
}


// ==================== 每个组件单独一份字体设置 ====================

/** 组件实例的字体倍率存这个键；组件删掉后残留的键不影响任何东西 */
private fun widgetFontIdKey(appWidgetId: Int): String = "widget_font_id_" + appWidgetId

/** 这个组件的字体倍率：自己没单独设过，就沿用这种尺寸的默认值 */
fun loadWidgetFontScaleForId(context: Context, sizeKey: String, appWidgetId: Int): Float {
    val def = loadWidgetFontScale(context, sizeKey)
    return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .getFloat(widgetFontIdKey(appWidgetId), def)
        .coerceIn(WIDGET_FONT_MIN, WIDGET_FONT_MAX)
}

/** 只改这一个组件的字体，改完立刻重画桌面 */
fun saveWidgetFontScaleForId(context: Context, sizeKey: String, appWidgetId: Int, scale: Float) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        .putFloat(widgetFontIdKey(appWidgetId), scale.coerceIn(WIDGET_FONT_MIN, WIDGET_FONT_MAX))
        .apply()
    // 拖动滑块时每一下都要立刻反映到桌面
    ScheduleWidgetProvider.refreshScheduleWidgetsQuick(context)
}

/** 桌面上现有的全部组件：按尺寸顺序列出 (尺寸, 组件 id)，同尺寸按 id 从小到大 */
fun listWidgetInstances(context: Context): List<Pair<String, Int>> {
    val out = mutableListOf<Pair<String, Int>>()
    val manager = AppWidgetManager.getInstance(context)
    WIDGET_SIZE_ORDER.forEach { k ->
        try {
            manager.getAppWidgetIds(ComponentName(context, widgetProviderClass(k)))
                .sorted()
                .forEach { id -> out.add(k to id) }
        } catch (_: Exception) {
        }
    }
    return out
}

/** 同尺寸里的第几个：①②③…（超过 9 个就用数字） */
fun widgetIndexMark(index: Int): String = when (index) {
    0 -> "①"
    1 -> "②"
    2 -> "③"
    3 -> "④"
    4 -> "⑤"
    5 -> "⑥"
    6 -> "⑦"
    7 -> "⑧"
    8 -> "⑨"
    else -> "(" + (index + 1) + ")"
}

/** 回到手机桌面（方便用户直接长按空白处手动添加组件） */
fun goToHome(context: Context) {
    try {
        val i = Intent(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_HOME)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    } catch (_: Exception) {
    }
}

@Composable
fun disclaimerSection(title: String, body: String) {
    Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF263238))
    Spacer(Modifier.height(3.dp))
    Text(body, fontSize = 12.sp, color = Color(0xFF546E7A), lineHeight = 18.sp)
    Spacer(Modifier.height(10.dp))
}

@Composable
fun DisclaimerBody(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
    ) {
        disclaimerSection(
            "一、这个 App 是什么",
            "「芝士院特供课表」是个人自用的小工具，用来查看和整理自己的上课安排。它不是学校官方应用，" +
                    "与学校、教务处、教务系统及其运营方没有任何隶属、授权或合作关系。" +
                    "所有课表内容一律以学校教务系统公布的信息为准。"
        )
        disclaimerSection(
            "二、课表数据从哪来",
            "课表数据只有三个来源：① 你手动导入自己从教务系统导出（或另存）的 HTML / txt 文件；" +
                    "② 你在「导入课表 → 教务获取」里填入学号、密码后，App 以你本人的身份向教务系统发起一次普通登录请求，" +
                    "并把返回的课表页面解析出来 —— 和你自己用浏览器登录属于同一种访问方式；" +
                    "③ 你在「手动添加课程」里一条条录入。App 不抓取任何其他网站的数据，" +
                    "不破解验证码、不绕过风控、不尝试攻击或拖慢服务器。如果学校不允许第三方程序访问教务系统，" +
                    "请改用方式 ① 或 ③，由你自己导入。"
        )
        disclaimerSection(
            "三、账号与密码安全",
            "学号、密码只用于上面方式 ② 的那一次登录。密码经 Android Keystore 提供的 AES-256-GCM 加密后存放在本机 App 私有目录，" +
                    "密钥由系统密钥库管理，App 不保存明文密码，也不会把账号密码上传到任何服务器。" +
                    "请勿使用来源不明的二次打包版本 —— 他人改过的安装包无法保证这一点；" +
                    "不放心的话可以把密码留空，改用「导入文件」的方式。"
        )
        disclaimerSection(
            "四、隐私与权限",
            "课程、节数与上课时间、学习周数、背景图、桌面组件字体等设置，全部只保存在本机（SharedPreferences 与 App 私有目录），" +
                    "不上传服务器；App 没有广告、统计与行为分析 SDK，不读取通讯录、位置、相册等信息。" +
                    "App 只申请常规权限：网络（仅用于上面方式 ② 的教务获取）与网络状态（判断是否联网），" +
                    "小部件相关权限只用于把桌面组件放上去。卸载 App 会删除全部本地数据。"
        )
        disclaimerSection(
            "五、关于「课时时间」",
            "设置里的「课表节数」和「课时时间」（每节课时长、课间休息、上午 / 下午 / 晚上的开始时间、各节起止时间）" +
                    "都是你自己填的本地显示值，只是为了把课表看得更直观 —— 它们会显示在课表最左边一列的节次下面（例如 8:00-8:40），" +
                    "并且按每个课表单独保存。这些时间不是学校发布的官方作息，不代表实际上下课时间；" +
                    "上课、考试、调课、放假等一切安排请以学校和任课老师公布的信息为准。" +
                    "因时间填错造成的迟到、漏课等后果由使用者自行承担。"
        )
        disclaimerSection(
            "六、桌面小部件与系统行为",
            "「一键添加小部件」能否弹出系统确认框，取决于手机桌面（Launcher）是否实现该能力，部分机型不会弹出，" +
                    "需要按页面里的手动步骤长按桌面添加；小部件显示的课程按 App 内设置的开学周一与周次推算，" +
                    "仅供参考，遇到调课、停课不会自动同步。"
        )
        disclaimerSection(
            "七、删除与数据丢失",
            "「导入课表 → 删除课程」和删除课表都是立即生效、不可撤销的操作；重新导入同一份教务文件或重新手动添加可以补回来。" +
                    "建议保留原始教务导出文件作为备份。清除 App 数据、卸载重装、系统清理或存储损坏都可能造成本地课表与设置丢失，" +
                    "作者不提供数据找回或备份承诺。"
        )
        disclaimerSection(
            "八、自愿支付",
            "App 完全免费：没有会员、没有功能锁、也没有广告。「支持作者」是自愿打赏 —— 不打赏同样可以使用全部功能；" +
                    "打赏不属于购买行为，不换取任何额外功能或服务，也不构成对后续更新、维护的承诺；" +
                    "除法律另有规定外不支持退款。"
        )
        disclaimerSection(
            "九、其他",
            "点击「同意并继续」即表示你已阅读、理解并同意以上内容，并确认你在所在地区使用本 App 的方式合法。" +
                    "因课表解析错误、时间设置偏差、系统兼容性、设备差异或数据丢失等造成的影响，作者不承担赔偿责任；" +
                    "本 App 为个人业余作品，可能随时停止更新或停止维护。"
        )
    }
}

@Composable
fun DisclaimerScreen(onAccept: () -> Unit) {
    // 倒计时：0 之前勾选框不可点
    var secondsLeft by remember { mutableIntStateOf(DISCLAIMER_READ_SECONDS) }
    var checked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        for (i in DISCLAIMER_READ_SECONDS - 1 downTo 0) {
            delay(1000)
            secondsLeft = i
        }
    }

    val canCheck = secondsLeft <= 0
    val canContinue = canCheck && checked

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Text("免责声明", fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                "首次使用请完整阅读（约 10 秒）。勾选一次后永久记住，版本更新也不会再要求勾选。",
                fontSize = 11.sp,
                color = Color(0xFF8A8A8A)
            )
            Spacer(Modifier.height(10.dp))

            DisclaimerBody(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(22.dp)
                        .height(22.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (checked) Color(0xFF37474F) else Color.White)
                        .border(
                            BorderStroke(1.dp, if (checked) Color(0xFF37474F) else Color(0xFFBDBDBD)),
                            RoundedCornerShape(6.dp)
                        )
                        .clickable(enabled = canCheck) { checked = !checked },
                    contentAlignment = Alignment.Center
                ) {
                    if (checked) {
                        Text("✓", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "我已阅读并理解上述免责声明（非官方 · 数据来源 · 安全 · 隐私 · 时间说明 · 自愿支付）",
                        fontSize = 12.sp
                    )
                    if (!canCheck) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "还需阅读 " + secondsLeft + " 秒才能勾选",
                            fontSize = 11.sp,
                            color = Color(0xFFE57373)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            SoftButton(
                text = when {
                    !canCheck -> "请先阅读 " + secondsLeft + " 秒"
                    !checked -> "请先勾选上方选项"
                    else -> "同意并继续"
                },
                onClick = onAccept,
                enabled = canContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                fontSize = 15.sp
            )
        }
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        System.setProperty("java.net.preferIPv4Stack", "true")
        System.setProperty("java.net.preferIPv6Addresses", "false")
        super.onCreate(savedInstanceState)

        // 打开 App 时顺便刷一次小组件：跨天 / 换周之后，让桌面和界面重新对上
        ScheduleWidgetProvider.refreshScheduleWidgets(this)

        // 第 50 批：App 不再自己播放提醒铃声（声音只认系统通知通道），这里也不用再停铃了

        // 第 49 批：一次性把「⑦其他」被误存成深灰的旧值清掉，回落到原色浅灰 #CFCFCF
        migrateWidgetInfoColorOnce(this)

        setContent {
            // 第 45 批：按设计稿改用动态取色（Android 12+ 由壁纸生成）+ Mono 备用
            MaterialTheme(
                colorScheme = appColorScheme()
            ) {
                val ctx = LocalContext.current
                // 首次启动：没同意过免责声明就先看声明页；同意一次后永久跳过（升级版本也不再弹）
                var disclaimerAccepted by remember { mutableStateOf(isDisclaimerAccepted(ctx)) }
                if (disclaimerAccepted) {
                    ScheduleMainScreen()
                } else {
                    DisclaimerScreen(
                        onAccept = {
                            setDisclaimerAccepted(ctx, true)
                            disclaimerAccepted = true
                        }
                    )
                }
            }
        }
    }
}

// ==================== 设置页（两级：一级列表 → 二级调节页） ====================
// 设置页统一按钮样式：奶白底 + 深灰字 + 细边框（不再用深色实心按钮）
@Composable
fun SoftButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    fontSize: androidx.compose.ui.unit.TextUnit = 14.sp
) {
    // 不依赖 Material Button 的 border 参数（避免 Compose 版本差异导致编译不过）
    val fillColor = when {
        !enabled -> Color(0xFFF4F4F4)
        selected -> Color(0xFF37474F)
        else -> Color(0xFFFFFFFF)
    }
    val softTextColor = when {
        !enabled -> Color(0xFFBDBDBD)
        selected -> Color.White
        else -> Color(0xFF37474F)
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(fillColor)
            .border(BorderStroke(1.dp, Color(0xFFE3E3E3)), RoundedCornerShape(10.dp))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = fontSize, color = softTextColor)
    }
}

// 二级页内容容器（扁平：不加方框、不加底色，直接就是内容）
@Composable
fun SettingsCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column { content() }
    }
}

/**
 * 小部件背景 + 课程格子透明度。
 * 设置页的二级页（字体·行高·背景）和「管理小部件 → 小部件背景」共用这一段。
 * bgRev 只是用来在换图 / 改模式后触发重组（值一变就重新读一遍当前设置）。
 */
@Composable
fun WidgetBgSection(
    context: Context,
    targetId: Int,
    bgRev: Int,
    onBgModeChange: (Int, String) -> Unit,
    onPickImage: (Int) -> Unit,
    onClearImage: (Int) -> Unit,
    onCellAlphaChange: (Int, Float) -> Unit
) {
    val bgPath = if (targetId >= 0) loadWidgetBgImageForId(context, targetId) else ""
    val bgMode = if (targetId >= 0) loadWidgetBgModeForId(context, targetId) else WIDGET_BG_WHITE
    // 滑块自己的值：拖动时立刻跟手，存盘 + 重画桌面同时进行
    var editAlpha by remember(targetId) {
        mutableFloatStateOf(
            if (targetId >= 0) loadWidgetCellAlphaForId(context, targetId) else 1.0f
        )
    }
    Text("小部件背景", fontSize = 15.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(6.dp))
    Text(
        "背景：" + when {
            targetId < 0 -> "先在下面选一个组件"
            bgMode == WIDGET_BG_TRANSPARENT -> "透明（去掉白底，只留字）"
            bgMode == WIDGET_BG_IMAGE && bgPath.isNotBlank() -> "背景图（已设置）"
            else -> "白色（默认，圆角白底卡片）"
        },
        fontSize = 12.sp,
        color = Color(0xFF546E7A)
    )
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SoftButton(
            text = "透明",
            selected = bgMode == WIDGET_BG_TRANSPARENT,
            onClick = { if (targetId >= 0) onBgModeChange(targetId, WIDGET_BG_TRANSPARENT) },
            enabled = targetId >= 0,
            modifier = Modifier
                .weight(1f)
                .height(42.dp),
            fontSize = 13.sp
        )
        SoftButton(
            text = "选择背景图",
            onClick = { if (targetId >= 0) onPickImage(targetId) },
            enabled = targetId >= 0,
            modifier = Modifier
                .weight(1f)
                .height(42.dp),
            fontSize = 13.sp
        )
        SoftButton(
            text = "移除背景",
            onClick = { if (targetId >= 0) onClearImage(targetId) },
            // 只要选了组件就能点：点一下就是把背景恢复成白色（默认）
            enabled = targetId >= 0,
            modifier = Modifier
                .weight(1f)
                .height(42.dp),
            fontSize = 13.sp
        )
    }
    Spacer(Modifier.height(14.dp))
    Text(
        "课程格子透明度：" + (editAlpha * 100).toInt() + "%",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
    )
    Slider(
        value = editAlpha,
        onValueChange = {
            editAlpha = it
            if (targetId >= 0) onCellAlphaChange(targetId, it)
        },
        valueRange = 0.2f..1.0f,
        enabled = targetId >= 0
    )
    Spacer(Modifier.height(6.dp))
}

// ==================== 小部件颜色（设置页里的挑选面板） ====================

/**
 * 小部件颜色条：一条“连续渐变”，从左到右依次是
 * 黑 → 白 → 红 → 品红 → 蓝 → 青 → 绿 → 黄。
 *
 * 位置 t（0 ~ 1）分三段算：
 *   0.000 ~ 0.067  黑 → 白（提亮）
 *   0.067 ~ 0.130  白 → 红（加饱和度）
 *   0.130 ~ 1.000  红 → 黄（色相沿色环反向走 300°，中间依次经过品红、蓝、青、绿）
 *
 * 想换渐变的颜色走向，只改这一个函数就行（显示用的渐变和取色都从它算）。
 *
 * 第 49 批：灰白分界由 0.07 微调到 0.067 —— 56 格下这样算出来，
 * 第 4 格正好落在 #CFCFCF（「⑦其他」的原色浅灰）上，不用再数格子猜。
 * 改动时要和下面的 spectrumT() 同步，不然白色「|」会指错位置。
 */
/** 位置 → 颜色 */
private fun spectrumAt(t: Float): Int {
    val x = t.coerceIn(0f, 1f)
    // 三段：黑 → 白（提亮） / 白 → 红（加饱和） / 红 → 品红 → 蓝 → 青 → 绿 → 黄（色相反向走一圈）
    val seg: FloatArray = when {
        x < 0.067f -> floatArrayOf(0f, 0f, x / 0.067f)
        x < 0.13f -> floatArrayOf(0f, (x - 0.067f) / 0.063f, 1f)
        else -> floatArrayOf(360f - (x - 0.13f) / 0.87f * 300f, 1f, 1f)
    }
    return android.graphics.Color.HSVToColor(
        floatArrayOf(((seg[0] % 360f) + 360f) % 360f, seg[1], seg[2])
    )
}

/** 颜色 → 位置（用来把白色「|」摆在当前颜色那一处） */
private fun spectrumT(color: Int): Float {
    val r = (color shr 16 and 0xFF) / 255f
    val g = (color shr 8 and 0xFF) / 255f
    val b = (color and 0xFF) / 255f
    val mx = maxOf(r, g, b)
    val mn = minOf(r, g, b)
    val v = mx
    val s = if (mx <= 0f) 0f else (mx - mn) / mx
    if (s < 0.02f) return (v * 0.067f).coerceIn(0f, 0.067f)
    var h = when (mx) {
        r -> 60f * (((g - b) / (mx - mn)) % 6f)
        g -> 60f * (((b - r) / (mx - mn)) + 2f)
        else -> 60f * (((r - g) / (mx - mn)) + 4f)
    }
    if (h < 0f) h += 360f
    // 白 → 红那一段（亮红、粉红）
    if (v > 0.97f && s < 0.97f && (h < 12f || h > 348f)) return 0.067f + s * 0.063f
    // 彩虹段：红（360°）→ 品红 → 蓝 → 青 → 绿 → 黄（60°）
    return (0.13f + (360f - h) / 300f * 0.87f).coerceIn(0.13f, 1f)
}

/** 两个颜色差多少（取 R/G/B 里差得最大的那个通道，0 ~ 255） */
private fun colorDist(a: Int, b: Int): Int {
    val dr = kotlin.math.abs(((a shr 16) and 0xFF) - ((b shr 16) and 0xFF))
    val dg = kotlin.math.abs(((a shr 8) and 0xFF) - ((b shr 8) and 0xFF))
    val db = kotlin.math.abs((a and 0xFF) - (b and 0xFF))
    return maxOf(dr, dg, db)
}

/** 颜色条显示用的色标（和 spectrumAt 同一套算法） */
private val SPECTRUM_STOPS: List<Color> = List(49) { i -> Color(spectrumAt(i / 48f)) }

/** 颜色条分多少格：每格一个颜色（第 1 格黑，越往右越亮 / 越彩），拖动只会停在这些格上 */
private const val WIDGET_COLOR_STEPS = 56

/** 第 step 格（0 起）对应的颜色 */
private fun spectrumAtStep(step: Int): Int =
    spectrumAt(step.toFloat() / (WIDGET_COLOR_STEPS - 1).toFloat())

/** 某个颜色落在第几格（0 起） */
private fun spectrumStepOf(color: Int): Int =
    Math.round(spectrumT(color) * (WIDGET_COLOR_STEPS - 1)).coerceIn(0, WIDGET_COLOR_STEPS - 1)

/**
 * 小部件颜色条（零件名在外面选好之后显示这一条）：
 *   · 一条 40 格的颜色条，每格一个颜色、编号 1 ~ 40 —— 就是把颜色换成数字；
 *   · 点一下 = 选那一格；横向拖 = 一格一格跟手跳（只改本地数字，很轻）；
 *   · 只有松手（或点选）那一下才写盘 + 刷一次桌面。
 * 原先每动一下都写盘 + 刷桌面，所以拖起来一卡一卡的。
 */
@Composable
fun WidgetColorRow(
    keyId: Int,
    label: String,
    hint: String,
    currentValue: Int,
    hasAuto: Boolean,
    onPick: (Int) -> Unit,
    onReset: () -> Unit,
    showReset: Boolean = true
) {
    var idx by remember(keyId, label) { mutableIntStateOf(spectrumStepOf(currentValue)) }
    val dragFlag = remember(keyId, label) { booleanArrayOf(false) }
    // 外部改了（恢复默认色 / 换零件 / 换组件）就同步过来
    LaunchedEffect(currentValue) {
        if (!dragFlag[0]) idx = spectrumStepOf(currentValue)
    }
    val auto = hasAuto && currentValue == WIDGET_CELL_COLOR_AUTO
    val now = spectrumAtStep(idx)

    Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF37474F))
    Spacer(Modifier.height(3.dp))
    Text(hint, fontSize = 11.sp, color = Color(0xFF8A8A8A))
    Spacer(Modifier.height(6.dp))
    if (hasAuto) {
        SoftButton(
            text = "用原色（课表自动分配）",
            selected = auto,
            onClick = onReset,
            modifier = Modifier.height(38.dp),
            fontSize = 12.sp
        )
        Spacer(Modifier.height(6.dp))
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .pointerInput(keyId, label) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val w = size.width.toFloat().coerceAtLeast(1f)
                    val x0 = down.position.x
                    val y0 = down.position.y
                    val slop = viewConfiguration.touchSlop
                    var moved = false
                    fun stepAt(x: Float): Int =
                        (x / w * WIDGET_COLOR_STEPS).toInt().coerceIn(0, WIDGET_COLOR_STEPS - 1)
                    while (true) {
                        val ch = awaitPointerEvent().changes.firstOrNull { it.id == down.id } ?: break
                        if (!ch.pressed) {
                            if (moved) {
                                dragFlag[0] = false
                                onPick(spectrumAtStep(idx))
                            } else {
                                val s = stepAt(x0)
                                idx = s
                                onPick(spectrumAtStep(s))
                            }
                            break
                        }
                        val dx = kotlin.math.abs(ch.position.x - x0)
                        val dy = kotlin.math.abs(ch.position.y - y0)
                        if (!moved && dy > slop && dy > dx) break
                        if (dx > slop || moved) {
                            moved = true
                            dragFlag[0] = true
                            val s = stepAt(ch.position.x)
                            if (s != idx) idx = s
                            ch.consume()
                        }
                    }
                }
            }
    ) {
        // 条子本身就是 40 个小格子拼起来的（比渐变刷子更能看出“一格一个颜色”）
        Row(modifier = Modifier.fillMaxWidth().height(34.dp).align(Alignment.Center).clip(RoundedCornerShape(8.dp))) {
            repeat(WIDGET_COLOR_STEPS) { i ->
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(spectrumAtStep(i))))
            }
        }
        // 白色「|」：比条子上下各凸出 5dp，带一圈淡描边（在白色那一段也看得见）
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (maxWidth - 3.dp) * ((idx.toFloat() + 0.5f) / WIDGET_COLOR_STEPS.toFloat()))
                .width(3.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(1.5.dp))
                .background(Color.White)
                .border(BorderStroke(1.dp, Color(0x66000000)), RoundedCornerShape(1.5.dp))
        )
    }
    Spacer(Modifier.height(8.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (auto) Color(0xFFEDEDED) else Color(now))
                .border(BorderStroke(1.dp, Color(0x33000000)), CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            if (auto) "现在：原色（课表自动分配的颜色）"
            else "第 " + (idx + 1) + " 格 / " + WIDGET_COLOR_STEPS + " 格　" + argbHex(now),
            fontSize = 11.sp,
            color = Color(0xFF8A8A8A)
        )
    }
    if (showReset) {
        Spacer(Modifier.height(10.dp))
        SoftButton(
            text = "恢复默认色",
            onClick = onReset,
            modifier = Modifier.height(38.dp),
            fontSize = 12.sp
        )
    }
    Spacer(Modifier.height(18.dp))
}

/** 小部件颜色里的一个「零件」：槽位 key、显示名、说明、默认色、是否支持「原色」 */
private data class WidgetColorSlotInfo(
    val slot: String,
    val label: String,
    val hint: String,
    val def: Int,
    val hasAuto: Boolean
)

/** 「小部件颜色」里的零件行：点一下选中它（选中的有蓝框），右边显示它现在的颜色 */
@Composable
fun WidgetSlotRow(
    label: String,
    hint: String,
    color: Int,
    showAuto: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) Color(0xFFE8F0FE) else Color(0xFFF7F7F5))
            .then(
                if (selected) Modifier.border(BorderStroke(1.5.dp, Color(0xFF3B82F6)), RoundedCornerShape(10.dp))
                else Modifier
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF37474F))
                Spacer(Modifier.height(2.dp))
                Text(hint, fontSize = 11.sp, color = Color(0xFF9E9E9E))
            }
            if (showAuto) {
                Text("原色", fontSize = 11.sp, color = Color(0xFF9E9E9E))
            } else {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(color))
                        .border(BorderStroke(1.dp, Color(0x33000000)), CircleShape)
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                if (selected) "\u2713" else "\u203a",
                fontSize = 14.sp,
                color = if (selected) Color(0xFF3B82F6) else Color(0xFF9E9E9E)
            )
        }
    }
}

/**
 * 「小部件颜色」面板（组件在外层已经选好）：一屏搞定 ——
 *   上面列出所有能改的零件（点名字就选中它，选中的有蓝框），
 *   下面就是那一条颜色条；点名字换零件、拖颜色条改颜色，颜色条只对当前选中的零件生效。
 */
@Composable
fun WidgetColorSection(
    context: Context,
    targetId: Int,
    colorRev: Int,
    slot: String?,
    onPickSlot: (String?) -> Unit,
    onColorChange: (Int, String, Int) -> Unit,
    onColorReset: (Int, String) -> Unit
) {
    if (targetId < 0) {
        return
    }
    val slots = listOf(
        WidgetColorSlotInfo(WIDGET_COLOR_TITLE, "① 周数", "小部件左上角那行字", WIDGET_DEF_TITLE, false),
        WidgetColorSlotInfo(WIDGET_COLOR_DATE, "② 周数日期", "右上角那行字（1×1 的顶行也跟着它）", WIDGET_DEF_DATE, false),
        WidgetColorSlotInfo(WIDGET_COLOR_CELL, "③ 课表格子", "每行课程那个圆角色块；挑的色会覆盖整块底图，格子形状与圆角不变", WIDGET_CELL_COLOR_AUTO, true),
        WidgetColorSlotInfo(WIDGET_COLOR_DIVIDER, "④ 分割线", "左边时间与右边课程之间那条竖线", WIDGET_DEF_DIVIDER, false),
        WidgetColorSlotInfo(WIDGET_COLOR_TIME, "⑤ 时间", "左边那两行上课时间", WIDGET_DEF_TIME, false),
        WidgetColorSlotInfo(WIDGET_COLOR_NAME, "⑥ 课程名", "课名那行字", WIDGET_DEF_NAME, false),
        WidgetColorSlotInfo(WIDGET_COLOR_INFO, "⑦ 其他（节数 · 教室 · 教师）", "也管「今天没课」「点一下打开课表」这类提示字", WIDGET_DEF_INFO, false)
    )
    val cur = slots.firstOrNull { it.slot == (slot ?: WIDGET_COLOR_TITLE) } ?: slots[0]
    key(colorRev, targetId) {
        Text(
            "第二步：点零件名选中它，再拖下面的颜色条改颜色",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF37474F)
        )
        Spacer(Modifier.height(8.dp))
        slots.forEach { info ->
            val c = loadWidgetColorForId(context, targetId, info.slot, info.def)
            WidgetSlotRow(
                label = info.label,
                hint = info.hint,
                color = c,
                showAuto = info.hasAuto && c == WIDGET_CELL_COLOR_AUTO,
                selected = info.slot == cur.slot,
                onClick = { onPickSlot(info.slot) }
            )
            Spacer(Modifier.height(8.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "正在调：" + cur.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF37474F)
        )
        Spacer(Modifier.height(6.dp))
        WidgetColorRow(
            keyId = targetId,
            label = cur.label,
            hint = cur.hint,
            currentValue = loadWidgetColorForId(context, targetId, cur.slot, cur.def),
            hasAuto = cur.hasAuto,
            onPick = { onColorChange(targetId, cur.slot, it) },
            onReset = { onColorReset(targetId, cur.slot) }
        )
    }
}



// 一级列表行：整行可点，扁平样式（没有方框、没有底色）
@Composable
fun SettingsRow(
    title: String,
    summary: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(summary, fontSize = 12.sp, color = Color(0xFF8A8A8A))
            }
            Text("›", fontSize = 22.sp, color = Color(0xFFBDBDBD))
        }
    }
}

// 大标题：只用来给设置项分类，不可点
@Composable
fun SettingsGroupTitle(text: String) {
    Text(
        text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF37474F),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 2.dp, bottom = 8.dp)
    )
}

// 大标题之间的分隔横线（页面是纯白底，纯白线会看不见，这里用极浅灰；要纯白把下面 Color 改成 Color.White）
@Composable
fun SettingsGroupDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .height(1.dp)
            .background(Color(0xFFEEEEEE))
    )
}

fun settingsPageTitle(page: String): String = when (page) {
    "weekanim" -> "切换动画"
    "books" -> "课表"
    "start" -> "开学周一"
    "weeks" -> "总学习周数"
    "sections" -> "课表节数"
    "times" -> "课时时间"
    "import" -> "课表编辑"
    "font" -> "字体大小"
    "row" -> "课表行高"
    "indexw" -> "节数列宽度"
    "bg" -> "背景设置"
    "widget" -> "桌面小部件"
    "widgetfont" -> "字体 · 行高 · 背景与格子"
    "widgetadd" -> "添加小部件"
    "widgetmanage" -> "管理小部件"
    "widgetbg" -> "小部件背景"
    "widgetcolor" -> "小部件颜色"
    "edit" -> "课表编辑"
    "timeset" -> "时间设置"
    "display" -> "显示调整"
    "manual" -> "操作手册"
    "reminder" -> "上课提醒/静音"
    "share" -> "分享与导入"
    "conflict" -> "课程冲突检测"
    "disclaimer" -> "免责声明"
    "support" -> "支持作者"
    else -> "设置"
}

@Composable
fun SettingsScreen(
    startDateText: String,
    onStartDateChange: (String) -> Unit,
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    rowHeightDp: Int,
    onRowHeightChange: (Int) -> Unit,
    jwUsername: String,
    onUsernameChange: (String) -> Unit,
    jwPassword: String,
    onPasswordChange: (String) -> Unit,
    isFetching: Boolean,
    onFetchFromJw: () -> Unit,
    autoUpdateJw: Boolean,
    onAutoUpdateChange: (Boolean) -> Unit,
    onImportFile: () -> Unit,
    books: List<ScheduleBook>,
    currentBookId: String,
    onSelectBook: (String) -> Unit,
    onAddBook: () -> Unit,
    onRenameBook: (String, String) -> Unit,
    onDeleteBook: (String) -> Unit,
    totalWeeks: Int,
    onTotalWeeksChange: (Int) -> Unit,
    bgImagePath: String,
    cellAlpha: Float,
    onCellAlphaChange: (Float) -> Unit,
    borderAlpha: Float,
    onBorderAlphaChange: (Float) -> Unit,
    indexColWidthDp: Int,
    weekAnim: String,
    onWeekAnimChange: (String) -> Unit,
    onIndexColWidthChange: (Int) -> Unit,
    onPickBgImage: () -> Unit,
    onClearBgImage: () -> Unit,
    widgetBgRev: Int,
    onWidgetCellAlphaChange: (Int, Float) -> Unit,
    onPickWidgetBgImage: (Int) -> Unit,
    onClearWidgetBgImage: (Int) -> Unit,
    onBack: () -> Unit,
    widgetFonts: Map<String, Float>,
    onWidgetFontChange: (String, Int, Float) -> Unit,
    timeCfg: TimeConfig,
    onTimeCfgChange: (TimeConfig) -> Unit,
    onAddCourse: (Course) -> Unit,
    courses: List<Course>,
    onDeleteCourse: (Int) -> Unit,
    onUpdateCourse: (Int, Course) -> Unit,
    onWidgetBgModeChange: (Int, String) -> Unit,
    onWidgetColorChange: (Int, String, Int) -> Unit,
    onWidgetColorReset: (Int, String) -> Unit,
    // 第 44 批新增：把分享码里的课表「另存为新课表」/「并入当前课表」
    onImportBook: (SharedTimetable) -> Unit,
    onMergeCourses: (List<Course>) -> Unit
) {
    // 一级 / 二级页面：main = 纯列表（没有按钮）
    var page by remember { mutableStateOf("main") }
    var showPassword by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showJwLogin by remember { mutableStateOf(false) }
    var renamingId by remember { mutableStateOf("") }
    var renameText by remember { mutableStateOf("") }
    var showManual by remember { mutableStateOf(false) }
    var deleteIdx by remember { mutableIntStateOf(-1) }   // >= 0 表示正在确认删这一条课程
    var editIdx by remember { mutableIntStateOf(-1) }     // >= 0 表示正在编辑这一条课程
    var colorIdx by remember { mutableIntStateOf(-1) }    // >= 0 表示正在给这一条课程挑颜色
    var widgetSizeKey by remember { mutableStateOf(WIDGET_KEY_FULL) }
    var widgetTargetId by remember { mutableIntStateOf(-1) }  // -1 = 按尺寸调，>=0 = 调某一个组件
    var widgetRev by remember { mutableIntStateOf(0) }
    // ---------- 节数 / 上课时间：底部滚轮弹窗要改哪一项 ----------
    var wheelTarget by remember { mutableStateOf("") }   // "" / count / len / break / start / sec
    var wheelGroup by remember { mutableIntStateOf(0) }
    var wheelSection by remember { mutableIntStateOf(1) }         // 组件字体改过之后靠它触发重画
    val ctx = LocalContext.current

    // 系统返回键：三级页（小部件字体）先回小部件页，其余二级页回一级，一级再退回课表
    val goBack: () -> Unit = {
        when (page) {
            "main" -> onBack()
            "widgetfont" -> page = "widgetmanage"
            "widgetbg" -> page = "widget"
            "widgetcolor" -> page = "widget"
            "widgetadd", "widgetmanage" -> page = "widget"
            else -> page = "main"
        }
    }

    BackHandler {
        goBack()
    }

    // 一级 / 二级页面共用同一个 Column，滚动状态会被带到下一页：
    // 从滚过的设置列表点进子页时，标题栏和「← 返回」会被顶出屏幕（第 43 批实测发现）。
    // 所以每次换页都把滚动位置归零。
    val pageScroll = rememberScrollState()
    LaunchedEffect(page) { pageScroll.scrollTo(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(pageScroll)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // 第 45 批：设计稿顶栏 —— [←] 与 28sp 大标题同一行、垂直居中、靠左
            M3TopBar(title = settingsPageTitle(page), onBack = goBack)

            Spacer(Modifier.height(8.dp))

            when (page) {
                // ---------------- 一级：纯列表，没有任何按钮 ----------------
                "main" -> {
                    Column {
                        // ---------- 课表基础设置 ----------
                        M3GroupTitle("课表基础设置")
                        M3ListGroup {
                            M3ListItem(
                                index = 0, count = 3,
                                title = "课表选择",
                                supporting = "查看旧课表，添加新课表",
                                onClick = { page = "books" }
                            )
                            M3ListItem(
                                index = 1, count = 3,
                                title = "课表编辑",
                                supporting = "导入课表，添加课程、删除课程",
                                onClick = { page = "import" }
                            )
                            M3ListItem(
                                index = 2, count = 3,
                                title = "时间设置",
                                supporting = "开学周一，学习周数，课表节数，课时时间",
                                onClick = { page = "timeset" }
                            )
                        }

                        // ---------- 显示样式设置 ----------
                        M3GroupTitle("显示样式设置")
                        M3ListGroup {
                            M3ListItem(
                                index = 0, count = 1,
                                title = "显示调整",
                                supporting = "字体大小，课表行高，节数列宽度",
                                onClick = { page = "display" }
                            )
                        }
                        Spacer(Modifier.height(3.dp))
                        M3ListGroup {
                            M3ListItem(
                                index = 0, count = 3,
                                title = "切换动画",
                                supporting = "切换周数显示动画",
                                onClick = { page = "weekanim" }
                            )
                            M3ListItem(
                                index = 1, count = 3,
                                title = "背景设置",
                                supporting = "添加课表背景，设置透明度",
                                onClick = { page = "bg" }
                            )
                            M3ListItem(
                                index = 2, count = 3,
                                title = "桌面小部件",
                                supporting = "设置小部件各种参数",
                                onClick = { page = "widget" }
                            )
                        }

                        // ---------- 实用功能 ----------
                        // 设计稿：课程冲突检测不再单独列入口，改成导入课表时顺便检测
                        M3GroupTitle("实用功能")
                        M3ListGroup {
                            M3ListItem(
                                index = 0, count = 2,
                                title = "上课提醒/静音",
                                supporting = "上课前提醒，上课静音",
                                onClick = { page = "reminder" }
                            )
                            M3ListItem(
                                index = 1, count = 2,
                                title = "分享与导入",
                                supporting = "分享课表给好友，导入好友课表",
                                onClick = { page = "share" }
                            )
                        }

                        // ---------- 其他 ----------
                        M3GroupTitle("其他")
                        M3ListGroup {
                            M3ListItem(
                                index = 0, count = 3,
                                title = "操作手册",
                                supporting = "每项设置怎么用，常见问题",
                                onClick = { page = "manual" }
                            )
                            M3ListItem(
                                index = 1, count = 3,
                                title = "免责声明",
                                supporting = "非官方 · 数据来源 · 隐私 · 时间说明",
                                onClick = { page = "disclaimer" }
                            )
                            M3ListItem(
                                index = 2, count = 3,
                                title = "支持作者",
                                supporting = "扫码请我喝杯奶茶 ☕",
                                onClick = { page = "support" }
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
                // ============ 第 45 批新增（设计稿 屏幕3 / 屏幕4）============

                // ---------------- 时间设置（屏幕 3）----------------
                "timeset" -> {
                    Column {
                        M3GroupTitle("时间设置")
                        M3ListGroup {
                            M3ListItem(
                                index = 0, count = 4,
                                title = "开学周一",
                                showChevron = false,
                                trailing = {
                                    Text(
                                        startDateForDisplay(startDateText),
                                        fontSize = 22.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = { showDatePicker = true }
                            )
                            M3ListItem(
                                index = 1, count = 4,
                                title = "学习周数",
                                showChevron = false,
                                trailing = {
                                    Text(
                                        totalWeeks.toString(),
                                        fontSize = 22.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = { wheelTarget = "weeks" }
                            )
                            M3ListItem(
                                index = 2, count = 4,
                                title = "课表节数",
                                showChevron = false,
                                trailing = {
                                    Text(
                                        timeCfg.counts.getOrElse(0) { 0 }.toString() + "-" +
                                                timeCfg.counts.getOrElse(1) { 0 } + "-" +
                                                timeCfg.counts.getOrElse(2) { 0 },
                                        fontSize = 22.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                onClick = { wheelTarget = "count" }
                            )
                            M3ListItem(
                                index = 3, count = 4,
                                title = "课表时间",
                                onClick = { page = "times" }
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
                // ---------------- 显示调整（屏幕 4）----------------
                "display" -> {
                    Column {
                        M3GroupTitle("显示调整")
                        Text(
                            "字体大小",
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                        )
                        Spacer(Modifier.height(6.dp))
                        M3SliderBar(
                            value = fontScale,
                            onValueChange = { v -> onFontScaleChange(Math.round(v * 10f) / 10f) },
                            valueRange = 0.6f..2.0f
                        )
                        Text(
                            "当前 " + (fontScale * 100).toInt() + "%　（只影响课表里的字）",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        Spacer(Modifier.height(16.dp))
                        Text(
                            "课表行高",
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Spacer(Modifier.height(6.dp))
                        M3SliderBar(
                            value = rowHeightDp.toFloat(),
                            onValueChange = { v ->
                                onRowHeightChange((Math.round(v / 4f) * 4).coerceIn(32, 100))
                            },
                            valueRange = 32f..100f
                        )
                        Text(
                            "当前 " + rowHeightDp + " dp　（每节课那一行的高度）",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        Spacer(Modifier.height(16.dp))
                        Text(
                            "节数列宽度",
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Spacer(Modifier.height(6.dp))
                        M3SliderBar(
                            value = (if (indexColWidthDp > 0) indexColWidthDp else 60).toFloat(),
                            onValueChange = { v -> onIndexColWidthChange(v.toInt().coerceIn(40, 110)) },
                            valueRange = 40f..110f
                        )
                        Text(
                            "当前 " + (if (indexColWidthDp > 0) indexColWidthDp.toString() + " dp" else "自动（54〜60 dp）"),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        Spacer(Modifier.height(24.dp))
                        M3FilledButton(
                            text = "恢复默认",
                            onClick = {
                                onFontScaleChange(1.0f)
                                onRowHeightChange(52)
                                onIndexColWidthChange(0)
                            }
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                }

                // ---------------- 操作手册（第 45 批新增）----------------
                "manual" -> {
                    Column {
                        M3GroupTitle("课表怎么用")
                        ManualPara(
                            title = "加入课程",
                            body = "课表编辑 → 教务获取：填学号与密码后点「开始导入」，直接从教务系统拉取；" +
                                    "也可以用「导入TXT」选一个 txt 文件，或「手动添加课程」一条一条加。"
                        )
                        ManualPara(
                            title = "开学周一 / 学习周数",
                            body = "时间设置里，「开学周一」决定第 1 周从哪天算，「学习周数」是这个课表一共上几周。" +
                                    "两项都是点一下、从底部升起选择，滑动选好按确定生效。"
                        )
                        ManualPara(
                            title = "课表节数",
                            body = "时间设置 → 课表节数：一次设置上午 / 下午 / 晚上三段的节数（每段 0~10 节）。" +
                                    "如果某节课的节次超过这里的设置，它就显示不出来，把对应那段调大即可。"
                        )
                        ManualPara(
                            title = "课表时间",
                            body = "时间设置 → 课表时间：分别设置三段的上课开始时间、每节课时长、课间休息。" +
                                    "打开「每节时长相同」只需调时长；关掉之后可以逐节精调每节的起止时间。"
                        )
                        ManualPara(
                            title = "显示样式",
                            body = "显示调整里改课表格子的透明度；字体大小、课表行高、节数列宽度分别在各自页面里调；" +
                                    "背景设置可以选一张图片作底，并调整课程格子的透明度。"
                        )
                        ManualPara(
                            title = "切换动画",
                            body = "在上下周之间切换时是否播放滑动动画，关掉会更跟手。"
                        )
                        ManualPara(
                            title = "桌面小部件",
                            body = "桌面小部件 → 添加小部件放到桌面上：点某个尺寸让系统弹确认框即可；" +
                                    "没弹的话，长按桌面空白处 → 选「小部件」（有的手机叫「添加工具」）→ " +
                                    "找到「芝士院特供课表」→ 把想要的尺寸拖到桌面上。" +
                                    "字体行高、小部件背景、小部件颜色都在这一页。" +
                                    "颜色页先选小部件编号，再点部位（周数 / 日期 / 提示 / 格子 / 时间 / 丨线 / 课名 / 其他）调色；" +
                                    "打开「统一颜色」后所有文字与线会一起变。"
                        )
                        ManualPara(
                            title = "上课提醒与上课静音",
                            body = "上课提醒会按「提前量」发通知（锁屏会亮屏弹窗）；提醒有没有声音、以及悬浮通知 / 锁屏显示，" +
                                    "都在设置里的「通知设置」那一页改（那是系统的页面，App 不会自己发出声音）。" +
                                    "「上课静音」会在上课时自动静音、下课时恢复原来的铃声，需要先授予勿打扰访问权限。"
                        )
                        ManualPara(
                            title = "课表管理",
                            body = "课表选择里可以新建、切换、重命名、删除课表，每张课表的开学时间与周数互不影响；" +
                                    "分享与导入可以把课表生成分享码，或从分享码导入、并入当前课表。"
                        )
                        ManualPara(
                            title = "常见问题",
                            body = "① 某节课显示不出来：多半是节次超过「课表节数」，把它调大。\n" +
                                    "② 换了新课表，旧课表去哪了：还在课表选择里，切回去就行。\n" +
                                    "③ 想换上课时间或地点：课表编辑 → 编辑课程 → 点铅笔图标。"
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                }
                "books" -> {
                    Column {
                        SettingsCard {
                            books.forEachIndexed { idx, b ->
                                val isCur = b.id == currentBookId
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isCur) Color(0xFFF2F3F5) else Color.Transparent
                                        )
                                        .clickable { onSelectBook(b.id) }
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            if (isCur) "●" else "○",
                                            fontSize = 12.sp,
                                            color = if (isCur) Color(0xFF37474F) else Color(0xFFBDBDBD)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                bookDisplayName(books, idx),
                                                fontSize = 15.sp,
                                                fontWeight = if (isCur) FontWeight.Bold else FontWeight.Normal
                                            )
                                            Text(
                                                "开学 " + startDateForDisplay(b.startDate) +
                                                        " · 共 " + b.totalWeeks + " 周" +
                                                        (if (isCur) " · 当前使用" else ""),
                                                fontSize = 11.sp,
                                                color = Color(0xFF8A8A8A)
                                            )
                                        }
                                        TextButton(
                                            onClick = {
                                                renamingId = b.id
                                                renameText = b.name
                                            },
                                            contentPadding = PaddingValues(4.dp),
                                            modifier = Modifier.height(36.dp)
                                        ) {
                                            Text("改名", fontSize = 13.sp, color = Color(0xFF546E7A))
                                        }
                                        TextButton(
                                            onClick = { onDeleteBook(b.id) },
                                            contentPadding = PaddingValues(4.dp),
                                            modifier = Modifier.height(36.dp)
                                        ) {
                                            Text("删除", fontSize = 13.sp, color = Color(0xFFB00020))
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        SoftButton(
                            text = "＋ 添加课表",
                            onClick = onAddBook,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            fontSize = 15.sp
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }

                // ---------------- 开学周一 ----------------
                "start" -> {
                    Column {
                        SettingsCard {
                            Text("当前开学周一", fontSize = 12.sp, color = Color(0xFF8A8A8A))
                            Spacer(Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFFFFFFF))
                                    .border(
                                        BorderStroke(1.dp, Color(0xFFE3E3E3)),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { showDatePicker = true }
                                    .padding(horizontal = 12.dp, vertical = 14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        startDateForDisplay(startDateText),
                                        fontSize = 17.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text("📅", fontSize = 16.sp)
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                // ---------------- 学习周数 ----------------
                "weeks" -> {
                    Column {
                        SettingsCard {
                            Text("当前：共 " + totalWeeks + " 周", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SoftButton(
                                    text = "-1",
                                    onClick = { onTotalWeeksChange((totalWeeks - 1).coerceAtLeast(1)) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                )
                                SoftButton(
                                    text = "重置",
                                    onClick = { onTotalWeeksChange(20) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                )
                                SoftButton(
                                    text = "+1",
                                    onClick = { onTotalWeeksChange((totalWeeks + 1).coerceAtMost(30)) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                // ---------------- 导入课表 ----------------
                "import" -> {
                    Column {
                        M3GroupTitle("导入课表")
                        M3ListGroup {
                            // 第 47 批（二轮）：自动更新挪到「教务获取」这一行的右边，不再单独占一行
                            M3ListItem(
                                index = 0, count = 3,
                                title = "教务获取",
                                supporting = "输入学号密码获取课表元素",
                                showChevron = false,
                                trailing = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            "自动更新",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Switch(
                                            checked = autoUpdateJw,
                                            onCheckedChange = onAutoUpdateChange
                                        )
                                    }
                                },
                                onClick = { showJwLogin = !showJwLogin }
                            )
                            M3ListItem(
                                index = 1, count = 3,
                                title = "导入TXT",
                                supporting = "在电脑上获取教务系统个人课表中的元素",
                                showChevron = false,
                                onClick = onImportFile
                            )
                            M3ListItem(
                                index = 2, count = 3,
                                title = "手动添加课程",
                                showChevron = false,
                                onClick = { showManual = true }
                            )
                        }

                        // 点「教务获取」向下展开：账号 / 密码 / 开始导入
                        if (showJwLogin) {
                            Spacer(Modifier.height(10.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(m3eLow())
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                            ) {
                                OutlinedTextField(
                                    value = jwUsername,
                                    onValueChange = onUsernameChange,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(64.dp),
                                    singleLine = true,
                                    label = { Text("学号", fontSize = 12.sp) },
                                    textStyle = TextStyle(fontSize = 15.sp)
                                )
                                Spacer(Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = jwPassword,
                                    onValueChange = onPasswordChange,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(64.dp),
                                    singleLine = true,
                                    label = { Text("密码", fontSize = 12.sp) },
                                    visualTransformation = if (showPassword) {
                                        VisualTransformation.None
                                    } else {
                                        PasswordVisualTransformation()
                                    },
                                    trailingIcon = {
                                        TextButton(
                                            onClick = { showPassword = !showPassword },
                                            contentPadding = PaddingValues(4.dp),
                                            modifier = Modifier.height(44.dp)
                                        ) {
                                            Text(if (showPassword) "🙈" else "👁", fontSize = 18.sp)
                                        }
                                    },
                                    textStyle = TextStyle(fontSize = 15.sp)
                                )
                                Spacer(Modifier.height(12.dp))
                                SoftButton(
                                    text = if (isFetching) "导入中..." else "开始导入",
                                    onClick = onFetchFromJw,
                                    enabled = !isFetching,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp),
                                    fontSize = 15.sp
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))
                        M3GroupTitle("编辑课程")
                        if (courses.isEmpty()) {
                            M3ListGroup {
                                M3ListItem(
                                    index = 0, count = 1,
                                    title = "这个课表现在还没有课程",
                                    showChevron = false,
                                    onClick = { showManual = true }
                                )
                            }
                        } else {
                            courses.forEachIndexed { ci, c ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(m3eLow())
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            c.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            courseMainInfo(c),
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    // 颜色块：点它换这门课的颜色（没手动选过 = 按课程名自动配）
                                    Box(
                                        modifier = Modifier
                                            .width(30.dp)
                                            .height(34.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(getCourseColor(c))
                                            .border(BorderStroke(1.dp, Color(0x1F000000)), RoundedCornerShape(6.dp))
                                            .clickable { colorIdx = ci },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "调",
                                            fontSize = 10.sp,
                                            color = getCourseTextColor(c),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(34.dp)
                                            .height(34.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFF2F2F2))
                                            .clickable { editIdx = ci },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_edit),
                                            contentDescription = "编辑课程",
                                            tint = Color(0xFF1F1F1F),
                                            modifier = Modifier
                                                .width(17.dp)
                                                .height(17.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(34.dp)
                                            .height(34.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFF2F2F2))
                                            .clickable { deleteIdx = ci },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "×",
                                            fontSize = 18.sp,
                                            color = Color(0xFFC62828),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(Modifier.height(3.dp))
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
                // ---------------- 字体大小 ----------------
                "font" -> {
                    Column {
                        Text(
                            "字体大小：" + (fontScale * 100).toInt() + "%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Slider(
                            value = fontScale,
                            onValueChange = { v -> onFontScaleChange(Math.round(v * 10f) / 10f) },
                            valueRange = 0.6f..2.0f,
                            steps = 13
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("60%", fontSize = 11.sp, color = Color(0xFF9E9E9E))
                            Text("200%", fontSize = 11.sp, color = Color(0xFF9E9E9E))
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SoftButton(
                                text = "标准 100%",
                                onClick = { onFontScaleChange(1.0f) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            )
                            SoftButton(
                                text = "放大 150%",
                                onClick = { onFontScaleChange(1.5f) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                // ---------------- 课表行高 ----------------
                "row" -> {
                    Column {
                        Text(
                            "课表行高：" + rowHeightDp + " dp",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Slider(
                            value = rowHeightDp.toFloat(),
                            onValueChange = { v -> onRowHeightChange((Math.round(v / 4f) * 4).coerceIn(32, 100)) },
                            valueRange = 32f..100f,
                            steps = 16
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("32 dp", fontSize = 11.sp, color = Color(0xFF9E9E9E))
                            Text("100 dp", fontSize = 11.sp, color = Color(0xFF9E9E9E))
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SoftButton(
                                text = "紧凑 40 dp",
                                onClick = { onRowHeightChange(40) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            )
                            SoftButton(
                                text = "默认 52 dp",
                                onClick = { onRowHeightChange(52) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                // ---------------- 节数列宽度 ----------------
                "indexw" -> {
                    Column {
                        Text(
                            "节数列宽度：" + (if (indexColWidthDp > 0) indexColWidthDp.toString() + " dp" else "自动"),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Slider(
                            value = (if (indexColWidthDp > 0) indexColWidthDp else 60).toFloat(),
                            onValueChange = { v -> onIndexColWidthChange(v.toInt().coerceIn(40, 110)) },
                            valueRange = 40f..110f
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("40 dp", fontSize = 11.sp, color = Color(0xFF9E9E9E))
                            Text("110 dp", fontSize = 11.sp, color = Color(0xFF9E9E9E))
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SoftButton(
                                text = "自动",
                                selected = indexColWidthDp <= 0,
                                onClick = { onIndexColWidthChange(0) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            )
                            SoftButton(
                                text = "默认 60 dp",
                                onClick = { onIndexColWidthChange(60) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                // ---------------- 切换动画 ----------------
                "weekanim" -> {
                    Column {
                        WEEK_ANIM_OPTIONS.forEach { opt ->
                            val isCur = opt.first == weekAnim
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isCur) Color(0xFFE8F0FE)
                                        else Color(0xFFFFFFFF)
                                    )
                                    .border(
                                        width = if (isCur) 1.5.dp else 1.dp,
                                        color = if (isCur) Color(0xFF3B82F6)
                                                else Color(0xFFE0E0E0),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { onWeekAnimChange(opt.first) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            opt.second,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCur) Color(0xFF1D4ED8)
                                                    else Color(0xFF263238)
                                        )
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            opt.third,
                                            fontSize = 11.sp,
                                            color = Color(0xFF8A8A8A)
                                        )
                                    }
                                    if (isCur) {
                                        Text("✓", fontSize = 18.sp, color = Color(0xFF3B82F6))
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
                // ---------------- 桌面小部件 ----------------
                // ---------------- 桌面小部件（屏幕 5 ·第 45 批改版）----------------
                "widget" -> {
                    val wInstances = listWidgetInstances(ctx)
                    // 字体行高滑块：默认调桌面上的第一个组件；一个组件都没有时按 2×2 中号那个尺寸调
                    val wTargetId = if (widgetTargetId >= 0) widgetTargetId
                    else (wInstances.firstOrNull()?.second ?: -1)
                    val wTargetKey = if (widgetTargetId >= 0) widgetSizeKey
                    else (wInstances.firstOrNull()?.first ?: WIDGET_KEY_FULL)
                    val wTargetName = if (wTargetId >= 0) {
                        val ids = wInstances.filter { it.first == wTargetKey }.map { it.second }
                        val idx = ids.indexOf(wTargetId)
                        widgetSizeLabel(wTargetKey) + " " + widgetIndexMark(if (idx >= 0) idx else 0)
                    } else {
                        widgetSizeLabel(wTargetKey) + "（默认）"
                    }
                    var wScale by remember(wTargetKey, wTargetId) {
                        mutableFloatStateOf(
                            if (wTargetId >= 0) loadWidgetFontScaleForId(ctx, wTargetKey, wTargetId)
                            else (widgetFonts[wTargetKey] ?: 1f)
                        )
                    }
                    Column {
                        M3GroupTitle("桌面小部件")
                        M3ListGroup {
                            M3ListItem(
                                index = 0, count = 4,
                                title = "添加小部件",
                                supporting = "点击即可添加桌面小部件，但有的机型不支持",
                                onClick = { page = "widgetadd" }
                            )
                            // 字体行高：滑块直接叠在这一行里（设计稿「内部叠放」）
                            M3ItemBox(index = 1, count = 4) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "字体行高",
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        wTargetName + "　" + (wScale * 100).toInt() + "%",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                M3SliderBar(
                                    value = wScale,
                                    onValueChange = { v ->
                                        wScale = v
                                        onWidgetFontChange(wTargetKey, wTargetId, v)
                                        widgetRev++
                                    },
                                    valueRange = WIDGET_FONT_MIN..WIDGET_FONT_MAX,
                                    modifier = Modifier.width(140.dp)
                                )
                            }
                            M3ListItem(
                                index = 2, count = 4,
                                title = "小部件背景",
                                supporting = "添加背景，设置透明度",
                                onClick = {
                                    // 进来默认选上第一个组件，省得还要再点一下
                                    val first = listWidgetInstances(ctx).firstOrNull()
                                    if (widgetTargetId < 0 && first != null) {
                                        widgetSizeKey = first.first
                                        widgetTargetId = first.second
                                    }
                                    page = "widgetbg"
                                }
                            )
                            M3ListItem(
                                index = 3, count = 4,
                                title = "小部件颜色",
                                supporting = "设置小部件各个部位的颜色",
                                onClick = {
                                    val first = listWidgetInstances(ctx).firstOrNull()
                                    if (widgetTargetId < 0 && first != null) {
                                        widgetSizeKey = first.first
                                        widgetTargetId = first.second
                                    }
                                    page = "widgetcolor"
                                }
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                // ---------------- 添加小部件 ----------------
                "widgetadd" -> {
                    Column {
                        WIDGET_SIZE_ORDER.forEach { k ->
                            val n = countWidgets(ctx, k)
                            SettingsRow(
                                title = widgetSizeLabel(k),
                                summary = "点这一行试试一键添加" +
                                        (if (n > 0) " · 桌面上已有 " + n + " 个" else ""),
                                onClick = {
                                    if (pinWidgetToHome(ctx, k)) {
                                        Toast.makeText(
                                            ctx,
                                            "如果没弹出确认框，请用下面的手动方法",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            ctx,
                                            "这个系统不支持一键添加，请用下面的手动方法",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            )
                        }
                        Spacer(Modifier.height(14.dp))
                        Text("手动添加（四下就好）", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        SoftButton(
                            text = "回到桌面，我现在就试",
                            onClick = { goToHome(ctx) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            fontSize = 15.sp
                        )
                    }
                }


                // ---------------- 管理小部件 ----------------
                "widgetmanage" -> {
                    val instances = listWidgetInstances(ctx)
                    Column {
                        Text(
                            "桌面上已添加 " + instances.size + " 个组件",
                            fontSize = 12.sp,
                            color = Color(0xFF8A8A8A)
                        )
                        Spacer(Modifier.height(10.dp))
                        if (instances.isEmpty()) {
                            Text(
                                "现在桌面上还没有这个 App 的小部件，去「添加小部件」里加一个吧。",
                                fontSize = 12.sp,
                                color = Color(0xFF9E9E9E)
                            )
                            Spacer(Modifier.height(12.dp))
                            SoftButton(
                                text = "去添加小部件",
                                onClick = { page = "widgetadd" },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                fontSize = 15.sp
                            )
                        } else {
                            WIDGET_SIZE_ORDER.forEach { k ->
                                val ids = instances.filter { it.first == k }.map { it.second }
                                ids.forEachIndexed { idx, id ->
                                    // 一行搞定：名字 + 缩小版的「字号 / 行高」拉条 + 当前百分比。
                                    // 这里不再是入口，所以没有「点进去」这回事（背景那些都在「小部件背景」里调）。
                                    var scale by remember(id) {
                                        mutableFloatStateOf(loadWidgetFontScaleForId(ctx, k, id))
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            widgetSizeLabel(k) + " " + widgetIndexMark(idx),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(Modifier.width(10.dp))
                                        Slider(
                                            value = scale,
                                            onValueChange = {
                                                scale = it
                                                onWidgetFontChange(k, id, it)
                                                widgetRev++
                                            },
                                            valueRange = WIDGET_FONT_MIN..WIDGET_FONT_MAX,
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(30.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            (scale * 100).toInt().toString() + "%",
                                            fontSize = 13.sp,
                                            color = Color(0xFF546E7A),
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.width(44.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }


                // ---------------- 小部件字体与行高 ----------------
                // （已无入口：字体 / 行高挪到「管理小部件」行内，背景与格子挪到「小部件背景」；
                //  保留在这里是为了以后想恢复时不用重写）
                "widgetfont" -> {
                    val rev = widgetRev   // 改过字体之后靠它触发重画（读取即订阅）
                    val curScale = if (widgetTargetId >= 0)
                        loadWidgetFontScaleForId(ctx, widgetSizeKey, widgetTargetId)
                    else
                        (widgetFonts[widgetSizeKey] ?: 1f)
                    // 滑块自己的值：拖动时立刻跟手，存盘 + 重画桌面同时进行
                    var editScale by remember(widgetSizeKey, widgetTargetId) {
                        mutableFloatStateOf(curScale)
                    }
                    val targetName = if (widgetTargetId >= 0) {
                        val idx = listWidgetInstances(ctx)
                            .filter { it.first == widgetSizeKey }
                            .map { it.second }
                            .indexOf(widgetTargetId)
                        widgetSizeLabel(widgetSizeKey) + " " + widgetIndexMark(if (idx >= 0) idx else 0)
                    } else {
                        widgetSizeLabel(widgetSizeKey)
                    }
                    Column {
                        Text(targetName, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "当前字号 / 行高：" + (editScale * 100).toInt() + "%",
                            fontSize = 13.sp,
                            color = Color(0xFF546E7A)
                        )
                        Spacer(Modifier.height(8.dp))
                        Slider(
                            value = editScale,
                            onValueChange = {
                                editScale = it
                                onWidgetFontChange(widgetSizeKey, widgetTargetId, it)
                                widgetRev++
                            },
                            valueRange = WIDGET_FONT_MIN..WIDGET_FONT_MAX
                        )
                        Spacer(Modifier.height(10.dp))
                        WidgetBgSection(
                            context = ctx,
                            targetId = widgetTargetId,
                            bgRev = widgetBgRev,
                            onBgModeChange = onWidgetBgModeChange,
                            onPickImage = onPickWidgetBgImage,
                            onClearImage = onClearWidgetBgImage,
                            onCellAlphaChange = onWidgetCellAlphaChange
                        )
                        Spacer(Modifier.height(16.dp))
                        SoftButton(
                            text = "返回组件列表",
                            onClick = { page = "widgetmanage" },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            fontSize = 15.sp
                        )
                    }
                }


                // ---------------- 小部件背景（先选编号，再调背景与格子） ----------------
                "widgetbg" -> {
                    val instances = listWidgetInstances(ctx)
                    // 拉平成「尺寸 + id」，编号按同一尺寸内出现的顺序排，跟管理页一致
                    val all = mutableListOf<Pair<String, Int>>()
                    WIDGET_SIZE_ORDER.forEach { k ->
                        instances.filter { it.first == k }.forEach { all.add(k to it.second) }
                    }
                    val labels = mutableMapOf<Int, String>()
                    WIDGET_SIZE_ORDER.forEach { k ->
                        instances.filter { it.first == k }.map { it.second }
                            .forEachIndexed { i, id ->
                                labels[id] = widgetSizeLabel(k) + " " + widgetIndexMark(i)
                            }
                    }
                    Column {
                        Text("小部件背景", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        if (all.isEmpty()) {
                            Text(
                                "现在桌面上还没有这个 App 的小部件，先去「添加小部件」加一个。",
                                fontSize = 12.sp,
                                color = Color(0xFF9E9E9E)
                            )
                            Spacer(Modifier.height(12.dp))
                            SoftButton(
                                text = "去添加小部件",
                                onClick = { page = "widgetadd" },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                fontSize = 15.sp
                            )
                        } else {
                            Text(
                                "选择组件编号",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF37474F)
                            )
                            Spacer(Modifier.height(6.dp))
                            all.chunked(2).forEach { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowItems.forEach { item ->
                                        SoftButton(
                                            text = labels[item.second] ?: "组件",
                                            selected = item.second == widgetTargetId,
                                            onClick = {
                                                widgetSizeKey = item.first
                                                widgetTargetId = item.second
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(42.dp),
                                            fontSize = 13.sp
                                        )
                                    }
                                    if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                                }
                                Spacer(Modifier.height(8.dp))
                            }
                            Spacer(Modifier.height(2.dp))
                        }
                        WidgetBgSection(
                            context = ctx,
                            targetId = widgetTargetId,
                            bgRev = widgetBgRev,
                            onBgModeChange = onWidgetBgModeChange,
                            onPickImage = onPickWidgetBgImage,
                            onClearImage = onClearWidgetBgImage,
                            onCellAlphaChange = onWidgetCellAlphaChange
                        )
                        Spacer(Modifier.height(16.dp))
                        SoftButton(
                            text = "返回",
                            onClick = { page = "widget" },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            fontSize = 15.sp
                        )
                    }
                }



                // ---------------- 小部件颜色（先选编号，再逐项挑颜色） ----------------
// ---------------- 小部件颜色（屏幕 6 ·第 45 批改版）----------------
                "widgetcolor" -> {
                    val instances = listWidgetInstances(ctx)
                    // 挑完颜色靠它触发重组（读取即订阅，值一变就重新读一遍颜色）
                    var colorRevLocal by remember { mutableIntStateOf(0) }
                    // 拉平成「尺寸 + 组件 id」，编号按同一尺寸内出现的顺序排，跟管理页一致
                    val all = mutableListOf<Pair<String, Int>>()
                    WIDGET_SIZE_ORDER.forEach { k ->
                        instances.filter { it.first == k }.forEach { all.add(k to it.second) }
                    }
                    val labels = mutableMapOf<Int, String>()
                    WIDGET_SIZE_ORDER.forEach { k ->
                        instances.filter { it.first == k }.map { it.second }
                            .forEachIndexed { i, id ->
                                labels[id] = widgetSizeLabel(k) + " " + widgetIndexMark(i)
                            }
                    }
                    // 进来没选过组件就自动选第一个，省得还要再点一下
                    LaunchedEffect(all.size) {
                        if (widgetTargetId < 0 && all.isNotEmpty()) {
                            widgetSizeKey = all[0].first
                            widgetTargetId = all[0].second
                        }
                    }
                    val selIdx = all.indexOfFirst { it.second == widgetTargetId }
                    Column {
                        M3GroupTitle("小部件颜色")
                        if (all.isEmpty()) {
                            M3ListGroup {
                                M3ListItem(
                                    index = 0, count = 1,
                                    title = "现在还没有小部件",
                                    supporting = "先去「桌面小部件 → 添加小部件」放一个到桌面上",
                                    onClick = { page = "widgetadd" }
                                )
                            }
                        } else {
                            // ① 选小部件编号（描边下拉）
                            M3DropdownSelect(
                                options = all.map { labels[it.second] ?: "组件" },
                                selectedIndex = if (selIdx >= 0) selIdx else 0,
                                onSelect = { i ->
                                    widgetSizeKey = all[i].first
                                    widgetTargetId = all[i].second
                                    colorRevLocal++
                                },
                                supporting = "选中小部件后调试其各部位颜色"
                            )
                            Spacer(Modifier.height(20.dp))
                            // ② 部位标签片 + 颜色条 + 恢复默认色 / 统一颜色
                            WidgetColorPanel(
                                context = ctx,
                                targetId = widgetTargetId,
                                colorRev = colorRevLocal,
                                onColorChange = { id, slot, c ->
                                    onWidgetColorChange(id, slot, c)
                                    colorRevLocal++
                                },
                                onColorReset = { id, slot ->
                                    onWidgetColorReset(id, slot)
                                    colorRevLocal++
                                }
                            )
                        }
                    }
                }                // ---------------- 免责声明 ----------------
                "disclaimer" -> {
                    Column {
                        DisclaimerBody(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(430.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }


                // ---------------- 支持作者 ----------------
                "support" -> {
                    Column {
                        SettingsCard {
                            Text("如果这个课表帮到了你", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        SettingsCard {
                            Text("微信支付", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Image(
                                painter = painterResource(R.drawable.support_wechat),
                                contentDescription = "微信赞赏码",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        SettingsCard {
                            Text("支付宝", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.support_alipay),
                                    contentDescription = "支付宝收款码",
                                    modifier = Modifier
                                        .fillMaxWidth(0.62f)
                                        .height(380.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }

                // ---------------- 第 44 批新增：上课提醒 ----------------
                "reminder" -> {
                    Column { ReminderPage(onGoEdit = { page = "import" }) }
                }

                // ---------------- 第 44 批新增：课表分享 / 导入 ----------------
                "share" -> {
                    Column {
                        SharePage(
                            courses = courses,
                            startDateText = startDateText,
                            totalWeeks = totalWeeks,
                            bookName = bookDisplayName(
                                books,
                                books.indexOfFirst { it.id == currentBookId }
                            ),
                            onImportBook = onImportBook,
                            onMergeCourses = onMergeCourses
                        )
                    }
                }

                // ---------------- 第 44 批新增：课程冲突检测 ----------------
                "conflict" -> {
                    Column { ConflictPage(courses) }
                }

                // ---------------- 课表节数 ----------------
                "sections" -> {
                    Column {
                        GROUP_NAMES.forEachIndexed { gi, gname ->
                            SettingsRow(
                                title = gname,
                                summary = timeCfg.counts.getOrElse(gi) { 0 }.toString() + " 节",
                                onClick = {
                                    wheelGroup = gi
                                    wheelTarget = "count"
                                }
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "当前共 " + timeCfg.counts.sum() + " 节（上午 " +
                                    timeCfg.counts.getOrElse(0) { 0 } + " · 下午 " +
                                    timeCfg.counts.getOrElse(1) { 0 } + " · 晚上 " +
                                    timeCfg.counts.getOrElse(2) { 0 } + "）",
                            fontSize = 12.sp,
                            color = Color(0xFF546E7A)
                        )
                        Spacer(Modifier.height(14.dp))
                        SoftButton(
                            text = "恢复默认（上午 5 · 下午 4 · 晚上 2）",
                            onClick = { onTimeCfgChange(timeCfg.copy(counts = DEFAULT_SECTION_COUNTS)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }

                // ---------------- 课时时间 ----------------
                "times" -> {
                    val c0 = timeCfg.counts.getOrElse(0) { 0 }
                    val c1 = timeCfg.counts.getOrElse(1) { 0 }
                    val c2 = timeCfg.counts.getOrElse(2) { 0 }
                    val grpFirst = listOf(1, c0 + 1, c0 + c1 + 1)
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("每节课时长相同", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(2.dp))
                            }
                            Spacer(Modifier.width(8.dp))
                            SoftSwitch(
                                checked = timeCfg.sameLength,
                                onCheckedChange = { v ->
                                    onTimeCfgChange(
                                        if (v) timeCfg.copy(sameLength = true)
                                        else timeCfg.copy(sameLength = false, times = timeCfg.times)
                                    )
                                }
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        SettingsRow(
                            title = "每节课时长",
                            summary = timeCfg.sectionLen.toString() + " 分钟",
                            onClick = { wheelTarget = "len" }
                        )
                        SettingsRow(
                            title = "课间休息时长",
                            summary = timeCfg.breakLen.toString() + " 分钟",
                            onClick = { wheelTarget = "break" }
                        )
                        Spacer(Modifier.height(12.dp))
                        Text("三个时间段的上课开始时间", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        GROUP_NAMES.forEachIndexed { gi, gname ->
                            SettingsRow(
                                title = gname + "开始时间",
                                summary = timeCfg.groupStarts.getOrElse(gi) { DEFAULT_GROUP_STARTS[gi] },
                                onClick = {
                                    wheelGroup = gi
                                    wheelTarget = "start"
                                }
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("各节时间（点一行可精确调节）", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        if (c0 + c1 + c2 == 0) {
                            Text(
                                "现在总节数是 0，先去「课表节数」里设置一下",
                                fontSize = 12.sp,
                                color = Color(0xFF9E9E9E)
                            )
                        } else {
                            GROUP_NAMES.forEachIndexed { gi, gname ->
                                val n = timeCfg.counts.getOrElse(gi) { 0 }
                                if (n > 0) {
                                    Text(
                                        gname + "（" + n + " 节）",
                                        fontSize = 12.sp,
                                        color = Color(0xFF546E7A)
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    for (i in 0 until n) {
                                        val sec = grpFirst[gi] + i
                                        SettingsRow(
                                            title = "第 " + sec + " 节",
                                            summary = timeCfg.times.getOrNull(sec - 1) ?: "--:-- ~ --:--",
                                            onClick = {
                                                wheelSection = sec
                                                wheelTarget = "sec"
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        SoftButton(
                            text = "恢复默认时间（08:00 / 13:00 / 19:00，每节 40 分钟）",
                            onClick = {
                                onTimeCfgChange(
                                    TimeConfig(
                                        counts = timeCfg.counts,
                                        sameLength = true,
                                        sectionLen = DEFAULT_SECTION_LEN,
                                        breakLen = DEFAULT_BREAK_LEN,
                                        groupStarts = DEFAULT_GROUP_STARTS
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            fontSize = 13.sp
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }

                // ---------------- 背景设置 ----------------
                else -> {
                    Column {
                        SettingsCard {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SoftButton(
                                    text = if (bgImagePath.isBlank()) "选择背景图" else "换一张",
                                    onClick = onPickBgImage,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                )
                                SoftButton(
                                    text = "移除背景",
                                    onClick = onClearBgImage,
                                    enabled = bgImagePath.isNotBlank(),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                if (bgImagePath.isBlank()) "当前：无背景（纯白底）" else "当前：已设置背景图",
                                fontSize = 11.sp,
                                color = Color(0xFF8A8A8A)
                            )

                            Spacer(Modifier.height(14.dp))
                            Text(
                                "课程格子透明度：" + (cellAlpha * 100).toInt() + "%",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Slider(
                                value = cellAlpha,
                                onValueChange = onCellAlphaChange,
                                valueRange = 0.2f..1.0f
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SoftButton(
                                    text = "不透明",
                                    onClick = { onCellAlphaChange(1.0f) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp),
                                    fontSize = 13.sp
                                )
                                SoftButton(
                                    text = "半透明",
                                    onClick = { onCellAlphaChange(0.6f) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp),
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(Modifier.height(14.dp))
                            Text(
                                "边界透明度：" + ((1f - borderAlpha) * 100).toInt() + "%",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Slider(
                                value = 1f - borderAlpha,
                                onValueChange = { v -> onBorderAlphaChange(1f - v) },
                                valueRange = 0f..1f
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SoftButton(
                                    text = "不透明",
                                    onClick = { onBorderAlphaChange(1f) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp),
                                    fontSize = 13.sp
                                )
                                SoftButton(
                                    text = "半透明",
                                    onClick = { onBorderAlphaChange(0.5f) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp),
                                    fontSize = 13.sp
                                )
                                SoftButton(
                                    text = "全透明",
                                    onClick = { onBorderAlphaChange(0f) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp),
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "当前课表：" + bookDisplayName(books, books.indexOfFirst { it.id == currentBookId }) +
                        "  |  共 " + totalWeeks + " 周" +
                        "  |  真实当前周：" + calcCurrentWeek(startDateText),
                fontSize = 12.sp,
                color = Color(0xFF9E9E9E)
            )
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showDatePicker) {
        StartDatePickerDialog(
            current = parseStartDateOrNull(startDateText) ?: LocalDate.now(),
            onDismiss = { showDatePicker = false },
            onConfirm = { picked ->
                showDatePicker = false
                // 第 47 批：开学日一定是周一 —— 选了别的日子，就自动回到那一周的周一
                val monday = picked.with(
                    java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)
                )
                onStartDateChange(monday.format(START_DATE_STORE_FMT))
            }
        )
    }

    if (renamingId.isNotBlank()) {
        RenameBookDialog(
            initial = renameText,
            onDismiss = { renamingId = "" },
            onConfirm = { newName ->
                onRenameBook(renamingId, newName)
                renamingId = ""
            }
        )
    }

    if (showManual) {
        ManualCourseDialog(
            onDismiss = { showManual = false },
            onConfirm = { c ->
                showManual = false
                onAddCourse(c)
            }
        )
    }

    // 编辑课程：点课程行里的铅笔图标打开，内容已回填
    if (editIdx in courses.indices) {
        ManualCourseDialog(
            onDismiss = { editIdx = -1 },
            onConfirm = { c ->
                onUpdateCourse(editIdx, c)
                editIdx = -1
            },
            initial = courses[editIdx],
            title = "编辑课程",
            confirmText = "保存"
        )
    }

    // 换颜色：点课程行最左边那个色块打开
    if (colorIdx in courses.indices) {
        CourseColorDialog(
            courseName = courses[colorIdx].name,
            current = courses[colorIdx].colorIndex,
            auto = courseColorIndex(courses[colorIdx].name),
            onDismiss = { colorIdx = -1 },
            onConfirm = { picked ->
                onUpdateCourse(colorIdx, courses[colorIdx].copy(colorIndex = picked))
                colorIdx = -1
            }
        )
    }

    if (deleteIdx in courses.indices) {
        CourseDeleteDialog(
            name = courses[deleteIdx].name,
            detail = courseMainInfo(courses[deleteIdx]),
            onDismiss = { deleteIdx = -1 },
            onConfirm = {
                onDeleteCourse(deleteIdx)
                deleteIdx = -1
            }
        )
    }

    when (wheelTarget) {
        "weeks" -> WheelDialog(
            title = "学习周数",
            labels = listOf("周"),
            mins = listOf(1),
            maxs = listOf(30),
            inits = listOf(totalWeeks.coerceIn(1, 30)),
            onDismiss = { wheelTarget = "" },
            onConfirm = { v ->
                wheelTarget = ""
                onTotalWeeksChange(v[0])
            }
        )

        "count" -> WheelDialog(
            title = "课表节数设置",
            labels = GROUP_NAMES,
            mins = listOf(0, 0, 0),
            maxs = listOf(MAX_SECTION_PER_GROUP, MAX_SECTION_PER_GROUP, MAX_SECTION_PER_GROUP),
            inits = listOf(
                timeCfg.counts.getOrElse(0) { 0 },
                timeCfg.counts.getOrElse(1) { 0 },
                timeCfg.counts.getOrElse(2) { 0 }
            ),
            onDismiss = { wheelTarget = "" },
            onConfirm = { v ->
                wheelTarget = ""
                onTimeCfgChange(timeCfg.copy(counts = v))
            }
        )

        "len" -> WheelDialog(
            title = "每节课时长",
            labels = listOf("分钟"),
            mins = listOf(5),
            maxs = listOf(90),
            inits = listOf(timeCfg.sectionLen.coerceIn(5, 90)),
            onDismiss = { wheelTarget = "" },
            onConfirm = { v ->
                wheelTarget = ""
                onTimeCfgChange(timeCfg.copy(sectionLen = v[0]))
            }
        )

        "break" -> WheelDialog(
            title = "课间休息时长",
            labels = listOf("分钟"),
            mins = listOf(0),
            maxs = listOf(60),
            inits = listOf(timeCfg.breakLen.coerceIn(0, 60)),
            onDismiss = { wheelTarget = "" },
            onConfirm = { v ->
                wheelTarget = ""
                onTimeCfgChange(timeCfg.copy(breakLen = v[0]))
            }
        )

        "start" -> {
            val g = wheelGroup.coerceIn(0, 2)
            val curStart = timeToMinutes(timeCfg.groupStarts.getOrElse(g) { DEFAULT_GROUP_STARTS[g] })
            val cm = if (curStart < 0) timeToMinutes(DEFAULT_GROUP_STARTS[g]) else curStart
            WheelDialog(
                title = GROUP_NAMES[g] + "开始时间",
                labels = listOf("时", "分"),
                mins = listOf(0, 0),
                maxs = listOf(23, 59),
                inits = listOf(cm / 60, cm % 60),
                onDismiss = { wheelTarget = "" },
                onConfirm = { v ->
                    wheelTarget = ""
                    val ns = timeCfg.groupStarts.toMutableList()
                    while (ns.size < 3) ns.add(DEFAULT_GROUP_STARTS[ns.size])
                    ns[g] = minutesToTime(v[0] * 60 + v[1])
                    onTimeCfgChange(timeCfg.copy(groupStarts = ns))
                }
            )
        }

        "sec" -> {
            val idx = (wheelSection - 1).coerceIn(0, maxOf(0, timeCfg.times.size - 1))
            val sp = timeCfg.times.getOrNull(idx)?.let { parseSlot(it) }
            val sm = sp?.first ?: (8 * 60)
            val em = sp?.second ?: (8 * 60 + timeCfg.sectionLen)
            WheelDialog(
                title = "第 " + (idx + 1) + " 节时间",
                labels = listOf("起时", "起分", "止时", "止分"),
                mins = listOf(0, 0, 0, 0),
                maxs = listOf(23, 59, 23, 59),
                inits = listOf(sm / 60, sm % 60, em / 60, em % 60),
                onDismiss = { wheelTarget = "" },
                onConfirm = { v ->
                    wheelTarget = ""
                    val st = v[0] * 60 + v[1]
                    var en = v[2] * 60 + v[3]
                    if (en <= st) en = st + timeCfg.sectionLen
                    val nt = timeCfg.times.toMutableList()
                    while (nt.size <= idx) {
                        nt.add(minutesToTime(8 * 60) + "-" + minutesToTime(8 * 60 + timeCfg.sectionLen))
                    }
                    nt[idx] = minutesToTime(st) + "-" + minutesToTime(en)
                    onTimeCfgChange(timeCfg.copy(sameLength = false, times = nt))
                }
            )
        }
    }
}

// ==================== 手动添加课程对话框 ====================
@Composable
fun ManualCourseDialog(
    onDismiss: () -> Unit,
    onConfirm: (Course) -> Unit,
    initial: Course? = null,               // 不为空 = 编辑课程，把原来的内容回填
    title: String = "手动添加课程",
    confirmText: String = "添加"
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var weekCn by remember { mutableStateOf(initial?.weekCn ?: "一") }
    var startSec by remember { mutableStateOf((initial?.startSection ?: 1).toString()) }
    var endSec by remember { mutableStateOf((initial?.endSection ?: 2).toString()) }
    var weeks by remember {
        mutableStateOf(if (initial == null) "1-16" else formatWeekList(initial.weekRangeList))
    }
    var location by remember { mutableStateOf(initial?.location ?: "") }
    var teacher by remember { mutableStateOf(initial?.teacher ?: "") }
    var error by remember { mutableStateOf("") }
    val dayOptions = listOf("一", "二", "三", "四", "五", "六", "日")

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.width(330.dp)) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("课程名（必填）", fontSize = 12.sp) },
                    textStyle = TextStyle(fontSize = 15.sp)
                )

                Spacer(Modifier.height(8.dp))
                Text("上课星期", fontSize = 12.sp, color = Color(0xFF8A8A8A))
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    dayOptions.forEach { d ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (d == weekCn) Color(0xFF37474F) else Color(0xFFFFFFFF))
                                .border(
                                    BorderStroke(1.dp, if (d == weekCn) Color(0xFF37474F) else Color(0xFFE3E3E3)),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { weekCn = d },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "周" + d,
                                fontSize = 11.sp,
                                color = if (d == weekCn) Color.White else Color(0xFF37474F)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = startSec,
                        onValueChange = { startSec = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        label = { Text("起始节", fontSize = 12.sp) },
                        textStyle = TextStyle(fontSize = 15.sp)
                    )
                    OutlinedTextField(
                        value = endSec,
                        onValueChange = { endSec = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        label = { Text("结束节", fontSize = 12.sp) },
                        textStyle = TextStyle(fontSize = 15.sp)
                    )
                }

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = weeks,
                    onValueChange = { weeks = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("上课周数（1-16 / 1,3,5 / 1-16单）", fontSize = 12.sp) },
                    textStyle = TextStyle(fontSize = 15.sp)
                )

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("教室（可选）", fontSize = 12.sp) },
                    textStyle = TextStyle(fontSize = 15.sp)
                )

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("任课老师（可选）", fontSize = 12.sp) },
                    textStyle = TextStyle(fontSize = 15.sp)
                )

                if (error.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(error, fontSize = 12.sp, color = Color(0xFFE53935))
                }

                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SoftButton(
                        text = "取消",
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        fontSize = 15.sp
                    )
                    SoftButton(
                        text = confirmText,
                        onClick = {
                            val n = name.trim()
                            val sn = startSec.trim().toIntOrNull()
                            val en = endSec.trim().toIntOrNull()
                            val snv = sn ?: -1
                            val env = en ?: -1
                            val wl = parseWeekRange(weeks)
                            error = when {
                                n.isEmpty() -> "课程名不能为空"
                                sn == null || en == null -> "节次请填数字，如 1 和 2"
                                snv < 1 || env > totalSections || snv > env ->
                                    "节次应在 1~" + totalSections + "，且起始不大于结束"
                                wl.isEmpty() -> "周次填得不对，例如 1-16 或 1-16单"
                                else -> ""
                            }
                            if (error.isEmpty()) {
                                onConfirm(
                                    Course(
                                        weekRangeList = wl,
                                        weekCn = weekCn,
                                        startSection = snv,
                                        endSection = env,
                                        name = n,
                                        location = location.trim(),
                                        teacher = teacher.trim()
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}


// ==================== 课程配色：选择对话框 ====================

/**
 * 挑课程颜色：20 个色块，点一下立刻生效。
 * 左下角「自动配色」= 回到按课程名算出来的颜色（和从没手动选过时一样）。
 */
@Composable
fun CourseColorDialog(
    courseName: String,
    current: Int,               // 当前下标；-1 = 还没手动选过
    auto: Int,                  // 课程名自动算出来的下标，用来标记自动配色落在哪一格
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit    // 传 -1 = 恢复自动配色
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.width(320.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("课程颜色", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(
                    courseName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    if (current in colorPalette.indices) "现在用的是你挑的颜色"
                    else "现在用的是自动配色（按课程名分配）",
                    fontSize = 11.sp,
                    color = Color(0xFF8A8A8A)
                )
                Spacer(Modifier.height(12.dp))
                // 颜色条：20 个格子拼成一条（格子颜色 = 课程调色板），
                // 格子之间的白色「|」比条子高一点，上下各凸出 3dp。
                val curIdx = if (current in colorPalette.indices) current else auto
                Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    for (i in colorPalette.indices) {
                        if (i > 0) {
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .offset(x = (-0.5).dp)
                                    .fillMaxHeight()
                                    .background(Color.White)
                            )
                        }
                        val shape = when (i) {
                            0 -> RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                            colorPalette.size - 1 ->
                                RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                            else -> RoundedCornerShape(0.dp)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .align(Alignment.CenterVertically)
                                .clip(shape)
                                .background(colorPalette[i])
                                .then(
                                    if (i == curIdx)
                                        Modifier.border(
                                            BorderStroke(2.dp, Color(0xFF37474F)), shape
                                        )
                                    else Modifier
                                )
                                .clickable { onConfirm(i) }
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    "点哪一格就用哪个颜色（每格颜色与课表格子、小部件完全一致）。",
                    fontSize = 11.sp,
                    color = Color(0xFF9E9E9E)
                )
                Text(
                    "颜色只是用来区分课程的，不影响课程内容；系统本来就会按课程名自动分配，" +
                            "不满意的时候再自己挑一个就行。",
                    fontSize = 11.sp,
                    color = Color(0xFF9E9E9E),
                    lineHeight = 16.sp
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SoftButton(
                        text = "自动配色",
                        onClick = { onConfirm(-1) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        fontSize = 14.sp
                    )
                    SoftButton(
                        text = "取消",
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}



// ==================== 删除课程：确认对话框 ====================
@Composable
fun CourseDeleteDialog(
    name: String,
    detail: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.width(300.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("删除这门课？", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF263238))
                Spacer(Modifier.height(3.dp))
                Text(detail, fontSize = 12.sp, color = Color(0xFF78909C))
                Spacer(Modifier.height(8.dp))
                Text(
                    "删除后本机这个课表里就没有它了，不能撤销；重新导入教务文件可以补回来。",
                    fontSize = 11.sp,
                    color = Color(0xFF9E9E9E),
                    lineHeight = 16.sp
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SoftButton(
                        text = "取消",
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        fontSize = 15.sp
                    )
                    SoftButton(
                        text = "删除",
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

// ==================== 课表改名对话框 ====================
@Composable
fun RenameBookDialog(
    initial: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(initial) }
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.width(300.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("给课表起个名字", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("名称（留空＝按编号命名）", fontSize = 12.sp) },
                    textStyle = TextStyle(fontSize = 15.sp)
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SoftButton(
                        text = "取消",
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        fontSize = 15.sp
                    )
                    SoftButton(
                        text = "确定",
                        onClick = { onConfirm(text) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

// ==================== 日期滚轮（年 / 月 / 日 + 确定） ====================
@Composable
fun NumberWheel(
    curValue: Int,
    minV: Int,
    maxV: Int,
    modifier: Modifier = Modifier,
    wheelValues: Array<String>? = null,
    onChange: (Int) -> Unit
) {
    AndroidView(
        modifier = modifier.height(140.dp),
        factory = { c ->
            NumberPicker(c).apply {
                descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                minValue = minV
                maxValue = maxV
                if (wheelValues != null) displayedValues = wheelValues
                value = curValue.coerceIn(minV, maxV)
                wrapSelectorWheel = true
                setOnValueChangedListener { _, _, newVal -> onChange(newVal) }
            }
        },
        update = { picker ->
            if (wheelValues != null && picker.displayedValues?.size != wheelValues.size) {
                picker.displayedValues = wheelValues
            }
            if (picker.minValue != minV) picker.minValue = minV
            if (picker.maxValue != maxV) picker.maxValue = maxV
            val target = curValue.coerceIn(minV, maxV)
            if (picker.value != target) picker.value = target
        }
    )
}

@Composable
fun StartDatePickerDialog(
    current: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit
) {
    var year by remember { mutableIntStateOf(current.year) }
    var month by remember { mutableIntStateOf(current.monthValue) }
    var day by remember { mutableIntStateOf(current.dayOfMonth) }

    val yearMin = current.year - 5
    val yearMax = current.year + 6
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    val safeDay = day.coerceIn(1, daysInMonth)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("选择开学周一", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "${year}年 ${month}月 ${safeDay}日",
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        NumberWheel(
                            curValue = year,
                            minV = yearMin,
                            maxV = yearMax,
                            modifier = Modifier.weight(1f),
                            onChange = { year = it }
                        )
                        NumberWheel(
                            curValue = month,
                            minV = 1,
                            maxV = 12,
                            modifier = Modifier.weight(1f),
                            wheelValues = Array(12) { "${it + 1}月" },
                            onChange = { month = it }
                        )
                        NumberWheel(
                            curValue = safeDay,
                            minV = 1,
                            maxV = daysInMonth,
                            modifier = Modifier.weight(1f),
                            onChange = { day = it }
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SoftButton(
                            text = "取消",
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            fontSize = 15.sp
                        )
                        SoftButton(
                            text = "确定",
                            onClick = { onConfirm(LocalDate.of(year, month, safeDay)) },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

// ==================== 课表节数 / 上课时间用的组件 ====================

/** 开关（自绘，避免 Material Switch 的版本差异导致编译不过） */
@Composable
fun SoftSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(52.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(if (checked) Color(0xFF3B82F6) else Color(0xFFCFCFCF))
            .clickable { onCheckedChange(!checked) },
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .width(24.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        )
    }
}

/** 通用滚轮弹窗：上下滑动选数字，取消 / 确定（最多 4 列滚轮，和「开学周一」的选法一样） */
@Composable
fun WheelDialog(
    title: String,
    labels: List<String>,
    mins: List<Int>,
    maxs: List<Int>,
    inits: List<Int>,
    suffixes: List<String> = emptyList(),
    onDismiss: () -> Unit,
    onConfirm: (List<Int>) -> Unit
) {
    var v0 by remember(inits) { mutableIntStateOf(inits.getOrElse(0) { 0 }) }
    var v1 by remember(inits) { mutableIntStateOf(inits.getOrElse(1) { 0 }) }
    var v2 by remember(inits) { mutableIntStateOf(inits.getOrElse(2) { 0 }) }
    var v3 by remember(inits) { mutableIntStateOf(inits.getOrElse(3) { 0 }) }
    val cur = listOf(v0, v1, v2, v3)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        labels.forEachIndexed { i, lab ->
                            val lo = mins.getOrElse(i) { 0 }
                            val hi = maxs.getOrElse(i) { lo }
                            val sfx = suffixes.getOrElse(i) { "" }
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(lab, fontSize = 11.sp, color = Color(0xFF8A8A8A))
                                Spacer(Modifier.height(2.dp))
                                NumberWheel(
                                    curValue = cur.getOrElse(i) { lo }.coerceIn(lo, hi),
                                    minV = lo,
                                    maxV = hi,
                                    modifier = Modifier.fillMaxWidth(),
                                    wheelValues = if (sfx.isEmpty()) null
                                    else Array(hi - lo + 1) { k -> (lo + k).toString() + sfx },
                                    onChange = { nv ->
                                        when (i) {
                                            0 -> v0 = nv
                                            1 -> v1 = nv
                                            2 -> v2 = nv
                                            else -> v3 = nv
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SoftButton(
                            text = "取消",
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            fontSize = 15.sp
                        )
                        SoftButton(
                            text = "确定",
                            onClick = { onConfirm(cur.take(labels.size)) },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}


// ======================================================================
// 第 44 批新增：① 上课提醒 / ③ 课表分享（分享码）/ ⑥ 课程冲突检测
// 这份是「上半部分：纯逻辑」，界面在下半部分（同一个文件末尾接着写）。
// ======================================================================

// ---------------------------------------------------------------- 公共小工具
private val WEEKDAYS_CN = listOf("一", "二", "三", "四", "五", "六", "日")

/** 算某个日期是第几周（与小组件里 weekOf 同一算法，只是这里允许传任意日期） */
fun weekNoOfDate(startDateStr: String, date: LocalDate): Int = try {
    val start = LocalDate.parse(startDateStr, START_DATE_STORE_FMT)
    val diff = ChronoUnit.DAYS.between(start, date)
    if (diff < 0) 1 else (diff / 7).toInt() + 1
} catch (_: Exception) {
    1
}

/** 某一天有哪些课：跟主界面表格同一套筛选逻辑（星期对得上 + 该周在课程的周次范围内） */
fun coursesOnDate(all: List<Course>, date: LocalDate, startDateStr: String): List<Course> {
    val cn = WEEKDAYS_CN[date.dayOfWeek.value - 1]
    val wk = weekNoOfDate(startDateStr, date)
    return all
        .filter { it.weekCn == cn && wk in it.weekRangeList }
        .sortedWith(compareBy({ it.startSection }, { it.endSection }))
}

/** 这节课的起止钟点（"08:00" 到 "09:40"）；课时时间表里查不到就返回 null */
fun slotClockRange(cfg: TimeConfig, startSec: Int, endSec: Int): Pair<String, String>? {
    val a = cfg.times.getOrNull(startSec - 1)?.let { parseSlot(it) } ?: return null
    val b = cfg.times.getOrNull(endSec - 1)?.let { parseSlot(it) } ?: return null
    return minutesToTime(a.first) to minutesToTime(b.second)
}

// ---------------------------------------------------------------- ① 上课提醒
private const val REMINDER_ON_KEY = "class_reminder_on"
private const val REMINDER_LEAD_KEY = "class_reminder_lead"
const val REMINDER_CHANNEL_ID = "class_reminder"
const val REMINDER_ACTION = "com.example.myproject.CLASS_REMINDER"
private const val NOTIFY_ID_CLASS = 44100

fun isReminderOn(context: Context): Boolean =
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getBoolean(REMINDER_ON_KEY, false)

fun saveReminderOn(context: Context, on: Boolean) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .edit().putBoolean(REMINDER_ON_KEY, on).apply()
}

/** 提前多少分钟提醒（0~20，默认 10；第 45 批把范围放宽到 0~20） */
fun loadReminderLead(context: Context): Int {
    val v = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getInt(REMINDER_LEAD_KEY, 10)
    return if (v in 0..20) v else 10
}

fun saveReminderLead(context: Context, minutes: Int) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .edit().putInt(REMINDER_LEAD_KEY, minutes).apply()
}

/** 下一节课：哪门课 + 哪一天 + 起止时刻 */
data class NextClass(
    val course: Course,
    val date: LocalDate,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime
)

/** 一节课的身份。用来避免「刚提醒完又立刻重复提醒同一节」 */
fun classIdentity(date: LocalDate, c: Course): String =
    date.toString() + "|" + c.name + "|" + c.startSection + "-" + c.endSection

/** 从今天起往后找 8 天，找最近一节「还没开始」的课；skip 用来跳过刚提醒过的那节 */
fun findNextClass(context: Context, skip: String? = null): NextClass? {
    val startDate = loadStartDate(context)
    val all = loadCoursesFromLocal(context)
    if (all.isEmpty()) return null
    val cfg = loadTimeConfig(context, loadCurrentBookId(context))
    val now = LocalDateTime.now()
    for (i in 0..7) {
        val date = LocalDate.now().plusDays(i.toLong())
        for (c in coursesOnDate(all, date, startDate)) {
            val r = slotClockRange(cfg, c.startSection, c.endSection) ?: continue
            val startAt = LocalDateTime.of(date, LocalTime.parse(r.first))
            val endAt = LocalDateTime.of(date, LocalTime.parse(r.second))
            if (!startAt.isAfter(now)) continue
            if (skip != null && classIdentity(date, c) == skip) continue
            return NextClass(c, date, startAt, endAt)
        }
    }
    return null
}

/**
 * 第 47 批：正在上的那节课（带起止时刻），没有就返回 null。
 * 「打开静音开关时立刻静音」和「下课的恢复闹钟」都要靠它。
 */
fun currentClassSlot(context: Context): NextClass? {
    val startDate = loadStartDate(context)
    val all = loadCoursesFromLocal(context)
    if (all.isEmpty()) return null
    val cfg = loadTimeConfig(context, loadCurrentBookId(context))
    val now = LocalDateTime.now()
    val date = LocalDate.now()
    val t = now.toLocalTime()
    for (c in coursesOnDate(all, date, startDate)) {
        val r = slotClockRange(cfg, c.startSection, c.endSection) ?: continue
        val s = LocalTime.parse(r.first)
        val e = LocalTime.parse(r.second)
        if (!t.isBefore(s) && !t.isAfter(e)) {
            return NextClass(c, date, LocalDateTime.of(date, s), LocalDateTime.of(date, e))
        }
    }
    return null
}

/** 正在上的那门课；没有就 null（第 47 批：给静音开关用） */
fun currentClassNow(context: Context): Course? = currentClassSlot(context)?.course

/**
 * 第 47 批：换一个新的 channel id —— 旧的「上课提醒」通道属性被系统锁死了（没铃声），只能新建才能加响铃与振动。
 * 第 47 批（二轮）：通道属性建好后系统一律不允许再改（声音 / 振动都不行），
 * 所以「在 App 里换铃声」的做法是：建一个新 id 的通道、把旧的删掉——两个 id 来回切就够用。
 *
 * 第 50 批：铃声改成「完全由系统通知通道说了算」，App 不再自己播、界面上也去掉了 App 内的选铃声入口。
 * 于是下面 loadReminderSound / effectiveReminderSound / describeReminderSound / setReminderSound /
 * openReminderSoundPicker 目前没有调用者，先留着备用（将来若恢复「App 内选铃声」可以直接用）。
 */
const val REMINDER_CHANNEL_ID_V2 = "class_reminder_v2"
const val REMINDER_CHANNEL_ID_V3 = "class_reminder_v3"
private const val REMINDER_CHANNEL_KEY = "class_reminder_channel"
private const val REMINDER_SOUND_KEY = "class_reminder_sound_uri"

/** 当前生效的提醒通道 id（换铃声音时会切到另一个） */
fun reminderChannelId(context: Context): String {
    val v = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .getString(REMINDER_CHANNEL_KEY, REMINDER_CHANNEL_ID_V2)
    return if (v == REMINDER_CHANNEL_ID_V3) REMINDER_CHANNEL_ID_V3 else REMINDER_CHANNEL_ID_V2
}

/** 用户在 App 里选的铃声；没选过返回 null（这时用系统闹钟铃声） */
fun loadReminderSound(context: Context): Uri? {
    val s = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .getString(REMINDER_SOUND_KEY, null)
    if (s.isNullOrBlank()) return null
    return try {
        Uri.parse(s)
    } catch (_: Exception) {
        null
    }
}

/** 实际要响的铃声：App 里选的 → 系统闹钟铃声 → 系统通知铃声 */
fun effectiveReminderSound(context: Context): Uri? {
    loadReminderSound(context)?.let { return it }
    try {
        val a = android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI
        if (a != null) return a
    } catch (_: Exception) {
    }
    return try {
        android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM)
            ?: android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)
    } catch (_: Exception) {
        null
    }
}

/** 铃声在界面上显示成什么名字 */
fun describeReminderSound(context: Context): String {
    val custom = loadReminderSound(context)
    if (custom == null) return "系统闹钟铃声"
    return try {
        val title = android.media.RingtoneManager.getRingtone(context, custom)?.getTitle(context)
        if (title.isNullOrBlank()) "自定义铃声" else title
    } catch (_: Exception) {
        "自定义铃声"
    }
}

/** 换铃声：存下来 + 换个通道 id 重建（旧通道删掉），这样新铃声才会真的生效 */
fun setReminderSound(context: Context, uri: Uri?) {
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    sp.edit().putString(REMINDER_SOUND_KEY, uri?.toString() ?: "").apply()
    val cur = reminderChannelId(context)
    val next = if (cur == REMINDER_CHANNEL_ID_V2) REMINDER_CHANNEL_ID_V3 else REMINDER_CHANNEL_ID_V2
    sp.edit().putString(REMINDER_CHANNEL_KEY, next).apply()
    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    try {
        nm?.deleteNotificationChannel(cur)
    } catch (_: Exception) {
    }
    ensureReminderChannel(context)
}

/** 打开系统的铃声选择器（选的是「闹钟」类铃声） */
fun openReminderSoundPicker(context: Context, launch: (Intent) -> Unit) {
    try {
        launch(
            Intent(android.media.RingtoneManager.ACTION_RINGTONE_PICKER)
                .putExtra(
                    android.media.RingtoneManager.EXTRA_RINGTONE_TYPE,
                    android.media.RingtoneManager.TYPE_ALARM
                )
                .putExtra(
                    android.media.RingtoneManager.EXTRA_RINGTONE_TITLE,
                    "选上课提醒的铃声"
                )
                .putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
                .putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                .putExtra(
                    android.media.RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                    effectiveReminderSound(context)
                )
        )
    } catch (_: Exception) {
        Toast.makeText(context, "这个系统没有铃声选择器，请到系统设置里改通知铃声", Toast.LENGTH_LONG).show()
    }
}

/**
 * 跳到本 App 的「上课提醒（响铃）」通道设置页——「悬浮通知」「在锁定屏幕上显示」这两个开关
 * 系统不允许 App 自己改，只能用户点一下。用动作字符串写，避开 compileSdk 里的新常量。
 */
fun openReminderChannelSettings(context: Context) {
    try {
        context.startActivity(
            Intent("android.settings.CHANNEL_NOTIFICATION_SETTINGS")
                .putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                .putExtra("android.provider.extra.CHANNEL_ID", reminderChannelId(context))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (_: Exception) {
        openAppNotificationSettings(context)
    }
}

// 第 50 批（用户反馈）：**App 不再自己播放铃声**。
// 原来这里有一整套「系统通道没设铃声，就由 App 用 MediaPlayer 兜底播」的逻辑
// （reminderPlayer / startReminderRing / stopReminderRing / channelHasSound / ringClassReminder），
// 结果用户没在系统里选铃声时，App 反而叮叮叮响了一路。已整段删掉，现在声音只认系统通知通道：
//   · 通道默认没有铃声（见 ensureReminderChannel 里的 setSound(null)）→ 一点声音都不会有；
//   · 用户到「响铃与弹出 → 通知设置」那一页自己选了铃声 → 弹通知时由系统负责响。
// 也就是说：系统那边没开（没选铃声），这里就安安静静，不会再有任何 App 自播的声音。

fun ensureReminderChannel(context: Context) {
    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
    val id = reminderChannelId(context)
    if (nm.getNotificationChannel(id) != null) return
    val ch = NotificationChannel(id, "上课提醒（响铃）", NotificationManager.IMPORTANCE_HIGH)
    ch.description = "每节课上课前提醒（有没有声音，看系统通知设置里选的铃声）"
    ch.enableVibration(true)
    ch.vibrationPattern = longArrayOf(0, 500, 250, 500)
    try {
        ch.setShowBadge(true)
    } catch (_: Exception) {
    }
    try {
        // 锁屏上也要能看见（否则锁屏只会「不显示通知」）
        ch.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
    } catch (_: Exception) {
    }
    try {
        // 第 50 批：通道默认就是「无铃声」——系统这边没选声音时，App 也不会自己发声，全程安静。
        // 想要响铃的话，请在「响铃与弹出 → 通知设置」那一页自己挑一个铃声（系统会记住），
        // 之后弹通知时由系统负责播放；反之系统没设，就一点声音都没有。
        ch.setSound(
            null,
            android.media.AudioAttributes.Builder()
                .setUsage(android.media.AudioAttributes.USAGE_ALARM)
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
    } catch (_: Exception) {
    }
    nm.createNotificationChannel(ch)
}

fun hasNotificationPermission(context: Context): Boolean =
    Build.VERSION.SDK_INT < 33 ||
            context.checkSelfPermission("android.permission.POST_NOTIFICATIONS") ==
            android.content.pm.PackageManager.PERMISSION_GRANTED

fun openAppNotificationSettings(context: Context) {
    try {
        context.startActivity(
            Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (_: Exception) {
        Toast.makeText(context, "没能打开系统通知设置，请手动到系统设置里开", Toast.LENGTH_LONG).show()
    }
}

/**
 * 第 47 批：Android 14+ 起「全屏弹窗」默认不给，得用户手动允许一次，
 * 否则锁屏/息屏时只会响铃 + 通知，不会自动弹页面。
 * 这里用一个动作字符串跳转，避免依赖 compileSdk 里的新常量。
 */
fun openFullScreenIntentSettings(context: Context) {
    val action = "android.settings.MANAGE_APP_USE_FULL_SCREEN_INTENT"
    try {
        context.startActivity(
            Intent(action)
                .setData(Uri.parse("package:" + context.packageName))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (_: Exception) {
        try {
            context.startActivity(Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (_: Exception) {
            Toast.makeText(context, "没能打开「全屏弹窗」设置，请到系统设置 → 应用 → 通知里找", Toast.LENGTH_LONG).show()
        }
    }
}

fun reminderPending(context: Context): PendingIntent {
    val i = Intent(context, ClassReminderReceiver::class.java).setAction(REMINDER_ACTION)
    return PendingIntent.getBroadcast(
        context, 4401, i,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

/** 排下一次上课提醒；没开提醒 / 往后 8 天都没课，就把闹钟撤掉 */
fun scheduleClassReminder(context: Context, skip: String? = null) {
    val am = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
    val pi = reminderPending(context)
    if (!isReminderOn(context)) {
        am.cancel(pi)
        return
    }
    val next = findNextClass(context, skip)
    if (next == null) {
        am.cancel(pi)
        return
    }
    val trigger = next.startAt.minusMinutes(loadReminderLead(context).toLong())
    val nowMs = System.currentTimeMillis()
    val triggerMs = trigger.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
    // 提前量算出来的时刻已经过去了（比如刚打开开关、课马上就开始）→ 3 秒后补一条
    val at = if (triggerMs <= nowMs) nowMs + 3000L else triggerMs
    try {
        if (Build.VERSION.SDK_INT < 31 || am.canScheduleExactAlarms()) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi)
        } else {
            am.set(AlarmManager.RTC_WAKEUP, at, pi)
        }
    } catch (_: Exception) {
        // 精确闹钟被系统拒了（机型限制 / 权限被收回）→ 退回普通闹钟，别让设置页崩
        try {
            am.set(AlarmManager.RTC_WAKEUP, at, pi)
        } catch (_: Exception) {
        }
    }
}

/** 发一条「快上课了」的通知：第 47 批起支持全屏弹窗（不玩手机也能看见）；声音由系统通知通道决定（第 50 批） */
fun showClassNotification(context: Context, c: Course, lead: Int) {
    if (!hasNotificationPermission(context)) return
    ensureReminderChannel(context)
    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
    val cfg = loadTimeConfig(context, loadCurrentBookId(context))
    val r = slotClockRange(cfg, c.startSection, c.endSection)
    val timeText = if (r == null) "" else shortClock(r.first) + "-" + shortClock(r.second)
    val where = listOf(c.location, c.teacher).filter { it.isNotBlank() }.joinToString(" · ")
    val head = if (lead <= 0) "现在上课" else lead.toString() + " 分钟后上课"
    val body = listOf(head, timeText, where).filter { it.isNotBlank() }.joinToString("　")
    val tap = Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val tapPi = PendingIntent.getActivity(
        context, 4402, tap,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    // 第 47 批：全屏弹窗的 PendingIntent —— 锁屏/息屏时直接亮屏把课表弹到眼前
    val full = Intent(context, MainActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        .putExtra("from_class_reminder", true)
    val fullPi = PendingIntent.getActivity(
        context, 4406, full,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val n = Notification.Builder(context, reminderChannelId(context))
        .setSmallIcon(R.drawable.ic_class_reminder)
        .setContentTitle(c.name)
        .setContentText(body)
        .setStyle(Notification.BigTextStyle().bigText(body))
        .setContentIntent(tapPi)
        .setFullScreenIntent(fullPi, true)
        .setCategory(Notification.CATEGORY_ALARM)
        .setPriority(Notification.PRIORITY_MAX)
        .setVisibility(Notification.VISIBILITY_PUBLIC)
        .setAutoCancel(true)
        .setShowWhen(false)
        .build()
    nm.notify(NOTIFY_ID_CLASS, n)
}

/** 到点提醒：发通知，接着把「下一节」的闹钟排上 */

/** 开机后把提醒重新排一遍（闹钟不会跨重启保留） */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) scheduleClassReminder(context)
    }
}

// ---------------------------------------------------------------- ③ 课表分享（分享码）
const val SHARE_CODE_PREFIX = "ZSKB1:"

/** 一份可分享的课表 */
data class SharedTimetable(
    val name: String,
    val startDate: String,
    val totalWeeks: Int,
    val courses: List<Course>
)

/**
 * 一组课程 + 开学周一 + 学习周数 → 分享码。
 * 就是 "ZSKB1:" + Base64(JSON)：纯本地生成，不含学号密码、不含任何服务器地址。
 */
fun buildShareCode(name: String, startDate: String, totalWeeks: Int, courses: List<Course>): String {
    val root = JSONObject()
    root.put("v", 1)
    root.put("n", name)
    root.put("d", startDate)
    root.put("w", totalWeeks)
    root.put("c", coursesToJsonArray(courses))
    val bytes = root.toString().toByteArray(Charsets.UTF_8)
    return SHARE_CODE_PREFIX + Base64.encodeToString(bytes, Base64.NO_WRAP)
}

/** 从一段文本里把分享码抠出来（能容忍后面还跟着别的字、中间被插了空格换行） */
fun extractShareCode(text: String): String? {
    val i = text.indexOf(SHARE_CODE_PREFIX)
    if (i < 0) return null
    val rest = text.substring(i + SHARE_CODE_PREFIX.length).filter { !it.isWhitespace() }
    return Regex("^[A-Za-z0-9+/]+={0,2}").find(rest)?.value
}

/** 解析分享码；不是分享码 / 内容坏了都返回 null */
fun parseShareCode(text: String): SharedTimetable? {
    val code = extractShareCode(text) ?: return null
    if (code.length < 8) return null
    return try {
        val bytes = Base64.decode(code, Base64.DEFAULT)
        val root = JSONObject(String(bytes, Charsets.UTF_8))
        val w = root.optInt("w", 20)
        SharedTimetable(
            name = root.optString("n", ""),
            startDate = root.optString("d", ""),
            totalWeeks = if (w in 1..30) w else 20,
            courses = coursesFromJsonArray(root.optJSONArray("c") ?: JSONArray())
        )
    } catch (_: Exception) {
        null
    }
}

fun copyToClipboard(context: Context, text: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
    cm.setPrimaryClip(ClipData.newPlainText("课表分享码", text))
}

fun readClipboard(context: Context): String? {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return null
    val clip = cm.primaryClip ?: return null
    if (clip.itemCount <= 0) return null
    return clip.getItemAt(0).coerceToText(context).toString()
}

fun shareText(context: Context, text: String) {
    val i = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "我的课表（芝士院特供课表分享码）")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    try {
        context.startActivity(Intent.createChooser(i, "分享课表").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (_: Exception) {
    }
}

// ---------------------------------------------------------------- ⑥ 课程冲突检测
/** 两条课的冲突：哪两门 + 重叠的周次 + 重叠的节次 */
data class CourseConflict(
    val a: Course,
    val b: Course,
    val weeks: List<Int>,
    val sections: List<Int>
)

/** 冲突的判定：星期相同 + 节次区间有交集 + 周次有交集 */
fun findCourseConflicts(courses: List<Course>): List<CourseConflict> {
    val out = mutableListOf<CourseConflict>()
    for (i in courses.indices) {
        for (j in i + 1 until courses.size) {
            val a = courses[i]
            val b = courses[j]
            if (a.weekCn != b.weekCn) continue
            val from = maxOf(a.startSection, b.startSection)
            val to = minOf(a.endSection, b.endSection)
            if (from > to) continue
            val w = a.weekRangeList.toSet().intersect(b.weekRangeList.toSet()).sorted()
            if (w.isEmpty()) continue
            out.add(CourseConflict(a, b, w, (from..to).toList()))
        }
    }
    return out
}

/** 跟某一条课有关的冲突（新增 / 修改课程之后拿来提示） */
fun conflictsWith(courses: List<Course>, target: Course): List<CourseConflict> =
    findCourseConflicts(courses).filter { it.a === target || it.b === target }

/** 冲突的一句话提示；没冲突返回空串 */
fun conflictHintText(list: List<CourseConflict>, target: Course): String {
    if (list.isEmpty()) return ""
    val c = list[0]
    val other = if (c.a === target) c.b else c.a
    return "⚠ 跟「" + other.name + "」时间冲突（周" + target.weekCn +
            " · 第 " + c.sections.first() + "-" + c.sections.last() + " 节）"
}

// ======================================================================
// 第 44 批新增（下半部分）：① 上课提醒 / ③ 课表分享 / ⑥ 课程冲突检测 的设置页
// ======================================================================

// ---------------------------------------------------------------- ① 上课提醒页
// ---------------------------------------------------------------- ⑦ 上课提醒 / 静音页（第 45 批改版）
@Composable
fun ReminderPage(onGoEdit: () -> Unit = {}) {
    val ctx = LocalContext.current
    var on by remember { mutableStateOf(isReminderOn(ctx)) }
    var mute by remember { mutableStateOf(isMuteOn(ctx)) }
    var lead by remember { mutableIntStateOf(loadReminderLead(ctx)) }
    var tick by remember { mutableIntStateOf(0) }
    var permOk by remember { mutableStateOf(hasNotificationPermission(ctx)) }
    var dndOk by remember { mutableStateOf(hasDndAccess(ctx)) }

    val notifLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permOk = granted
        Toast.makeText(
            ctx,
            if (granted) "通知权限已开" else "没给通知权限，提醒发不出来",
            Toast.LENGTH_LONG
        ).show()
    }

    val next = remember(tick, on, lead) { findNextClass(ctx) }
    val cfg = remember(tick) { loadTimeConfig(ctx, loadCurrentBookId(ctx)) }

    M3GroupTitle("上课提醒 / 静音")
    M3ListGroup {
        M3SwitchRow(
            index = 0, count = 2,
            title = "上课静音",
            supporting = if (mute) "已开启：上课自动静音，下课恢复" else "上课自动静音，下课恢复原有铃声",
            checked = mute,
            onCheckedChange = { v ->
                if (v && !hasDndAccess(ctx)) {
                    Toast.makeText(
                        ctx,
                        "要先允许「勿打扰访问权限」，否则系统不让改铃声",
                        Toast.LENGTH_LONG
                    ).show()
                    openDndAccessSettings(ctx)
                    dndOk = false
                } else {
                    mute = v
                    saveMuteOn(ctx, v)
                    // 第 47 批：正在上课的话立刻静音，别等闹钟（部分机型会把普通闹钟拖后很久）
                    if (v) {
                        if (currentClassNow(ctx) != null) {
                            val ok = applyClassMute(ctx, true)
                            Toast.makeText(
                                ctx,
                                if (ok) "正在上课 → 已静音，下课后自动恢复"
                                else "静音失败：请到系统设置里允许本 App 的「勿打扰访问权限」",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        applyClassMute(ctx, false)
                    }
                    scheduleClassMute(ctx)
                    tick++
                }
            }
        )
        M3SwitchRow(
            index = 1, count = 2,
            title = "上课提醒",
            supporting = if (on) "已开启：上课前发通知（锁屏会亮屏弹窗）；声音按系统「通知设置」里的铃声" else "已关闭（默认关）",
            checked = on,
            onCheckedChange = { v ->
                on = v
                saveReminderOn(ctx, v)
                if (v && !hasNotificationPermission(ctx) && Build.VERSION.SDK_INT >= 33) {
                    notifLauncher.launch("android.permission.POST_NOTIFICATIONS")
                }
                scheduleClassReminder(ctx)
                tick++
            }
        )
    }

    // 提前量：0~20 分钟，后面跟着显示“N 分钟”
    // 第 47 批（三轮）：只留一行「通知设置」，悬浮通知 / 铃声 / 锁屏显示都进系统那一页改
    M3GroupTitle("响铃与弹出")
    M3ListGroup {
        M3ListItem(
            index = 0, count = 1,
            title = "通知设置",
            supporting = "设置悬浮通知、铃声、锁屏显示",
            onClick = { openReminderChannelSettings(ctx) }
        )
    }

    M3GroupTitle("提醒提前量")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        M3SliderBar(
            value = lead.toFloat(),
            onValueChange = { v ->
                lead = Math.round(v).coerceIn(0, 20)
                saveReminderLead(ctx, lead)
                if (on) scheduleClassReminder(ctx)
                if (mute) scheduleClassMute(ctx)
                tick++
            },
            valueRange = 0f..20f,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(14.dp))
        Text(
            if (lead <= 0) "不提前" else lead.toString() + "分钟",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    Spacer(Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        M3FilledButton(
            text = "试一下通知",
            onClick = {
                val demo = findNextClass(ctx)?.course ?: Course(
                    weekRangeList = listOf(1),
                    weekCn = "一",
                    startSection = 1,
                    endSection = 2,
                    name = "测试课程",
                    location = "测试教室",
                    teacher = "测试老师"
                )
                showClassNotification(ctx, demo, lead)
                // 第 50 批：不再顺便响铃（App 不自己发声）。有没有声音看系统通知设置里的铃声。
                if (!hasNotificationPermission(ctx)) {
                    Toast.makeText(ctx, "没有通知权限，可能看不到", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(ctx, "已发出通知；响不响看系统「通知设置」里选没选铃声", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.weight(1f)
        )
        M3OutlinedButton(
            text = "开启通知权限",
            onClick = {
                if (Build.VERSION.SDK_INT >= 33) {
                    notifLauncher.launch("android.permission.POST_NOTIFICATIONS")
                } else {
                    openAppNotificationSettings(ctx)
                }
            },
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(Modifier.height(8.dp))
    Text(
        if (permOk) "通知权限：已允许" else "通知权限：没允许（提醒发不出来）",
        fontSize = 12.sp,
        color = if (permOk) MaterialTheme.colorScheme.onSurfaceVariant
        else MaterialTheme.colorScheme.error
    )
    if (Build.VERSION.SDK_INT >= 34) {
        Spacer(Modifier.height(8.dp))
        M3OutlinedButton(
            text = "允许全屏弹窗（不玩手机也能看到）",
            onClick = { openFullScreenIntentSettings(ctx) },
            modifier = Modifier.fillMaxWidth()
        )
    }
    if (!dndOk) {
        Spacer(Modifier.height(4.dp))
        Text(
            "勿打扰权限：没允许（上课静音不会生效，点上面的开关会自动跳去开）",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.error
        )
    }

    M3GroupTitle("下一节课")
    M3ListGroup {
        if (next == null) {
            M3ListItem(
                index = 0, count = 1,
                title = "往后 8 天里没有还没上的课",
                supporting = "先到「课表编辑」把课加上，或检查「开学周一」填对没",
                showChevron = true,
                onClick = { onGoEdit() }
            )
        } else {
            val r = slotClockRange(cfg, next.course.startSection, next.course.endSection)
            val where = listOf(next.course.location, next.course.teacher)
                .filter { it.isNotBlank() }.joinToString(" · ")
            M3ListItem(
                index = 0, count = 2,
                title = next.course.name,
                supporting = next.date.monthValue.toString() + " 月 " + next.date.dayOfMonth + " 日（周" +
                        WEEKDAYS_CN[next.date.dayOfWeek.value - 1] + "）" +
                        (if (r == null) "" else "　" + shortClock(r.first) + "-" + shortClock(r.second)) +
                        (if (where.isBlank()) "" else "　" + where),
                showChevron = false,
                onClick = { onGoEdit() }
            )
            val fireAt = next.startAt.minusMinutes(lead.toLong())
            M3ListItem(
                index = 1, count = 2,
                title = "提醒时间",
                supporting = if (lead <= 0) {
                    "到上课时间发通知"
                } else {
                    fireAt.monthValue.toString() + " 月 " + fireAt.dayOfMonth + " 日 " +
                            fireAt.hour + ":" + (if (fireAt.minute < 10) "0" else "") + fireAt.minute
                },
                showChevron = false,
                onClick = {}
            )
        }
    }
    Spacer(Modifier.height(10.dp))
    Text(
        "提醒用的是系统闹钟 + 系统通知，App 被划掉也会响；开机后会自动重排。" +
                "部分机型（小米 / 华为等）需要允许「自启动 / 后台弹出界面」，否则通知可能被拦。",
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(24.dp))
}
// ---------------------------------------------------------------- ③ 课表分享页
@Composable
fun SharePage(
    courses: List<Course>,
    startDateText: String,
    totalWeeks: Int,
    bookName: String,
    onImportBook: (SharedTimetable) -> Unit,
    onMergeCourses: (List<Course>) -> Unit
) {
    val ctx = LocalContext.current
    var pasteText by remember { mutableStateOf("") }
    val parsed = remember(pasteText) { parseShareCode(pasteText) }
    val code = remember(courses.size, startDateText, totalWeeks, bookName) {
        if (courses.isEmpty()) "" else buildShareCode(bookName, startDateText, totalWeeks, courses)
    }

    SettingsGroupTitle("分享我的课表")
    SettingsCard {
        if (courses.isEmpty()) {
            Text(
                "当前课表还没有课程，先导入或手动加几门再来分享。",
                fontSize = 13.sp,
                color = Color(0xFF8A8A8A)
            )
        } else {
            Text("当前：" + bookName + " · 共 " + courses.size + " 门课", fontSize = 13.sp)
            Spacer(Modifier.height(2.dp))
            Text(
                "开学 " + startDateForDisplay(startDateText) + " · 共 " + totalWeeks + " 周",
                fontSize = 11.sp,
                color = Color(0xFF8A8A8A)
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SoftButton(
                    text = "复制分享码",
                    onClick = {
                        copyToClipboard(ctx, code)
                        Toast.makeText(ctx, "分享码已复制", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    fontSize = 13.sp
                )
                SoftButton(
                    text = "分享给好友",
                    onClick = { shareText(ctx, code) },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    fontSize = 13.sp
                )
            }
            Spacer(Modifier.height(10.dp))
            Text("分享码（整段复制发给同学就行）", fontSize = 11.sp, color = Color(0xFF8A8A8A))
            Spacer(Modifier.height(4.dp))
            Text(code, fontSize = 10.sp, color = Color(0xFF546E7A))
            Spacer(Modifier.height(8.dp))
            Text(
                "码里只有课名 / 星期 / 节次 / 周次 / 地点 / 老师 / 颜色；" +
                        "没有学号密码，也不会往任何服务器上传。",
                fontSize = 11.sp,
                color = Color(0xFF9E9E9E)
            )
        }
    }

    Spacer(Modifier.height(14.dp))
    SettingsGroupTitle("导入同学的课表")
    SettingsCard {
        OutlinedTextField(
            value = pasteText,
            onValueChange = { pasteText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            label = { Text("把分享码粘到这里", fontSize = 12.sp) },
            textStyle = TextStyle(fontSize = 12.sp)
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SoftButton(
                text = "从剪贴板粘贴",
                onClick = {
                    val t = readClipboard(ctx)
                    if (t.isNullOrBlank()) {
                        Toast.makeText(ctx, "剪贴板里没有文字", Toast.LENGTH_SHORT).show()
                    } else {
                        pasteText = t
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp),
                fontSize = 13.sp
            )
            SoftButton(
                text = "清空",
                onClick = { pasteText = "" },
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp),
                fontSize = 13.sp
            )
        }

        val st = parsed
        if (st != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                "识别成功：" + (if (st.name.isBlank()) "未命名课表" else st.name),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "共 " + st.courses.size + " 门课 · 开学 " + startDateForDisplay(st.startDate) +
                        " · 共 " + st.totalWeeks + " 周",
                fontSize = 11.sp,
                color = Color(0xFF8A8A8A)
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SoftButton(
                    text = "另存为新课表",
                    onClick = {
                        onImportBook(st)
                        pasteText = ""
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    fontSize = 13.sp
                )
                SoftButton(
                    text = "并入当前课表",
                    onClick = {
                        val fresh = st.courses.filter { nc ->
                            courses.none {
                                it.name == nc.name && it.weekCn == nc.weekCn &&
                                        it.startSection == nc.startSection &&
                                        it.endSection == nc.endSection
                            }
                        }
                        if (fresh.isEmpty()) {
                            Toast.makeText(ctx, "这些课都已经在当前课表里了", Toast.LENGTH_SHORT).show()
                        } else {
                            onMergeCourses(fresh)
                            pasteText = ""
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    fontSize = 13.sp
                )
            }
        } else if (pasteText.isNotBlank()) {
            Spacer(Modifier.height(10.dp))
            Text(
                "没认出分享码：确认粘的是以 ZSKB1: 开头的那一整段。",
                fontSize = 12.sp,
                color = Color(0xFFB00020)
            )
        }
    }
}

// ---------------------------------------------------------------- ⑥ 课程冲突检测页
@Composable
fun ConflictPage(courses: List<Course>) {
    val list = findCourseConflicts(courses)

    SettingsGroupTitle("课程冲突检测")
    SettingsCard {
        Text(
            "同一星期、节次区间有重叠、并且周次也有重叠的两门课，就算冲突。",
            fontSize = 12.sp,
            color = Color(0xFF8A8A8A)
        )
    }

    Spacer(Modifier.height(10.dp))
    if (courses.isEmpty()) {
        SettingsCard {
            Text("当前课表还没有课程。", fontSize = 13.sp, color = Color(0xFF8A8A8A))
        }
    } else if (list.isEmpty()) {
        SettingsCard {
            Text("没有发现冲突", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A694D))
            Spacer(Modifier.height(2.dp))
            Text(
                "已按「星期 + 节次 + 周次」交叉检查了 " + courses.size + " 门课。",
                fontSize = 11.sp,
                color = Color(0xFF8A8A8A)
            )
        }
    } else {
        Text(
            "共发现 " + list.size + " 处冲突",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB00020)
        )
        Spacer(Modifier.height(4.dp))
        list.forEach { c ->
            SettingsCard {
                Text(
                    c.a.name + "  ↔  " + c.b.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "周" + c.a.weekCn + " · 第 " + c.sections.first() + "-" + c.sections.last() +
                            " 节 · " + compressWeeksText(c.weeks) + "重叠",
                    fontSize = 12.sp,
                    color = Color(0xFFB00020)
                )
                Spacer(Modifier.height(4.dp))
                Text(courseMainInfo(c.a), fontSize = 11.sp, color = Color(0xFF8A8A8A))
                Text(courseMainInfo(c.b), fontSize = 11.sp, color = Color(0xFF8A8A8A))
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "改法：到「导入课表 → 编辑课程」把其中一门的时间或周次改开，或者直接删掉。",
            fontSize = 11.sp,
            color = Color(0xFF9E9E9E)
        )
    }
}
// =====================================================================================
// 第 45 批 · 设置页改版（设计稿：Material 3 Expressive，见 design.txt）
// (一) 主题配色 + 列表 / 开关类组件
// 说明：全部为顶层声明，不与既有符号重名；颜色只引用 Material 角色，不写死界面色
// =====================================================================================

/** Mono 备用配色（设计稿指定的备用主题，Android 12 以下或取色不可用时使用） */
fun monoLightColorScheme(): ColorScheme = lightColorScheme(
    primary = Color(0xFF4A4459),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE6E0F0),
    onPrimaryContainer = Color(0xFF1A1626),
    secondary = Color(0xFF5F5D65),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE6E1E6),
    onSecondaryContainer = Color(0xFF1B1B1F),
    tertiary = Color(0xFF5F5D65),
    tertiaryContainer = Color(0xFFE9E0EA),
    onTertiaryContainer = Color(0xFF1E1A22),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE4E0E5),
    onSurfaceVariant = Color(0xFF48454E),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFFCFC3E0),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B)
)

/** 设计稿要求「动态取色」：Android 12+ 用壁纸生成配色，其余回落 Mono */
@Composable
fun appColorScheme(): ColorScheme {
    val base =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) dynamicLightColorScheme(LocalContext.current)
        else monoLightColorScheme()
    // 第 46 批：页面底色统一纯白。动态取色只用来定主色（按钮 / 选中态），
    // 不再让 surface 带米白 / 淡紫，这样整页看起来是干净的白色。
    // 第 47 批：主色固定成灰紫 #4A4459（按钮 / 滑块 / 开关 / 选中态），不再跟壁纸的棕色走。
    return base.copy(
        background = Color.White,
        surface = Color.White,
        surfaceVariant = Color(0xFFF2F2F2),
        onSurfaceVariant = Color(0xFF5F6368),
        primary = Color(0xFF4A4459),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFE6E0F0),
        onPrimaryContainer = Color(0xFF1A1626),
        secondary = Color(0xFF5F5D65),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE6E1E6),
        onSecondaryContainer = Color(0xFF1B1B1F),
        tertiary = Color(0xFF5F5D65),
        onTertiary = Color(0xFFFFFFFF)
    )
}

/**
 * 旧版 material3 没有 surfaceContainer* 角色，
 * 这里用 surface 与 surfaceVariant 插值近似，保证任何 material3 版本都能编译。
 */
@Composable
fun m3eContainer(level: Float): Color =
    lerp(Color.White, Color(0xFFEFEFEF), level)

@Composable
fun m3eLow(): Color = m3eContainer(0.35f)

@Composable
fun m3eMid(): Color = m3eContainer(0.55f)

@Composable
fun m3eHigh(): Color = m3eContainer(0.80f)

/** 列表项圆角（第 46 批）：统一小圆角，方形观感，不再外圆内方 */
fun m3eItemShape(index: Int, count: Int): RoundedCornerShape = RoundedCornerShape(6.dp)

/** 设置列表分组：相连项之间 3dp 间距 */
@Composable
fun M3ListGroup(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) { content() }
}

/** 分组小标题（16sp，onSurface） */
@Composable
fun M3GroupTitle(text: String) {
    Text(
        text,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 18.dp, bottom = 8.dp)
    )
}

/** 72dp 列表项：主文本 16sp / 辅助文本 12sp，右侧可带 chevron；点击有涟漪与轻微缩放 */
@Composable
fun M3ListItem(
    index: Int,
    count: Int,
    title: String,
    supporting: String? = null,
    showChevron: Boolean = true,
    trailing: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.985f else 1f, tween(90))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(m3eLow(), m3eItemShape(index, count))
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    pressed = true
                    while (true) {
                        val ev = awaitPointerEvent()
                        if (ev.changes.all { !it.pressed }) break
                    }
                    pressed = false
                }
            }
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, color = cs.onSurface)
                if (!supporting.isNullOrEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    Text(supporting, fontSize = 12.sp, color = cs.onSurfaceVariant)
                }
            }
            if (trailing != null) trailing()
            if (showChevron) {
                Spacer(Modifier.width(4.dp))
                M3ChevronRight()
            }
        }
    }
}

/** 列表项容器（用于自定义内容的行：滑块行、下拉行等），只管 72dp + 圆角 + 背景 */
@Composable
fun M3ItemBox(index: Int, count: Int, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(m3eLow(), m3eItemShape(index, count))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

/** 开关行：标签在左，M3 标准开关在右（轨道 52×32dp） */
@Composable
fun M3SwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    supporting: String? = null,
    index: Int = -1,
    count: Int = 0
) {
    val cs = MaterialTheme.colorScheme
    val bg = if (count > 0) m3eLow() else Color.Transparent
    val shape = if (count > 0) m3eItemShape(index, count) else RoundedCornerShape(0.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(bg, shape)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, color = cs.onSurface)
            if (!supporting.isNullOrEmpty()) {
                Spacer(Modifier.height(2.dp))
                Text(supporting, fontSize = 12.sp, color = cs.onSurfaceVariant)
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}// =====================================================================================
// 第 45 批 · 设置页改版（设计稿：Material 3 Expressive）
// (二) 图标（自绘 Material Symbols 风格）/ 滑块 / 标签片 / 按钮 / 下拉 / 顶栏
// =====================================================================================

/** 自绘 chevron_right */
@Composable
fun M3ChevronRight(tint: Color = MaterialTheme.colorScheme.onSurfaceVariant) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val p = Path().apply {
            moveTo(w * 0.38f, h * 0.24f)
            lineTo(w * 0.66f, h * 0.50f)
            lineTo(w * 0.38f, h * 0.76f)
        }
        drawPath(
            p,
            color = tint,
            style = Stroke(width = w * 0.11f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

/** 自绘勾（标签片选中态） */
@Composable
fun M3CheckIcon(tint: Color = MaterialTheme.colorScheme.onSecondaryContainer) {
    Canvas(modifier = Modifier.size(16.dp)) {
        val w = size.width
        val h = size.height
        val p = Path().apply {
            moveTo(w * 0.20f, h * 0.54f)
            lineTo(w * 0.42f, h * 0.76f)
            lineTo(w * 0.80f, h * 0.28f)
        }
        drawPath(
            p,
            color = tint,
            style = Stroke(width = w * 0.13f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

/** 自绘 arrow_drop_down */
@Composable
fun M3ArrowDropDown(tint: Color = MaterialTheme.colorScheme.onSurfaceVariant) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val p = Path().apply {
            moveTo(w * 0.28f, h * 0.42f)
            lineTo(w * 0.72f, h * 0.42f)
            lineTo(w * 0.50f, h * 0.66f)
            close()
        }
        drawPath(p, color = tint)
    }
}

/** M3E 粗轨道滑块：轨道高 16dp，竖长手柄 4×44dp，手柄左侧 primary、右侧 secondaryContainer */
@Composable
fun M3SliderBar(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .pointerInput(valueRange) {
                fun pick(x: Float) {
                    val t = (x / size.width.toFloat()).coerceIn(0f, 1f)
                    onValueChange(valueRange.start + t * (valueRange.endInclusive - valueRange.start))
                }
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    pick(down.position.x)
                    down.consume()
                    while (true) {
                        val ev = awaitPointerEvent()
                        val ch = ev.changes.firstOrNull { it.id == down.id } ?: break
                        if (!ch.pressed) break
                        pick(ch.position.x)
                        ch.consume()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val trackH = 16.dp.toPx()
            val handW = 4.dp.toPx()
            val cy = size.height / 2f
            val frac = if (valueRange.endInclusive > valueRange.start)
                ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start))
                    .coerceIn(0f, 1f) else 0f
            val hx = (frac * (size.width - handW)).coerceIn(0f, (size.width - handW).coerceAtLeast(0f))
            val r = CornerRadius(trackH / 2f, trackH / 2f)
            drawRoundRect(
                color = cs.primary,
                topLeft = Offset(0f, cy - trackH / 2f),
                size = Size(hx + handW / 2f, trackH),
                cornerRadius = r
            )
            drawRoundRect(
                color = cs.secondaryContainer,
                topLeft = Offset(hx + handW / 2f, cy - trackH / 2f),
                size = Size((size.width - hx - handW / 2f).coerceAtLeast(0f), trackH),
                cornerRadius = r
            )
            drawRoundRect(
                color = cs.primary,
                topLeft = Offset(hx, cy - 22.dp.toPx()),
                size = Size(handW, 44.dp.toPx()),
                cornerRadius = CornerRadius(handW / 2f, handW / 2f)
            )
        }
    }
}

/** 标签片：默认 32dp，设计稿用 40dp；选中填 secondaryContainer 并在前面显示勾 */
@Composable
fun M3Chip(
    label: String,
    selected: Boolean,
    index: Int = 0,
    count: Int = 1,
    height: Dp = 32.dp,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(6.dp)
    Surface(
        color = if (selected) cs.secondaryContainer else Color.Transparent,
        shape = shape,
        border = if (selected) null else BorderStroke(1.dp, cs.outline),
        modifier = Modifier
            .height(height)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selected) {
                M3CheckIcon(cs.onSecondaryContainer)
                Spacer(Modifier.width(6.dp))
            }
            Text(
                label,
                fontSize = 14.sp,
                color = if (selected) cs.onSecondaryContainer else cs.onSurfaceVariant
            )
        }
    }
}

/** 相连标签片组：3dp 间距，溢出横向滚动 */
@Composable
fun M3ChipRow(
    labels: List<String>,
    selectedIndex: Int,
    height: Dp = 40.dp,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        labels.forEachIndexed { i, s ->
            M3Chip(
                label = s,
                selected = i == selectedIndex,
                index = i,
                count = labels.size,
                height = height
            ) { onSelect(i) }
        }
    }
}

/** 中号按钮：高 56dp、圆角 12dp（设计稿「不使用胶囊形」） */
@Composable
fun M3FilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val cs = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = cs.primary,
            contentColor = cs.onPrimary,
            disabledContainerColor = cs.surfaceVariant,
            disabledContentColor = cs.onSurfaceVariant
        ),
        contentPadding = PaddingValues(horizontal = 24.dp),
        modifier = modifier.height(56.dp)
    ) {
        Text(text, fontSize = 15.sp)
    }
}

/** 描边按钮（次级操作）：高 56dp、1dp outline */
@Composable
fun M3OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = cs.primary
        ),
        border = BorderStroke(1.dp, cs.outline),
        contentPadding = PaddingValues(horizontal = 24.dp),
        modifier = modifier.height(56.dp)
    ) {
        Text(text, fontSize = 15.sp)
    }
}

/** 描边下拉菜单：高 56dp，点击在下方展开（容器圆角 4dp、项高 48dp） */
@Composable
fun M3DropdownSelect(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    supporting: String? = null,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    var open by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(6.dp)
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(shape)
                .border(1.dp, cs.outline, shape)
                .clickable { open = !open }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    options.getOrElse(selectedIndex) { "" },
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    color = cs.onSurface
                )
                M3ArrowDropDown()
            }
        }
        if (!supporting.isNullOrEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(supporting, fontSize = 12.sp, color = cs.onSurfaceVariant)
        }
        if (open) {
            Spacer(Modifier.height(4.dp))
            Surface(
                color = m3eHigh(),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    options.forEachIndexed { i, o ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clickable {
                                    onSelect(i)
                                    open = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(o, fontSize = 15.sp, color = cs.onSurface)
                            }
                        }
                    }
                }
            }
        }
    }
}

/** 设置页顶栏：[←] 与 28sp 大标题同一行、垂直居中、靠左 */
@Composable
fun M3TopBar(title: String, onBack: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .pointerInput(Unit) { detectTapGestures { onBack() } }
                .padding(4.dp)
        ) {
            Text("←", fontSize = 28.sp, color = cs.onSurface)
        }
        Spacer(Modifier.width(12.dp))
        Text(title, fontSize = 28.sp, color = cs.onSurface)
    }
}
// =====================================================================================
// 第 45 批 · 上课静音（设计稿 屏幕 7）
// 上课自动静音、下课恢复；改铃声模式需要系统「勿打扰访问权限」
// =====================================================================================

private const val MUTE_ON_KEY = "class_mute_on"
private const val MUTE_PREV_KEY = "class_mute_prev_mode"
// 第 47 批：铃声模式改不动时的音量兜底（记原音量，下课好还回去）
private const val MUTE_VOL_FALLBACK_KEY = "class_mute_vol_fallback"
private const val MUTE_PREV_VOL_KEY = "class_mute_prev_volume"
const val MUTE_ACTION_ON = "com.example.myproject.MUTE_ON"
const val MUTE_ACTION_OFF = "com.example.myproject.MUTE_OFF"

fun isMuteOn(context: Context): Boolean =
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getBoolean(MUTE_ON_KEY, false)

fun saveMuteOn(context: Context, on: Boolean) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .edit().putBoolean(MUTE_ON_KEY, on).apply()
}

/** 有没有「勿打扰访问权限」——没有它 System 会拒绝改铃声模式 */
fun hasDndAccess(context: Context): Boolean =
    if (Build.VERSION.SDK_INT < 23) true
    else {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        nm?.isNotificationPolicyAccessGranted ?: false
    }

fun openDndAccessSettings(context: Context) {
    try {
        context.startActivity(
            Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (_: Exception) {
        Toast.makeText(context, "没能打开「勿打扰访问权限」页面，请手动到系统设置里找", Toast.LENGTH_LONG).show()
    }
}

/**
 * 上课静音 / 下课恢复；返回是否真的改成功（被系统拒了返回 false）。
 * 第 47 批加强：不再「写完就当真」——
 * ① 改完 ringerMode 会再读一次确认，部分机型（兼容性差的系统）会把改动静默吞掉；
 * ② 确认没生效就退回「把铃声音量压到 0」，下课再把音量还回去，保证静音真的看得见。
 */
fun applyClassMute(context: Context, silent: Boolean): Boolean {
    val am = context.getSystemService(Context.AUDIO_SERVICE) as? android.media.AudioManager
        ?: return false
    val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    val RING = android.media.AudioManager.STREAM_RING
    return if (silent) {
        var ok = false
        try {
            if (am.ringerMode != android.media.AudioManager.RINGER_MODE_SILENT) {
                sp.edit().putInt(MUTE_PREV_KEY, am.ringerMode).apply()
            }
            am.ringerMode = android.media.AudioManager.RINGER_MODE_SILENT
            ok = am.ringerMode == android.media.AudioManager.RINGER_MODE_SILENT
        } catch (_: Exception) {
            ok = false
        }
        if (!ok) {
            // 兜底：系统不让换铃声模式，就把铃声音量压到 0（上课同样不会响）
            try {
                if (!sp.getBoolean(MUTE_VOL_FALLBACK_KEY, false)) {
                    sp.edit()
                        .putInt(MUTE_PREV_VOL_KEY, am.getStreamVolume(RING))
                        .putBoolean(MUTE_VOL_FALLBACK_KEY, true)
                        .apply()
                }
                am.setStreamVolume(RING, 0, 0)
                ok = am.getStreamVolume(RING) == 0
            } catch (_: Exception) {
                ok = false
            }
        }
        ok
    } else {
        try {
            val prev = sp.getInt(MUTE_PREV_KEY, android.media.AudioManager.RINGER_MODE_NORMAL)
            am.ringerMode = prev
        } catch (_: Exception) {
        }
        if (sp.getBoolean(MUTE_VOL_FALLBACK_KEY, false)) {
            try {
                val v = sp.getInt(MUTE_PREV_VOL_KEY, -1)
                if (v >= 0) am.setStreamVolume(RING, v, 0)
            } catch (_: Exception) {
            }
            sp.edit().putBoolean(MUTE_VOL_FALLBACK_KEY, false).apply()
        }
        true
    }
}

/** 静音闹钟：on = 上课静音，off = 下课恢复 */
fun mutePending(context: Context, on: Boolean): PendingIntent {
    val i = Intent(context, ClassReminderReceiver::class.java)
        .setAction(if (on) MUTE_ACTION_ON else MUTE_ACTION_OFF)
    return PendingIntent.getBroadcast(
        context, if (on) 4403 else 4404, i,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

/**
 * 排「上课静音 + 下课恢复」两个闹钟；没开静音 / 往后没课就撤掉。
 * 第 47 批：如果当下正在上课，下课的恢复闹钟要排在**当前这节课**的下课点，
 * 否则「上课途中才打开开关」会出现下课了还一直静音的情况。
 */
fun scheduleClassMute(context: Context, skip: String? = null) {
    val am = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
    val onPi = mutePending(context, true)
    val offPi = mutePending(context, false)
    if (!isMuteOn(context)) {
        am.cancel(onPi)
        am.cancel(offPi)
        return
    }
    val cur = currentClassSlot(context)
    val next = findNextClass(context, skip)
    if (cur == null && next == null) {
        am.cancel(onPi)
        am.cancel(offPi)
        return
    }
    fun setAt(pi: PendingIntent, t: LocalDateTime) {
        val ms = t.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        val nowMs = System.currentTimeMillis()
        val at = if (ms <= nowMs) nowMs + 1000L else ms
        try {
            if (Build.VERSION.SDK_INT < 31 || am.canScheduleExactAlarms()) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi)
            } else {
                am.set(AlarmManager.RTC_WAKEUP, at, pi)
            }
        } catch (_: Exception) {
            try {
                am.set(AlarmManager.RTC_WAKEUP, at, pi)
            } catch (_: Exception) {
            }
        }
    }
    am.cancel(onPi)
    am.cancel(offPi)
    // 正在上课 → 到这节课下课点恢复；否则 → 按下一节的下课点恢复
    if (cur != null) setAt(offPi, cur.endAt) else if (next != null) setAt(offPi, next.endAt)
    // 下一节上课 → 静音
    if (next != null) setAt(onPi, next.startAt)
}

/**
 * 到点提醒：发通知，接着把「下一节」的闹钟排上。
 * 第 45 批起这个接收器还负责上课静音 / 下课恢复（靠 action 分流）。
 */
class ClassReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            MUTE_ACTION_ON -> {
                applyClassMute(context, true)
                val nx = findNextClass(context)
                scheduleClassMute(context, nx?.let { classIdentity(it.date, it.course) })
                return
            }
            MUTE_ACTION_OFF -> {
                applyClassMute(context, false)
                // 第 50 批：App 不再自己播铃，这里也就没有「停铃」这一步了
                return
            }
        }
        val next = findNextClass(context)
        if (next != null) {
            // 第 50 批：只发通知。有没有声音由系统通知通道决定
            // （通道默认无铃声 → 静音；用户自己到「通知设置」里选了铃声 → 由系统响）
            showClassNotification(context, next.course, loadReminderLead(context))
        }
        // 这节已经提醒过了 → 跳过它，再排后面那一节
        val skip = next?.let { classIdentity(it.date, it.course) }
        scheduleClassReminder(context, skip)
        scheduleClassMute(context, skip)
    }
}
// =====================================================================================
// 第 45 批 · 小部件颜色页（设计稿 屏幕 6）
// 下拉选小部件编号 → 8 个标签片选部位 → 一条颜色条调色 → 恢复默认色 / 统一颜色
// =====================================================================================

/** 新增颜色槽：提示（「今天没课」「点一下打开课表」这类提示字） */
const val WIDGET_COLOR_HINT = "hint"
const val WIDGET_DEF_HINT = 0x99000000.toInt()

private const val WIDGET_UNIFORM_KEY = "widget_uniform_"
private const val WIDGET_UNIFORM_COLOR_KEY = "widget_uniform_color_"

fun loadWidgetUniformForId(context: Context, id: Int): Boolean =
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .getBoolean(WIDGET_UNIFORM_KEY + id, false)

fun saveWidgetUniformForId(context: Context, id: Int, on: Boolean) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .edit().putBoolean(WIDGET_UNIFORM_KEY + id, on).apply()
}

fun loadWidgetUniformColorForId(context: Context, id: Int): Int =
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .getInt(WIDGET_UNIFORM_COLOR_KEY + id, WIDGET_DEF_NAME)

fun saveWidgetUniformColorForId(context: Context, id: Int, color: Int) {
    context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        .edit().putInt(WIDGET_UNIFORM_COLOR_KEY + id, color).apply()
}

/** 第 45 批的部位清单：8 项（周数 / 日期 / 提示 / 格子 / 时间 / 丨线 / 课名 / 其他） */
private fun m3eWidgetSlots(): List<WidgetColorSlotInfo> = listOf(
    WidgetColorSlotInfo(WIDGET_COLOR_TITLE, "周数", "左上角「第 N 周」", WIDGET_DEF_TITLE, false),
    WidgetColorSlotInfo(WIDGET_COLOR_DATE, "日期", "右上角那行日期", WIDGET_DEF_DATE, false),
    WidgetColorSlotInfo(WIDGET_COLOR_HINT, "提示", "「今天没课」「点一下打开课表」这类提示字", WIDGET_DEF_HINT, false),
    WidgetColorSlotInfo(WIDGET_COLOR_CELL, "格子", "课程那个圆角色块，可以设回原色", WIDGET_CELL_COLOR_AUTO, true),
    WidgetColorSlotInfo(WIDGET_COLOR_TIME, "时间", "左边那两行上课时间", WIDGET_DEF_TIME, false),
    WidgetColorSlotInfo(WIDGET_COLOR_DIVIDER, "丨线", "时间与课程之间那条竖线", WIDGET_DEF_DIVIDER, false),
    WidgetColorSlotInfo(WIDGET_COLOR_NAME, "课名", "课程名那行字", WIDGET_DEF_NAME, false),
    WidgetColorSlotInfo(
        WIDGET_COLOR_INFO, "其他", "节数 · 教室 · 教师；原色是浅灰，颜色条第 4 格（#CFCFCF）",
        WIDGET_DEF_INFO, false
    )
)

/** 颜色面板：标签片选部位 + 一条颜色条 + 恢复默认色 + 统一颜色 */
@Composable
fun WidgetColorPanel(
    context: Context,
    targetId: Int,
    colorRev: Int,
    onColorChange: (Int, String, Int) -> Unit,
    onColorReset: (Int, String) -> Unit
) {
    if (targetId < 0) {
        Text(
            "先在「小部件编号」里选一个组件，再调它各部位的颜色。",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }
    val slots = m3eWidgetSlots()
    var sel by remember(targetId) { mutableIntStateOf(0) }
    var revLocal by remember { mutableIntStateOf(0) }
    var uniform by remember(targetId, colorRev) { mutableStateOf(loadWidgetUniformForId(context, targetId)) }

    key(colorRev, targetId, revLocal) {
        val info = slots.getOrElse(sel) { slots[0] }
        Column {
            // 两行标签片（设计稿：第一行 周数/日期/提示/格子，第二行 时间/丨线/课名/其他）
            M3ChipRow(
                labels = slots.take(4).map { it.label },
                selectedIndex = if (sel < 4) sel else -1,
                height = 40.dp,
                onSelect = { sel = it }
            )
            Spacer(Modifier.height(3.dp))
            M3ChipRow(
                labels = slots.drop(4).map { it.label },
                selectedIndex = if (sel >= 4) sel - 4 else -1,
                height = 40.dp,
                onSelect = { sel = it + 4 }
            )
            Spacer(Modifier.height(14.dp))

            Text(
                if (uniform) "统一颜色：所有部位都用这个色（格子除外）" else "正在调：" + info.label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(2.dp))
            Text(
                if (uniform) "拖动下面的颜色条，所有文字与线一起变" else info.hint,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))

            val curColor = if (uniform) loadWidgetUniformColorForId(context, targetId)
            else loadWidgetColorForId(context, targetId, info.slot, info.def)
            WidgetColorRow(
                keyId = targetId * 100 + sel,
                label = "",
                hint = "",
                currentValue = curColor,
                hasAuto = !uniform && info.hasAuto,
                showReset = false,
                onPick = { c ->
                    if (uniform) {
                        saveWidgetUniformColorForId(context, targetId, c)
                        onColorChange(targetId, WIDGET_COLOR_NAME, c)
                    } else {
                        onColorChange(targetId, info.slot, c)
                    }
                    ScheduleWidgetProvider.refreshScheduleWidgets(context)
                },
                onReset = {
                    if (uniform) {
                        saveWidgetUniformColorForId(context, targetId, WIDGET_DEF_NAME)
                    } else {
                        onColorReset(targetId, info.slot)
                    }
                    ScheduleWidgetProvider.refreshScheduleWidgets(context)
                }
            )

            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                M3FilledButton(
                    text = "恢复默认色",
                    onClick = {
                        slots.forEach { s -> onColorReset(targetId, s.slot) }
                        saveWidgetUniformColorForId(context, targetId, WIDGET_DEF_NAME)
                        ScheduleWidgetProvider.refreshScheduleWidgets(context)
                        revLocal++
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "统一颜色",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(2.dp))
                    Switch(
                        checked = uniform,
                        onCheckedChange = { v ->
                            uniform = v
                            saveWidgetUniformForId(context, targetId, v)
                            if (v) {
                                // 开启时把当前部位的颜色抄成统一色
                                val c = loadWidgetColorForId(context, targetId, info.slot, info.def)
                                if (c != WIDGET_CELL_COLOR_AUTO) {
                                    saveWidgetUniformColorForId(context, targetId, c)
                                }
                            }
                            ScheduleWidgetProvider.refreshScheduleWidgets(context)
                            revLocal++
                        }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "格子（课程色块）不受「统一颜色」影响，仍然按课表自动配色。" +
                        "颜色条分 " + WIDGET_COLOR_STEPS + " 格，白色「|」是当前停的那一格。",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

/** 操作手册的纯文字段落（第 46 批）：只有标题与说明，不跳转 */
@Composable
fun ManualPara(title: String, body: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
    ) {
        Text(
            title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
            body,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

