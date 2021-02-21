package org.kentunc.trader.extension.mapper

import org.kentunc.trader.domain.model.order.OrderRequest
import org.kentunc.trader.infrastructure.repository.webclient.http.model.OrderRequestModel

fun OrderRequest.toOrderRequestModel() = OrderRequestModel(
    productCode = productCode,
    orderType = orderType,
    side = orderSide,
    price = price?.toBigDecimal(),
    size = size.toBigDecimal(),
    minutesToExpire = minutesToExpire.toInt(),
    timeInForce = timeInForce
)
