package org.kentunc.trader.infrastructure.repository.database.dao

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.infrastructure.repository.database.entity.CandleEntity
import org.kentunc.trader.infrastructure.repository.database.entity.CandlePrimaryKey
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.insert
import org.springframework.data.r2dbc.core.select
import org.springframework.data.r2dbc.core.update
import org.springframework.data.relational.core.query.Query.query
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CandleDao(private val template: R2dbcEntityTemplate) {

    @Transactional(readOnly = true)
    suspend fun select(primaryKey: CandlePrimaryKey): Candle? {
        return template.select<CandleEntity>()
            .matching(query(primaryKey.toCriteria()))
            .one()
            .awaitFirstOrNull()?.toCandle()
    }

    @Transactional
    suspend fun insert(candleEntity: CandleEntity): Void? {
        return template.insert<CandleEntity>()
            .using(candleEntity)
            .then()
            .awaitFirstOrNull()
    }

    @Transactional
    suspend fun update(candleEntity: CandleEntity): Void? {
        return template.update<CandleEntity>()
            .matching(query(candleEntity.getPrimaryKey().toCriteria()))
            .apply(
                Update.update("open", candleEntity.open)
                    .set("close", candleEntity.close)
                    .set("high", candleEntity.high)
                    .set("low", candleEntity.low)
                    .set("volume", candleEntity.volume)
            )
            .then()
            .awaitFirstOrNull()
    }
}
