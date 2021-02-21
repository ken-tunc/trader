package org.kentunc.trader.infrastructure.repository.demo

import org.kentunc.trader.domain.model.market.CurrencyCode
import java.math.BigDecimal

data class Currency(
    val currencyCode: CurrencyCode,
    val size: BigDecimal
)
