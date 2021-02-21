package org.kentunc.trader.infrastructure.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import org.kentunc.trader.infrastructure.repository.webclient.http.BitflyerHttpPrivateApiClient
import org.kentunc.trader.infrastructure.repository.webclient.http.BitflyerHttpPublicApiClient
import org.kentunc.trader.infrastructure.repository.webclient.http.connector.BitflyerSigner
import org.kentunc.trader.infrastructure.repository.webclient.http.connector.BodyProvidingJsonEncoder
import org.kentunc.trader.infrastructure.repository.webclient.http.connector.MessageSigningHttpConnector
import org.kentunc.trader.infrastructure.repository.webclient.realtime.BitflyerRealtimeClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.web.reactive.function.client.ExchangeFunctions
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.net.URI

@Configuration
@EnableConfigurationProperties(
    value = [BitflyerRealtimeConfigurationProperties::class, BitflyerHttpConfigurationProperties::class]
)
class BitflyerConfiguration(
    private val realtimeProperties: BitflyerRealtimeConfigurationProperties,
    private val httpProperties: BitflyerHttpConfigurationProperties
) {

    @Bean
    fun bitflyerRealtimeClient(): BitflyerRealtimeClient {
        return BitflyerRealtimeClient(URI(realtimeProperties.endpoint), realtimeProperties.maxRetryNum)
    }

    @Bean
    fun bitflyerPublicApiClient(): BitflyerHttpPublicApiClient {
        val connector = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, httpProperties.connectionTimeout)
            .doOnConnected { it.addHandler(ReadTimeoutHandler(httpProperties.readTimeOut)) }
            .let { ReactorClientHttpConnector(it) }

        return WebClient.builder()
            .baseUrl(realtimeProperties.endpoint)
            .clientConnector(connector)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
            .let { BitflyerHttpPublicApiClient(it) }
    }

    @Bean
    fun bitflyerPrivateApiClient(objectMapper: ObjectMapper): BitflyerHttpPrivateApiClient {
        // NOTE: Enable hmac base authentication.
        // ref. https://andrew-flower.com/blog/Custom-HMAC-Auth-with-Spring-WebClient
        val signer = BitflyerSigner(httpProperties.accessKey, httpProperties.secretKey)
        val connector = MessageSigningHttpConnector(signer)
        val jsonEncoder = BodyProvidingJsonEncoder(connector::signWithBody)
        val jsonDecoder = Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON)

        return WebClient.builder()
            .baseUrl(httpProperties.endpoint)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchangeFunction(
                ExchangeFunctions.create(
                    connector,
                    ExchangeStrategies.builder()
                        .codecs {
                            it.defaultCodecs().jackson2JsonEncoder(jsonEncoder)
                            it.defaultCodecs().jackson2JsonDecoder(jsonDecoder)
                        }
                        .build()
                )
            )
            .build()
            .let { BitflyerHttpPrivateApiClient(it) }
    }
}
