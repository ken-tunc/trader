package org.kentunc.trader.infrastructure.repository.database.entity

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.time.Duration
import org.springframework.data.relational.core.query.Criteria
import java.time.LocalDateTime

data class CandlePrimaryKey(
    val productCode: ProductCode,
    val duration: Duration,
    val dateTime: LocalDateTime,
) {

    fun toCriteria() = Criteria.where("product_code").`is`(productCode)
        .and("duration").`is`(duration)
        .and("date_time").`is`(dateTime)
}
