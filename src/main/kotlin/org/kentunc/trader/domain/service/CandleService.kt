package org.kentunc.trader.domain.service

import kotlinx.coroutines.flow.toList
import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.candle.CandleCreated
import org.kentunc.trader.domain.model.candle.CandleList
import org.kentunc.trader.domain.model.candle.CandleRepository
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.time.Duration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CandleService(private val candleRepository: CandleRepository) {

    @Transactional
    suspend fun feed(ticker: Ticker, duration: Duration): Pair<Candle, CandleCreated> {
        val candle = Candle(ticker, duration)
        return when (val updated = candleRepository.find(candle.id)?.extendWith(ticker)) {
            null -> candleRepository.save(candle).let { candle to true }
            else -> candleRepository.update(updated).let { updated to false }
        }
    }

    suspend fun getLatest(productCode: ProductCode, duration: Duration, maxNum: Int?): CandleList {
        return candleRepository.findLatest(productCode, duration, maxNum)
            .toList()
            .let { CandleList(it) }
    }
}
