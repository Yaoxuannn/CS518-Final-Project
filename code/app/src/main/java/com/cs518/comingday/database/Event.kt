package com.cs518.comingday.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "event_table")
data class Event(
    @PrimaryKey(autoGenerate = true)
    var eventId: Long = 0L,

    @ColumnInfo(name = "event_name")
    var eventName: String = "",

    @ColumnInfo(name = "target_date")
    var eventDate: String = "",

    @ColumnInfo(name = "category_id")
    var categoryId: Long = -1L,

    @ColumnInfo(name = "is_header_event")
    var isHeaderEvent: Boolean = false
)