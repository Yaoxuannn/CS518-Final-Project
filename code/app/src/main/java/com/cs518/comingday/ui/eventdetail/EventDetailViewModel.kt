package com.cs518.comingday.ui.eventdetail

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs518.comingday.database.EventDatabaseDao

class EventDetailViewModel(private val eventId: Long = 0L, dataSource: EventDatabaseDao) : ViewModel() {
    val database = dataSource

    private val _navigateToDashboard = MutableLiveData<Boolean?>()
    val navigateToDashboard: LiveData<Boolean?>
        get() = _navigateToDashboard

    private val _deleteBtnVisible = MutableLiveData<Int?>()
    val deleteBtnVisible: LiveData<Int?>
        get() = _deleteBtnVisible

    fun doneNavigating() {
        _navigateToDashboard.value = null
    }

    init {
        if (eventId == 0L) _deleteBtnVisible.value = View.GONE
    }

}