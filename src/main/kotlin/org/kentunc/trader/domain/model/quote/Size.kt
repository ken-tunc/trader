package org.kentunc.trader.domain.model.quote

import java.math.BigDecimal

data class Size(private val value: BigDecimal) {

    constructor(value: Double) : this(BigDecimal.valueOf(value))

    init {
        require(value >= BigDecimal.ZERO) { "Size must not be a negative number, got=$value" }
    }

    fun toBigDecimal() = value

    operator fun plus(other: Size): Size {
        return Size(this.value.add(other.value))
    }

    operator fun minus(other: Size): Size {
        return Size(this.value.minus(other.value))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Size

        // NOTE: BigDecimal#equals returns false for values with different numbers of digits
        return this.value.compareTo(other.value) == 0
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
