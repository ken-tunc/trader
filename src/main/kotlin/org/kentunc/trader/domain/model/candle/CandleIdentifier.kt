package org.kentunc.trader.domain.model.candle

import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.time.Duration
import org.kentunc.trader.domain.model.time.TruncatedDateTime

data class CandleIdentifier(val productCode: ProductCode, val duration: Duration, val dateTime: TruncatedDateTime)
