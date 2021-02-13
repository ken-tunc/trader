package org.kentunc.trader.domain.service

import kotlinx.coroutines.flow.Flow
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.ticker.TickerRepository
import org.springframework.stereotype.Service

@Service
class TickerService(private val tickerRepository: TickerRepository) {

    fun subscribe(productCode: ProductCode): Flow<Ticker> = tickerRepository.stream(productCode)
}
