package org.kentunc.trader.infrastructure.repository.webclient.http

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.function.Executable
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.quote.Price
import org.kentunc.trader.domain.model.quote.Volume
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.time.DateTime
import org.kentunc.trader.test.extension.WebClientExtension
import org.kentunc.trader.test.util.ResourceReader
import org.springframework.http.HttpMethod
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month

internal class BitflyerHttpPublicApiClientTest {

    lateinit var target: BitflyerHttpPublicApiClient

    @BeforeEach
    internal fun setUp() {
        target = BitflyerHttpPublicApiClient(helper.createWebClient())
    }

    @Test
    fun testGetTicker() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val responseBody = ResourceReader.readResource("/response/bitflyer/get_ticker.json")
        helper.enqueueResponse(responseBody)
        val expected = Ticker(
            productCode = productCode,
            dateTime = DateTime(LocalDateTime.of(2015, Month.JULY, 8, 2, 50, 59)),
            bestBid = Price(BigDecimal("30000")),
            bestAsk = Price(BigDecimal("36640")),
            volume = Volume(BigDecimal("16819.26"))
        )

        // exercise:
        val actual = target.getTicker(productCode).toTicker()

        // verify:
        assertAll(
            Executable { assertEquals(expected.id, actual.id) },
            Executable { assertEquals(expected.bestBid, actual.bestBid) },
            Executable { assertEquals(expected.bestAsk, actual.bestAsk) },
            Executable { assertEquals(expected.volume, actual.volume) },
            Executable { helper.assertRequest(HttpMethod.GET, "/ticker?product_code=$productCode") }
        )
    }

    companion object {
        @JvmField
        @RegisterExtension
        internal val helper = WebClientExtension()
    }
}
