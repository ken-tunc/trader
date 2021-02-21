package org.kentunc.trader.infrastructure.strategy

import org.kentunc.trader.domain.model.candle.CandleList
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.trade.TradingStrategy
import org.kentunc.trader.extension.toBarSeries
import org.ta4j.core.BarSeries
import org.ta4j.core.Strategy

interface Ta4jTradingStrategy : TradingStrategy {

    override fun shouldBuyOrSell(candleList: CandleList): OrderSide {
        val barSeries = candleList.toBarSeries()
        val strategy = buildStrategy(barSeries)
        val endIndex = barSeries.endIndex
        if (strategy.shouldEnter(endIndex)) {
            return OrderSide.BUY
        }
        if (strategy.shouldExit(endIndex)) {
            return OrderSide.SELL
        }
        return OrderSide.NEUTRAL
    }

    fun buildStrategy(series: BarSeries): Strategy
}
