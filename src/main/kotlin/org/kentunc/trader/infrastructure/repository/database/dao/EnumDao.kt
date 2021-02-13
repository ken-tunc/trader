package org.kentunc.trader.infrastructure.repository.database.dao

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Component
class EnumDao(private val template: R2dbcEntityTemplate) {

    @Transactional
    suspend fun <E : Enum<*>> saveIfNotPresent(value: E, name: String): Void? {
        return template.databaseClient
            .sql("insert into $name values (:$name)")
            .bind(name, value.toString())
            .then()
            .onErrorResume { Mono.empty() } // ignore duplicate key error
            .awaitFirstOrNull()
    }
}
