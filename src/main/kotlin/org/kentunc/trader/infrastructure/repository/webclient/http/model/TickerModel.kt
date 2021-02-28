package org.kentunc.trader.infrastructure.repository.webclient.http.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Volume
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.time.DateTime
import java.time.LocalDateTime

data class TickerModel(
    @field:JsonProperty("product_code")
    val productCode: ProductCode,
    val timestamp: LocalDateTime,
    @field:JsonProperty("best_bid")
    val bestBid: Double,
    @field:JsonProperty("best_ask")
    val bestAsk: Double,
    val volume: Double
) {

    fun toTicker(): Ticker {
        return Ticker(
            productCode = productCode,
            dateTime = DateTime(timestamp),
            bestBid = Price(bestBid),
            bestAsk = Price(bestAsk),
            volume = Volume(volume)
        )
    }
}
