package com.foo.pomodoro.viewmodels

import androidx.lifecycle.*
import com.foo.pomodoro.Event
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository
import kotlinx.coroutines.launch

class PomoListViewModel(private val repository: PomodoroRepository) : ViewModel() {

    val allPomos : LiveData<List<Pomodoro>> = repository.allPomodoros.asLiveData()

    private val _openTimerEvent = MutableLiveData<Event<Int>>()
    val openTimerEvent : LiveData<Event<Int>>
        get() = _openTimerEvent


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