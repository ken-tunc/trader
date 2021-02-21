package org.kentunc.trader.domain.model.order

import kotlinx.coroutines.flow.Flow
import org.kentunc.trader.domain.model.market.ProductCode

interface OrderRepository {

    fun find(productCode: ProductCode): Flow<Order>
    suspend fun send(order: OrderRequest): Void?
}
