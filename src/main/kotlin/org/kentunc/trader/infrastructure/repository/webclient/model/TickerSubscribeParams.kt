package org.kentunc.trader.infrastructure.repository.webclient.model

data class TickerSubscribeParams(
    val channel: String,
    val message: TickerMessage
)
