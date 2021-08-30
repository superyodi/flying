package com.yodi.flying.model.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.dao.ReportDao
import com.yodi.flying.model.dao.TicketDao
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.entity.Report
import com.yodi.flying.model.entity.Ticket
import com.yodi.flying.model.entity.User
import com.yodi.flying.utils.Constants
import com.yodi.flying.utils.convertDateToLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class TicketRepository(
    private val ticketDao: TicketDao,
    private val reportDao: ReportDao,
    private val preferences: SharedPreferenceManager

) {

    val userId : Long
        get() = preferences.getLong(Constants.PREF_USER_ID)

    private val todayDate : Long
        get() = preferences.getLong(Constants.PREF_TODAY_DATE)

    private val refreshIntervalMs : Long = 500 //5000

    fun getTickets(): Flow<List<Ticket>> = ticketDao.getTickets(userId, todayDate)

    suspend fun getTotalTimeFlow(): Flow<Long> = flow {
        while (true) {
            val latestTotalTime = reportDao.getTodayTotalTime(userId, todayDate)

            emit(latestTotalTime)
            kotlinx.coroutines.delay(refreshIntervalMs)
        }
    }

    fun getUserGoalTime() = preferences.getLong(Constants.PREF_USER_GOAL_TIME)

    suspend fun getTotalTime() = reportDao.getTodayTotalTime(userId, todayDate)
    suspend fun insert()  {
        val report = Report(userId, todayDate)
        reportDao.insertTodayReport(report)
    }

    suspend fun updateTodayTotalTime(runningTime : Long) {
        reportDao.updateTotalTime(userId, todayDate, runningTime)
    }

    suspend fun resetTotalTime() {
        reportDao.resetTotalTime(userId, todayDate)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getTodayCityDepth(): Int = reportDao. getTodayCityDepth(userId, todayDate)




}