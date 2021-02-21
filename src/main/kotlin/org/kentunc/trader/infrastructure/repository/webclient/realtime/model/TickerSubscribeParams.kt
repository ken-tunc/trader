package org.kentunc.trader.infrastructure.repository.webclient.realtime.model

data class TickerSubscribeParams(
    val channel: String,
    val message: TickerMessage
)
