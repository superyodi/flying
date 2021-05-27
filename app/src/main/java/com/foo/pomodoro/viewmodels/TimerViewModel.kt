package com.foo.pomodoro.viewmodels

import android.os.NetworkOnMainThreadException
import android.util.Log
import androidx.lifecycle.*
import com.foo.pomodoro.Event
import com.foo.pomodoro.TimerFragment.Companion.RUNNING_TIME
import com.foo.pomodoro.TimerFragment.Companion.SHORT_REST_TIME
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository
import com.foo.pomodoro.data.PomodoroState
import com.foo.pomodoro.data.PomodoroState.Companion.LONG_BREAK
import com.foo.pomodoro.data.PomodoroState.Companion.NONE
import com.foo.pomodoro.data.PomodoroState.Companion.SHORT_BREAK
import com.foo.pomodoro.data.TimerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer

class TimerViewModel(
    private val pomodoroRepository: PomodoroRepository,
    private val pomodoroId: Int

) : ViewModel() {

    private lateinit var timerTask : Timer
    private val TAG = "PlantDetailViewModel"



    private val _pomodoro = MutableLiveData<Pomodoro>()
    val pomodoro : LiveData<Pomodoro>
        get() =  _pomodoro

    private val _pomodoroState = MutableLiveData<Int>()
    val timerState : LiveData<Int>
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



    init {
        loadPomodoro()
    }

    fun loadPomodoro() {
        viewModelScope.launch {

            _pomodoro.value = pomodoroRepository.getPomodoro(pomodoroId)
            _pomodoroState.value = _pomodoro.value!!.state
            _timerNowCount.value = _pomodoro.value!!.nowCount
            _timerGoalCount.value = _pomodoro.value!!.goalCount

        }
    }

    fun plusTomatoCount() {
        val cnt = _timerNowCount.value
        if (cnt != null) {
            _timerNowCount.value = cnt + 1
        }
    }

    fun setPomodoroState(cnt : Int) {

        val goalCount = _timerGoalCount.value


        when(cnt) {

            4 ->
                _pomodoroState.value = PomodoroState.LONG_BREAK

            goalCount ->
                _pomodoroState.value = PomodoroState.FINISHED

        }


    }



}

class TimerViewModelFactory(val repository: PomodoroRepository, val pomoId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {


        return if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            TimerViewModel(repository, pomoId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
