package mobi.cwiklinski.bloodline.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation
import kotlin.random.Random

object TestTools {
    private const val POLISH =
        "0123456789AĄBCDEĘFGHIJKLŁMNOÓPQRSŚTUVWXYZŻaąbcćdeęfghijklłmnńoópqrsśtuvwxyzźż"
    private const val LATIN = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

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

    fun generateCenter(
        id: String = randomString(12),
        name: String = randomString(12),
        voivodeship: String = randomString(12),
        latitude: Double = 12.556,
        longitude: Double = 45.7787,
        zip: String = randomString(6),
        city: String = randomString(6)
    ) = Center(
        id = id,
        name = name,
        voivodeship = voivodeship,
        voivodeshipId = randomString(12),
        phone = randomString(12),
        street = randomString(12),
        zip = zip,
        city = city,
        latitude = latitude,
        longitude = longitude,
        site = randomString(12),
        info = randomString(12)
    )

    fun generateDonation(
        id: String? = null,
        year: Int = randomInt(2010, 2018),
        month: Int = randomInt(1, 12),
        day: Int = randomInt(1, 29),
        type: Int = randomInt(1, 4),
        amount: Int = randomInt(100, 500),
        centerId: String = randomString(12),
        disqualification: Boolean = false
    ) = Donation(
        id ?: randomStringLatin(12),
        LocalDate(year, month, day),
        DonationType.byType(type),
        amount,
        randomFloat(),
        randomInt(100, 200),
        randomInt(50, 120),
        disqualification,
        generateCenter(id = centerId)
    )

    fun generateDonations(amount: Int, donation: Donation? = null): List<Donation> {
        val list = arrayListOf<Donation>()
        donation?.let {
            list += donation
        }
        for (i in 0..amount) {
            list += generateDonation()
        }
        return list
    }

    fun generateCenters(max: Int = 20): List<Center> {
        val centers = arrayListOf<Center>()
        for (i in 1..max) {
            centers += Center(
                "id$i",
                "${POLISH.toCharArray()[i + 10]}name",
                "${i.toString()[0]}voivodeship",
                "voivodeship_id",
                "phone$i",
                "street$i",
                "zip$i",
                "city$i",
                1.1 * (10 - i),
                2.2 * (10 - i),
                "site$i",
                "info$i"
            )
        }
        return centers
    }
}