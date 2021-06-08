package com.foo.pomodoro.viewmodels

import android.os.NetworkOnMainThreadException
import android.util.Log
import androidx.lifecycle.*
import com.foo.pomodoro.Event
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository
import com.foo.pomodoro.data.PomodoroState
import com.foo.pomodoro.data.PomodoroState.Companion.FLYING
import com.foo.pomodoro.data.PomodoroState.Companion.LONG_BREAK
import com.foo.pomodoro.data.PomodoroState.Companion.SHORT_BREAK
import com.foo.pomodoro.data.TimerState
import com.foo.pomodoro.service.TimerService
import com.foo.pomodoro.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timer

class TimerViewModel(
    private val pomodoroRepository: PomodoroRepository
) : ViewModel() {

    private lateinit var timerTask : Timer
    private val TAG = "PlantDetailViewModel"


    val pomodoro : LiveData<Pomodoro?>
        get() = TimerService.currentPomodoro







    private val _pomodoroState = MutableLiveData<Int>()
    val pomodoroState : LiveData<Int>
        get() = _pomodoroState

    private val _timerGoalCount = MutableLiveData<Int>()
    val timerGoalCount : LiveData<Int>
        get() = _timerGoalCount

    private val _timerNowCount = MutableLiveData<Int>()
    val timerNowCount : LiveData<Int>
        get() = _timerNowCount

    private val _timeLeft = MutableLiveData<Long>()
    val timeLeft : LiveData<Long>
        get() = _timeLeft


    val timerStateString: LiveData<String>
        get() = timerNowCount.map {

            if (pomodoro.value != null && it != -1) {
                val goalCount = pomodoro.value!!.goalCount

                when (it) {
                    4 -> "Long Short"
                    in 0..goalCount ->"$it/${goalCount}"
                    else -> "타이머가 끝났습니다."
                }
            }
            else {
                "상태 알 수 없음"
            }

        }

    val timerState: LiveData<TimerState>
        get() = pomodoroRepository.getTimerServiceTimerState()

    val repString: LiveData<String>
        get() = pomodoroRepository.getTimerServiceRepetition().map {
            if(pomodoro.value != null && it != -1) "$it/${pomodoro.value?.goalCount}"
            else ""
        }

    val timeString: LiveData<String>
        get() = pomodoroRepository.getTimerServiceElapsedTimeMillisESeconds().map {
            if(pomodoro.value != null){
                if(timerState.value != TimerState.EXPIRED)
                    getFormattedStopWatchTime(it)
                else
                    getFormattedStopWatchTime(TIMER_STARTING_IN_TIME)
            }else ""
        }

    val elapsedTime: LiveData<Long>
        get() = pomodoroRepository.getTimerServiceElapsedTimeMillis().map {
            //Timber.i("elapsedTime: $it")
            if(timerState.value != TimerState.EXPIRED)
                it
            else
                TIMER_STARTING_IN_TIME
        }



    fun plusTomatoCount() {
        val cnt = _timerNowCount.value
        if (cnt != null) {
            _timerNowCount.value = cnt + 1
        }
    }



}

class TimerViewModelFactory(val repository: PomodoroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {


        return if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            TimerViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
