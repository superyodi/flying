package com.yodi.flying.features.pomolist

import android.view.View
import androidx.lifecycle.*
import com.yodi.flying.R
import com.yodi.flying.mvvm.Event
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.repository.PomodoroRepository
import com.yodi.flying.model.TimerState
import com.yodi.flying.model.repository.TicketRepository
import com.yodi.flying.mvvm.SingleLiveEvent
import com.yodi.flying.service.TimerService
import com.yodi.flying.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


class PomoListViewModel(private val pomodoroRepository: PomodoroRepository,
                        private val ticketRepository: TicketRepository) : ViewModel() {

    val allPomos : LiveData<List<Pomodoro>> = pomodoroRepository.getPomodoros().asLiveData()
    var deletedPomooro : Pomodoro? = null

    val navigateToTicket : SingleLiveEvent<Void> = SingleLiveEvent()


    private val _openTimerEvent = MutableLiveData<Event<Int>>()
    val openTimerEvent : LiveData<Event<Int>>
        get() = _openTimerEvent

    val timerState: LiveData<TimerState>
        get() = pomodoroRepository.getTimerServiceTimerState()

    val isTimerRunning: LiveData<Boolean>
        get() = pomodoroRepository.getTimerServiceTimerState().map {
            it != TimerState.EXPIRED && it != TimerState.DONE
        }
    val runningPomodoroId : Long?
        get() = TimerService.currentPomodoro.value?.id

    val totalTime = MutableLiveData<Long>()

    val leftGoalTimeString = totalTime.map {
        val goalTime = ticketRepository.getUserGoalTime()
        Timber.d(getFormattedTotalTime(goalTime))
        if(goalTime- it <= 0) {
            "일일 목표 시간을 달성하셨습니다!🥳🎉"
        }
        else "Left by " + getFormattedTotalTime(goalTime- it)
    }


    val totalTimeString = totalTime.map {
        getFormattedTotalTime(it)
    }
    val todayDate = convertDateToString(Date(), "MM/dd yyyy")

    val currentCity = totalTime.map {
        getCityFromTotalTime(it)
    }

    val nextCity = currentCity.map {
        if( it == Constants.MOON) "?"
        else getNextCity(it)
    }

    val ticketBackgroundResource = MutableLiveData<Int>()


    init {
        viewModelScope.launch {

            ticketRepository.getTotalTimeFlow().collect {
                totalTime.value = it
            }

        }
    }


//    fun onTicketButtonClicked(view: View) {
//
//        navigateToTicket.call()
//    }


    // test code
    fun onTicketButtonClicked(view: View) {
        // insertTestData()

        if (currentCity.value == Constants.MOON) resetTestData()
        else insertTestData()

    }


    private fun insertTestData() = viewModelScope.launch {
        ticketRepository.updateTodayTotalTime(
            TimeUnit.HOURS.toMillis(2.toLong())
        )

        Timber.d("hour to long: ${TimeUnit.HOURS.toMillis(2.toLong())}")
    }

    private fun resetTestData() = viewModelScope.launch {
        ticketRepository.resetTotalTime()

    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(pomodoro: Pomodoro) = viewModelScope.launch {
        pomodoroRepository.insert(pomodoro)
    }


    fun delete(pomodoro: Pomodoro) = viewModelScope.launch {

        Timber.i(pomodoro.title)
        deletedPomooro = pomodoro
        pomodoroRepository.delete(pomodoro)

    }
    fun setTicketBackgroundResource(city: String) {
        when (city) {
            Constants.JEJU -> ticketBackgroundResource.value = R.drawable.jeju_1
            Constants.TOKYO -> ticketBackgroundResource.value = R.drawable.tokyo_2
            Constants.HANOI -> ticketBackgroundResource.value = R.drawable.hanoi_3
            Constants.HAWAII -> ticketBackgroundResource.value = R.drawable.hawai_4
            Constants.NEWYORK -> ticketBackgroundResource.value = R.drawable.newyork_5
            Constants.HAVANA -> ticketBackgroundResource.value = R.drawable.havanna_6
            Constants.MOON -> ticketBackgroundResource.value = R.drawable.moon_7
            else -> ticketBackgroundResource.value = R.drawable.ticket_background

        }
    }

}

class PomoListViewModelFactory(private val pomodoroRepository: PomodoroRepository, private val ticketRepository: TicketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(PomoListViewModel::class.java)) {
            PomoListViewModel(pomodoroRepository, ticketRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

