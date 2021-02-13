package org.kentunc.trader.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.ticker.TickerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(TickerService::class)
internal class TickerServiceTest {

    @MockkBean(relaxed = true)
    private lateinit var tickerRepository: TickerRepository

    @Autowired
    private lateinit var target: TickerService

    @Test
    fun testSubscribe() {
        val productCode = ProductCode.BTC_JPY
        target.subscribe(productCode)
        verify { tickerRepository.stream(productCode) }
    }
}
