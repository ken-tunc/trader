package org.kentunc.trader.domain.model.ticker

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.time.DateTime

data class TickerIdentifier(val productCode: ProductCode, val dateTime: DateTime)
