package org.kentunc.trader.domain.model.time

import java.time.LocalDateTime

data class DateTime(private val localDateTime: LocalDateTime) {

    fun toLocalDateTime() = localDateTime
}
