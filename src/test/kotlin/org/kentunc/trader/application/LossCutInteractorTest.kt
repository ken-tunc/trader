package org.kentunc.trader.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.kentunc.trader.domain.service.OrderService
import org.kentunc.trader.domain.specification.LossCutSpecification
import org.kentunc.trader.test.model.TestTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(LossCutInteractor::class)
internal class LossCutInteractorTest {

    @MockkBean
    private lateinit var orderService: OrderService
    @MockkBean
    private lateinit var lossCutSpecification: LossCutSpecification

    @Autowired
    private lateinit var target: LossCutInteractor

    @Test
    fun testLossCutIfStopLimitExceeded_exceeded() = runBlocking {
        // setup:
        val ticker = TestTicker.create()
        coEvery { lossCutSpecification.shouldSell(ticker) } returns false

        // exercise:
        target.lossCutIfStopLimitExceeded(ticker)

        // verify:
        coVerify(exactly = 0) { orderService.sendSellAllOrder(any()) }
    }

    @Test
    fun testLossCutIfStopLimitExceeded_notExceeded() = runBlocking {
        // setup:
        val ticker = TestTicker.create()
        coEvery { lossCutSpecification.shouldSell(ticker) } returns true
        coEvery { orderService.sendSellAllOrder(any()) } returns null

        // exercise:
        target.lossCutIfStopLimitExceeded(ticker)

        // verify:
        coVerify { orderService.sendSellAllOrder(ticker.id.productCode) }
    }
}
