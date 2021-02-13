package org.kentunc.trader.domain.model.ticker

import kotlinx.coroutines.flow.Flow
import org.kentunc.trader.domain.model.market.ProductCode

interface TickerRepository {

    fun stream(productCode: ProductCode): Flow<Ticker>
}
