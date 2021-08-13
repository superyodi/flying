package com.yodi.flying.model.dao

import androidx.room.*
import com.yodi.flying.model.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserWithId(userId: Long): User

    @Update
    suspend fun updateUser(user: User): Int

}