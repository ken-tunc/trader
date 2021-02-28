package org.kentunc.trader.domain.model.candle

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.kentunc.trader.test.model.TestCandle
import java.time.LocalDateTime

internal class CandleListTest {

    @Test
    fun testSort() {
        val baseDateTime = LocalDateTime.now()
        val oldestCandle = TestCandle.create(dateTime = baseDateTime)
        val intermediateCandle = TestCandle.create(dateTime = baseDateTime.plusMinutes(30))
        val latestCandle = TestCandle.create(dateTime = baseDateTime.plusHours(3))

        val candleList = CandleList(listOf(latestCandle, oldestCandle, intermediateCandle))

        assertAll(
            Executable { assertFalse(candleList.isEmpty) },
            Executable { assertEquals(3, candleList.size) },
            Executable { assertEquals(oldestCandle.id, candleList.toList()[0].id) },
            Executable { assertEquals(intermediateCandle.id, candleList.toList()[1].id) },
            Executable { assertEquals(latestCandle.id, candleList.toList()[2].id) },
            Executable { assertEquals(latestCandle.id, candleList.latestOrNull()?.id) }
        )
    }
}
