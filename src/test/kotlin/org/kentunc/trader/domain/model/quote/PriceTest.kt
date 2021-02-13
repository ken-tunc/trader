package org.kentunc.trader.domain.model.quote

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class PriceTest {

    @Test
    fun testAdd() {
        val price = Price(100.0)
        val augend = Price(234.56789)
        val expected = Price(334.56789)
        assertEquals(expected, price + augend)
    }

    @Test
    fun testDivide() {
        val price = Price(123.4)
        val divisor = Price(20.0)
        val expected = Price(6.17)
        assertEquals(expected, price / divisor)
    }

    @Test
    fun testCompare() {
        val price = Price(100.0)
        assertAll(
            Executable { assertTrue(price > Price(99.9999)) },
            Executable { assertTrue(price == Price(100.0)) },
            Executable { assertTrue(price < Price(100.0001)) }
        )
    }
}
