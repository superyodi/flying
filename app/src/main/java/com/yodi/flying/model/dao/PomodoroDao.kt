package com.yodi.flying.model.dao

import androidx.room.*
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.entity.PomodoroMini

@Dao
interface PomodoroDao {

    @Query("SELECT * FROM pomodoros WHERE userId = :userId ORDER BY id ASC")
    fun getPomodoros(userId : Long): kotlinx.coroutines.flow.Flow<List<Pomodoro>>

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

    @Query("DELETE FROM pomodoros WHERE hasDuedate = :hasDueDate")
    suspend fun deleteLastPomodoros(hasDueDate : Boolean)

    @Query("DELETE FROM pomodoros WHERE hasDuedate = :hasDueDate and dueDate < :date")
    suspend fun deleteLastPomodoros(hasDueDate : Boolean, date : Long)

    @Query("UPDATE pomodoros SET nowCount = 0, state = 0, leftTime = 0 WHERE hasDuedate = :hasDueDate and dueDate >= :date")
    suspend fun updateHasDueDatePomodoros(hasDueDate : Boolean, date : Long)


}