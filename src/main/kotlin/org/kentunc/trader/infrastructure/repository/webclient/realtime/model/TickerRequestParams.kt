package org.kentunc.trader.infrastructure.repository.webclient.realtime.model

import org.kentunc.trader.domain.model.market.ProductCode

class TickerRequestParams(productCode: ProductCode) {

    val channel = "lightning_ticker_$productCode"
}
