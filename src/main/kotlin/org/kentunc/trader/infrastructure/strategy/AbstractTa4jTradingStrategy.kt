package org.kentunc.trader.infrastructure.strategy

import org.kentunc.trader.domain.model.candle.CandleList
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.trade.TradingStrategy
import org.kentunc.trader.extension.toBarSeries
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.Strategy
import org.ta4j.core.analysis.criteria.TotalProfitCriterion

abstract class AbstractTa4jTradingStrategy<T : StrategyParams>(
    private val strategyParamsRepository: AbstractStrategyParamsRepository<T>
) : TradingStrategy {

    override suspend fun shouldBuyOrSell(candleList: CandleList): OrderSide {
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

    private suspend fun buildStrategy(series: BarSeries): Strategy {
        val params = strategyParamsRepository.getParams()
        return buildStrategy(series, params)
    }

    protected abstract fun buildStrategy(series: BarSeries, params: T): Strategy

    override suspend fun optimize(candleList: CandleList) {
        val series = candleList.toBarSeries()
        strategyParamsRepository.getParamsListForOptimization()
            .map {
                val strategy = buildStrategy(series, it)
                val tradingRecord = BarSeriesManager(series).run(strategy)
                val profit = TotalProfitCriterion().calculate(series, tradingRecord)
                it to profit
            }
            .maxByOrNull { it.second }
            ?.also { strategyParamsRepository.saveParams(it.first) }
    }
}
