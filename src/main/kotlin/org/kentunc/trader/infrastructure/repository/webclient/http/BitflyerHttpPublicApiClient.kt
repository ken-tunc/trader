package org.kentunc.trader.infrastructure.repository.webclient.http

import kotlinx.coroutines.reactive.awaitFirst
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.infrastructure.repository.webclient.http.model.TickerModel
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class BitflyerHttpPublicApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_TICKER = "/ticker"
    }

    suspend fun getTicker(productCode: ProductCode): TickerModel {
        return webClient.get()
            .uri {
                it.path(PATH_TICKER)
                    .queryParam("product_code", productCode)
                    .build()
            }
            .exchangeToMono { it.bodyToMono<TickerModel>() }
            .awaitFirst()
    }
}
