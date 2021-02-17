package com.dzakyhdr.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {
    /*
    menggunakan suspend karena kita ingin menjalankannya di background thread
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubscriber(subscriber: Subscriber): Long

    @Update
    suspend fun updatedSubscriber(subscriber: Subscriber)

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber)

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAll()

    /**
     *  tidak memakai suspend function karena sudah mengembalikan value dengan livedata
     *  livedata sudah berjalan di background thread
      */
    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscriber(): LiveData<List<Subscriber>>
}