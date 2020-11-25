package com.cs518.comingday.ui.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cs518.comingday.database.CategoryDatabaseDao
import com.cs518.comingday.database.EventDatabaseDao

class EventDetailViewModelFactory(
    private val eventId: Long,
    private val dataSource: EventDatabaseDao,
    private val categoryDataSource: CategoryDatabaseDao,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(eventId, dataSource, categoryDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}