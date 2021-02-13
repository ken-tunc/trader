package org.kentunc.trader.domain.model.candle

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Volume
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.time.DateTime
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.domain.model.time.TruncatedDateTime

class Candle(
    productCode: ProductCode,
    duration: Duration,
    dateTime: DateTime,
    val open: Price,
    val close: Price,
    val high: Price,
    val low: Price,
    val volume: Volume
) {

    val id = CandleIdentifier(productCode, duration, TruncatedDateTime(dateTime, duration))

    init {
        require(low <= high) { "`high` is less than `low`, but high=$high, low=$low" }
    }

    constructor(ticker: Ticker, duration: Duration) : this(
        productCode = ticker.id.productCode,
        duration = duration,
        dateTime = ticker.id.dateTime,
        open = ticker.midPrice,
        close = ticker.midPrice,
        high = ticker.midPrice,
        low = ticker.midPrice,
        volume = ticker.volume
    )

    fun extendWith(ticker: Ticker): Candle {
        require(this.id.productCode == ticker.id.productCode) {
            "product code mismatch: this=${this.id.productCode}, ticker=${ticker.id.productCode}"
        }
        require(this.id.dateTime == TruncatedDateTime(ticker.id.dateTime, this.id.duration)) {
            "datetime mismatch: this=${this.id.dateTime}, ticker=${ticker.id.dateTime}, " +
                "duration=${this.id.duration}"
        }

        val price = ticker.midPrice
        return Candle(
            productCode = this.id.productCode,
            duration = this.id.duration,
            dateTime = ticker.id.dateTime,
            open = this.open,
            close = price,
            high = maxOf(this.high, price),
            low = minOf(this.low, price),
            volume = this.volume + ticker.volume
        )
    }
}
