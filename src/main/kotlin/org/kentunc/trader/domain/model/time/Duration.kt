package org.kentunc.trader.domain.model.time

import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

enum class Duration(val unit: TemporalUnit) {

    MINUTES(ChronoUnit.MINUTES),
    HOURS(ChronoUnit.HOURS),
    DAYS(ChronoUnit.DAYS);
}
