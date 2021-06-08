package com.foo.pomodoro.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.foo.pomodoro.MainActivity
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroState
import com.foo.pomodoro.data.PomodoroState.Companion.LONG_BREAK
import com.foo.pomodoro.data.PomodoroState.Companion.NONE
import com.foo.pomodoro.data.PomodoroState.Companion.SHORT_BREAK
import com.foo.pomodoro.service.TimerService


fun provideMainActivityPendingIntent(
    app: Context
): PendingIntent = PendingIntent.getActivity(
    app,
    0,
    Intent(app, MainActivity::class.java).also {
        it.action = ACTION_SHOW_MAIN_ACTIVITY
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
