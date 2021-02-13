package org.kentunc.trader.test

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Volume
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.time.DateTime
import java.time.LocalDateTime

internal object TestTicker {

    fun create(
        productCode: ProductCode = ProductCode.BTC_JPY,
        dateTime: LocalDateTime = LocalDateTime.now(),
        bestBid: Double = 100.0,
        bestAsk: Double = 200.0,
        volume: Double = 150.0
    ) = Ticker(
        productCode = productCode,
        dateTime = DateTime(dateTime),
        bestBid = Price(bestBid),
        bestAsk = Price(bestAsk),
        volume = Volume(volume)
    )
}
