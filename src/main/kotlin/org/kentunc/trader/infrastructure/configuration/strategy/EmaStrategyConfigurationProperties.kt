package org.kentunc.trader.infrastructure.configuration.strategy

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "trader.strategy.ema")
data class EmaStrategyConfigurationProperties(
    val initialShortTimeFrame: Int,
    val initialLongTimeFrame: Int,
    private val shortTimeFrameFrom: Int,
    private val shortTimeFrameTo: Int,
    private val longTimeFrameFrom: Int,
    private val longTimeFrameTo: Int
) {

    val shortTimeFrameRange
        get() = IntRange(shortTimeFrameFrom, shortTimeFrameTo)
    val longTimeFrameRange
        get() = IntRange(longTimeFrameFrom, longTimeFrameTo)
}
