package org.kentunc.trader.application

import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.service.OrderService
import org.kentunc.trader.domain.specification.LossCutSpecification
import org.springframework.stereotype.Service

@Service
class LossCutInteractor(
    private val orderService: OrderService,
    private val lossCutSpecification: LossCutSpecification
) {

    suspend fun lossCutIfStopLimitExceeded(ticker: Ticker) {
        if (lossCutSpecification.shouldSell(ticker)) {
            orderService.sendSellAllOrder(ticker.id.productCode)
        }
    }
}
