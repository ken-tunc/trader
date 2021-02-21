package org.kentunc.trader.presentation.configuration

import org.kentunc.trader.application.LossCutInteractor
import org.kentunc.trader.application.TradeInteractor
import org.kentunc.trader.presentation.event.listener.LiveTrader
import org.kentunc.trader.presentation.event.listener.LossCutMonitor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(prefix = "trader.live", name = ["enabled"], havingValue = "true")
@EnableConfigurationProperties(TraderLiveConfigurationProperties::class)
class TraderLiveConfiguration(private val properties: TraderLiveConfigurationProperties) {

    @Bean
    fun liveTrader(tradeInteractor: TradeInteractor) =
        LiveTrader(properties.tradeDuration, properties.maxCandleNum, tradeInteractor)

    @Bean
    fun lossCutMonitor(lossCutInteractor: LossCutInteractor) = LossCutMonitor(lossCutInteractor)
}
