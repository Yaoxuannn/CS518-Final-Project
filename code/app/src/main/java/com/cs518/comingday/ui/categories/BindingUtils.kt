package com.cs518.comingday.ui.categories

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cs518.comingday.database.Category

@BindingAdapter("categoryName")
fun TextView.setCategoryName(item: Category?) {
    item?.let {
        text = item.categoryName
    }
}