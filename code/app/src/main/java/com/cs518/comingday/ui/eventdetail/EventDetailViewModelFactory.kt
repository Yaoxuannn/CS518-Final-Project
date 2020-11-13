package com.cs518.comingday.ui.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cs518.comingday.database.EventDatabaseDao
import java.lang.IllegalArgumentException

class EventDetailViewModelFactory(
    private val eventId: Long, private val dataSource: EventDatabaseDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(eventId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}