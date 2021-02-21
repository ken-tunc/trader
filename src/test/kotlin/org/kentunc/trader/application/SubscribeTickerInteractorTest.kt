package org.kentunc.trader.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.service.TickerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(SubscribeTickerInteractor::class)
internal class SubscribeTickerInteractorTest {

    @MockkBean(relaxed = true)
    private lateinit var tickerService: TickerService

    @Autowired
    private lateinit var target: SubscribeTickerInteractor

    @Test
    fun testSubscribe() {
        val productCode = ProductCode.BTC_JPY
        target.subscribe(productCode)
        verify { tickerService.subscribe(productCode) }
    }
}
