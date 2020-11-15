package com.cs518.comingday.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Query("SELECT * FROM category_table WHERE categoryId = :key")
    suspend fun get(key: Long): Category

    @Query("SELECT * FROM category_table ORDER BY categoryId")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM category_table ORDER BY categoryId")
    suspend fun getCategories(): List<Category>

    @Query("SELECT categoryId FROM category_table WHERE category_name = :name")
    suspend fun getCategoryIdWithName(name: String): Long

    @Query("DELETE FROM category_table WHERE categoryId = :key")
    suspend fun clearCategoryWithId(key: Long)

}