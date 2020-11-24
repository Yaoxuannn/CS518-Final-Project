package com.cs518.comingday

import java.util.*

fun getTodayStartTime(tz: TimeZone): Long {
    val calendar = Calendar.getInstance(tz)
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}