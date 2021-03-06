package org.kentunc.trader.infrastructure.configuration

import org.kentunc.trader.domain.model.trade.TradingStrategy
import org.kentunc.trader.infrastructure.strategy.ema.EmaStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TradingStrategyConfiguration {

    @Bean
    fun tradingStrategies(emaStrategy: EmaStrategy): List<TradingStrategy> = listOf(emaStrategy)
}
