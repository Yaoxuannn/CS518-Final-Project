package com.cs518.comingday.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cs518.comingday.database.CategoryDatabaseDao
import com.cs518.comingday.database.EventDatabaseDao

class CategoriesViewModelFactory(
    private val dataSource: CategoryDatabaseDao,
    private val eventDataSource: EventDatabaseDao,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(dataSource, eventDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}