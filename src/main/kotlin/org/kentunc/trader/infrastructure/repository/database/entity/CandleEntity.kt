package org.kentunc.trader.infrastructure.repository.database.entity

import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Volume
import org.kentunc.trader.domain.model.time.DateTime
import org.kentunc.trader.domain.model.time.Duration
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("candle")
data class CandleEntity(
    // FIXME: composite primary key actually
    @field:Id
    var productCode: ProductCode,
    val duration: Duration,
    val dateTime: LocalDateTime,
    val open: BigDecimal,
    val close: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal,
    val volume: BigDecimal
) {

    fun getPrimaryKey() = CandlePrimaryKey(
        productCode = productCode,
        duration = duration,
        dateTime = dateTime
    )

    fun toCandle() = Candle(
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
