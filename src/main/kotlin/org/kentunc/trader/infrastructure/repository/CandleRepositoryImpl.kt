package org.kentunc.trader.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.candle.CandleIdentifier
import org.kentunc.trader.domain.model.candle.CandleRepository
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.extension.toCandleEntity
import org.kentunc.trader.extension.toCandlePrimaryKey
import org.kentunc.trader.infrastructure.repository.persistence.dao.CandleDao
import org.springframework.stereotype.Repository

@Repository
class CandleRepositoryImpl(private val candleDao: CandleDao) : CandleRepository {

    override suspend fun find(identifier: CandleIdentifier): Candle? {
        return candleDao.selectOne(identifier.toCandlePrimaryKey())
            ?.toCandle()
    }

    override suspend fun findLatest(productCode: ProductCode, duration: Duration, maxNum: Int?): Flow<Candle> {
        return candleDao.select(productCode, duration, maxNum)
            .map { it.toCandle() }
    }

    override suspend fun save(candle: Candle): Void? {
        return candleDao.insert(candle.toCandleEntity())
    }

    override suspend fun update(candle: Candle): Void? {
        return candleDao.update(candle.toCandleEntity())
    }
}
