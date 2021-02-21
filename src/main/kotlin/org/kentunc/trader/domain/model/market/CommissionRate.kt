package org.kentunc.trader.domain.model.market

import org.kentunc.trader.domain.model.quote.Size
import java.math.BigDecimal

data class CommissionRate(private val rate: BigDecimal) {

    fun fee(size: Size): Size {
        return Size(size.toBigDecimal() * rate)
    }
}
