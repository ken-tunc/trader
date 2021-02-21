package org.kentunc.trader.infrastructure.configuration.strategy

import org.kentunc.trader.infrastructure.strategy.EmaStrategy
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(EmaStrategyConfigurationProperties::class)
class EmaStrategyConfiguration(private val properties: EmaStrategyConfigurationProperties) {

    @Bean
    fun emaStrategy(): EmaStrategy = EmaStrategy(
        shortTimeFrame = properties.initialShortTimeFrame,
        longTimeFrame = properties.initialLongTimeFrame,
        shortTimeFrameRange = properties.shortTimeFrameRange,
        longTimeFrameRange = properties.longTimeFrameRange
    )
}
