package org.kentunc.trader.domain.specification

import org.kentunc.trader.domain.model.quote.Price
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class LossCutSpecification(@Value("\${trader.strategy.loss-cut.stop-limit-percentage}") stopLimitPercentage: Int) {

    private val stopLimitRate = BigDecimal(stopLimitPercentage).movePointLeft(2)

    fun shouldSell(currentPrice: Price, priceOfLastBuySignal: Price): Boolean {
        return currentPrice.toBigDecimal() < priceOfLastBuySignal.toBigDecimal() * stopLimitRate
    }
}
