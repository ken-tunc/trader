package org.kentunc.trader.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kentunc.trader.domain.model.market.ProductCode
import org.kentunc.trader.domain.model.ticker.Ticker
import org.kentunc.trader.domain.model.ticker.TickerRepository
import org.kentunc.trader.infrastructure.repository.webclient.BitflyerRealtimeClient
import org.springframework.stereotype.Repository

@Repository
class TickerRepositoryImpl(private val bitflyerRealtimeClient: BitflyerRealtimeClient) : TickerRepository {

    override fun stream(productCode: ProductCode): Flow<Ticker> {
        return bitflyerRealtimeClient.stream(productCode)
            .map { it.toTicker() }
    }
}
