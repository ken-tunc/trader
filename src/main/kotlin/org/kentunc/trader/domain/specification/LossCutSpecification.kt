package org.kentunc.trader.domain.specification

import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.service.OrderService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class LossCutSpecification(
    @Value("\${trader.strategy.loss-cut.stop-limit-percentage}") stopLimitPercentage: Int,
    private val orderService: OrderService
) {

    private val stopLimitRate = BigDecimal(stopLimitPercentage).movePointLeft(2)

    suspend fun shouldSell(ticker: Ticker): Boolean {
        val signals = orderService.getSignals(ticker.id.productCode)
        val priceOfLastBuySignal = signals.lastPriceOfBuy() ?: return false

        val stopLimitExceeded = ticker.midPrice.toBigDecimal() < priceOfLastBuySignal.toBigDecimal() * stopLimitRate
        return signals.canSell() && stopLimitExceeded
    }
}
