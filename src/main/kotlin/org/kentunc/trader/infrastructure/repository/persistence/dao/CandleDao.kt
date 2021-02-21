package org.kentunc.trader.infrastructure.repository.persistence.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.infrastructure.repository.persistence.entity.CandleEntity
import org.kentunc.trader.infrastructure.repository.persistence.entity.CandlePrimaryKey
import org.springframework.data.domain.Sort.Order.desc
import org.springframework.data.domain.Sort.by
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.insert
import org.springframework.data.r2dbc.core.select
import org.springframework.data.r2dbc.core.update
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CandleDao(private val template: R2dbcEntityTemplate) {

    @Transactional(readOnly = true)
    suspend fun selectOne(primaryKey: CandlePrimaryKey): CandleEntity? {
        return template.select<CandleEntity>()
            .matching(query(primaryKey.toCriteria()))
            .one()
            .awaitFirstOrNull()
    }

    @Transactional(readOnly = true)
    suspend fun select(productCode: ProductCode, duration: Duration, maxNum: Int?): Flow<CandleEntity> {
        var query = query(
            where("product_code").`is`(productCode)
                .and("duration").`is`(duration)
        ).sort(by(desc("date_time")))
        maxNum?.also { query = query.limit(maxNum) }

        return template.select<CandleEntity>()
            .matching(query)
            .all()
            .asFlow()
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
