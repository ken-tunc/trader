package org.kentunc.trader.domain.model.quote

import java.math.BigDecimal

data class Price(private val value: BigDecimal) : Comparable<Price> {

    constructor(value: Long) : this(BigDecimal.valueOf(value))
    constructor(value: Double) : this(BigDecimal.valueOf(value))

    fun toBigDecimal() = value

    override fun compareTo(other: Price): Int {
        return this.value.compareTo(other.value)
    }

    operator fun plus(other: Price): Price {
        return Price(this.value.add(other.value))
    }

    operator fun minus(other: Price): Price {
        return Price(this.value.minus(other.value))
    }

    operator fun times(other: Price): Price {
        return Price(this.value.times(other.value))
    }

    operator fun div(other: Price): Price {
        return Price(this.value.divide(other.value))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Price

        // NOTE: BigDecimal#equals returns false for values with different numbers of digits
        return this.value.compareTo(other.value) == 0
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
