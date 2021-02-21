package org.kentunc.trader.presentation.event.listener

import kotlinx.coroutines.flow.collect
import org.kentunc.trader.application.SubscribeTickerInteractor
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.extension.forEachParallel
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener

class TickerSubscriber(
    private val productCodes: List<ProductCode>,
    private val subscribeTickerInteractor: SubscribeTickerInteractor,
    private val eventPublisher: ApplicationEventPublisher
) {

    companion object {
        private val log = LoggerFactory.getLogger(TickerSubscriber::class.java)
    }

    @EventListener(ApplicationReadyEvent::class)
    fun publishTickersParallel() {
        productCodes.forEachParallel { productCode ->
            log.info("Start subscribing ticker: {}", productCode)
            subscribeTickerInteractor.subscribe(productCode)
                .collect { eventPublisher.publishEvent(it) }
        }
    }
}
