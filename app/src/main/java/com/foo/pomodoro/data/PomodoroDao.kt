package com.foo.pomodoro.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PomodoroDao {

    @Query("SELECT * FROM pomodoros ORDER BY id ASC")
    fun getPomodoros(): kotlinx.coroutines.flow.Flow<List<Pomodoro>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pomodoro: Pomodoro)

    @Query("SELECT * FROM pomodoros WHERE id = :pomodoroId")
    suspend fun getPomodoro(pomodoroId: Int): Pomodoro

    @Query("DELETE FROM pomodoros")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(pomodoro: Pomodoro): Int
    @Update
    suspend fun updatePomodoro(pomodoro: Pomodoro): Int
}