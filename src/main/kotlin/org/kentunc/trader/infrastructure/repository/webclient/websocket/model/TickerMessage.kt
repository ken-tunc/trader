package org.kentunc.trader.infrastructure.repository.webclient.websocket.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Volume
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.time.DateTime
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class TickerMessage(
    @field:JsonProperty("product_code")
    val productCode: ProductCode,
    val timestamp: String,
    @field:JsonProperty("best_bid")
    val bestBid: BigDecimal,
    @field:JsonProperty("best_ask")
    val bestAsk: BigDecimal,
    val volume: BigDecimal
) {

    fun toTicker(): Ticker {
        val localDateTime = LocalDateTime.ofInstant(Instant.parse(timestamp), ZoneId.systemDefault())
        return Ticker(
            productCode = productCode,
            dateTime = DateTime(localDateTime),
            bestBid = Price(bestBid),
            bestAsk = Price(bestAsk),
            volume = Volume(volume)
        )
    }
}
