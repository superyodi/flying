package com.foo.pomodoro.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.foo.pomodoro.Event
import com.foo.pomodoro.R
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository
import kotlinx.coroutines.launch
import java.lang.Integer.parseInt

class NewPomodoroViewModel(
    private val pomodoroRepository: PomodoroRepository
) : ViewModel() {

    val TAG = "NewPomodoroViewModel"



    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarText

    private val _pomodoroUpdated = MutableLiveData<Event<Unit>>()
    val pomodoroUpdatedEvent: LiveData<Event<Unit>>
        get() = _pomodoroUpdated

    private var pomodoroId: Int? = null

    private var isNewPomo: Boolean = false

    private var isDataLoaded = false

    private var pomodoroCompleted = false



//    fun savePomo(currentTitle: String, currentDescription: String, currentGoalCount: String) {
//
//        if (currentTitle.isNullOrEmpty()) {
//            _snackbarText.value = Event(R.string.empty_pomodoro_message)
//            return
//        }
//
//        if (currentGoalCount.isNullOrEmpty()) {
//            _snackbarText.value = Event(R.string.empty_pomodoro_count_message)
//            return
//        }
//
//        val goalCountNum = currentGoalCount.toInt()
//
//        if (goalCountNum < 1) {
//            _snackbarText.value = Event(R.string.wrong_pomodoro_goal_count)
//            return
//        }
//
//        createPomodoro(Pomodoro(currentTitle, currentDescription, goalCountNum, 0))
//
//        Log.d(TAG, "저장 완료")
//    }

    fun savePomo(currentTitle: String, currentDescription: String) {

        if (currentTitle.isNullOrEmpty()) {
            _snackbarText.value = Event(R.string.empty_pomodoro_message)
            return
        }



        createPomodoro(Pomodoro(currentTitle, currentDescription, 5, 0, false))

        Log.d(TAG, "저장 완료")
    }


    private fun createPomodoro(newPomodoro: Pomodoro) {

        viewModelScope.launch {
            pomodoroRepository.insert(newPomodoro)
            _pomodoroUpdated.value = Event(Unit)

            Log.d(TAG, "생성 완료")
        }
    }
}


class NewPomodoroViewModelFactory(val repository: PomodoroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {


        return if (modelClass.isAssignableFrom(NewPomodoroViewModel::class.java)) {
            NewPomodoroViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}