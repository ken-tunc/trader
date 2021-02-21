package org.kentunc.trader.domain.model.candle

class CandleList(candleList: List<Candle>) {

    private val sortedCandles = candleList.sortedBy { it.id.dateTime.toLocalDateTime() }

    val size: Int
        get() = sortedCandles.size
    val isEmpty: Boolean
        get() = sortedCandles.isEmpty()

    fun latestOrNull(): Candle? = sortedCandles.lastOrNull()

    fun toList() = sortedCandles.toList()
}
