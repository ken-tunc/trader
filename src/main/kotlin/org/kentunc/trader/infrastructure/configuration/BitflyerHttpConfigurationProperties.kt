package org.kentunc.trader.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "external.bitflyer.http")
@ConstructorBinding
data class BitflyerHttpConfigurationProperties(
    val endpoint: String,
    val connectionTimeout: Int,
    val readTimeOut: Int,
    val accessKey: String,
    val secretKey: String
)
