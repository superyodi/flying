package com.foo.pomodoro.service

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
import com.foo.pomodoro.MainApplication
import com.foo.pomodoro.R
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository
import com.foo.pomodoro.data.PomodoroState
import com.foo.pomodoro.data.PomodoroState.Companion.NONE
import com.foo.pomodoro.data.TimerState

import com.foo.pomodoro.utils.*
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

                */
                ACTION_CANCEL_AND_RESET -> {
                    /*Is called when navigating back to ListsScreen, resetting acquired data
                    * to null*/
                    Timber.i("ACTION_CANCEL_AND_RESET")
                    resetData()
                }


            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // UI is visible, use service without being foreground
        isBound = true
        if(!isKilled) pushToForeground()
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
        // get wakelock
//        acquireWakelock()
        isKilled = false
        resetTimer()
        startTimer()
        currentTimerState.postValue(TimerState.RUNNING)
    }

    private fun startTimer(wasPaused: Boolean = false){
        pomodoro?.let {

            // time to count down
            val time = getTimeFromPomodoroState(wasPaused, pomodoroState, millisToCompletion)
            Timber.i("Starting timer - time: $time - workoutState: ${pomodoroState}")

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
        // increase timerIndex
        timerIndex += 1
        Timber.i("onTimerFinish - timerIndex: $timerIndex - maxRep: $timerMaxRepetitions")

        // check if index still in bound
        if(timerIndex < timerMaxRepetitions){
            // if timerIndex odd -> post new rep
            if(timerIndex % 2 != 0)
                currentTomatoCount.postValue(currentTomatoCount.value?.plus(1))

            val _currentTomatoCount = currentTomatoCount.value ?: 0
            // get next workout state
            pomodoroState = getNextPomodoroState(pomodoroState, _currentTomatoCount)

            currentPomodoroState.postValue(pomodoroState)

            // start new timer
            startTimer()
        }else{
            // finished all repetitions, cancel timer
            cancelTimer()
        }
    }

    private fun cancelTimer(){
        timer?.cancel()
        resetTimer()
    }

    private fun resetTimer(){
        timerIndex = 0
        timerMaxRepetitions = pomodoro?.goalCount?.times(2)?.minus(2) ?: 0
        pomodoroState = NONE
        postInitData()
    }


    private fun pushToBackground(){
        Timber.i("pushToBackground - isBound: $isBound")
        stopForeground(true)
    }

    // 지금은 그냥 테스트
    private fun pushToForeground() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
    }

    private fun initializeData(intent: Intent) {
        if (!isInitialized) {
            intent.extras?.let {
                val id = it.getInt(EXTRA_POMODORO_ID)
                if (id != -1) {
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
            currentPomodoroState.postValue(it.state)
            currentTomatoCount.postValue(it.nowCount)
            elapsedTimeInMillis.postValue(TIMER_STARTING_IN_TIME)
            elapsedTimeInMillisEverySecond.postValue(TIMER_STARTING_IN_TIME)

            goalTomatoCount = it.goalCount
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

    }

    private fun setupObservers(){
        // observe timerState and update notification actions
        currentTimerState.observe(this, Observer {
            Timber.i("currentTimerState changed - ${it.stateName}")
            if(!isKilled && !isBound)
                updateNotificationActions(it)
        })

        // Observe timeInMillis and update notification
        elapsedTimeInMillisEverySecond.observe(this, Observer {
            if (!isKilled && !isBound) {
                // Only do something if timer is running and service in foreground
                val notification = currentNotificationBuilder
                    .setContentText(getFormattedStopWatchTime(it))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        })
    }





}