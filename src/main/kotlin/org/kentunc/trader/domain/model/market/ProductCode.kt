package org.kentunc.trader.domain.model.market

enum class ProductCode(val coin: CurrencyCode, val money: CurrencyCode) {
    BTC_JPY(CurrencyCode.BTC, CurrencyCode.JPY),
    ETH_JPY(CurrencyCode.ETH, CurrencyCode.JPY);
}
