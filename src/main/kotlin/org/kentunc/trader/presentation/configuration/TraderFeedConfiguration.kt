package org.kentunc.trader.presentation.configuration

import org.kentunc.trader.application.FeedCandleService
import org.kentunc.trader.application.SubscribeTickerService
import org.kentunc.trader.presentation.event.publisher.CreatedCandlePublisher
import org.kentunc.trader.presentation.event.publisher.TickerPublisher
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
    fun tickerPublisher(subscribeTickerService: SubscribeTickerService, eventPublisher: ApplicationEventPublisher) =
        TickerPublisher(properties.productCodes, subscribeTickerService, eventPublisher)

    @Bean
    fun createdCandlePublisher(feedCandleService: FeedCandleService, eventPublisher: ApplicationEventPublisher) =
        CreatedCandlePublisher(feedCandleService, eventPublisher)
}
