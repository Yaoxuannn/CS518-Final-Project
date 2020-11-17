package com.cs518.comingday

import java.util.*

// TODO("这里写一些文本/日期转换辅助函数")
fun getTodayStartTime(tz: TimeZone): Long {
    val calendar = Calendar.getInstance(tz)
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}