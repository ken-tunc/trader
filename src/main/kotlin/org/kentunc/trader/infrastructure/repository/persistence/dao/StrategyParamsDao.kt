package org.kentunc.trader.infrastructure.repository.persistence.dao

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.kentunc.trader.infrastructure.repository.persistence.entity.StrategyParamsEntity
import org.kentunc.trader.infrastructure.repository.persistence.entity.StrategyParamsPrimaryKey
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.insert
import org.springframework.data.r2dbc.core.select
import org.springframework.data.r2dbc.core.update
import org.springframework.data.relational.core.query.Query.query
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class StrategyParamsDao(private val template: R2dbcEntityTemplate) {

    @Transactional(readOnly = true)
    suspend fun selectOne(primaryKey: StrategyParamsPrimaryKey): StrategyParamsEntity? {
        return template.select<StrategyParamsEntity>()
            .matching(query(primaryKey.toCriteria()))
            .one()
            .awaitFirstOrNull()
    }

    @Transactional
    suspend fun insert(strategyParamsEntity: StrategyParamsEntity): Void? {
        return template.insert<StrategyParamsEntity>()
            .using(strategyParamsEntity)
            .then()
            .awaitFirstOrNull()
    }

    @Transactional
    suspend fun update(strategyParamsEntity: StrategyParamsEntity): Void? {
        return template.update<StrategyParamsEntity>()
            .matching(query(strategyParamsEntity.primaryKey.toCriteria()))
            .apply(Update.update("params", strategyParamsEntity.params))
            .then()
            .awaitFirstOrNull()
    }
}
