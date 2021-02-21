package org.kentunc.trader.domain.model.order

data class MinutesToExpire(private val minutes: Int) {
    init {
        require(minutes >= 0) { "MinutesToExpire must be 0 or a positive number." }
    }

    fun toInt() = minutes
}
