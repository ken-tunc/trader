package org.kentunc.trader.presentation.event.listener

import kotlinx.coroutines.runBlocking
import org.kentunc.trader.application.FeedCandleInteractor
import org.kentunc.trader.domain.model.ticker.Ticker
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener

class CandleFeeder(
    private val feedCandleInteractor: FeedCandleInteractor,
    private val eventPublisher: ApplicationEventPublisher
) {

    @EventListener
    fun publishCreatedCandles(ticker: Ticker) = runBlocking {
        feedCandleInteractor.feedCandlesByTicker(ticker)
            .forEach { eventPublisher.publishEvent(it) }
    }
}
