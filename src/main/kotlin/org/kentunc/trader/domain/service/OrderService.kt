package org.kentunc.trader.domain.service

import kotlinx.coroutines.flow.toList
import org.kentunc.trader.domain.model.market.BalanceRepository
import org.kentunc.trader.domain.model.market.CommissionRateRepository
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.OrderRepository
import org.kentunc.trader.domain.model.order.OrderRequest
import org.kentunc.trader.domain.model.order.OrderSignals
import org.springframework.stereotype.Service
import java.lang.IllegalStateException

@Service
class OrderService(
    private val balanceRepository: BalanceRepository,
    private val commissionRateRepository: CommissionRateRepository,
    private val orderRepository: OrderRepository
) {

    suspend fun getSignals(productCode: ProductCode): OrderSignals {
        return orderRepository.find(productCode)
            .toList()
            .let { OrderSignals(it, productCode) }
    }

    suspend fun sendBuyAllOrder(productCode: ProductCode): Void? {
        val balance = balanceRepository.findOne(productCode.money)
            ?: throw IllegalStateException("Balance is not available. product code = $productCode")
        val request = OrderRequest.buyAll(
            productCode = productCode,
            balance = balance,
            commissionRate = commissionRateRepository.get(productCode)
        )
        return orderRepository.send(request)
    }

    suspend fun sendSellAllOrder(productCode: ProductCode): Void? {
        val balance = balanceRepository.findOne(productCode.coin)
            ?: throw IllegalStateException("Balance is not available. product code = $productCode")
        val request = OrderRequest.sellAll(productCode = productCode, balance = balance)
        return orderRepository.send(request)
    }
}
