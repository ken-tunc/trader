package org.kentunc.trader.application

import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.domain.service.CandleService
import org.springframework.stereotype.Service

@Service
class FeedCandleService(private val candleService: CandleService) {

    /**
     * Returns a list of newly create candles.
     */
    suspend fun feedCandlesByTicker(ticker: Ticker): List<Candle> {
        return Duration.values().toList()
            .map { candleService.feed(ticker, it) }
            .filter { (_, created) -> created }
            .map { (candle, _) -> candle }
    }
}
