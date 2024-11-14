package mobi.cwiklinski.bloodline.data.firebase

import mobi.cwiklinski.bloodline.data.firebase.util.RegionCenterComparator
import kotlin.test.Test
import kotlin.test.assertEquals


class ComparatorsTests {

    @Test
    fun `sort centers by polish diacritics`() {
        val city1 = TestTools.generateCenter(voivodeship = "óóóóó", city = "zzzzz")
        val city2 = TestTools.generateCenter(voivodeship = "ććććć", city = "bbbbb")
        val city3 = TestTools.generateCenter(voivodeship = "eeeee", city = "zzzzz")
        val city4 = TestTools.generateCenter(voivodeship = "eeeee", city = "ęęęęę")
        val city5 = TestTools.generateCenter(voivodeship = "aaaaa", city = "zzzzz")
        var centers = listOf(city1, city2, city3, city4, city5)
        assertEquals(
            0,
            centers.indexOf(city1),
            "Without sorting expected: 0, result: ${centers.indexOf(city1)}"
        )
        assertEquals(
            1,
            centers.indexOf(city2),
            "Without sorting expected: 1, result: ${centers.indexOf(city2)}"
        )
        assertEquals(
            2,
            centers.indexOf(city3),
            "Without sorting expected: 2, result: ${centers.indexOf(city3)}"
        )
        assertEquals(
            3,
            centers.indexOf(city4),
            "Without sorting expected: 3, result: ${centers.indexOf(city4)}"
        )
        assertEquals(
            4,
            centers.indexOf(city5),
            "Without sorting expected: 4, result: ${centers.indexOf(city5)}"
        )
        centers = centers.sortedWith(RegionCenterComparator())
        assertEquals(
            4,
            centers.indexOf(city1),
            "With sorting expected: 4, result: ${centers.indexOf(city1)}"
        )
        assertEquals(
            1,
            centers.indexOf(city2),
            "With sorting expected: 1, result: ${centers.indexOf(city1)}"
        )
        assertEquals(
            3,
            centers.indexOf(city3),
            "With sorting expected: 3, result: ${centers.indexOf(city1)}"
        )
        assertEquals(
            2,
            centers.indexOf(city4),
            "With sorting expected: 2, result: ${centers.indexOf(city1)}"
        )
        assertEquals(
            0,
            centers.indexOf(city5),
            "With sorting expected: 0, result: ${centers.indexOf(city1)}"
        )
    }
}