package com.cs518.comingday.ui.categories

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs518.comingday.database.Category
import com.cs518.comingday.database.CategoryDatabaseDao
import com.cs518.comingday.database.EventDatabaseDao
import kotlinx.coroutines.launch
import java.util.*

class CategoriesViewModel(dataSource: CategoryDatabaseDao, val eventDatabase: EventDatabaseDao) :
    ViewModel() {

    val database = dataSource

    private var editLayoutVisible = false
    private var curCategory: Category?

    private val _categoryEditLayoutVisible = MutableLiveData<Int?>()
    val categoryEditLayoutVisible: LiveData<Int?>
        get() = _categoryEditLayoutVisible

    private val _deleteButtonVisible = MutableLiveData<Int?>()
    val deleteButtonVisible: LiveData<Int?>
        get() = _deleteButtonVisible

    private val _showSnackBar = MutableLiveData<Boolean?>()
    val showSnackBar: LiveData<Boolean?>
        get() = _showSnackBar

    private val _showDeleteConfirmation = MutableLiveData<Boolean?>()
    val showDeleteConfirmation: LiveData<Boolean?>
        get() = _showDeleteConfirmation

    private val _hideInputMethod = MutableLiveData<Boolean?>()
    val hideInputMethod: LiveData<Boolean?>
        get() = _hideInputMethod

    val categoryName = MutableLiveData<String>()
    val addButtonText = MutableLiveData<String>()
    val confirmButtonText = MutableLiveData<String>()

    val categories = database.getAllCategories()

    init {
        _categoryEditLayoutVisible.value = View.GONE
        _deleteButtonVisible.value = View.GONE
        categoryName.value = ""
        curCategory = null
        addButtonText.value = DEFAULT_ADD_TEXT
        confirmButtonText.value = DEFAULT_CONFIRM_TEXT
    }

    fun doneShowSnackBar() {
        _showSnackBar.value = null
    }

    fun doneHideInputMethod() {
        _hideInputMethod.value = null
    }

    fun doneShowDeleteConfirmation() {
        _showDeleteConfirmation.value = null
    }

    fun onAddButtonClicked() {
        confirmButtonText.value = DEFAULT_CONFIRM_TEXT
        if (editLayoutVisible) hideEditLayout()
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
        _deleteButtonVisible.value = View.GONE
        if (categoryNameCheck()) {
            curCategory?.let { updateCategory(it) } ?: addNewCategory()
        } else {
            _showSnackBar.value = true
        }
        _hideInputMethod.value = true
    }

    fun onDeleteButtonClicked() {
        hideEditLayout()
        _deleteButtonVisible.value = View.GONE
        _hideInputMethod.value = true
        _showDeleteConfirmation.value = true
    }

    fun doDelete() {
        curCategory?.let {
            viewModelScope.launch {
                clear(it)
                eventDatabase.clearEventWithCategoryId(it.categoryId)
            }
        }
    }

    private fun addNewCategory() {
        viewModelScope.launch {
            val newCategory = Category(categoryName = categoryName.value.toString().trim())
            insert(newCategory)
        }
    }

    private fun updateCategory(category: Category) {
        viewModelScope.launch {
            category.categoryName = categoryName.value.toString()
            update(category)
        }
    }

    private fun categoryNameCheck(): Boolean {
        // Unique Test
        categories.value?.forEach { category ->
            if (categoryName.value?.toLowerCase(Locale.ROOT) == category.categoryName.toLowerCase(
                    Locale.ROOT)
            ) return false
        }
        // Length Test
        if (categoryName.value?.isEmpty()!!) return false
        // Legality Test
        if (categoryName.value?.contains(Regex("\\s"))!!) return false
        // All Pass
        return true
    }

    fun onCategoryClicked(categoryId: Long) {
        viewModelScope.launch {
            val category = database.get(categoryId)
            showEditLayout()
            categoryName.value = category.categoryName
            curCategory = category
            confirmButtonText.value = MODIFIED_CONFIRM_TEXT
            _deleteButtonVisible.value = View.VISIBLE
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
    }


}