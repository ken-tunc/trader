package org.kentunc.trader.presentation.event.listener

import kotlinx.coroutines.runBlocking
import org.kentunc.trader.application.TradeInteractor
import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.time.Duration
import org.springframework.context.event.EventListener

class LiveTrader(
    private val tradeDuration: Duration,
    private val maxCandleNum: Int,
    private val tradeInteractor: TradeInteractor,
) {

    @EventListener
    fun trade(candle: Candle) = runBlocking {
        if (candle.id.duration == tradeDuration) {
            tradeInteractor.trade(candle.id.productCode, candle.id.duration, maxCandleNum)
        }
    }
}
