package org.kentunc.trader.domain.model.order

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Size
import org.kentunc.trader.domain.model.time.DateTime

data class Order(
    val productCode: ProductCode,
    val orderSide: OrderSide,
    val orderType: OrderType,
    val price: Price?,
    val size: Size,
    val averagePrice: Price,
    val state: OrderState,
    val orderDate: DateTime
)
