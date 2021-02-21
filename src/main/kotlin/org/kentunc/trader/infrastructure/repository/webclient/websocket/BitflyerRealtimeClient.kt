package org.kentunc.trader.infrastructure.repository.webclient.websocket

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.infrastructure.repository.webclient.websocket.model.JsonRPC2Request
import org.kentunc.trader.infrastructure.repository.webclient.websocket.model.TickerMessage
import org.kentunc.trader.infrastructure.repository.webclient.websocket.model.TickerRequestParams
import org.kentunc.trader.infrastructure.repository.webclient.websocket.model.TickerSubscribeParams
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.net.URI

class BitflyerRealtimeClient(
    private val endpoint: URI,
    private val maxRetryNum: Long
) {

    companion object {
        private const val SUBSCRIBE_METHOD = "subscribe"
        private val objectMapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        private val webSocketClient = ReactorNettyWebSocketClient()
    }

    fun stream(productCode: ProductCode): Flow<TickerMessage> {
        val buffer = Sinks.many().multicast().onBackpressureBuffer<TickerMessage>()

        val sessionMono = webSocketClient.execute(endpoint) { session ->
            val request = JsonRPC2Request(method = SUBSCRIBE_METHOD, params = TickerRequestParams(productCode))
            val requestMessage = Mono.fromCallable {
                session.textMessage(objectMapper.writeValueAsString(request))
            }

            session.send(requestMessage)
                .thenMany(
                    session.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .map { objectMapper.readValue<JsonRPC2Request<TickerSubscribeParams>>(it).params.message }
                        .doOnNext { buffer.tryEmitNext(it) }
                        .then()
                )
                .then()
        }.retry(maxRetryNum)

        return buffer.asFlux()
            .doOnSubscribe { sessionMono.subscribe() }
            .asFlow()
    }
}
