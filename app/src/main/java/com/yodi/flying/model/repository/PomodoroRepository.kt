package com.yodi.flying.model.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.TimerState
import com.yodi.flying.model.dao.PomodoroDao
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.service.TimerService
import com.yodi.flying.utils.Constants
import com.yodi.flying.utils.Constants.Companion.PREF_IS_AUTO_BREAK_MODE
import com.yodi.flying.utils.Constants.Companion.PREF_IS_AUTO_SKIP_MODE
import com.yodi.flying.utils.Constants.Companion.PREF_IS_NON_BREAK_MODE
import kotlinx.coroutines.flow.Flow


class PomodoroRepository(private val pomodoroDao: PomodoroDao, private val preferences:
SharedPreferenceManager) {


    val userId : Long
        get() = preferences.getLong(Constants.PREF_USER_ID)
    val runningTime : Long
        get() = preferences.getLong(Constants.PREF_RUNNING_TIME)
    val shortRestTime : Long
        get() = preferences.getLong(Constants.PREF_SHORT_REST_TIME)
    val longRestTime : Long
        get() = preferences.getLong(Constants.PREF_LONG_REST_TIME)
    val longRestTerm : Int
        get() = preferences.getInt(Constants.PREF_LONG_REST_TERM)
    val isAutoBreakMode : Boolean
        get() = preferences.getBoolen(PREF_IS_AUTO_BREAK_MODE)
    val isAutoSkipMode : Boolean
        get() = preferences.getBoolen(PREF_IS_AUTO_SKIP_MODE)
    val isNoneBreakMode : Boolean
        get() = preferences.getBoolen(PREF_IS_NON_BREAK_MODE)



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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteLastPomodoros(date : Long) {

    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateTodayPomodoros(date : Long) {
        pomodoroDao.deleteLastPomodoros(false)
        pomodoroDao.deleteLastPomodoros(true, date)
        pomodoroDao.updateHasDueDatePomodoros(true, date)

    }

    // return immutable livedata from timer service

    fun getTotalTime() = TimerService.currentTotalTime as LiveData<Long>
    fun getTimerServicePomodoroState() = TimerService.currentPomodoroState as LiveData<Int>
    fun getTimerServiceTimerState() = TimerService.currentTimerState as LiveData<TimerState>
    fun getTimerServiceRepetition() = TimerService.currentTomatoCount as LiveData<Int>
    fun getTimerServiceElapsedTimeMillisESeconds ()
            = TimerService.elapsedTimeInMillisEverySecond as LiveData<Long>
    fun getTimerServiceElapsedTimeMillis () = TimerService.elapsedTimeInMillis as LiveData<Long>

}