package org.kentunc.trader.application

import kotlinx.coroutines.flow.Flow
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.service.TickerService
import org.springframework.stereotype.Service

@Service
class SubscribeTickerInteractor(private val tickerService: TickerService) {

    fun subscribe(productCode: ProductCode): Flow<Ticker> = tickerService.subscribe(productCode)
}
