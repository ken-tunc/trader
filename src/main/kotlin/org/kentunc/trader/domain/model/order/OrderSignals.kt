package org.kentunc.trader.domain.model.order

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price

class OrderSignals(orders: List<Order>, productCode: ProductCode) {

    init {
        require(orders.all { it.productCode == productCode }) { "Invalid product code order is included." }
    }

    private val sortedOrders = orders.sortedBy { it.orderDate.toLocalDateTime() }
    val size: Int
        get() = sortedOrders.size
    val isEmpty: Boolean
        get() = sortedOrders.isEmpty()

    fun canBuy(): Boolean {
        val lastOrder = sortedOrders.lastOrNull()
        return when (lastOrder?.state) {
            null -> true
            OrderState.ACTIVE -> false
            OrderState.COMPLETED -> lastOrder.orderSide == OrderSide.SELL
            else -> lastOrder.orderSide == OrderSide.BUY
        }
    }

    fun canSell(): Boolean {
        val lastOrder = sortedOrders.lastOrNull()
        return when (lastOrder?.state) {
            null -> false
            OrderState.ACTIVE -> false
            OrderState.COMPLETED -> lastOrder.orderSide == OrderSide.BUY
            else -> lastOrder.orderSide == OrderSide.SELL
        }
    }

    fun lastPriceOfBuy(): Price? {
        return sortedOrders.filter { it.orderSide == OrderSide.BUY }
            .lastOrNull { it.state == OrderState.COMPLETED }
            ?.price
    }

    fun toList() = sortedOrders.toList()
}
