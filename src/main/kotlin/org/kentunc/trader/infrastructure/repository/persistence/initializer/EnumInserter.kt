package org.kentunc.trader.infrastructure.repository.persistence.initializer

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.infrastructure.repository.persistence.dao.EnumDao
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class EnumInserter(private val enumDao: EnumDao) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @Transactional
    suspend fun insertEnumsIfNotPresent() {
        ProductCode.values().forEach { insertIfNotPresent(it, "product_code") }
        Duration.values().forEach { insertIfNotPresent(it, "duration") }
    }

    private suspend fun insertIfNotPresent(enumValue: Enum<*>, name: String) {
        try {
            enumDao.insert(enumValue, name)
        } catch (ex: DataIntegrityViolationException) {
            log.info("Enum value `$enumValue` already registered in database.")
        }
    }
}
