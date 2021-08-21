package com.yodi.flying.model.repository

import androidx.annotation.WorkerThread
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.dao.ReportDao
import com.yodi.flying.model.dao.TicketDao
import com.yodi.flying.model.entity.User
import com.yodi.flying.utils.Constants

class TicketRepository(
    private val ticketDao: TicketDao,
    private val reportDao: ReportDao,
    private val preferences: SharedPreferenceManager
) {

    val userId : Long
        get() = preferences.getLong(Constants.PREF_USER_ID)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getTotalTime(date : Long): Long = reportDao.getTodayTotalTime(userId, date)



    /*
     @Query("SELECT totalTime FROM reports WHERE userId = :userId AND date = :date")
    suspend fun getTodayTotalTime(userId : Long, date: Long): Long

    @Query("SELECT cityDepth FROM reports WHERE userId = :userId AND date = :date")
    suspend fun getTodayCityDepth(userId : Long, date: Long): Int
     */
}