package org.kentunc.trader.domain.model.candle

import kotlinx.coroutines.flow.Flow
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.time.Duration

interface CandleRepository {

    suspend fun find(identifier: CandleIdentifier): Candle?
    suspend fun findLatest(productCode: ProductCode, duration: Duration, maxNum: Int?): Flow<Candle>
    suspend fun save(candle: Candle): Void?
    suspend fun update(candle: Candle): Void?
}
