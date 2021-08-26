package com.yodi.flying.model.dao;

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.entity.TagWithTime

import com.yodi.flying.model.entity.Report;
import com.yodi.flying.model.entity.Ticket
import kotlinx.coroutines.flow.Flow


@Dao
interface ReportDao {

    @Query("SELECT * FROM reports WHERE userId = :userId AND date = :date")
    suspend fun getReport(userId : Long, date: Long): Report

    @Query("SELECT totalTime FROM reports WHERE userId = :userId AND date = :date")
    suspend fun getTodayTotalTime(userId : Long, date: Long): Long

    @Query("SELECT cityDepth FROM reports WHERE userId = :userId AND date = :date")
    suspend fun getTodayCityDepth(userId : Long, date: Long): Int

    @Query("SELECT totalTime FROM reports WHERE userId = :userId AND date = :date")
    suspend fun getTimeWithTagForWeek(userId : Long, date: Long): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTodayReport(report: Report)

    @Update
    suspend fun update(report: Report) : Int

    @Query("UPDATE reports SET totalTime = totalTime + :runningTime WHERE userId = :userId AND date = :date")
    suspend fun updateTotalTime(userId : Long, date: Long, runningTime : Long) : Int

    @Query("UPDATE reports SET totalTime = 0 WHERE userId = :userId AND date = :date")
    suspend fun resetTotalTime(userId : Long, date: Long) : Int






}


