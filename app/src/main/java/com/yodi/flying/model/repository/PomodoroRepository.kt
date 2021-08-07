package com.yodi.flying.model.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.TimerState
import com.yodi.flying.model.dao.PomodoroDao
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.service.TimerService
import com.yodi.flying.utils.Constants
import kotlinx.coroutines.flow.Flow


class PomodoroRepository(private val pomodoroDao: PomodoroDao, private val preferences:
SharedPreferenceManager) {


    val userId : Long
        get() = preferences.getLong(Constants.PREF_USER_ID)

    fun getPomodoros(): Flow<List<Pomodoro>> = pomodoroDao.getPomodoros(userId)


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(pomodoro: Pomodoro) {
        pomodoroDao.insert(pomodoro)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getPomodoro(pomoId: Long): Pomodoro = pomodoroDao.getPomodoro(pomoId, userId)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(pomodoro: Pomodoro) {
        pomodoroDao.delete(pomodoro)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(pomodoro: Pomodoro) {
        pomodoroDao.updatePomodoro(pomodoro)
    }

    // return immutable livedata from timer service

    fun getTimerServicePomodoroState() = TimerService.currentPomodoroState as LiveData<Int>
    fun getTimerServiceTimerState() = TimerService.currentTimerState as LiveData<TimerState>
    fun getTimerServiceRepetition() = TimerService.currentTomatoCount as LiveData<Int>
    fun getTimerServiceElapsedTimeMillisESeconds ()
            = TimerService.elapsedTimeInMillisEverySecond as LiveData<Long>
    fun getTimerServiceElapsedTimeMillis () = TimerService.elapsedTimeInMillis as LiveData<Long>

}