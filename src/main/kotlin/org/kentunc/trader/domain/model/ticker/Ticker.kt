package org.kentunc.trader.domain.model.ticker

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Volume
import org.kentunc.trader.domain.model.time.DateTime

class Ticker(productCode: ProductCode, dateTime: DateTime, val bestBid: Price, val bestAsk: Price, val volume: Volume) {

    val id = TickerIdentifier(productCode, dateTime)
    val midPrice: Price = (bestBid + bestAsk) / Price(2)
}
