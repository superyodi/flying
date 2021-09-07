package com.yodi.flying.model.dao

import androidx.room.*
import com.yodi.flying.model.entity.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE userId = :userId AND date = :date AND cityTime = :cityTime")
    suspend fun getTasksForCity(userId : Long, date: Long, cityTime : Long): List<Task>

    @Query("SELECT * FROM tasks WHERE userId = :userId AND date = :date AND cityTime = :cityTime AND pomoId = :pomoId")
    suspend fun getTaskForCity(userId : Long, date: Long, cityTime : Long, pomoId: Long): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task) : Int
}