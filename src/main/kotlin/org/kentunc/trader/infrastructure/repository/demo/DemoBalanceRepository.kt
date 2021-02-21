package org.kentunc.trader.infrastructure.repository.demo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.kentunc.trader.domain.model.market.Balance
import org.kentunc.trader.domain.model.market.BalanceRepository

class DemoBalanceRepository(private val demoBroker: DemoBroker) : BalanceRepository {

    override fun findAll(): Flow<Balance> = demoBroker.getBalances().asFlow()
}
