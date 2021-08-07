package com.yodi.flying.service

import android.app.NotificationManager
import android.app.PendingIntent
import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.repository.PomodoroRepository
import com.yodi.flying.model.PomodoroState
import com.yodi.flying.model.PomodoroState.Companion.NONE
import com.yodi.flying.model.TimerState

import com.yodi.flying.utils.*
import com.yodi.flying.utils.Constants.Companion.ACTION_CANCEL
import com.yodi.flying.utils.Constants.Companion.ACTION_CANCEL_AND_RESET
import com.yodi.flying.utils.Constants.Companion.ACTION_INITIALIZE_DATA
import com.yodi.flying.utils.Constants.Companion.ACTION_PAUSE
import com.yodi.flying.utils.Constants.Companion.ACTION_RESUME
import com.yodi.flying.utils.Constants.Companion.ACTION_START
import com.yodi.flying.utils.Constants.Companion.EXTRA_POMODORO_ID
import com.yodi.flying.utils.Constants.Companion.NOTIFICATION_ID
import com.yodi.flying.utils.Constants.Companion.TIMER_STARTING_IN_TIME
import com.yodi.flying.utils.Constants.Companion.TIMER_UPDATE_INTERVAL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber


class TimerService : LifecycleService(){


    // repository
    lateinit var pomodoroRepository: PomodoroRepository
    // current pomodoro
    private var pomodoro: Pomodoro? = null

    // service state
    private var isInitialized = false
    private var isKilled = true
    private var isBound = false
    private var pomodoroState = PomodoroState.FLYING

    // timer
    private var timer: CountDownTimer? = null
    private var millisToCompletion = 0L
    private var lastSecondTimestamp = 0L
    private var timerIndex = 0
    private var timerMaxRepetitions = 0


    // notification builder
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    lateinit var currentNotificationBuilder: NotificationCompat.Builder
    private val notificationManager
        get() = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


    // utility
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var wakeLock: PowerManager.WakeLock? = null

    // pending intents for notification action-handling
    lateinit var mainActivityPendingIntent: PendingIntent
    lateinit var resumeActionPendingIntent: PendingIntent
    lateinit var pauseActionPendingIntent: PendingIntent
    lateinit var cancelActionPendingIntent: PendingIntent

    companion object{
        // holds MutableLiveData for UI to observe
        val currentTimerState = MutableLiveData<TimerState>()
        val currentPomodoro = MutableLiveData<Pomodoro>()
        val currentPomodoroState = MutableLiveData<Int>()
        val currentTomatoCount = MutableLiveData<Int>()
        val elapsedTimeInMillis = MutableLiveData<Long>()
        val elapsedTimeInMillisEverySecond = MutableLiveData<Long>()

        var goalTomatoCount  = 0
        var nowTomatoCount = 0
    }



    override fun onCreate() {
        super.onCreate()

        pomodoroRepository = (application as MainApplication).pomodoroRepository

        initializeNotification()
        setupObservers()

    }

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
                    initializeData(it)
                }
                ACTION_START -> {
                    /*This is called when Start-Button is pressed, starting timer here and setting*/
                    Timber.i("ACTION_START")

                    startServiceTimer()
                }

                ACTION_PAUSE -> {
                    /*Called when pause button is pressed, pause timer, set isTimerRunning = false*/
                    Timber.i("ACTION_PAUSE")
                    pauseTimer()
                }

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
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // UI is visible, use service without being foreground
        Timber.i("onBind")
        isBound = true
        if(!isKilled) pushToBackground()
        return super.onBind(intent)
    }

    override fun onRebind(intent: Intent?) {
        // UI is visible again, push service to background -> notification are not visible
        Timber.i("onRebind")
        isBound = true
        if(!isKilled) pushToBackground()
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        // UI is not visible anymore, push service to foreground -> notifications visible
        Timber.i("onUnbind")
        isBound = false
        if(!isKilled) pushToForeground()
        // return true so onRebind is used if service is alive and client connects
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
        // cancel coroutine job
        serviceJob.cancel()
    }

    private fun initializeNotification(){
        mainActivityPendingIntent = provideMainActivityPendingIntent(this)
        resumeActionPendingIntent = provideResumeActionPendingIntent(this)
        pauseActionPendingIntent = providePauseActionPendingIntent(this)
        cancelActionPendingIntent = provideCancelActionPendingIntent(this)

        baseNotificationBuilder = provideBaseNotificationBuilder(this, mainActivityPendingIntent)
        currentNotificationBuilder = baseNotificationBuilder
    }

    private fun startServiceTimer(){

        isKilled = false
        resetTimer()
        currentTimerState.postValue(TimerState.RUNNING)
        startTimer()

    }

    private fun startTimer(wasPaused: Boolean = false){
        pomodoro?.let {

            // set pomodoro state
            if(pomodoroState == PomodoroState.NONE) {
                pomodoroState = PomodoroState.FLYING
            }
            currentPomodoroState.postValue(pomodoroState)

            if(currentPomodoroState.value == PomodoroState.NONE) {
                currentPomodoroState.postValue(PomodoroState.FLYING)
            }


            // time to count down
            val time = getTimeFromPomodoroState(wasPaused, pomodoroState, millisToCompletion)
            Timber.i("Starting timer - time: $time - pomodoroState: ${pomodoroState}")

            // post start values
            elapsedTimeInMillisEverySecond.postValue(time)
            elapsedTimeInMillis.postValue(time)
            lastSecondTimestamp = time

            //initialize timer and start
            timer = object : CountDownTimer(time, TIMER_UPDATE_INTERVAL){
                override fun onTick(millisUntilFinished: Long) {
                    /*handle what happens on every tick with interval of TIMER_UPDATE_INTERVAL*/
                    onTimerTick(millisUntilFinished)
                }

                override fun onFinish() {
                    /*handle finishing of a timer
                    * start new one if there are still repetition left*/
                    Timber.i("onFinish")
                    onTimerFinish()
                }
            }.start()
        }
    }
    private fun onTimerTick(millisUntilFinished: Long){
        millisToCompletion = millisUntilFinished
        elapsedTimeInMillis.postValue(millisUntilFinished)
        if(millisUntilFinished <= lastSecondTimestamp - 1000L){
            lastSecondTimestamp -= 1000L
            //Timber.i("onTick - lastSecondTimestamp: $lastSecondTimestamp")
            elapsedTimeInMillisEverySecond.postValue(lastSecondTimestamp)
        }
    }

    private fun onTimerFinish(){

        Timber.i("onTimerFinish - timerIndex: $timerIndex - maxRep: $timerMaxRepetitions")

        // increase timerIndex
        timerIndex += 1

        // check if index still in bound
        if(timerIndex != timerMaxRepetitions){

            // if timerIndex odd -> post one more tomato
            if(timerIndex % 2 != 0)
                nowTomatoCount++
                currentTomatoCount.postValue(nowTomatoCount)


            // get next workout state
            pomodoroState = getNextPomodoroState(pomodoroState, nowTomatoCount)

            Timber.i("currentTomatoCount: ${nowTomatoCount}- maxRep: $timerMaxRepetitions")


            // start new timer
            startTimer()

        }else{
            // finished all repetitions, cancel timer
            cancelTimer()
        }

        // update currentPomodoro
        serviceScope.launch {
            // 현재 pomodoro state, tomato count update
            currentPomodoro.value.let {
                if (it != null) {
                    it.state = pomodoroState
                    it.nowCount = currentTomatoCount.value ?: it.nowCount

                    pomodoroRepository.update(it)
                }
            }
        }


    }

    private fun cancelServiceTimer(){
        cancelTimer()
        currentTimerState.postValue(TimerState.EXPIRED)
        isKilled = true
        stopForeground(true)
    }



    private fun pauseTimer(){
        currentTimerState.postValue(TimerState.PAUSED)
        timer?.cancel()
    }

    private fun resumeTimer(){
        currentTimerState.postValue(TimerState.RUNNING)
        startTimer(wasPaused = true)
    }


    private fun cancelTimer(){
        timer?.cancel()
        resetTimer()
    }

    private fun resetTimer(){
        timerIndex = 0
        timerMaxRepetitions = pomodoro?.goalCount?.times(2) ?: 0
        pomodoroState = NONE
        postInitData()
    }


    private fun pushToBackground(){
        Timber.i("pushToBackground - isBound: $isBound")
        stopForeground(true)
    }

    private fun pushToForeground() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
        currentTimerState.value?.let { updateNotificationActions(it) }
    }

    private fun initializeData(intent: Intent) {
        if (!isInitialized) {
            intent.extras?.let {
                val id = it.getLong(EXTRA_POMODORO_ID)
                if (id != -1L) {
                    // id is valid
                    currentNotificationBuilder
                        .setContentIntent(buildTimeFragmentPendingIntentWithId(id, this))

                    // launch coroutine, fetch workout from db & audiostate from data store
                    serviceScope.launch {
                        pomodoro = pomodoroRepository.getPomodoro(id)
                        isInitialized = true
                        postInitData()
                    }

                }
            }
        }
    }

    private fun postInitData(){
        /*Post current data to MutableLiveData*/
        pomodoro?.let {
            currentTimerState.postValue(TimerState.EXPIRED)
            currentPomodoro.postValue(it)
            currentPomodoroState.postValue(PomodoroState.NONE)
            currentTomatoCount.postValue(it.nowCount)
            elapsedTimeInMillis.postValue(TIMER_STARTING_IN_TIME)
            elapsedTimeInMillisEverySecond.postValue(TIMER_STARTING_IN_TIME)

            goalTomatoCount = it.goalCount
            nowTomatoCount = it.nowCount
        }
    }
    private fun updateNotificationActions(state: TimerState){
        // Updates actions of current notification depending on TimerState
        val notificationActionText = if(state == TimerState.RUNNING) "Pause" else "Resume"

        // Build pendingIntent depending on TimerState
        val pendingIntent = if(state == TimerState.RUNNING){
            pauseActionPendingIntent
        }else{
            resumeActionPendingIntent
        }

        // Clear current actions
        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        // Set Action, icon seems irrelevant
        currentNotificationBuilder = baseNotificationBuilder
            .setContentTitle(pomodoro?.title)
            .addAction(R.drawable.ic_baseline_access_alarm_24, notificationActionText, pendingIntent)
            .addAction(R.drawable.ic_baseline_access_alarm_24, "Cancel", cancelActionPendingIntent)
        notificationManager.notify(NOTIFICATION_ID, currentNotificationBuilder.build())
    }

    private fun resetData(){
        isInitialized = false
        pomodoro = null
        currentTomatoCount.postValue(0)
        nowTomatoCount = 0

    }

    private fun setupObservers(){
        // observe timerState and update notification actions
        currentTimerState.observe(this, {
            Timber.i("currentTimerState changed - ${it.stateName}")
            if(!isKilled && !isBound)
                updateNotificationActions(it)
        })

        // Observe timeInMillis and update notification
        elapsedTimeInMillisEverySecond.observe(this, {
            if (!isKilled && !isBound) {
                // Only do something if timer is running and service in foreground
                val notification = currentNotificationBuilder
                    .setContentText(getFormattedStopWatchTime(it))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        })
    }





}