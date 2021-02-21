package org.kentunc.trader.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "external.bitflyer.realtime")
@ConstructorBinding
data class BitflyerRealtimeConfigurationProperties(
    val endpoint: String,
    val maxRetryNum: Long
)
