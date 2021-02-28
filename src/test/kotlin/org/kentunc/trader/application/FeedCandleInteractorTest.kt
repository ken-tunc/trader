package org.kentunc.trader.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.domain.service.CandleService
import org.kentunc.trader.test.model.TestCandle
import org.kentunc.trader.test.model.TestTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(FeedCandleInteractor::class)
internal class FeedCandleInteractorTest {

    @MockkBean
    private lateinit var candleService: CandleService

    @Autowired
    private lateinit var target: FeedCandleInteractor

    @Test
    fun testFeedCandlesByTicker() = runBlocking {
        // setup:
        val ticker = TestTicker.create()
        val minutesCandle = TestCandle.create(duration = Duration.MINUTES)
        val hoursCandle = TestCandle.create(duration = Duration.HOURS)
        val daysCandle = TestCandle.create(duration = Duration.DAYS)
        coEvery { candleService.feed(ticker, Duration.MINUTES) } returns (minutesCandle to false)
        coEvery { candleService.feed(ticker, Duration.HOURS) } returns (hoursCandle to true)
        coEvery { candleService.feed(ticker, Duration.DAYS) } returns (daysCandle to true)

        val expected = listOf(hoursCandle, daysCandle)

        // exercise:
        val actual = target.feedCandlesByTicker(ticker)

        // verify:
        assertEquals(expected, actual)
    }
}
