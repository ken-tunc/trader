package org.kentunc.trader.domain.service

import org.kentunc.trader.domain.model.candle.CandleList
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.trade.TradingStrategy
import org.kentunc.trader.extension.forEachParallel
import org.springframework.stereotype.Service

@Service
class TradeService(private val strategies: List<TradingStrategy>) {

    suspend fun shouldBuyOrSell(candleList: CandleList): OrderSide {
        return strategies.map { it.shouldBuyOrSell(candleList) }
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }?.key ?: OrderSide.NEUTRAL
    }

    suspend fun optimize(candleList: CandleList) {
        strategies.forEachParallel { it.optimize(candleList) }
    }
}
