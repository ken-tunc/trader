package org.kentunc.trader.infrastructure.repository.webclient.http

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.infrastructure.repository.webclient.http.model.BalanceModel
import org.kentunc.trader.infrastructure.repository.webclient.http.model.CommissionRateModel
import org.kentunc.trader.infrastructure.repository.webclient.http.model.OrderModel
import org.kentunc.trader.infrastructure.repository.webclient.http.model.OrderRequestModel
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.client.exchangeToFlow

class BitflyerHttpPrivateApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_BALANCE = "/me/getbalance"
        private const val PATH_ORDERS = "/me/getchildorders"
        private const val PATH_SEND_ORDER = "/me/sendchildorder"
        private const val PATH_COMMISSION_RATE = "/me/gettradingcommission"
    }

    fun getBalances(): Flow<BalanceModel> = webClient.get()
        .uri(PATH_BALANCE)
        .exchangeToFlow { it.bodyToFlow() }

    fun getOrders(productCode: ProductCode): Flow<OrderModel> = webClient.get()
        .uri {
            it.path(PATH_ORDERS)
                .queryParam("product_code", productCode.toString())
                .build()
        }
        .exchangeToFlow { it.bodyToFlow() }

    suspend fun sendOrder(orderRequest: OrderRequestModel): Void? = webClient.post()
        .uri(PATH_SEND_ORDER)
        .bodyValue(orderRequest)
        .exchangeToMono { it.releaseBody() }
        .awaitFirstOrNull()

    suspend fun getCommissionRate(productCode: ProductCode): CommissionRateModel = webClient.get()
        .uri {
            it.path(PATH_COMMISSION_RATE)
                .queryParam("product_code", productCode.toString())
                .build()
        }
        .exchangeToMono { it.bodyToMono<CommissionRateModel>() }
        .awaitFirst()
}
