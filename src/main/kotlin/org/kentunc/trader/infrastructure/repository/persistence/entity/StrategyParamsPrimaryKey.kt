package org.kentunc.trader.infrastructure.repository.persistence.entity

import org.springframework.data.relational.core.query.Criteria

data class StrategyParamsPrimaryKey(val name: String) {

    fun toCriteria() = Criteria.where("name").`is`(name)
}
