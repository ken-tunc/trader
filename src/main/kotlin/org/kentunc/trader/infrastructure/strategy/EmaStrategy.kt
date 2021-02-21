package org.kentunc.trader.infrastructure.strategy

import org.kentunc.trader.domain.model.candle.CandleList
import org.kentunc.trader.extension.toBarSeries
import org.slf4j.LoggerFactory
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Strategy
import org.ta4j.core.analysis.criteria.TotalProfitCriterion
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.num.PrecisionNum
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule

class EmaStrategy(
    private var shortTimeFrame: Int,
    private var longTimeFrame: Int,
    private val shortTimeFrameRange: IntRange,
    private val longTimeFrameRange: IntRange
) : Ta4jTradingStrategy {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun buildStrategy(series: BarSeries): Strategy {
        return buildStrategy(series, shortTimeFrame, longTimeFrame)
    }

    private fun buildStrategy(series: BarSeries, shortTimeFrame: Int, longTimeFrame: Int): Strategy {
        val closePrice = ClosePriceIndicator(series)
        val shortEma = EMAIndicator(closePrice, shortTimeFrame)
        val longEma = EMAIndicator(closePrice, longTimeFrame)

        val entryRule = CrossedUpIndicatorRule(shortEma, longEma)
        val exitRule = CrossedDownIndicatorRule(shortEma, longEma)

        return BaseStrategy(entryRule, exitRule)
    }

    override fun optimize(candleList: CandleList) {
        val series = candleList.toBarSeries()
        var bestProfit = PrecisionNum.valueOf(Double.MIN_VALUE)

        for (currentShortTimeFrame in shortTimeFrameRange) {
            for (currentLongTimeFrame in longTimeFrameRange) {
                if (currentShortTimeFrame > currentLongTimeFrame) {
                    continue
                }

                val seriesManager = BarSeriesManager(series)
                val strategy = buildStrategy(series, currentShortTimeFrame, currentLongTimeFrame)
                val tradingRecord = seriesManager.run(strategy)

                val profit = TotalProfitCriterion().calculate(series, tradingRecord)
                if (profit.isGreaterThan(bestProfit)) {
                    this.shortTimeFrame = currentShortTimeFrame
                    this.longTimeFrame = currentLongTimeFrame
                    bestProfit = profit as PrecisionNum?
                }
            }
        }
        log.info("EMA strategy parameters updated: shortTimeFrame={}, longTimeFrame={}", shortTimeFrame, longTimeFrame)
    }
}
