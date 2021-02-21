package org.kentunc.trader.infrastructure.configuration

import org.kentunc.trader.domain.model.market.CurrencyCode
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.math.BigDecimal

@ConfigurationProperties(prefix = "trader.demo")
@ConstructorBinding
class DemoConfigurationProperties(val balances: List<InitialBalance>, val commissionRate: BigDecimal) {

    class InitialBalance(
        val currencyCode: CurrencyCode,
        val amount: Double
    )
}
