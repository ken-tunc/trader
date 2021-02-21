package org.kentunc.trader.infrastructure.repository.webclient.http.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.order.OrderType
import org.kentunc.trader.domain.model.order.TimeInForce
import java.math.BigDecimal

data class OrderRequestModel(
    @field:JsonProperty("product_code")
    val productCode: ProductCode,
    @field:JsonProperty("child_order_type")
    val orderType: OrderType,
    val side: OrderSide,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val price: BigDecimal?,
    val size: BigDecimal,
    @field:JsonProperty("minute_to_expire")
    val minutesToExpire: Int,
    @field:JsonProperty("time_in_force")
    val timeInForce: TimeInForce
)
