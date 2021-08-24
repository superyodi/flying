package com.yodi.flying.model.repository

import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.dao.ReportDao
import com.yodi.flying.model.dao.TicketDao
import com.yodi.flying.model.entity.Report
import com.yodi.flying.utils.Constants

class ReportRepository (private val reportDao: ReportDao,
                        private val preferences: SharedPreferenceManager) {

    private val userId : Long
        get() = preferences.getLong(Constants.PREF_USER_ID)

    val todayDate : Long
        get() = preferences.getLong(Constants.PREF_TODAY_DATE)

    fun setTodayDateToPreferences(date : Long) = preferences.setLong(Constants.PREF_TODAY_DATE, date)

    suspend fun insertReport(date: Long) {
        reportDao.insertTodayReport(Report(userId, date))
    }

    suspend fun getReport(date: Long): Report = reportDao.getReport(userId, date)


}