package org.kentunc.trader.domain.model.order

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.test.model.TestOrder
import java.time.LocalDateTime
import java.util.stream.Stream

internal class OrderSignalsTest {

    @Test
    fun testSorted() {
        // setup:
        val baseDateTime = LocalDateTime.now()
        val oldestOrder =
            TestOrder.create(orderSide = OrderSide.BUY, state = OrderState.COMPLETED, orderDate = baseDateTime)
        val intermediateOrder = TestOrder.create(
            orderSide = OrderSide.SELL,
            state = OrderState.COMPLETED,
            orderDate = baseDateTime.plusMinutes(1)
        )
        val latestOrder = TestOrder.create(
            orderSide = OrderSide.BUY,
            state = OrderState.ACTIVE,
            orderDate = baseDateTime.plusHours(1)
        )
        val orders = listOf(intermediateOrder, latestOrder, oldestOrder)

        // exercise
        val orderSignals = OrderSignals(orders, ProductCode.BTC_JPY)

        // verify
        assertAll(
            Executable { assertFalse(orderSignals.isEmpty) },
            Executable { assertEquals(orders.size, orderSignals.size) },
            Executable { assertEquals(oldestOrder.orderDate, orderSignals.toList()[0].orderDate) },
            Executable { assertEquals(intermediateOrder.orderDate, orderSignals.toList()[1].orderDate) },
            Executable { assertEquals(latestOrder.orderDate, orderSignals.toList()[2].orderDate) }
        )
    }

    @Test
    fun instantiate_invalidProductCodes() {
        assertThrows(IllegalArgumentException::class.java) {
            OrderSignals(
                listOf(
                    TestOrder.create(
                        orderSide = OrderSide.BUY,
                        state = OrderState.ACTIVE,
                        orderDate = LocalDateTime.now()
                    )
                ),
                ProductCode.ETH_JPY
            )
        }
    }

    @ParameterizedTest
    @ArgumentsSource(OrdersProvider::class)
    fun `canSell and canBuy`(orders: List<Order>, canBuy: Boolean, canSell: Boolean) {
        val orderSignals = OrderSignals(orders, ProductCode.BTC_JPY)
        assertAll(
            Executable { assertEquals(canBuy, orderSignals.canBuy()) },
            Executable { assertEquals(canSell, orderSignals.canSell()) }
        )
    }

    private class OrdersProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val baseDateTime = LocalDateTime.now()
            return Stream.of(
                // orders, canBuy, canSell
                arguments(
                    listOf<Order>(),
                    true,
                    false
                ),
                arguments(
                    listOf(
                        TestOrder.create(
                            orderSide = OrderSide.BUY,
                            state = OrderState.ACTIVE,
                            orderDate = baseDateTime
                        )
                    ),
                    false,
                    false
                ),
                arguments(
                    listOf(
                        TestOrder.create(
                            orderSide = OrderSide.BUY,
                            state = OrderState.REJECTED,
                            orderDate = baseDateTime
                        )
                    ),
                    true,
                    false
                ),
                arguments(
                    listOf(
                        TestOrder.create(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            orderDate = baseDateTime
                        )
                    ),
                    false,
                    true
                ),
                arguments(
                    listOf(
                        TestOrder.create(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            orderDate = baseDateTime
                        ),
                        TestOrder.create(
                            orderSide = OrderSide.SELL,
                            state = OrderState.ACTIVE,
                            orderDate = baseDateTime.plusMinutes(1)
                        )
                    ),
                    false,
                    false
                ),
                arguments(
                    listOf(
                        TestOrder.create(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            orderDate = baseDateTime
                        ),
                        TestOrder.create(
                            orderSide = OrderSide.SELL,
                            state = OrderState.CANCELED,
                            orderDate = baseDateTime.plusMinutes(1)
                        )
                    ),
                    false,
                    true
                ),
                arguments(
                    listOf(
                        TestOrder.create(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            orderDate = baseDateTime
                        ),
                        TestOrder.create(
                            orderSide = OrderSide.SELL,
                            state = OrderState.COMPLETED,
                            orderDate = baseDateTime.plusMinutes(1)
                        )
                    ),
                    true,
                    false
                ),
            )
        }
    }
}
