package com.foo.pomodoro.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

import androidx.lifecycle.MutableLiveData
import com.foo.pomodoro.MainActivity
import com.foo.pomodoro.R
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.TimerState
import com.foo.pomodoro.utils.NOTIFICATION_CHANNEL_ID
import com.foo.pomodoro.utils.NOTIFICATION_CHANNEL_NAME
import androidx.annotation.IntRange
import com.foo.pomodoro.utils.NOTIFICATION_COMPLETE_ID
import kotlin.concurrent.thread

import com.foo.pomodoro.utils.*

class TimerService : Service(){

    // notification builder
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    lateinit var currentNotificationBuilder: NotificationCompat.Builder
    private val notificationManager
        get() = getSystemService(NOTIFICATION_SERVICE) as NotificationManager



    // pending intents for notification action-handling
    lateinit var mainActivityPendingIntent: PendingIntent
    lateinit var resumeActionPendingIntent: PendingIntent
    lateinit var pauseActionPendingIntent: PendingIntent
    lateinit var cancelActionPendingIntent: PendingIntent


    override fun onCreate() {

        initializeNotification()
        pushToForeground()
    }






    override fun onBind(p0: Intent?): IBinder?  = null


    private fun initializeNotification(){
        mainActivityPendingIntent = PendingIntent.getActivity(
            this@TimerService, 0, Intent(this@TimerService, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }, 0
        )
        baseNotificationBuilder = provideBaseNotificationBuilder(this, mainActivityPendingIntent)
        resumeActionPendingIntent = provideResumeActionPendingIntent(this)
        pauseActionPendingIntent = providePauseActionPendingIntent(this)
        cancelActionPendingIntent = provideCancelActionPendingIntent(this)

        currentNotificationBuilder = baseNotificationBuilder


    }

    // 지금은 그냥 테스트
    private fun pushToForeground() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

    }












    companion object{
        // holds MutableLiveData for UI to observe
        val currentTimerState = MutableLiveData<TimerState>()
        val currentPomodoro = MutableLiveData<Pomodoro>()
        val currentPomodoroState = MutableLiveData<Int>()
        val currentTomatoCount = MutableLiveData<Int>()
        val elapsedTimeInMillis = MutableLiveData<Long>()
        val elapsedTimeInMillisEverySecond = MutableLiveData<Long>()
    }


}