package com.cs518.comingday.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs518.comingday.database.EventDatabaseDao

class DashboardViewModel(
    dataSource: EventDatabaseDao,
) : ViewModel() {

    val database = dataSource

    val events = database.getAllEvents()

    private val _navigateToEventDetail = MutableLiveData<Long>()
    val navigateToEventDetail: LiveData<Long>
        get() = _navigateToEventDetail

    fun doneNavigating() {
        _navigateToEventDetail.value = null
    }

    fun onEventCardClicked(eventId: Long) {
        _navigateToEventDetail.value = eventId
    }

    fun onCreateButtonClicked() {
        _navigateToEventDetail.value = 0
    }


}