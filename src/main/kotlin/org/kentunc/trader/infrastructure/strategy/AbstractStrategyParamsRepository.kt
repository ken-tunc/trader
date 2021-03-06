package org.kentunc.trader.infrastructure.strategy

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.kentunc.trader.infrastructure.repository.persistence.dao.StrategyParamsDao
import org.kentunc.trader.infrastructure.repository.persistence.entity.StrategyParamsEntity
import org.kentunc.trader.infrastructure.repository.persistence.entity.StrategyParamsPrimaryKey
import kotlin.reflect.KClass

abstract class AbstractStrategyParamsRepository<T : StrategyParams>(
    private val genericClass: KClass<T>,
    private val strategyParamsDao: StrategyParamsDao
) {

    private val paramsName: String

    init {
        require(genericClass.simpleName != null) { "Invalid generic type: $genericClass" }
        paramsName = genericClass.simpleName!!
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }

    protected abstract fun getDefaultValue(): T

    abstract fun getParamsListForOptimization(): List<T>

    suspend fun getParams(): T {
        val primaryKey = StrategyParamsPrimaryKey(paramsName)
        return when (val entity = strategyParamsDao.selectOne(primaryKey)) {
            null -> getDefaultValue()
            else -> objectMapper.readValue(entity.params, genericClass.java)
        }
    }

    suspend fun saveParams(params: T): Void? {
        val entity = StrategyParamsEntity(
            name = paramsName,
            params = objectMapper.writeValueAsString(params)
        )
        return when (strategyParamsDao.selectOne(entity.primaryKey)) {
            null -> strategyParamsDao.insert(entity)
            else -> strategyParamsDao.update(entity)
        }
    }
}
