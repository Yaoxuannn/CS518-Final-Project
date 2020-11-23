package com.cs518.comingday.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDatabaseDao {

    @Insert
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Query("SELECT * FROM event_table WHERE eventId = :key")
    suspend fun get(key: Long): Event

    @Query("SELECT * FROM event_table ORDER BY eventId DESC")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("DELETE FROM event_table WHERE eventId = :key")
    suspend fun clearEventWithId(key: Long)

    @Query("DELETE FROM event_table WHERE category_id = :key")
    suspend fun clearEventWithCategoryId(key: Long)

    @Query("SELECT * from event_table WHERE eventId = :key")
    suspend fun getEventWithId(key: Long): Event
}