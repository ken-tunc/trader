package org.kentunc.trader.domain.model.time

import java.time.LocalDateTime

data class TruncatedDateTime(private val localDateTime: LocalDateTime) {

    constructor(dateTime: DateTime, duration: Duration) : this(
        dateTime.toLocalDateTime().truncatedTo(duration.unit)
    )

    fun toLocalDateTime() = localDateTime
}
