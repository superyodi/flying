package com.foo.pomodoro.viewmodels

import androidx.lifecycle.*
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository
import com.foo.pomodoro.data.PomodoroState
import com.foo.pomodoro.data.TimerState
import com.foo.pomodoro.service.TimerService
import com.foo.pomodoro.utils.*
import java.util.*

class TimerViewModel(
    private val pomodoroRepository: PomodoroRepository
) : ViewModel() {

    private lateinit var timerTask : Timer
    private val TAG = "PlantDetailViewModel"

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
                    getFormattedStopWatchTime(RUNNING_TIME)
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
