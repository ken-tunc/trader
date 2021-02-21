package org.kentunc.trader.extension

import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.candle.CandleIdentifier
import org.kentunc.trader.domain.model.candle.CandleList
import org.kentunc.trader.infrastructure.repository.persistence.entity.CandleEntity
import org.kentunc.trader.infrastructure.repository.persistence.entity.CandlePrimaryKey
import org.ta4j.core.Bar
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseBar
import org.ta4j.core.BaseBarSeries

fun Candle.toCandleEntity() = CandleEntity(
    productCode = id.productCode,
    duration = id.duration,
    dateTime = id.dateTime.toLocalDateTime(),
    open = open.toBigDecimal(),
    close = close.toBigDecimal(),
    high = high.toBigDecimal(),
    low = low.toBigDecimal(),
    volume = volume.toBigDecimal()
)

fun Candle.toBar(): Bar = BaseBar(
    id.duration.unit.duration,
    id.dateTime.toZonedDateTime(),
    open.toBigDecimal(),
    high.toBigDecimal(),
    low.toBigDecimal(),
    close.toBigDecimal(),
    volume.toBigDecimal()
)

fun CandleIdentifier.toCandlePrimaryKey() = CandlePrimaryKey(
    productCode = productCode,
    duration = duration,
    dateTime = dateTime.toLocalDateTime()
)

fun CandleList.toBarSeries(): BarSeries {
    val barSeries = BaseBarSeries()
    this.toList().forEach {
        barSeries.addBar(it.toBar())
    }
    return barSeries
}
