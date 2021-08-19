package com.yodi.flying.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yodi.flying.model.entity.Tag

@Dao
interface TagDao {

    @Query("SELECT * FROM tags WHERE userId = :userId")
    fun getTags(userId : Long): kotlinx.coroutines.flow.Flow<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: Tag)

    @Query("DELETE FROM tags WHERE userId = :userId")
    suspend fun deleteAll(userId: Long)
}