package org.kentunc.trader.test.model

import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Volume
import org.kentunc.trader.domain.model.time.DateTime
import org.kentunc.trader.domain.model.time.Duration
import java.time.LocalDateTime

internal object TestCandle {

    fun create(
        productCode: ProductCode = ProductCode.BTC_JPY,
        duration: Duration = Duration.DAYS,
        dateTime: LocalDateTime = LocalDateTime.now(),
        open: Double = 100.0,
        close: Double = 120.0,
        high: Double = 130.0,
        low: Double = 90.0,
        volume: Double = 100.0
    ) = Candle(
        productCode = productCode,
        duration = duration,
        dateTime = DateTime(dateTime),
        open = Price(open),
        close = Price(close),
        high = Price(high),
        low = Price(low),
        volume = Volume(volume)
    )
}
