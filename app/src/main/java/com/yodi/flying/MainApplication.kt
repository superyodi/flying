package com.yodi.flying

import android.app.Application
import com.yodi.flying.model.AppDatabase
import com.yodi.flying.model.repository.PomodoroRepository
import com.yodi.flying.model.repository.TagRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val pomodoroRepository by lazy { PomodoroRepository(database.pomodoroDao()) }

    val tagRepository by lazy { TagRepository(database.tagDao()) }
}