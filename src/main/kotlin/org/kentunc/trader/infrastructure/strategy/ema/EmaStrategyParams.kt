package org.kentunc.trader.infrastructure.strategy.ema

import org.kentunc.trader.infrastructure.strategy.StrategyParams

data class EmaStrategyParams(val shortTimeFrame: Int, val longTimeFrame: Int) : StrategyParams
