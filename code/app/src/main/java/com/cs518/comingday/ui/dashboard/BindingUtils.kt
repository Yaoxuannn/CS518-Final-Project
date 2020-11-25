package com.cs518.comingday.ui.dashboard

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cs518.comingday.database.Event
import com.cs518.comingday.getTodayStartTime
import java.util.*
import kotlin.math.abs

@BindingAdapter("numberOfRemainingDays")
fun TextView.setNumberOfRemainDays(item: Event?) {
    item?.let {
        val today = getTodayStartTime(TimeZone.getDefault())
        val days = ((item.eventDate - today) / (1000 * 3600 * 24)).toInt()
        text = when {
            days == -1 -> {
                "1 day passed"
            }
            days == 0 -> {
                "Today. Big day!"
            }
            days == 1 -> {
                "Tomorrow"
            }
            days < 1 -> {
                "${abs(days)} days passed"
            }
            else -> {
                "$days days"
            }
        }
    }
}

@BindingAdapter("eventName")
fun TextView.setEventName(item: Event?) {
    item?.let {
        text = item.eventName
    }
}
