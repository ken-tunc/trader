package org.kentunc.trader.domain.model.trade

import org.kentunc.trader.domain.model.candle.CandleList
import org.kentunc.trader.domain.model.order.OrderSide

interface TradingStrategy {

    suspend fun shouldBuyOrSell(candleList: CandleList): OrderSide

    suspend fun optimize(candleList: CandleList)
}
