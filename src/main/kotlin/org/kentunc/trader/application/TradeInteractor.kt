package org.kentunc.trader.application

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.OrderSide
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.domain.service.CandleService
import org.kentunc.trader.domain.service.OrderService
import org.kentunc.trader.domain.service.TradeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TradeInteractor(
    private val candleService: CandleService,
    private val orderService: OrderService,
    private val tradeService: TradeService
) {

    @Transactional
    suspend fun trade(productCode: ProductCode, duration: Duration, maxCandleNum: Int) {
        val candleList = candleService.getLatest(productCode, duration, maxCandleNum)
        val orderSignals = orderService.getSignals(productCode)
        when (tradeService.shouldBuyOrSell(candleList)) {
            OrderSide.BUY -> if (orderSignals.canBuy()) orderService.sendBuyAllOrder(productCode) else Unit
            OrderSide.SELL -> if (orderSignals.canSell()) orderService.sendSellAllOrder(productCode) else Unit
            OrderSide.NEUTRAL -> Unit
        }

        tradeService.optimize(candleList)
    }
}
