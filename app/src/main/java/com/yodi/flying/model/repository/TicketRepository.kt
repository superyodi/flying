package com.yodi.flying.model.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.dao.ReportDao
import com.yodi.flying.model.dao.TicketDao
import com.yodi.flying.model.entity.Report
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

    private val today : Long
        get() = convertDateToLong(Date())
    private val refreshIntervalMs : Long = 5000


    suspend fun getTotalTime(): Flow<Long> = flow {
        while (true) {
            val latestTotalTime = reportDao.getTodayTotalTime(userId, today)

            emit(latestTotalTime)
            kotlinx.coroutines.delay(refreshIntervalMs)
        }
    }

    suspend fun insert()  {
        val report = Report(userId, today)
        reportDao.insertTodayReport(report)
    }




    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getTodayCityDepth(): Int = reportDao. getTodayCityDepth(userId, today)




}