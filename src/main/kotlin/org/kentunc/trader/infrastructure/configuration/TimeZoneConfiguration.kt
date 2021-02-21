package org.kentunc.trader.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.ZoneId

@Configuration
class TimeZoneConfiguration {

    @Bean
    fun clock(): Clock = Clock.system(ZoneId.of("Asia/Tokyo"))
}
