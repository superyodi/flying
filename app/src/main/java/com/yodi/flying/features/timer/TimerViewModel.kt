package com.yodi.flying.features.timer

import android.view.View
import androidx.lifecycle.*
import com.yodi.flying.R
import com.yodi.flying.model.PomodoroState
import com.yodi.flying.model.TimerState
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.repository.PomodoroRepository
import com.yodi.flying.model.repository.TicketRepository
import com.yodi.flying.mvvm.SingleLiveEvent
import com.yodi.flying.service.TimerService
import com.yodi.flying.utils.Constants
import com.yodi.flying.utils.Constants.Companion.TIMER_STARTING_IN_TIME
import com.yodi.flying.utils.convertDateToString
import com.yodi.flying.utils.getCityFromTotalTime
import com.yodi.flying.utils.getFormattedStopWatchTime
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class TimerViewModel(
    private val pomodoroRepository: PomodoroRepository,
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

    val totalTime = MutableLiveData<Long>()

    val currentCity : LiveData<String> = pomodoroRepository.getTotalTime().map {
        getCityFromTotalTime(it)
    }
    val timerBackgroundResource = MutableLiveData<Int>()




    fun setTimerBackgroundResource(city: String) {
        when (city) {
            Constants.JEJU -> timerBackgroundResource.value = R.drawable.jeju_timer_1
            Constants.TOKYO -> timerBackgroundResource.value = R.drawable.tokyo_timer_2
            Constants.HANOI -> timerBackgroundResource.value = R.drawable.hanoi_timer_3
            Constants.HAWAII -> timerBackgroundResource.value = R.drawable.hawaii_timer_4
            Constants.NEWYORK -> timerBackgroundResource.value = R.drawable.newyork_timer_5
            Constants.HAVANA -> timerBackgroundResource.value = R.drawable.havana_timer_6
            Constants.MOON -> timerBackgroundResource.value = R.drawable.moon_timer_7
            else -> timerBackgroundResource.value = R.drawable.jeju_timer_1

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
