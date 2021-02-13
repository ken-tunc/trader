package org.kentunc.trader.presentation.event.publisher

import kotlinx.coroutines.runBlocking
import org.kentunc.trader.application.FeedCandleService
import org.kentunc.trader.domain.model.ticker.Ticker
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener

class CreatedCandlePublisher(
    private val feedCandleService: FeedCandleService,
    private val eventPublisher: ApplicationEventPublisher
) {

    @EventListener
    fun publishCreatedCandles(ticker: Ticker) {
        runBlocking {
            feedCandleService.feedCandlesByTicker(ticker)
                .forEach { eventPublisher.publishEvent(it) }
        }
    }
}
