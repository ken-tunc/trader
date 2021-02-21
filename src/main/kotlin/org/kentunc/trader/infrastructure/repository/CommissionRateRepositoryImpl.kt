package org.kentunc.trader.infrastructure.repository

import org.kentunc.trader.domain.model.market.CommissionRate
import org.kentunc.trader.domain.model.market.CommissionRateRepository
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.infrastructure.repository.webclient.http.BitflyerHttpPrivateApiClient
import org.springframework.stereotype.Repository

@Repository
class CommissionRateRepositoryImpl(
    private val bitflyerHttpPrivateApiClient: BitflyerHttpPrivateApiClient
) : CommissionRateRepository {

    override suspend fun get(productCode: ProductCode): CommissionRate {
        return bitflyerHttpPrivateApiClient.getCommissionRate(productCode)
            .toCommissionRate()
    }
}
