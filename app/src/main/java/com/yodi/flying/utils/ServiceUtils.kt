package com.yodi.flying.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.yodi.flying.MainActivity
import com.yodi.flying.model.PomodoroState.Companion.LONG_BREAK
import com.yodi.flying.model.PomodoroState.Companion.NONE
import com.yodi.flying.model.PomodoroState.Companion.SHORT_BREAK
import com.yodi.flying.service.TimerService
import com.yodi.flying.service.TimerService.Companion.LONG_BREAK_TIME
import com.yodi.flying.service.TimerService.Companion.RUNNING_TIME
import com.yodi.flying.service.TimerService.Companion.SHORT_BREAK_TIME
import com.yodi.flying.utils.Constants.Companion.ACTION_CANCEL
import com.yodi.flying.utils.Constants.Companion.ACTION_PAUSE
import com.yodi.flying.utils.Constants.Companion.ACTION_RESUME

import com.yodi.flying.utils.Constants.Companion.TIMER_STARTING_IN_TIME


fun provideMainActivityPendingIntent(
    app: Context
): PendingIntent = PendingIntent.getActivity(
    app,
    0,
    Intent(app, MainActivity::class.java).also {
        it.action = Constants.ACTION_SHOW_MAIN_ACTIVITY
    },
    PendingIntent.FLAG_UPDATE_CURRENT
)



fun provideCancelActionPendingIntent(
    app: Context
): PendingIntent = PendingIntent.getService(
    app,
    1,
    Intent(app, TimerService::class.java).also {
        it.action = ACTION_CANCEL
    },
    PendingIntent.FLAG_UPDATE_CURRENT
)



fun provideResumeActionPendingIntent(
    app: Context
): PendingIntent = PendingIntent.getService(
    app,
    2,
    Intent(app, TimerService::class.java).also {
        it.action = ACTION_RESUME
    },
    PendingIntent.FLAG_UPDATE_CURRENT
)


fun providePauseActionPendingIntent(
    app: Context
): PendingIntent = PendingIntent.getService(
    app,
    3,
    Intent(app, TimerService::class.java).also {
        it.action = ACTION_PAUSE
    },
    PendingIntent.FLAG_UPDATE_CURRENT
)

fun getTimeFromPomodoroState(
    wasPaused: Boolean,
    state: Int,
    currentTime: Long
): Long {
    return if (wasPaused) currentTime
    else {
        when (state) {
            NONE -> TIMER_STARTING_IN_TIME
            SHORT_BREAK -> SHORT_BREAK_TIME
            LONG_BREAK -> LONG_BREAK_TIME
            else -> RUNNING_TIME
        }
    }
}
