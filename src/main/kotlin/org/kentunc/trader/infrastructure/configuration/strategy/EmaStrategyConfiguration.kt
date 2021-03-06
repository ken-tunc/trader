package org.kentunc.trader.infrastructure.configuration.strategy

import org.kentunc.trader.infrastructure.repository.persistence.dao.StrategyParamsDao
import org.kentunc.trader.infrastructure.strategy.AbstractStrategyParamsRepository
import org.kentunc.trader.infrastructure.strategy.ema.EmaStrategy
import org.kentunc.trader.infrastructure.strategy.ema.EmaStrategyParams
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(EmaStrategyConfigurationProperties::class)
class EmaStrategyConfiguration(
    private val properties: EmaStrategyConfigurationProperties,
    private val strategyParamsDao: StrategyParamsDao
) {

    @Bean
    fun emaStrategy(): EmaStrategy = EmaStrategy(emaStrategyParamsRepository())

    fun emaStrategyParamsRepository() =
        object : AbstractStrategyParamsRepository<EmaStrategyParams>(EmaStrategyParams::class, strategyParamsDao) {
            override fun getDefaultValue(): EmaStrategyParams {
                return EmaStrategyParams(
                    shortTimeFrame = properties.initialShortTimeFrame,
                    longTimeFrame = properties.initialLongTimeFrame
                )
            }

            override fun getParamsListForOptimization(): List<EmaStrategyParams> {
                return properties.shortTimeFrameRange.zip(properties.longTimeFrameRange)
                    .filter { (shortTimeFrame, longTimeFrame) -> shortTimeFrame < longTimeFrame }
                    .map { (shortTimeFrame, longTimeFrame) -> EmaStrategyParams(shortTimeFrame, longTimeFrame) }
            }
        }
}
