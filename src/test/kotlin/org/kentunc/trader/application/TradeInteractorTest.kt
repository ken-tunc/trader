package org.kentunc.trader.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.trader.domain.model.candle.CandleList
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.order.OrderSignals
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.domain.service.CandleService
import org.kentunc.trader.domain.service.OrderService
import org.kentunc.trader.domain.service.TradeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.util.stream.Stream

@SpringJUnitConfig(TradeInteractor::class)
internal class TradeInteractorTest {

    @MockkBean
    private lateinit var candleService: CandleService

    @MockkBean
    private lateinit var orderService: OrderService

    @MockkBean
    private lateinit var tradeService: TradeService

    @Autowired
    private lateinit var target: TradeInteractor

    @ParameterizedTest
    @ArgumentsSource(TradeProvider::class)
    fun testTrade(
        shouldBuyOrSell: OrderSide,
        canBuy: Boolean,
        canSell: Boolean,
        callBuyOrder: Int,
        callSellOrder: Int
    ) = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        val maxCandleNum = 100

        val candleList = mockk<CandleList>()
        coEvery { candleService.getLatest(productCode, duration, maxCandleNum) } returns candleList
        coEvery { tradeService.shouldBuyOrSell(candleList) } returns shouldBuyOrSell

        val signals = mockk<OrderSignals>()
        every { signals.canBuy() } returns canBuy
        every { signals.canSell() } returns canSell
        coEvery { orderService.getSignals(productCode) } returns signals

        coEvery { orderService.sendBuyAllOrder(productCode) } returns null
        coEvery { orderService.sendSellAllOrder(productCode) } returns null

        coEvery { tradeService.optimize(candleList) } returns Unit

        // exercise:
        target.trade(productCode, duration, maxCandleNum)

        // verify:
        assertAll(
            Executable { coVerify(exactly = callBuyOrder) { orderService.sendBuyAllOrder(productCode) } },
            Executable { coVerify(exactly = callSellOrder) { orderService.sendSellAllOrder(productCode) } },
        )
    }

    private class TradeProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                arguments(OrderSide.NEUTRAL, true, true, 0, 0),
                arguments(OrderSide.NEUTRAL, true, false, 0, 0),
                arguments(OrderSide.NEUTRAL, false, true, 0, 0),
                arguments(OrderSide.NEUTRAL, false, false, 0, 0),
                arguments(OrderSide.BUY, true, true, 1, 0),
                arguments(OrderSide.BUY, true, false, 1, 0),
                arguments(OrderSide.BUY, false, true, 0, 0),
                arguments(OrderSide.BUY, false, false, 0, 0),
                arguments(OrderSide.SELL, true, true, 0, 1),
                arguments(OrderSide.SELL, true, false, 0, 0),
                arguments(OrderSide.SELL, false, true, 0, 1),
                arguments(OrderSide.SELL, false, false, 0, 0)
            )
        }
    }
}
