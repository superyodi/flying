package com.yodi.flying.model.dao;

import androidx.room.Dao;
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query;
import com.yodi.flying.model.entity.TagWithTime

import com.yodi.flying.model.entity.Report;
import com.yodi.flying.model.entity.Ticket


@Dao
interface ReportDao {

    @Query("SELECT * FROM reports WHERE userId = :userId AND date = :date")
    suspend fun getTodayReport(userId : Long, date: String): Report

    @Query("SELECT totalTime FROM reports WHERE userId = :userId AND date = :date")
    suspend fun getTodayTotalTime(userId : Long, date: String): Long

    @Query("SELECT totalTime FROM reports WHERE userId = :userId AND date = :date")
    suspend fun getTimeWithTagForWeek(userId : Long, date: String): Long


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTodayReport(report: Report)

}


