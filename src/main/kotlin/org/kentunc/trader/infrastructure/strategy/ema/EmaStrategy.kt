package org.kentunc.trader.infrastructure.strategy.ema

import org.kentunc.trader.infrastructure.strategy.AbstractStrategyParamsRepository
import org.kentunc.trader.infrastructure.strategy.AbstractTa4jTradingStrategy
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule

class EmaStrategy(strategyParamsRepository: AbstractStrategyParamsRepository<EmaStrategyParams>) :
    AbstractTa4jTradingStrategy<EmaStrategyParams>(strategyParamsRepository) {

    override fun buildStrategy(series: BarSeries, params: EmaStrategyParams): Strategy {
        val closePrice = ClosePriceIndicator(series)
        val shortEma = EMAIndicator(closePrice, params.shortTimeFrame)
        val longEma = EMAIndicator(closePrice, params.longTimeFrame)

        val entryRule = CrossedUpIndicatorRule(shortEma, longEma)
        val exitRule = CrossedDownIndicatorRule(shortEma, longEma)

        return BaseStrategy(entryRule, exitRule)
    }
}
