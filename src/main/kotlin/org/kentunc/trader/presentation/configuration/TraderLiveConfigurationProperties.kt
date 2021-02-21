package org.kentunc.trader.presentation.configuration

import org.kentunc.trader.domain.model.time.Duration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "trader.live")
data class TraderLiveConfigurationProperties(val tradeDuration: Duration, val maxCandleNum: Int)
