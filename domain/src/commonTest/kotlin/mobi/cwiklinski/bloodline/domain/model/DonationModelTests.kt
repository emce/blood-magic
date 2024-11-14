package mobi.cwiklinski.bloodline.domain.model

import mobi.cwiklinski.bloodline.domain.TestTools
import kotlin.test.Test
import kotlin.test.assertEquals

class DonationModelTests {

    private val donations = mapOf(
        "don1" to TestTools.generateDonation(
            "don1",
            1993,
            12,
            1,
            DonationType.FULL_BLOOD.type,
            450
        ),
        "don2" to TestTools.generateDonation(
            "don2",
            1995,
            12,
            1,
            DonationType.FULL_BLOOD.type,
            450
        ),
        "don3" to TestTools.generateDonation(
            "don3",
            2001,
            12,
            1,
            DonationType.FULL_BLOOD.type,
            450
        ),
        "don4" to TestTools.generateDonation(
            "don4",
            2005,
            12,
            1,
            DonationType.FULL_BLOOD.type,
            450
        ),
        "don5" to TestTools.generateDonation(
            "don5",
            2008,
            12,
            1,
            DonationType.FULL_BLOOD.type,
            450
        ),
        "don6" to TestTools.generateDonation(
            "don6",
            2010,
            12,
            1,
            DonationType.FULL_BLOOD.type,
            450
        ),
        "don7" to TestTools.generateDonation(
            "don7",
            2014,
            12,
            1,
            DonationType.FULL_BLOOD.type,
            450
        ),
        "don8" to TestTools.generateDonation(
            "don8",
            1994,
            1,
            11,
            DonationType.PLASMA.type,
            650
        ),
        "don9" to TestTools.generateDonation(
            "don9",
            2005,
            1,
            11,
            DonationType.PLASMA.type,
            650
        ),
        "don10" to TestTools.generateDonation(
            "don10",
            2009,
            1,
            11,
            DonationType.PLASMA.type,
            650
        ),
        "don11" to TestTools.generateDonation(
            "don11",
            2014,
            1,
            11,
            DonationType.PLASMA.type,
            650
        ),
        "don12" to TestTools.generateDonation(
            "don12",
            1990,
            6,
            21,
            DonationType.PLATELETS.type,
            250
        ),
        "don13" to TestTools.generateDonation(
            "don13",
            2003,
            6,
            21,
            DonationType.PLATELETS.type,
            250
        ),
        "don14" to TestTools.generateDonation(
            "don14",
            2004,
            6,
            21,
            DonationType.PLATELETS.type,
            250
        ),
        "don15" to TestTools.generateDonation(
            "don15",
            2009,
            6,
            21,
            DonationType.PLATELETS.type,
            250
        ),
        "don16" to TestTools.generateDonation(
            "don16",
            2015,
            6,
            21,
            DonationType.PLATELETS.type,
            250
        ),
        "don17" to TestTools.generateDonation(
            "don17",
            1990,
            8,
            1,
            DonationType.PACKED_CELLS.type,
            600
        ),
        "don18" to TestTools.generateDonation(
            "don18",
            2005,
            8,
            1,
            DonationType.PACKED_CELLS.type,
            600
        ),
        "don19" to TestTools.generateDonation(
            "don19",
            2015,
            8,
            1,
            DonationType.PACKED_CELLS.type,
            600
        )
    )

    @Test
    fun `full blood donation has relevant full blood amount`() {
        var donation = TestTools.generateDonation(
            "don10",
            1990,
            1,
            11,
            DonationType.FULL_BLOOD.type,
            450
        )
        assertEquals(450, donation.convertToFullBlood(Sex.FEMALE))
        donation = TestTools.generateDonation(
            "don10",
            2004,
            1,
            11,
            DonationType.FULL_BLOOD.type,
            400
        )
        assertEquals(400, donation.convertToFullBlood(Sex.MALE))
        donation = TestTools.generateDonation(
            "don10",
            2014,
            1,
            11,
            DonationType.FULL_BLOOD.type,
            450
        )
        assertEquals(450, donation.convertToFullBlood(Sex.FEMALE))
    }

    @Test
    fun `plasma donation has relevant full blood amount`() {
        var donation =
            TestTools.generateDonation("don10", 1990, 1, 11, DonationType.PLASMA.type, 650)
        assertEquals(650, donation.convertToFullBlood(Sex.FEMALE))
        assertEquals(650, donation.convertToFullBlood(Sex.MALE))
        donation =
            TestTools.generateDonation("don10", 2004, 1, 11, DonationType.PLASMA.type, 650)
        assertEquals(200, donation.convertToFullBlood(Sex.FEMALE))
        assertEquals(180, donation.convertToFullBlood(Sex.MALE))
        donation =
            TestTools.generateDonation("don10", 2014, 1, 11, DonationType.PLASMA.type, 650)
        assertEquals(216, donation.convertToFullBlood(Sex.FEMALE))
        assertEquals(216, donation.convertToFullBlood(Sex.MALE))
    }

    @Test
    fun `plates donation has relevant full blood amount`() {
        var donation =
            TestTools.generateDonation("don10", 1990, 1, 11, DonationType.PLATELETS.type, 250)
        assertEquals(250, donation.convertToFullBlood(Sex.FEMALE))
        assertEquals(250, donation.convertToFullBlood(Sex.MALE))
        donation =
            TestTools.generateDonation("don10", 2004, 1, 11, DonationType.PLATELETS.type, 250)
        assertEquals(830, donation.convertToFullBlood(Sex.FEMALE))
        assertEquals(670, donation.convertToFullBlood(Sex.MALE))
        donation =
            TestTools.generateDonation("don10", 2014, 1, 11, DonationType.PLATELETS.type, 250)
        assertEquals(500, donation.convertToFullBlood(Sex.FEMALE))
        assertEquals(500, donation.convertToFullBlood(Sex.MALE))
    }

    @Test
    fun `packed donation has relevant full blood amount`() {
        var donation = TestTools.generateDonation(
            "don10",
            1997,
            1,
            11,
            DonationType.PACKED_CELLS.type,
            600
        )
        assertEquals(600, donation.convertToFullBlood(Sex.FEMALE))
        assertEquals(600, donation.convertToFullBlood(Sex.MALE))
        donation = TestTools.generateDonation(
            "don10",
            2005,
            1,
            11,
            DonationType.PACKED_CELLS.type,
            600
        )
        assertEquals(1000, donation.convertToFullBlood(Sex.FEMALE))
        assertEquals(1000, donation.convertToFullBlood(Sex.MALE))
    }

    @Test
    fun `donation list has relevant full blood amount`() {
        val result = 7 * 450 + 650 + 200 + 2 * 216 + 250 + 2 * 830 + 2 * 500 + 600 + 1000 + 1000
        val list = donations.values.toList()
        assertEquals(19, list.size)
        assertEquals(result, list.sumOf { it.convertToFullBlood(Sex.FEMALE) })
    }

}