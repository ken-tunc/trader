package org.kentunc.trader.domain.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.trader.domain.model.candle.CandleList
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.trade.TradingStrategy
import java.util.stream.Stream

internal class TradeServiceTest {

    private lateinit var tradeStrategy1: TradingStrategy
    private lateinit var tradeStrategy2: TradingStrategy
    private lateinit var tradeStrategy3: TradingStrategy

    private lateinit var target: TradeService

    @BeforeEach
    internal fun setUp() {
        tradeStrategy1 = mockk(relaxUnitFun = true)
        tradeStrategy2 = mockk(relaxUnitFun = true)
        tradeStrategy3 = mockk(relaxUnitFun = true)
        target = TradeService(listOf(tradeStrategy1, tradeStrategy2, tradeStrategy3))
    }

    @ParameterizedTest
    @ArgumentsSource(OrderSideProvider::class)
    fun testShouldBuyOrSell(side1: OrderSide, side2: OrderSide, side3: OrderSide, expected: OrderSide) = runBlocking {
        // setup:
        val candleList = CandleList(listOf())
        coEvery { tradeStrategy1.shouldBuyOrSell(candleList) } returns side1
        coEvery { tradeStrategy2.shouldBuyOrSell(candleList) } returns side2
        coEvery { tradeStrategy3.shouldBuyOrSell(candleList) } returns side3

        // exercise:
        val actual = target.shouldBuyOrSell(candleList)

        // verify:
        assertEquals(expected, actual)
    }

    @Test
    fun testOptimize() = runBlocking {
        val candleList = CandleList(listOf())
        target.optimize(candleList)
        assertAll(
            Executable { coVerify { tradeStrategy1.optimize(candleList) } },
            Executable { coVerify { tradeStrategy2.optimize(candleList) } },
            Executable { coVerify { tradeStrategy3.optimize(candleList) } }
        )
    }

    private class OrderSideProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                arguments(OrderSide.BUY, OrderSide.BUY, OrderSide.SELL, OrderSide.BUY),
                arguments(OrderSide.BUY, OrderSide.SELL, OrderSide.SELL, OrderSide.SELL),
                arguments(OrderSide.BUY, OrderSide.NEUTRAL, OrderSide.NEUTRAL, OrderSide.NEUTRAL)
            )
        }
    }
}
