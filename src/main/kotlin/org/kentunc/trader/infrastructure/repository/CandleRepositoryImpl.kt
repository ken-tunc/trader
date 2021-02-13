package org.kentunc.trader.infrastructure.repository

import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.candle.CandleIdentifier
import org.kentunc.trader.domain.model.candle.CandleRepository
import org.kentunc.trader.extension.toCandleEntity
import org.kentunc.trader.extension.toCandlePrimaryKey
import org.kentunc.trader.infrastructure.repository.database.dao.CandleDao
import org.springframework.stereotype.Repository

@Repository
class CandleRepositoryImpl(private val candleDao: CandleDao) : CandleRepository {

    override suspend fun find(identifier: CandleIdentifier): Candle? {
        return candleDao.select(identifier.toCandlePrimaryKey())
    }

    override suspend fun save(candle: Candle): Void? {
        return candleDao.insert(candle.toCandleEntity())
    }

    override suspend fun update(candle: Candle): Void? {
        return candleDao.update(candle.toCandleEntity())
    }
}
