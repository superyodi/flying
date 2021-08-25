package com.yodi.flying.features.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.yodi.flying.model.PomodoroState
import com.yodi.flying.model.TimerState
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.repository.PomodoroRepository
import com.yodi.flying.service.TimerService
import com.yodi.flying.utils.Constants.Companion.TIMER_STARTING_IN_TIME
import com.yodi.flying.utils.getFormattedStopWatchTime
import java.util.*

class TimerViewModel(
    private val pomodoroRepository: PomodoroRepository
) : ViewModel() {

    private lateinit var timerTask : Timer

    private val runningTime : Long
        get() = pomodoroRepository.runningTime
    val pomodoro : LiveData<Pomodoro?>
        get() = TimerService.currentPomodoro
    val pomodoroState : LiveData<Int>
        get()  = pomodoroRepository.getTimerServicePomodoroState()
    val timerGoalCount : Int?
        get() = pomodoro.value?.goalCount
    val timerNowCount : Int?
       get() = pomodoro.value?.nowCount
    val timerState: LiveData<TimerState>
        get() = pomodoroRepository.getTimerServiceTimerState()
    val timeString: LiveData<String>
        get() = pomodoroRepository.getTimerServiceElapsedTimeMillisESeconds().map {
            if(pomodoro.value != null){
                if(pomodoroState.value != PomodoroState.NONE && timerState.value != TimerState.EXPIRED)
                    getFormattedStopWatchTime(it)
                else
                    getFormattedStopWatchTime(runningTime)
            }else ""
        }



    val elapsedTime: LiveData<Long>
        get() = pomodoroRepository.getTimerServiceElapsedTimeMillis().map {
            if(timerState.value != TimerState.EXPIRED)
                it
            else
                TIMER_STARTING_IN_TIME
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
