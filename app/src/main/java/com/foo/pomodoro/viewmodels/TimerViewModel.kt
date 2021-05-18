package com.foo.pomodoro.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.foo.pomodoro.Event
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

class TimerViewModel(
    private val pomodoroRepository: PomodoroRepository,
    private val pomodoroId: Int

) : ViewModel() {


    private val TAG = "PlantDetailViewModel"
    private val _pomodoro = MutableLiveData<Pomodoro>()
    val pomodoro : LiveData<Pomodoro> = _pomodoro


    private val _timerState = MutableLiveData<Int>()
    val timerState : LiveData<Int> = _timerState

    init {

        loadPomodoro()
//        _timerState.value = _pomodoro.value?.state
    }




    fun loadPomodoro() {
        viewModelScope.launch {

            _pomodoro.value = pomodoroRepository.getPomodoro(pomodoroId)

            Log.d(TAG, _pomodoro.value?.title+ _pomodoro.value?.state.toString())


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
