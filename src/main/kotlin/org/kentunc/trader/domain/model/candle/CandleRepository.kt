package org.kentunc.trader.domain.model.candle

interface CandleRepository {

    suspend fun find(identifier: CandleIdentifier): Candle?
    suspend fun save(candle: Candle): Void?
    suspend fun update(candle: Candle): Void?
}
