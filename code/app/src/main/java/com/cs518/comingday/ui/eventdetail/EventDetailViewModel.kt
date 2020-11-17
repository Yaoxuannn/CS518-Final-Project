package com.cs518.comingday.ui.eventdetail

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs518.comingday.database.Category
import com.cs518.comingday.database.CategoryDatabaseDao
import com.cs518.comingday.database.Event
import com.cs518.comingday.database.EventDatabaseDao
import kotlinx.coroutines.launch
import java.text.ParsePosition
import java.text.SimpleDateFormat

class EventDetailViewModel(
    private val eventId: Long = 0L,
    dataSource: EventDatabaseDao,
    categoryDataSource: CategoryDatabaseDao,
) : ViewModel() {
    val database = dataSource
    private val categoryDatabase = categoryDataSource

    lateinit var categoryNames: Array<String>
    var checkedCategoryNameIdx: Int? = null

    private val _navigateToDashboard = MutableLiveData<Boolean?>()
    val navigateToDashboard: LiveData<Boolean?>
        get() = _navigateToDashboard

    private val _showDatePicker = MutableLiveData<Boolean?>()
    val showDatePicker: LiveData<Boolean?>
        get() = _showDatePicker

    private val _showCategorySelector = MutableLiveData<Boolean?>()
    val showCategorySelector: LiveData<Boolean?>
        get() = _showCategorySelector

    private val _deleteBtnVisible = MutableLiveData<Int?>()
    val deleteBtnVisible: LiveData<Int?>
        get() = _deleteBtnVisible


    val datePickerString = MutableLiveData<String>()
    val categoryString = MutableLiveData<String>()
    val confirmString = MutableLiveData<String>()

    val eventName = MutableLiveData<String>()
    var eventDate = ""
    var categoryName = ""

    lateinit var curEvent: Event


    fun onDateSet(year: Int, month: Int, day: Int) {
        eventDate = "$month/$day/$year"
        datePickerString.value = "Date Selected: $eventDate"
        _showDatePicker.value = false
    }

    private fun onCategorySet() {
        categoryString.value = "Category Selected: $categoryName"
        _showCategorySelector.value = false
    }

    fun onDatePickerClicked() {
        _showDatePicker.value = true
    }

    fun onCategorySelectorClicked() {
        viewModelScope.launch {
            categoryNames = extractCategoryName(categoryDatabase.getCategories())
            _showCategorySelector.value = true
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            database.clearEventWithId(curEvent.eventId)
        }
        // SnackBar
        _navigateToDashboard.value = true
    }

    @SuppressLint("SimpleDateFormat")
    fun onConfirm() {
        // TODO: 检测函数
        // For creating new events
        if (eventId == 0L) {
            viewModelScope.launch {
                val event = Event(
                    eventName = requireNotNull(eventName.value),
                    eventDate = SimpleDateFormat("MM/dd/yyyy").parse(eventDate, ParsePosition(0)).time,
                    categoryId = categoryDatabase.getCategoryIdWithName(categoryName),
                )
                database.insert(event)
            }
        }
        // For updating existed events
        else {
            curEvent.eventName = requireNotNull(eventName.value)
            curEvent.eventDate = SimpleDateFormat("MM/dd/yyyy").parse(eventDate, ParsePosition(0)).time
            viewModelScope.launch {
                curEvent.categoryId = categoryDatabase.getCategoryIdWithName(categoryName)
                database.update(curEvent)
            }
        }
        _navigateToDashboard.value = true
    }

    fun setCategory(categoryNameIdx: Int) {
        categoryName = categoryNames[categoryNameIdx]
        checkedCategoryNameIdx = categoryNameIdx
        onCategorySet()
    }


    fun doneNavigating() {
        _navigateToDashboard.value = null
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDataString(eventId: Long) {
        confirmString.value = "Update"
        viewModelScope.launch {
            curEvent = database.getEventWithId(eventId)
            eventName.value = curEvent.eventName
            eventDate = SimpleDateFormat("MM/dd/yyyy").format(curEvent.eventDate)
            categoryName = categoryDatabase.getCategoryNameWithId(curEvent.categoryId)
            datePickerString.value = "Date Selected: $eventDate"
            categoryString.value = "Category Selected: $categoryName"
        }
    }

    init {
        if (eventId == 0L) {
            _deleteBtnVisible.value = View.GONE
            datePickerString.value = "Select a date"
            categoryString.value = "Select a category"
            confirmString.value = "Confirm"
        } else {
            _deleteBtnVisible.value = View.VISIBLE
            setDataString(eventId)
        }
    }

    private fun extractCategoryName(categories: List<Category>): Array<String> {
        return categories.map { category -> category.categoryName }.stream()
            .toArray { arrayOfNulls<String>(it) }
    }


}