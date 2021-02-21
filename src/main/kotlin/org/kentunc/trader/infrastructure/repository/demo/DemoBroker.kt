package org.kentunc.trader.infrastructure.repository.demo

import org.kentunc.trader.domain.model.market.Balance
import org.kentunc.trader.domain.model.market.CommissionRate
import org.kentunc.trader.domain.model.market.CommissionRateRepository
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.Order
import org.kentunc.trader.domain.model.order.OrderRequest
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.order.OrderState
import org.kentunc.trader.domain.model.order.OrderType
import org.kentunc.trader.domain.model.quote.Size
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.time.DateTime
import org.kentunc.trader.infrastructure.repository.webclient.http.BitflyerHttpPublicApiClient
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class DemoBroker(
    private val db: DemoDatabase,
    private val bitflyerHttpPublicApiClient: BitflyerHttpPublicApiClient,
    private val commissionRateRepository: CommissionRateRepository
) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    fun getBalances(): List<Balance> = db.getBalances()
        .map {
            Balance(
                currencyCode = it.key,
                amount = Size(it.value),
                available = Size(it.value)
            )
        }

    fun getOrders(productCode: ProductCode): List<Order> = db.getOrders(productCode)

    suspend fun sendOrder(order: OrderRequest) {
        val ticker = bitflyerHttpPublicApiClient.getTicker(order.productCode).toTicker()
        val commissionRate = commissionRateRepository.get(order.productCode)

        val currencyPair = when (order.orderSide) {
            OrderSide.BUY -> buy(order, ticker, commissionRate)
            OrderSide.SELL -> sell(order, ticker, commissionRate)
            else -> throw IllegalStateException("Neutral side order is not allowed.")
        }
        val (from, into) = currencyPair

        val canTransact = db.canExchange(from, into)
        if (canTransact) {
            db.exchange(from, into)
        }

        val orderState = if (canTransact) OrderState.COMPLETED else OrderState.REJECTED
        db.addOrder(
            Order(
                productCode = order.productCode,
                orderSide = order.orderSide,
                orderType = order.orderType,
                price = order.price,
                size = order.size,
                averagePrice = ticker.midPrice,
                state = orderState,
                orderDate = DateTime(LocalDateTime.now())
            )
        )
        log.info("Send order: $order")
    }

    private fun buy(order: OrderRequest, ticker: Ticker, commissionRate: CommissionRate): Pair<Currency, Currency> {
        val orderMoneyAmount = when (order.orderType) {
            OrderType.MARKET -> ticker.bestAsk.toBigDecimal() * order.size.toBigDecimal()
            OrderType.LIMIT -> order.price!!.toBigDecimal() * order.size.toBigDecimal()
        }.let { Size(it) }
        val fee = commissionRate.fee(orderMoneyAmount)
        val from = Currency(order.productCode.money, (orderMoneyAmount + fee).toBigDecimal())
        val into = Currency(order.productCode.coin, order.size.toBigDecimal())

        return Pair(from, into)
    }

    private fun sell(order: OrderRequest, ticker: Ticker, commissionRate: CommissionRate): Pair<Currency, Currency> {
        val fee = commissionRate.fee(order.size)
        val from = Currency(order.productCode.coin, (order.size + fee).toBigDecimal())

        val moneyAmount = when (order.orderType) {
            OrderType.MARKET -> ticker.bestBid.toBigDecimal() * order.size.toBigDecimal()
            OrderType.LIMIT -> order.price!!.toBigDecimal() * order.size.toBigDecimal()
        }
        val into = Currency(order.productCode.money, moneyAmount)

        return Pair(from, into)
    }
}
