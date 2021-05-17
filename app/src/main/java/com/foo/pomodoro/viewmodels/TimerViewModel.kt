package com.foo.pomodoro.viewmodels

import androidx.lifecycle.*
import com.foo.pomodoro.Event
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository
import kotlinx.coroutines.launch

class TimerViewModel(
    private val pomodoroRepository: PomodoroRepository,
    private val pomodoroId: Int

) : ViewModel() {

    val pomodoro = pomodoroRepository.getPomodoro(pomodoroId)



    private val TAG = "PlantDetailViewModel"
    private val _pomodoro = MutableLiveData<Pomodoro>()




    val timerState = "$${pomodoro.value?.goalCount}"



//    fun initPomodoro() {
//        viewModelScope.launch {
//            pomodoro = pomodoroRepository.getPomodoro(pomodoroId)
//        }
//    }



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
