package org.kentunc.trader.infrastructure.repository.persistence.initializer

import kotlinx.coroutines.runBlocking
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(private val enumInserter: EnumInserter) {

    @EventListener(ContextRefreshedEvent::class)
    fun initialize() = runBlocking {
        enumInserter.insertEnumsIfNotPresent()
    }
}
