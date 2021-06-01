package com.foo.pomodoro.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
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
import com.foo.pomodoro.utils.NOTIFICATION_ID
import androidx.annotation.IntRange
import com.foo.pomodoro.utils.NOTIFICATION_COMPLETE_ID
import kotlin.concurrent.thread

class TimerService : Service(){

    private val notificationManager
        get() = getSystemService(NOTIFICATION_SERVICE) as NotificationManager



    override fun onCreate() {
        registerDefaultNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createDownloadingNotification(0))
        thread {
            for (i in 1..100) {
                Thread.sleep(100)
                updateProgres(i)
            }
            stopForeground(true)
            stopSelf()
            notificationManager.notify(NOTIFICATION_COMPLETE_ID, createCompleteNotification())
        }
        return START_STICKY
    }



    override fun onBind(p0: Intent?): IBinder?  = null

    private fun registerDefaultNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createDefaultNotificationChannel())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDefaultNotificationChannel() =
        NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW).apply {
            description = "뽀모도로 타이머"
            this.setShowBadge(true)
            this.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }


    private fun updateProgres(@IntRange(from = 0L, to = 100L) progress: Int) {
        notificationManager.notify(NOTIFICATION_ID, createDownloadingNotification(progress))
    }
    private fun createDownloadingNotification(@IntRange(from = 0L, to = 100L) progress: Int) =
        NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).apply {
            setContentTitle("Download video...")
            setContentText("Wait!")
            setSmallIcon(R.drawable.ic_launcher_background)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            setContentIntent(
                PendingIntent.getActivity(
                    this@TimerService, 0, Intent(this@TimerService, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }, 0
                )
            )

            setProgress(100, progress, false)
        }.build()

    private  fun createCompleteNotification() = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).apply {

        setContentTitle("뽀모도로 타이머 왼료")
        setContentText("🛩 비행기가 도쿄에 도착했습니다.")
        setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        setContentIntent(
            PendingIntent.getActivity(
                this@TimerService, 0, Intent(this@TimerService, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }, 0
            )
        )
    }.build()




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