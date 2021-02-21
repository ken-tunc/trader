package org.kentunc.trader.presentation.event.listener

import kotlinx.coroutines.runBlocking
import org.kentunc.trader.application.LossCutInteractor
import org.kentunc.trader.domain.model.ticker.Ticker
import org.springframework.context.event.EventListener

class LossCutMonitor(private val lossCutInteractor: LossCutInteractor) {

    @EventListener
    fun monitorForLossCut(ticker: Ticker) = runBlocking {
        lossCutInteractor.lossCutIfStopLimitExceeded(ticker)
    }
}
