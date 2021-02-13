package org.kentunc.trader.infrastructure.repository.database.initializer

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.infrastructure.repository.database.dao.EnumDao
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class EnumInserter(private val enumDao: EnumDao) {

    @Transactional
    suspend fun insertEnumsIfNotPresent() {
        ProductCode.values().forEach { enumDao.saveIfNotPresent(it, "product_code") }
        Duration.values().forEach { enumDao.saveIfNotPresent(it, "duration") }
    }
}
