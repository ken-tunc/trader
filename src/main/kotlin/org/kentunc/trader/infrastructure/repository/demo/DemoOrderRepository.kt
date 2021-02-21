package org.kentunc.trader.infrastructure.repository.demo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.Order
import org.kentunc.trader.domain.model.order.OrderRepository
import org.kentunc.trader.domain.model.order.OrderRequest

class DemoOrderRepository(private val demoBroker: DemoBroker) : OrderRepository {

    override fun find(productCode: ProductCode): Flow<Order> {
        return demoBroker.getOrders(productCode)
            .asFlow()
    }

    override suspend fun send(order: OrderRequest): Void? {
        return demoBroker.sendOrder(order)
            .let { null }
    }
}
