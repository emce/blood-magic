package mobi.cwiklinski.bloodline.test

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

object CommonTestTools {
    const val POLISH =
        "0123456789AĄBCDEĘFGHIJKLŁMNOÓPQRSŚTUVWXYZŻaąbcćdeęfghijklłmnńoópqrsśtuvwxyzźż"
    const val LATIN = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    fun getToday() = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date

    fun randomInt() = Random.nextInt()

    fun randomInt(min: Int, max: Int) = Random.nextInt((max - min) + 1) + min

    private fun randomDouble() = Random.nextDouble()

    fun randomFloat() = Random.nextFloat()

    fun randomString(length: Int, letters: String = POLISH): String {
        val sb = StringBuilder(length)
        for (i in 0 until length) {
            sb.append(letters.toCharArray()[Random.nextInt(letters.length)])
        }
        return sb.toString()
    }

    fun randomStringLatin(length: Int, letters: String = LATIN): String {
        val sb = StringBuilder(length)
        for (i in 0 until length) {
            sb.append(letters.toCharArray()[Random.nextInt(letters.length)])
        }
        return sb.toString()
    }

    fun generateEmail() = StringBuilder(randomString(6, LATIN))
        .append("@")
        .append(randomString(6, LATIN))
        .append(".")
        .append("pl")
        .toString()
}