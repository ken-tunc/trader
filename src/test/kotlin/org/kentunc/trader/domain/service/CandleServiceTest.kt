package org.kentunc.trader.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.candle.CandleRepository
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.test.model.TestCandle
import org.kentunc.trader.test.model.TestTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CandleService::class)
internal class CandleServiceTest {

    @MockkBean
    private lateinit var candleRepository: CandleRepository

    @Autowired
    private lateinit var target: CandleService

    @Test
    fun testFeed_created() = runBlocking {
        // setup:
        val ticker = TestTicker.create()
        val duration = Duration.DAYS
        coEvery { candleRepository.find(any()) } returns null
        coEvery { candleRepository.save(any()) } returns null

        // exercise:
        val (actualCandle, isCreated) = target.feed(ticker, duration)

        // verify:
        assertAll(
            Executable { assertEquals(ticker.id.productCode, actualCandle.id.productCode) },
            Executable { assertEquals(duration, actualCandle.id.duration) },
            Executable { assertTrue(isCreated) },
            Executable { coVerify { candleRepository.save(actualCandle) } }
        )
    }

    @Test
    fun testFeed_updated() = runBlocking {
        // setup:
        val ticker = TestTicker.create()
        val duration = Duration.DAYS
        val existing = mockk<Candle>()
        val updated = TestCandle.create(duration = duration)
        every { existing.extendWith(ticker) } returns updated
        coEvery { candleRepository.find(any()) } returns existing
        coEvery { candleRepository.update(updated) } returns null

        // exercise:
        val (actualCandle, isCreated) = target.feed(ticker, duration)

        // verify:
        assertAll(
            Executable { assertEquals(updated, actualCandle) },
            Executable { assertFalse(isCreated) }
        )
    }

    @Test
    fun testGetLatest() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        val maxNum = 100
        val candle = TestCandle.create()
        coEvery { candleRepository.findLatest(productCode, duration, maxNum) } returns flowOf(candle)

        // exercise:
        val actual = target.getLatest(productCode, duration, maxNum)

        // verify:
        assertEquals(candle.id, actual.latestOrNull()!!.id)
    }
}
