package org.kentunc.trader.presentation.configuration

import org.kentunc.trader.application.FeedCandleInteractor
import org.kentunc.trader.application.SubscribeTickerInteractor
import org.kentunc.trader.presentation.event.listener.CandleFeeder
import org.kentunc.trader.presentation.event.listener.TickerSubscriber
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(prefix = "trader.feed", name = ["enabled"], havingValue = "true")
@EnableConfigurationProperties(TraderFeedConfigurationProperties::class)
class TraderFeedConfiguration(private val properties: TraderFeedConfigurationProperties) {

    @Bean
    fun tickerPublisher(subscribeTickerInteractor: SubscribeTickerInteractor, eventPublisher: ApplicationEventPublisher) =
        TickerSubscriber(properties.productCodes, subscribeTickerInteractor, eventPublisher)

    @Bean
    fun createdCandlePublisher(feedCandleInteractor: FeedCandleInteractor, eventPublisher: ApplicationEventPublisher) =
        CandleFeeder(feedCandleInteractor, eventPublisher)
}
