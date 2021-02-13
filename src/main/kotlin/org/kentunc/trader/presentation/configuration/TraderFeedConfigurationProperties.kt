package org.kentunc.trader.presentation.configuration

import org.kentunc.trader.domain.model.market.ProductCode
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "trader.feed")
data class TraderFeedConfigurationProperties(val productCodes: List<ProductCode>)
