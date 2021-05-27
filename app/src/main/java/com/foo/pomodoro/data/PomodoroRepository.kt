package com.foo.pomodoro.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.foo.pomodoro.service.TimerService
import kotlinx.coroutines.flow.Flow


class PomodoroRepository(private val pomodoroDao: PomodoroDao) {



    val allPomodoros : Flow<List<Pomodoro>> = pomodoroDao.getPomodoros()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getPomodoro(pomoId: Int): Pomodoro = pomodoroDao.getPomodoro(pomoId)



    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(pomodoro: Pomodoro) {
        pomodoroDao.insert(pomodoro)
    }

    // return immutable livedata from timer service

    //fun getTimerServiceWorkoutState() = TimerService.currentWorkoutState as LiveData<WorkoutState>
    fun getTimerServiceTimerState() = TimerService.currentTimerState as LiveData<TimerState>
    fun getTimerServiceRepetition() = TimerService.currentTomatoCount as LiveData<Int>
    fun getTimerServiceElapsedTimeMillisESeconds ()
            = TimerService.elapsedTimeInMillisEverySecond as LiveData<Long>
    fun getTimerServiceElapsedTimeMillis () = TimerService.elapsedTimeInMillis as LiveData<Long>

}