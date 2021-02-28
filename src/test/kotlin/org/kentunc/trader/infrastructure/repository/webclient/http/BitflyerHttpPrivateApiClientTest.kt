package org.kentunc.trader.infrastructure.repository.webclient.http

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.function.Executable
import org.kentunc.trader.domain.model.market.Balance
import org.kentunc.trader.domain.model.market.CommissionRate
import org.kentunc.trader.domain.model.market.CurrencyCode
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.Order
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.order.OrderState
import org.kentunc.trader.domain.model.order.OrderType
import org.kentunc.trader.domain.model.order.TimeInForce
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Size
import org.kentunc.trader.domain.model.time.DateTime
import org.kentunc.trader.infrastructure.repository.webclient.http.model.OrderRequestModel
import org.kentunc.trader.test.extension.WebClientExtension
import org.kentunc.trader.test.util.ResourceReader
import org.springframework.http.HttpMethod
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month

internal class BitflyerHttpPrivateApiClientTest {

    lateinit var target: BitflyerHttpPrivateApiClient

    @BeforeEach
    internal fun setUp() {
        target = BitflyerHttpPrivateApiClient(helper.createWebClient())
    }

    @Test
    fun testGetBalances() = runBlocking {
        // setup:
        val responseBody = ResourceReader.readResource("$PREFIX/get_getbalance.json")
        helper.enqueueResponse(responseBody)
        val expected = listOf(
            Balance(CurrencyCode.JPY, Size(1024078.0), Size(508000.0)),
            Balance(CurrencyCode.BTC, Size(10.24), Size(4.12)),
            Balance(CurrencyCode.ETH, Size(20.48), Size(16.38))
        )

        // exercise:
        val actual = target.getBalances().toList().map { it.toBalance() }

        // verify:
        assertAll(
            Executable { assertEquals(expected, actual) },
            Executable { helper.assertRequest(HttpMethod.GET, "/me/getbalance") }
        )
    }

    @Test
    fun testGetOrders() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val responseBody = ResourceReader.readResource("$PREFIX/get_getchildorders.json")
        helper.enqueueResponse(responseBody)
        val expected = listOf(
            Order(
                productCode = productCode,
                orderSide = OrderSide.BUY,
                orderType = OrderType.LIMIT,
                price = Price(30000),
                size = Size(0.1),
                averagePrice = Price(30000),
                state = OrderState.COMPLETED,
                orderDate = DateTime(LocalDateTime.of(2015, Month.JULY, 7, 8, 45, 53))
            ),
            Order(
                productCode = productCode,
                orderSide = OrderSide.SELL,
                orderType = OrderType.LIMIT,
                price = Price(30000),
                size = Size(0.1),
                averagePrice = Price(0),
                state = OrderState.CANCELED,
                orderDate = DateTime(LocalDateTime.of(2015, Month.JULY, 7, 8, 45, 47))
            )
        )

        // exercise:
        val actual = target.getOrders(productCode).toList().map { it.toOrder() }

        // verify:
        assertAll(
            Executable { assertEquals(expected, actual) },
            Executable { helper.assertRequest(HttpMethod.GET, "/me/getchildorders?product_code=$productCode") }
        )
    }

    @Test
    fun testSendOrder() = runBlocking {
        // setup:
        val request = OrderRequestModel(
            productCode = ProductCode.BTC_JPY,
            orderType = OrderType.LIMIT,
            side = OrderSide.BUY,
            price = BigDecimal.valueOf(30000),
            size = BigDecimal.valueOf(0.1),
            minutesToExpire = 10000,
            timeInForce = TimeInForce.GTC
        )
        val responseBody = ResourceReader.readResource("$PREFIX/post_sendchildorder.json")
        helper.enqueueResponse(responseBody)

        // exercise:
        target.sendOrder(request)

        // verify:
        helper.assertRequest(HttpMethod.POST, "/me/sendchildorder", request)
    }

    @Test
    fun testGetCommissionRate() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val responseBody = ResourceReader.readResource("$PREFIX/get_gettradingcommission.json")
        helper.enqueueResponse(responseBody)
        val expected = CommissionRate(BigDecimal.valueOf(0.001))

        // exercise:
        val actual = target.getCommissionRate(productCode).toCommissionRate()

        // verify:
        assertAll(
            Executable { assertEquals(expected, actual) },
            Executable { helper.assertRequest(HttpMethod.GET, "/me/gettradingcommission?product_code=$productCode") }
        )
    }

    companion object {
        @JvmField
        @RegisterExtension
        internal val helper = WebClientExtension()

        private const val PREFIX = "/response/bitflyer"
    }
}
