package com.foo.pomodoro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TagDao {

    @Query("SELECT * FROM tags ORDER BY id ASC")
    fun getTags(): kotlinx.coroutines.flow.Flow<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: Tag)

    @Query("DELETE FROM tags")
    suspend fun deleteAll()
}