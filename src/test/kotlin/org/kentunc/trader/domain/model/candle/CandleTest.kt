package org.kentunc.trader.domain.model.candle

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Volume
import org.kentunc.trader.domain.model.time.DateTime
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.test.model.TestCandle
import org.kentunc.trader.test.model.TestTicker
import java.time.LocalDateTime

internal class CandleTest {

    @Test
    fun instantiate() {
        val ticker = TestTicker.create()
        val candle = Candle(ticker, Duration.DAYS)
        assertAll(
            Executable { assertEquals(ticker.id.productCode, candle.id.productCode) },
            Executable { assertEquals(ticker.volume, candle.volume) }
        )
    }

    @Test
    fun invalidArgument() {
        assertThrows(IllegalArgumentException::class.java) {
            Candle(
                productCode = ProductCode.BTC_JPY,
                duration = Duration.DAYS,
                dateTime = DateTime(LocalDateTime.now()),
                open = Price(100.0),
                close = Price(100.0),
                high = Price(100.0),
                low = Price(100.01),
                volume = Volume(100.0)
            )
        }
    }

    @Test
    fun testExtendWith() {
        // setup:
        val localDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        val candle = TestCandle.create(
            dateTime = localDateTime,
            open = 100.0,
            close = 200.0,
            high = 300.0,
            low = 50.0,
            volume = 100.0
        )
        val ticker = TestTicker.create(
            dateTime = localDateTime,
            bestBid = 400.0,
            bestAsk = 400.0,
            volume = 150.0
        )

        // exercise
        val actual = candle.extendWith(ticker)

        // verify
        assertAll(
            Executable { assertEquals(ProductCode.BTC_JPY, actual.id.productCode) },
            Executable { assertEquals(Duration.DAYS, actual.id.duration) },
            Executable { assertEquals(Price(100.0), actual.open) },
            Executable { assertEquals(Price(400.0), actual.close) },
            Executable { assertEquals(Price(400.0), actual.high) },
            Executable { assertEquals(Price(50.0), actual.low) },
            Executable { assertEquals(Volume(250.0), actual.volume) }
        )
    }

    @Test
    fun `test extendWith() with invalid product code`() {
        val localDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        val candle = TestCandle.create(productCode = ProductCode.BTC_JPY, dateTime = localDateTime)
        val ticker = TestTicker.create(productCode = ProductCode.ETH_JPY, dateTime = localDateTime)

        assertThrows(IllegalArgumentException::class.java) {
            candle.extendWith(ticker)
        }
    }

    @Test
    fun `test extendWith() with invalid datetime`() {
        val localDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        val candle = TestCandle.create(dateTime = localDateTime)
        val ticker = TestTicker.create(dateTime = localDateTime.plusDays(1))

        assertThrows(IllegalArgumentException::class.java) {
            candle.extendWith(ticker)
        }
    }
}
