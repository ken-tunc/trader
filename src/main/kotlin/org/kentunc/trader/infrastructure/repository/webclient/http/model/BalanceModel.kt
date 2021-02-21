package org.kentunc.trader.infrastructure.repository.webclient.http.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.trader.domain.model.market.Balance
import org.kentunc.trader.domain.model.market.CurrencyCode
import org.kentunc.trader.domain.model.quote.Size
import java.math.BigDecimal

data class BalanceModel(
    @field:JsonProperty("currency_code")
    val currencyCode: String,
    val amount: BigDecimal,
    val available: BigDecimal
) {

    fun canTranslate() = try {
        CurrencyCode.valueOf(currencyCode)
        true
    } catch (ex: IllegalArgumentException) {
        false
    }

    fun toBalance() = Balance(
        currencyCode = CurrencyCode.valueOf(currencyCode),
        amount = Size(amount),
        available = Size(available)
    )
}
