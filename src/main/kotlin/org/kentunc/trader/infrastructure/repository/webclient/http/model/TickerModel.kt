package org.kentunc.trader.infrastructure.repository.webclient.http.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Volume
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.time.DateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class TickerModel(
    @field:JsonProperty("product_code")
    val productCode: ProductCode,
    val timestamp: String,
    @field:JsonProperty("best_bid")
    val bestBid: Double,
    @field:JsonProperty("best_ask")
    val bestAsk: Double,
    val volume: Double
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
