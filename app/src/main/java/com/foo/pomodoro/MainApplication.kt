package com.foo.pomodoro

import android.app.Application
import com.foo.pomodoro.data.AppDatabase
import com.foo.pomodoro.data.PomodoroRepository
import com.foo.pomodoro.data.TagRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val pomodoroRepository by lazy { PomodoroRepository(database.pomodoroDao()) }

    val tagRepository by lazy { TagRepository(database.tagDao()) }
}