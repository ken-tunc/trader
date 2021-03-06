package org.kentunc.trader.domain.model.market

import org.kentunc.trader.domain.model.quote.Size

data class Balance(val currencyCode: CurrencyCode, val amount: Size, val available: Size)
