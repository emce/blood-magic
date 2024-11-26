package mobi.cwiklinski.bloodline.ui.widget

import mobi.cwiklinski.bloodline.common.toPrecision

fun Int.capacity(ml: String, l: String): String = if (this < 1000) {
    "${this}$ml"
} else {
    val lowCapacity = this / 1000.00
    "${lowCapacity.toPrecision(2).replace(".", ",")}$l"
}