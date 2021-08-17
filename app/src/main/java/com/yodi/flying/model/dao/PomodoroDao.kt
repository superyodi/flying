package com.yodi.flying.model.dao

import androidx.room.*
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.entity.PomodoroMini

@Dao
interface PomodoroDao {

    @Query("SELECT * FROM pomodoros WHERE userId = :userId ORDER BY id ASC")
    fun getPomodoros(userId : Long): kotlinx.coroutines.flow.Flow<List<Pomodoro>>

    @Query("SELECT title, nowCount  FROM pomodoros WHERE userId = :userId AND nowCount > 0 ORDER BY id ASC")
    suspend fun getExcutedPomodoros(userId : Long): List<PomodoroMini>

    @Query("SELECT title, ticketCount  FROM pomodoros WHERE userId = :userId AND startedTime > :cityTime ORDER BY id ASC")
    suspend fun getPomodorosWithCity(userId: Long, cityTime : Long) : List<PomodoroMini>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pomodoro: Pomodoro)

    @Query("SELECT * FROM pomodoros WHERE userId = :userId AND id = :pomodoroId")
    suspend fun getPomodoro(pomodoroId: Long, userId: Long): Pomodoro

    @Query("DELETE FROM pomodoros")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(pomodoro: Pomodoro): Int
    @Update
    suspend fun updatePomodoro(pomodoro: Pomodoro): Int
}