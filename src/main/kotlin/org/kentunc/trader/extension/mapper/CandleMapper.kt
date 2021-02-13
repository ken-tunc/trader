package org.kentunc.trader.extension

import org.kentunc.trader.domain.model.candle.Candle
import org.kentunc.trader.domain.model.candle.CandleIdentifier
import org.kentunc.trader.infrastructure.repository.database.entity.CandleEntity
import org.kentunc.trader.infrastructure.repository.database.entity.CandlePrimaryKey

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

fun CandleIdentifier.toCandlePrimaryKey() = CandlePrimaryKey(
    productCode = productCode,
    duration = duration,
    dateTime = dateTime.toLocalDateTime()
)
