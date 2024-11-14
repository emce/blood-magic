package mobi.cwiklinski.bloodline.util

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList


suspend fun <T> Flow<List<T>>.flattenToList() =
    flatMapConcat { it.asFlow() }.toList()


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
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return this.matches(emailRegex)
}