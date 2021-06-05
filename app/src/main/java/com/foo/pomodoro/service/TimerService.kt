package com.foo.pomodoro.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.TimerState

import com.foo.pomodoro.utils.*
import kotlinx.coroutines.launch
import timber.log.Timber

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

    }


    override fun onBind(p0: Intent?): IBinder?  = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Handle action from the activity
        intent?.let{
            when(it.action){
                // Timer related actions
                ACTION_INITIALIZE_DATA -> {
                    /*Is called when navigating from ListScreen to DetailScreen, fetching data
                    * from database here -> data initialization*/
                    Timber.i("ACTION_INITIALIZE_DATA")
//                    initializeData(it)
                }
                ACTION_START -> {
                    /*This is called when Start-Button is pressed, starting timer here and setting*/
                    Timber.i("ACTION_START")

                    // test
                    pushToForeground()
                    initializeData(it)

//                    startServiceTimer()
                }


                ACTION_PAUSE -> {
                    /*Called when pause button is pressed, pause timer, set isTimerRunning = false*/
                    Timber.i("ACTION_PAUSE")
//                    pauseTimer()
                }
                /*
                ACTION_RESUME -> {
                    /*Called when resume button is pressed, resume timer here, set isTimerRunning
                    * = true*/
                    Timber.i("ACTION_RESUME")
                    resumeTimer()
                }
                ACTION_CANCEL -> {
                    /*This is called when cancel button is pressed - resets the current timer to
                    * start state*/
                    Timber.i("ACTION_CANCEL")
                    cancelServiceTimer()
                }
                ACTION_CANCEL_AND_RESET -> {
                    /*Is called when navigating back to ListsScreen, resetting acquired data
                    * to null*/
                    Timber.i("ACTION_CANCEL_AND_RESET")
                    cancelServiceTimer()
                    resetData()
                }

                 */
            }
        }
        return START_STICKY
    }


    private fun initializeNotification(){
        mainActivityPendingIntent = provideMainActivityPendingIntent(this)
        resumeActionPendingIntent = provideResumeActionPendingIntent(this)
        pauseActionPendingIntent = providePauseActionPendingIntent(this)
        cancelActionPendingIntent = provideCancelActionPendingIntent(this)

        baseNotificationBuilder = provideBaseNotificationBuilder(this, mainActivityPendingIntent)
        currentNotificationBuilder = baseNotificationBuilder
    }


    // 지금은 그냥 테스트
    private fun pushToForeground() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
    }

    private fun initializeData(intent: Intent){
        intent.extras?.let {
            val id = it.getInt(EXTRA_TIMER_ID)
            if(id != -1){
                // id is valid
                currentNotificationBuilder
                    .setContentIntent(buildTimeFragmentPendingIntentWithId(id, this))

            }
        }
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