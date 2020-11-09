package com.cs518.comingday.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs518.comingday.database.CategoryDatabaseDao

class CategoriesViewModel(dataSource: CategoryDatabaseDao) : ViewModel() {

    val database = dataSource

    private val _navigateToCategory = MutableLiveData<Boolean?>()
    val navigateToCategory: LiveData<Boolean?>
        get() = _navigateToCategory

    val categories = database.getAllCategories()

    fun doneNavigating() {
        _navigateToCategory.value = null
    }




}