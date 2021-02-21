package org.kentunc.trader.infrastructure.configuration

import org.kentunc.trader.infrastructure.repository.demo.DemoBalanceRepository
import org.kentunc.trader.infrastructure.repository.demo.DemoBroker
import org.kentunc.trader.infrastructure.repository.demo.DemoCommissionRateRepository
import org.kentunc.trader.infrastructure.repository.demo.DemoDatabase
import org.kentunc.trader.infrastructure.repository.demo.DemoOrderRepository
import org.kentunc.trader.infrastructure.repository.webclient.http.BitflyerHttpPublicApiClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.math.BigDecimal

@Profile("demo")
@Configuration
@EnableConfigurationProperties(DemoConfigurationProperties::class)
class DemoConfiguration(private val properties: DemoConfigurationProperties) {

    @Bean
    fun demoDatabase(): DemoDatabase {
        val initialBalances = properties.balances
            .map { it.currencyCode to BigDecimal.valueOf(it.amount) }
            .toMap()
        return DemoDatabase(initialBalances)
    }

    @Bean
    fun demoBroker(
        bitflyerHttpPublicApiClient: BitflyerHttpPublicApiClient
    ) = DemoBroker(demoDatabase(), bitflyerHttpPublicApiClient, demoCommissionRateRepository())

    @Bean
    @Primary
    fun demoBalanceRepository(demoBroker: DemoBroker): DemoBalanceRepository = DemoBalanceRepository(demoBroker)

    @Bean
    @Primary
    fun demoOrderRepository(demoBroker: DemoBroker): DemoOrderRepository = DemoOrderRepository(demoBroker)

    @Bean
    @Primary
    fun demoCommissionRateRepository(): DemoCommissionRateRepository = DemoCommissionRateRepository(properties.commissionRate)
}
