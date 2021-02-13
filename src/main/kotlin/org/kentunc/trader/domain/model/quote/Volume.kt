package org.kentunc.trader.domain.model.quote

import java.math.BigDecimal

data class Volume(private val value: BigDecimal) {

    constructor(value: Double) : this(BigDecimal.valueOf(value))

    init {
        require(value >= BigDecimal.ZERO) { "Volume must not be a negative number, got=$value" }
    }

    fun toBigDecimal() = value

    operator fun plus(other: Volume): Volume {
        return Volume(this.value.add(other.value))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Volume

        // NOTE: BigDecimal#equals returns false for values with different numbers of digits
        return this.value.compareTo(other.value) == 0
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
