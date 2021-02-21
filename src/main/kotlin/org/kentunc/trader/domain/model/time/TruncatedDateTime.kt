package org.kentunc.trader.domain.model.time

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

data class TruncatedDateTime(private val localDateTime: LocalDateTime) {

    constructor(dateTime: DateTime, duration: Duration) : this(
        dateTime.toLocalDateTime().truncatedTo(duration.unit)
    )

    fun toLocalDateTime() = localDateTime

    fun toZonedDateTime(): ZonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
}
