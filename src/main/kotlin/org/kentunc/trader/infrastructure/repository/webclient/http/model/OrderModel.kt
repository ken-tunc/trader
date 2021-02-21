package org.kentunc.trader.infrastructure.repository.webclient.http.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.Order
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.order.OrderState
import org.kentunc.trader.domain.model.order.OrderType
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Size
import org.kentunc.trader.domain.model.time.DateTime
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderModel(
    @field:JsonProperty("product_code")
    val productCode: ProductCode,
    val side: OrderSide,
    @field:JsonProperty("child_order_type")
    val orderType: OrderType,
    val price: BigDecimal,
    val size: BigDecimal,
    @field:JsonProperty("average_price")
    val averagePrice: BigDecimal,
    @field:JsonProperty("child_order_state")
    val state: OrderState,
    @field:JsonProperty("child_order_date")
    val orderDate: LocalDateTime
) {

    companion object {
        private val MARKET_PRICE_HOLDER = BigDecimal.ZERO
    }

    fun toOrder(): Order {
        return Order(
            productCode = productCode,
            orderSide = side,
            orderType = orderType,
            price = if (price == MARKET_PRICE_HOLDER) null else Price(price),
            size = Size(size),
            averagePrice = Price(averagePrice),
            state = state,
            orderDate = DateTime(orderDate)
        )
    }
}
