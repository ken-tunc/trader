package org.kentunc.trader.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.order.Order
import org.kentunc.trader.domain.model.order.OrderRepository
import org.kentunc.trader.domain.model.order.OrderRequest
import org.kentunc.trader.extension.mapper.toOrderRequestModel
import org.kentunc.trader.infrastructure.repository.webclient.http.BitflyerHttpPrivateApiClient
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(private val bitflyerClient: BitflyerHttpPrivateApiClient) : OrderRepository {

    override fun find(productCode: ProductCode): Flow<Order> {
        return bitflyerClient.getOrders(productCode)
            .map { it.toOrder() }
    }

    override suspend fun send(order: OrderRequest): Void? {
        return bitflyerClient.sendOrder(order.toOrderRequestModel())
    }
}
