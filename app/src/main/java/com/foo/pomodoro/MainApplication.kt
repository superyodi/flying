package com.foo.pomodoro

import android.app.Application
import com.foo.pomodoro.data.AppDatabase
import com.foo.pomodoro.data.PomodoroRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getInstance(this, applicationScope) }
    val repository by lazy { PomodoroRepository(database.pomodoroDao()) }
}