package com.yodi.flying.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.dao.ReportDao
import com.yodi.flying.model.dao.TaskDao
import com.yodi.flying.model.dao.TicketDao
import com.yodi.flying.model.entity.*
import com.yodi.flying.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TicketRepository(
    private val ticketDao: TicketDao,
    private val reportDao: ReportDao,
    private val taskDao: TaskDao,
    private val preferences: SharedPreferenceManager

) {

    val userId : Long
        get() = preferences.getLong(Constants.PREF_USER_ID)

    val todayDate : Long
        get() = preferences.getLong(Constants.PREF_TODAY_DATE)

    private val refreshIntervalMs : Long = 10000

    private suspend fun getTickets(): List<Ticket> = ticketDao.getTickets(userId, todayDate)

    suspend fun getTicketsWithTasks() : List<TicketWithTasks> {

        val ticketsWithTasks = mutableListOf<TicketWithTasks>()
        val job = CoroutineScope(Dispatchers.IO).launch {
            getTickets().forEach {
                val tasks = getTasksForCity(it.startTime)
                val ticketsWithTask = TicketWithTasks(
                    todayDate,
                    it.startTime,
                    it.depth,
                    it.endTime,
                    tasks
                )
                ticketsWithTasks.add(ticketsWithTask)
            }
        }

        job.join()
        return ticketsWithTasks
    }
    suspend fun getLatestTicket() : Ticket? = ticketDao.getLatestTicket(userId, todayDate)

    suspend fun getTodayCityDepth(): Int = reportDao. getTodayCityDepth(userId, todayDate)

    suspend fun updateTodayCityDepth() = reportDao.updateTodayCityDepth(userId, todayDate)

    suspend fun getTotalTimeFlow(): Flow<Long> = flow {
        while (true) {
            val latestTotalTime = reportDao.getTodayTotalTime(userId, todayDate)

            emit(latestTotalTime)
            kotlinx.coroutines.delay(refreshIntervalMs)
        }
    }

    suspend fun updateTicket(ticket: Ticket) = ticketDao.update(ticket)



    suspend fun getTasksForCity(cityTime : Long) = taskDao.getTasksForCity(userId, todayDate, cityTime)

    suspend fun getTaskForCity(cityTime : Long, pomoId : Long) =
        taskDao.getTaskForCity(userId, todayDate, cityTime, pomoId)


    fun getUserGoalTime() = preferences.getLong(Constants.PREF_USER_GOAL_TIME)

    suspend fun insertTask(cityTime: Long, pomoId: Long, count : Int, totalTime : Long, title: String) {
        taskDao.insert(
            Task(userId, todayDate, cityTime, pomoId, count, totalTime, title)
        )
    }

    suspend fun updateTask(task : Task) = taskDao.update(task)



    suspend fun getTotalTime() = reportDao.getTodayTotalTime(userId, todayDate)

    suspend fun insertTicket(startTime : Long, depth : Int) {
        ticketDao.insert(
            Ticket(userId, todayDate, startTime, depth)
        )
    }

    suspend fun updateTodayTotalTime(runningTime : Long) {
        reportDao.updateTotalTime(userId, todayDate, runningTime)
    }

    suspend fun resetTotalTime() {
        reportDao.resetTotalTime(userId, todayDate)
    }






}