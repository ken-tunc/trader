package org.kentunc.trader.infrastructure.repository.webclient.http.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.trader.domain.model.market.CommissionRate
import java.math.BigDecimal

data class CommissionRateModel(@field:JsonProperty("commission_rate") val commissionRate: BigDecimal) {

    fun toCommissionRate() = CommissionRate(commissionRate)
}
