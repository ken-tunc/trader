package org.kentunc.trader.infrastructure.repository.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("strategy_params")
data class StrategyParamsEntity(@field:Id val name: String, val params: String) {
    val primaryKey
        get() = StrategyParamsPrimaryKey(name)
}
