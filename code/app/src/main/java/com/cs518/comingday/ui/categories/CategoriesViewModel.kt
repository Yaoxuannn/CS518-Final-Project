package com.cs518.comingday.ui.categories

import android.util.Log
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

    private var editLayoutVisible = false
    private var clearReady = false
    private var curCategory: Category?

    private val _categoryEditLayoutVisible = MutableLiveData<Int?>()
    val categoryEditLayoutVisible : LiveData<Int?>
        get() = _categoryEditLayoutVisible

    private val _showSnackBar = MutableLiveData<Boolean?>()
    val showSnackBar: LiveData<Boolean?>
        get() = _showSnackBar

    val categoryName = MutableLiveData<String>()
    val addButtonText = MutableLiveData<String>()
    val confirmButtonText = MutableLiveData<String>()

    val categories = database.getAllCategories()

    init {
        _categoryEditLayoutVisible.value = View.GONE
        categoryName.value = ""
        curCategory = null
        addButtonText.value = DEFAULT_ADD_TEXT
        confirmButtonText.value = DEFAULT_CONFIRM_TEXT
    }

    fun doneShowSnackBar() {
        _showSnackBar.value = null
    }

    fun onAddButtonClicked() {
        confirmButtonText.value = DEFAULT_CONFIRM_TEXT
        if(editLayoutVisible) hideEditLayout()
        else {
            curCategory = null
            showEditLayout()
        }
    }

    private fun showEditLayout() {
        _categoryEditLayoutVisible.value = View.VISIBLE
        categoryName.value = ""
        addButtonText.value = MODIFIED_ADD_TEXT
        editLayoutVisible = true
    }

    private fun hideEditLayout() {
        _categoryEditLayoutVisible.value = View.GONE
        addButtonText.value = DEFAULT_ADD_TEXT
        editLayoutVisible = false
    }

    fun onConfirmButtonClicked() {
        hideEditLayout()
        if (clearReady) {
            curCategory?.let {
                viewModelScope.launch {
                    clear(it)
                }
            }
        }
        else {
            // TODO: 抽离update和add的检测函数
            curCategory?.let { updateCategory(it) } ?: addNewCategory()
        }
    }

    private fun addNewCategory() {
        if (categoryName.value.toString().isNotEmpty()) {
            viewModelScope.launch {
                val newCategory = Category(categoryName = categoryName.value.toString().trim())
                insert(newCategory)
            }
        }
        else {
            _showSnackBar.value = true
        }
    }

    private fun updateCategory(category: Category) {
        if (categoryName.value.toString().isNotEmpty()) {
            viewModelScope.launch {
                category.categoryName = categoryName.value.toString()
                update(category)
            }
        }
        else {
            _showSnackBar.value = true
        }
    }

    fun clearReady() {
        if(curCategory != null) {
            confirmButtonText.value = CLEAR_CONFIRM_TEXT
            clearReady = true
        }
    }

    fun doneClear() {
        if (curCategory != null) {
            confirmButtonText.value = DEFAULT_CONFIRM_TEXT
            clearReady = false
        }
    }

    fun onCategoryClicked(categoryId: Long) {
        viewModelScope.launch {
            val category = database.get(categoryId)
            showEditLayout()
            categoryName.value = category.categoryName
            curCategory = category
            confirmButtonText.value = MODIFIED_CONFIRM_TEXT
        }
    }

    private suspend fun insert(category: Category) {
        database.insert(category)
    }

    private suspend fun update(category: Category) {
        database.update(category)
    }

    private suspend fun clear(category: Category) {
        database.clearCategoryWithId(category.categoryId)
    }

    companion object {
        private const val DEFAULT_ADD_TEXT = "Click here to create a new category"
        private const val DEFAULT_CONFIRM_TEXT = "Confirm"
        private const val MODIFIED_ADD_TEXT = "Hide Component"
        private const val MODIFIED_CONFIRM_TEXT = "Update"
        private const val CLEAR_CONFIRM_TEXT = "Clear"
    }


}