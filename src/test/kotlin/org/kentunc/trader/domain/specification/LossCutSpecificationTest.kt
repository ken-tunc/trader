package org.kentunc.trader.domain.specification

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.trader.domain.model.order.OrderSignals
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.service.OrderService
import org.kentunc.trader.test.model.TestTicker
import java.math.BigDecimal
import java.util.stream.Stream

internal class LossCutSpecificationTest {

    lateinit var orderService: OrderService

    lateinit var target: LossCutSpecification

    @BeforeEach
    fun setUp() {
        orderService = mockk()
        target = LossCutSpecification(stopLimitPercentage, orderService)
    }

    @ParameterizedTest
    @ArgumentsSource(PricesProvider::class)
    fun testShouldSell(lastPriceOfBy: Price?, canSell: Boolean, expected: Boolean) = runBlocking {
        // setup:
        val ticker = TestTicker.create()
        val signals = mockk<OrderSignals>()
        every { signals.lastPriceOfBuy() } returns lastPriceOfBy
        every { signals.canSell() } returns canSell
        coEvery { orderService.getSignals(ticker.id.productCode) } returns signals

        // exercise:
        val actual = target.shouldSell(ticker)

        // verify:
        assertEquals(expected, actual)
    }

    companion object {
        private const val stopLimitPercentage = 80
    }

    private class PricesProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val boundaryPrice = TestTicker.create().midPrice
                .toBigDecimal()
                .multiply(BigDecimal.valueOf(100.0))
                .divide(BigDecimal(stopLimitPercentage))
            return Stream.of(
                arguments(Price(boundaryPrice), true, false),
                arguments(Price(boundaryPrice + BigDecimal.ONE), true, true),
                arguments(Price(boundaryPrice + BigDecimal.ONE), false, false),
                arguments(null, true, false)
            )
        }
    }
}
