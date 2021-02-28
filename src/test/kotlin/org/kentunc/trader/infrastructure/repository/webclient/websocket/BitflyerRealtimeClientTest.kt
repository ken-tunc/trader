package org.kentunc.trader.infrastructure.repository.webclient.websocket

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.trader.domain.model.market.ProductCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@SpringBootTest
@ActiveProfiles("test")
internal class BitflyerRealtimeClientTest {

    @Autowired
    lateinit var target: BitflyerRealtimeClient

    @ParameterizedTest
    @ArgumentsSource(ProductCodeProvider::class)
    fun `subscribe three tickers`(productCode: ProductCode) {
        runBlocking {
            target.stream(productCode)
                .take(3)
                .collect {
                    assertEquals(productCode, it.productCode)
                }
        }
    }

    private companion object {
        private class ProductCodeProvider : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return ProductCode.values().toList().stream()
                    .map { arguments(it) }
            }
        }
    }
}
