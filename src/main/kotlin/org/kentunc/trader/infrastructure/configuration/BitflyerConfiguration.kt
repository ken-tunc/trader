package org.kentunc.trader.infrastructure.configuration

import org.kentunc.trader.infrastructure.repository.webclient.BitflyerRealtimeClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI

@Configuration
@EnableConfigurationProperties(BitflyerConfigurationProperties::class)
class BitflyerConfiguration(private val properties: BitflyerConfigurationProperties) {

    @Bean
    fun bitflyerRealtimeClient(): BitflyerRealtimeClient {
        return BitflyerRealtimeClient(URI(properties.endpoint), properties.maxRetryNum)
    }
}
