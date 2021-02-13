package org.kentunc.trader.presentation.event.publisher

import kotlinx.coroutines.flow.collect
import org.kentunc.trader.application.SubscribeTickerService
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.extension.forEachParallel
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener

class TickerPublisher(
    private val productCodes: List<ProductCode>,
    private val subscribeTickerService: SubscribeTickerService,
    private val eventPublisher: ApplicationEventPublisher
) {

    companion object {
        private val log = LoggerFactory.getLogger(TickerPublisher::class.java)
    }

    @EventListener(ApplicationReadyEvent::class)
    fun publishTickersParallel() {
        productCodes.forEachParallel { productCode ->
            log.info("Start subscribing ticker: {}", productCode)
            subscribeTickerService.subscribe(productCode)
                .collect { eventPublisher.publishEvent(it) }
        }
    }
}
