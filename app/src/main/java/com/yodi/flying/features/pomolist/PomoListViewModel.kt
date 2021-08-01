package com.yodi.flying.viewmodels

import androidx.lifecycle.*
import com.yodi.flying.utils.Event
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.repository.PomodoroRepository
import com.yodi.flying.model.TimerState
import com.yodi.flying.service.TimerService
import kotlinx.coroutines.launch
import timber.log.Timber

class PomoListViewModel(private val repository: PomodoroRepository) : ViewModel() {

    val allPomos : LiveData<List<Pomodoro>> = repository.allPomodoros.asLiveData()

    var deletedPomooro : Pomodoro? = null

    private val _openTimerEvent = MutableLiveData<Event<Int>>()
    val openTimerEvent : LiveData<Event<Int>>
        get() = _openTimerEvent

    val timerState: LiveData<TimerState>
        get() = repository.getTimerServiceTimerState()

    val isTimerRunning: LiveData<Boolean>
        get() = repository.getTimerServiceTimerState().map {
            if(it != null){
                it != TimerState.EXPIRED
            }else false
        }

    val runningPomodoroId : Int?
        get() = TimerService.currentPomodoro.value?.id


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(pomodoro: Pomodoro) = viewModelScope.launch {
        repository.insert(pomodoro)
    }

    /**
     * Called by the [TasksAdapter].
     */
    internal fun openTimer(taskId: Int) {
        _openTimerEvent.value = Event(taskId)
    }

    fun delete(pomodoro: Pomodoro) = viewModelScope.launch {

        Timber.i(pomodoro.title)
        deletedPomooro = pomodoro
        repository.delete(pomodoro)

    }

}

class PomoListViewModelFactory(val repository: PomodoroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(PomoListViewModel::class.java)) {
            PomoListViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}