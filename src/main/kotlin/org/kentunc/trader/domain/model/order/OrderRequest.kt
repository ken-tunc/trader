package org.kentunc.trader.domain.model.order

import org.kentunc.trader.domain.model.market.Balance
import org.kentunc.trader.domain.model.market.CommissionRate
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Size

class OrderRequest private constructor(
    val productCode: ProductCode,
    val orderType: OrderType,
    val orderSide: OrderSide,
    val price: Price?,
    val size: Size,
    val minutesToExpire: MinutesToExpire,
    val timeInForce: TimeInForce
) {
    init {
        require(orderSide != OrderSide.NEUTRAL) { "Neutral side order is not allowed." }

        val isValidOrder = (orderType == OrderType.LIMIT && price != null) ||
            (orderType == OrderType.MARKET && price == null)
        require(isValidOrder) { "Order type and price are invalid, order=$orderType, price=$price" }
    }

    companion object {
        private val DEFAULT_EXPIRE_MINUTES = MinutesToExpire(10)

        fun sellAll(
            productCode: ProductCode,
            balance: Balance,
            minutesToExpire: MinutesToExpire = DEFAULT_EXPIRE_MINUTES,
            timeInForce: TimeInForce = TimeInForce.GTC
        ): OrderRequest {
            require(productCode.coin == balance.currencyCode) {
                "Invalid sell order: productCode=$productCode, balance currencyCode=${balance.currencyCode}"
            }
            return OrderRequest(
                productCode = productCode,
                orderType = OrderType.MARKET,
                orderSide = OrderSide.SELL,
                price = null,
                size = balance.available,
                minutesToExpire = minutesToExpire,
                timeInForce = timeInForce
            )
        }

        fun buyAll(
            productCode: ProductCode,
            balance: Balance,
            commissionRate: CommissionRate,
            minutesToExpire: MinutesToExpire = DEFAULT_EXPIRE_MINUTES,
            timeInForce: TimeInForce = TimeInForce.GTC
        ): OrderRequest {
            require(productCode.money == balance.currencyCode) {
                "Invalid buy order: productCode=$productCode, balance currencyCode=${balance.currencyCode}"
            }
            val adjustedSize = balance.available - commissionRate.fee(balance.available)
            return OrderRequest(
                productCode = productCode,
                orderType = OrderType.MARKET,
                orderSide = OrderSide.BUY,
                price = null,
                size = adjustedSize,
                minutesToExpire = minutesToExpire,
                timeInForce = timeInForce
            )
        }
    }
}
