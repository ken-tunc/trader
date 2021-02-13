package org.kentunc.trader.domain.model.quote

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

internal class VolumeTest {

    @ParameterizedTest
    @ValueSource(doubles = [0.0, 100.00001])
    fun instantiate(value: Double) {
        val volume = Volume(value)
        assertTrue(BigDecimal.valueOf(value).compareTo(volume.toBigDecimal()) == 0)
    }

    @Test
    fun invalidValue() {
        assertThrows<IllegalArgumentException> { Volume(-1.0) }
    }

    @Test
    fun testAdd() {
        val volume = Volume(100.0)
        val augend = Volume(234.567)
        val expected = Volume(334.567)
        assertEquals(expected, volume + augend)
    }
}
