package org.kentunc.trader.domain.model.market

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.trader.domain.model.quote.Size
import java.math.BigDecimal

internal class CommissionRateTest {

    @Test
    fun testFee() {
        val balance = Size(BigDecimal.valueOf(12345))
        val commissionRate = CommissionRate(BigDecimal.valueOf(0.0015))
        val expected = BigDecimal.valueOf(18.5175)
        val actual = commissionRate.fee(balance).toBigDecimal()
        assertEquals(expected, actual)
    }
}
