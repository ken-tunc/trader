package org.kentunc.trader.infrastructure.repository.webclient.websocket.model

data class TickerSubscribeParams(
    val channel: String,
    val message: TickerMessage
)
