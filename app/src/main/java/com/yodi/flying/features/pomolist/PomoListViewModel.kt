package com.yodi.flying.features.pomolist

import android.view.View
import androidx.lifecycle.*
import com.yodi.flying.model.PomodoroState
import com.yodi.flying.mvvm.Event
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.repository.PomodoroRepository
import com.yodi.flying.model.TimerState
import com.yodi.flying.model.repository.TicketRepository
import com.yodi.flying.mvvm.SingleLiveEvent
import com.yodi.flying.service.TimerService
import com.yodi.flying.utils.convertDateToString
import com.yodi.flying.utils.getFormattedStopWatchTime
import com.yodi.flying.utils.getFormattedTotalTime
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


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
            it != TimerState.EXPIRED
        }
    val runningPomodoroId : Long?
        get() = TimerService.currentPomodoro.value?.id

    val totalTime = MutableLiveData<Long>()
    val totalTimeString = totalTime.map {
        getFormattedTotalTime(it)
    }
    val todayDate = convertDateToString(Date(), "MM/dd yyyy")


    val titleText =  MutableLiveData<String>()
    val subTitleText =  MutableLiveData<String>()
    val iconResource = MutableLiveData<Int>()


    init {
        viewModelScope.launch {

            ticketRepository.getTotalTime().collect {
                totalTime.value = it
            }
        }
    }

    fun onTicketButtonClicked(view: View) {
        Timber.d("총시간: ${totalTimeString.value}")
        navigateToTicket.call()
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

