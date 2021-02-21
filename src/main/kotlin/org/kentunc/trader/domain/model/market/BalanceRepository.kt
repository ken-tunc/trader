package org.kentunc.trader.domain.model.market

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

interface BalanceRepository {

    suspend fun findOne(currencyCode: CurrencyCode): Balance? {
        return findAll()
            .firstOrNull { it.currencyCode == currencyCode }
    }

    fun findAll(): Flow<Balance>
}
