package org.kentunc.trader.infrastructure.repository.demo

import org.kentunc.trader.domain.model.market.CommissionRate
import org.kentunc.trader.domain.model.market.CommissionRateRepository
import org.kentunc.trader.domain.model.market.ProductCode
import java.math.BigDecimal

class DemoCommissionRateRepository(commissionRate: BigDecimal) : CommissionRateRepository {

    private val fixedCommissionRate = CommissionRate(commissionRate)

    override suspend fun get(productCode: ProductCode): CommissionRate = fixedCommissionRate
}
