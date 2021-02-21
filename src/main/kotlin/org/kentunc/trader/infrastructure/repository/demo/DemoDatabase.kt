package org.kentunc.trader.infrastructure.repository.demo

import org.kentunc.trader.domain.model.market.CurrencyCode
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.Order
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class DemoDatabase(initialBalances: Map<CurrencyCode, BigDecimal>) {

    private val balanceMap = ConcurrentHashMap<CurrencyCode, BigDecimal>()
    private val orderSignals = CopyOnWriteArrayList<Order>()

    init {
        initialBalances.forEach { balanceMap[it.key] = it.value }
    }

    fun getBalances() = balanceMap.toMap()

    fun canExchange(from: Currency, into: Currency): Boolean {
        return balanceMap[from.currencyCode]?.let {
            it >= from.size && balanceMap.containsKey(into.currencyCode)
        } ?: false
    }

    fun exchange(from: Currency, into: Currency) {
        check(canExchange(from, into)) {
            "Cannot exchange currency, from=$from, into=$into"
        }
        balanceMap[from.currencyCode] = balanceMap[from.currencyCode]!!.minus(from.size)
        balanceMap[into.currencyCode] = balanceMap[into.currencyCode]!!.plus(into.size)
    }

    fun getOrders(productCode: ProductCode): List<Order> = orderSignals.filter { it.productCode == productCode }

    fun addOrder(order: Order) {
        orderSignals.add(order)
    }
}
