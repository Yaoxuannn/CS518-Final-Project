package com.cs518.comingday.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var categoryId: Long = 0L,

    @ColumnInfo(name = "category_name")
    var categoryName: String = ""
)