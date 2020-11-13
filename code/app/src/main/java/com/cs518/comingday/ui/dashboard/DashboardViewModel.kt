package com.cs518.comingday.ui.dashboard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs518.comingday.database.Event
import com.cs518.comingday.database.EventDatabaseDao
import kotlinx.coroutines.launch

class DashboardViewModel(
        dataSource: EventDatabaseDao) : ViewModel() {

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