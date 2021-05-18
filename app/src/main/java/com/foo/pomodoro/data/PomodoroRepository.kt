package com.foo.pomodoro.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
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

}