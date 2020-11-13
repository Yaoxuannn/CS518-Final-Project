package com.cs518.comingday.ui.categories

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs518.comingday.database.Category
import com.cs518.comingday.database.CategoryDatabaseDao
import kotlinx.coroutines.launch

class CategoriesViewModel(dataSource: CategoryDatabaseDao) : ViewModel() {

    val database = dataSource

    private val _navigateToCategory = MutableLiveData<Boolean?>()
    val navigateToCategory: LiveData<Boolean?>
        get() = _navigateToCategory

    private val _categoryEditLayoutVisible = MutableLiveData<Int?>()
    val categoryEditLayoutVisible : LiveData<Int?>
        get() = _categoryEditLayoutVisible


    val categories = database.getAllCategories()

    init {
        _categoryEditLayoutVisible.value = View.GONE
    }

    fun doneNavigating() {
        _navigateToCategory.value = null
    }

    fun onAddButtonClicked() {
        _categoryEditLayoutVisible.value = View.VISIBLE
    }

    fun onConfirmButtonClicked() {
        _categoryEditLayoutVisible.value = View.GONE
    }

    fun addNewCategory(categoryName: String) {
        if (categoryName.isNotEmpty()) {
            viewModelScope.launch {
                val newCategory = Category(categoryName = categoryName.trim())
                insert(newCategory)
            }
        }
    }

    fun onCategoryClicked(categoryId: Long) {
        _categoryEditLayoutVisible.value = View.VISIBLE
    }

    private suspend fun insert(category: Category) {
        database.insert(category)
    }

    private suspend fun update(category: Category) {
        database.update(category)
    }

    private fun uniqueCheck(category: Category) {

    }


}