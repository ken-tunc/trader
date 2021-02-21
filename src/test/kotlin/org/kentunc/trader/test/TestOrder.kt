package org.kentunc.trader.test

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.Order
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.order.OrderState
import org.kentunc.trader.domain.model.order.OrderType
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Size
import org.kentunc.trader.domain.model.time.DateTime
import java.time.LocalDateTime

object TestOrder {

    fun create(
        productCode: ProductCode = ProductCode.BTC_JPY,
        orderSide: OrderSide = OrderSide.BUY,
        orderType: OrderType = OrderType.MARKET,
        price: Price? = null,
        size: Double = 10000.0,
        averagePrice: Double = 10000.0,
        state: OrderState = OrderState.COMPLETED,
        orderDate: LocalDateTime = LocalDateTime.now()
    ) = Order(
        productCode = productCode,
        orderSide = orderSide,
        orderType = orderType,
        price = price,
        size = Size(size),
        averagePrice = Price(averagePrice),
        state = state,
        orderDate = DateTime(orderDate)
    )
}
