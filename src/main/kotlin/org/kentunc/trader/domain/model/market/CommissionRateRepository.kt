package org.kentunc.trader.domain.model.market

interface CommissionRateRepository {

    suspend fun get(productCode: ProductCode): CommissionRate
}
