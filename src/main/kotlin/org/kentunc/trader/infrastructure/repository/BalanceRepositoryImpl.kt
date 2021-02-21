package org.kentunc.trader.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kentunc.trader.domain.model.market.Balance
import org.kentunc.trader.domain.model.market.BalanceRepository
import org.kentunc.trader.infrastructure.repository.webclient.http.BitflyerHttpPrivateApiClient
import org.springframework.stereotype.Repository

@Repository
class BalanceRepositoryImpl(
    private val bitflyerHttpPrivateApiClient: BitflyerHttpPrivateApiClient
) : BalanceRepository {

    override fun findAll(): Flow<Balance> {
        return bitflyerHttpPrivateApiClient.getBalances()
            .map { it.toBalance() }
    }
}
