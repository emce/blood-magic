package mobi.cwiklinski.bloodline.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt


@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Flow<List<T>>.flattenToList() =
    flatMapConcat { it.asFlow() }.toList()

fun today() = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.isBefore(date: LocalDate) =
    LocalDateTime(this.year, this.monthNumber, this.dayOfMonth, 0, 0, 0)
        .toInstant(TimeZone.currentSystemDefault()) < LocalDateTime(
        date.year,
        date.monthNumber,
        date.dayOfMonth,
        0,
        0,
        0
    )
        .toInstant(TimeZone.currentSystemDefault())

fun LocalDate.isAfter(date: LocalDate) =
    LocalDateTime(this.year, this.monthNumber, this.dayOfMonth, 0, 0, 0)
        .toInstant(TimeZone.currentSystemDefault()) > LocalDateTime(
        date.year,
        date.monthNumber,
        date.dayOfMonth,
        0,
        0,
        0
    )
        .toInstant(TimeZone.currentSystemDefault())

fun LocalDate.toMillis() =
    LocalDateTime(this.year, this.month, this.dayOfMonth, 12, 0, 0)
        .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

fun Double.toPrecision(precision: Int) =
    if (precision < 1) {
        "${this.roundToInt()}"
    } else {
        val p = 10.0.pow(precision)
        val v = (abs(this) * p).roundToInt()
        val i = floor(v / p)
        var f = "${floor(v - (i * p)).toInt()}"
        while (f.length < precision) f = "0$f"
        val s = if (this < 0) "-" else ""
        "$s${i.toInt()}.$f"
    }

fun String.removeDiacritics(): String {
    val diacritics = "ąćęłńóśźżĄĆĘŁŃÓŚŹŻ".toList()
    val replacements = "acelnoszzACELNOSZZ".toList()
    return this.toMutableList().map {
        if (it in diacritics) {
            replacements[diacritics.indexOf(it)]
        } else {
            it
        }
    }.joinToString("")
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?".toRegex()
    return this.matches(emailRegex)
}

fun String.isValidPressure(): Boolean {
    val pressureRegex = "^[0-9]{2,3}/[0-9]{2,3}$".toRegex()
    return this.matches(pressureRegex)
}

fun String.isValidUrl(): Boolean {
    val urlRegex = """^(https?|ftp)://[\w\-]+(\.[\w\-]+)+[/#?]?.*$""".toRegex()
    return urlRegex.matches(this)
}

fun CoroutineScope.launchUI(block: suspend CoroutineScope.() -> Unit): Job =
    launch(Dispatchers.Main, block = block)