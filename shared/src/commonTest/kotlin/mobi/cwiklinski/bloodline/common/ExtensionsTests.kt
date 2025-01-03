package mobi.cwiklinski.bloodline.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ExtensionsTests {

    @Test
    fun `transforms all characters to latin`() {
        assertEquals("Loza", "Łóżą".removeDiacritics())
    }

    @Test
    fun `returns doubles with two decimals`() {
        assertEquals("120.0000", 120.toDouble().toPrecision(4))
        assertEquals("0.0001", 0.000089f.toDouble().toPrecision(4))
        assertEquals("1.04", 1.0430.toDouble().toPrecision(2))
    }

    @Test
    fun `validates email`() {
        assertTrue("user@gmail.com".isValidEmail())
        assertFalse("messenger@me".isValidEmail())
    }
}