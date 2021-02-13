package org.kentunc.trader.domain.service

import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.candle.CandleCreated
import org.kentunc.trader.domain.model.candle.CandleRepository
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.time.Duration
import org.springframework.stereotype.Service

@Service
class CandleService(private val candleRepository: CandleRepository) {

    suspend fun feed(ticker: Ticker, duration: Duration): Pair<Candle, CandleCreated> {
        val candle = Candle(ticker, duration)
        return when (val updated = candleRepository.find(candle.id)?.extendWith(ticker)) {
            null -> candleRepository.save(candle).let { candle to true }
            else -> candleRepository.update(updated).let { updated to false }
        }
    }
}
