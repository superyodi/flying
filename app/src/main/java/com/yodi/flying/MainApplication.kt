package com.yodi.flying

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.yodi.flying.model.AppDatabase
import com.yodi.flying.model.SharedPreferenceManager
import com.yodi.flying.model.repository.PomodoroRepository
import com.yodi.flying.model.repository.TagRepository
import com.yodi.flying.model.repository.UserRepository
import com.yodi.flying.utils.Constants.Companion.KAKAO_SDK_APP_KEY

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }

    val sharedPreferences by lazy { SharedPreferenceManager(applicationContext)}

    val pomodoroRepository by lazy { PomodoroRepository(database.pomodoroDao()) }

    val tagRepository by lazy { TagRepository(database.tagDao()) }

    val userRepository by lazy { UserRepository(database.userDao(), sharedPreferences)}



    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, KAKAO_SDK_APP_KEY)
    }


}