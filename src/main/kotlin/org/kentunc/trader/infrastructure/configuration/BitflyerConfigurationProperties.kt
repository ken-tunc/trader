package org.kentunc.trader.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "external.bitflyer")
@ConstructorBinding
data class BitflyerConfigurationProperties(val endpoint: String, val maxRetryNum: Long = 3)
