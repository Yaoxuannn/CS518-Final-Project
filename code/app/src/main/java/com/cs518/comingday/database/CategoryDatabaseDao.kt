package com.cs518.comingday.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDatabaseDao {

    @Insert
    suspend fun insert(category: Category)

    @Query("SELECT * FROM category_table ORDER BY categoryId")
    suspend fun getAllCategories(): LiveData<List<Category>>

    @Query("DELETE FROM category_table WHERE categoryId = :key")
    fun clearCategoryWithId(key: Long)

}