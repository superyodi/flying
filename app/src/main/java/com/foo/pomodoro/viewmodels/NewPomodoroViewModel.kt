package com.foo.pomodoro.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.foo.pomodoro.utils.Event
import com.foo.pomodoro.R
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

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


    fun savePomo(currentTitle: String, currentTag: String, currentGoalCount: String, currentDescription: String,currentHasDuedate: Boolean, currentInitDate: String ,currentDueDate: String?) {

        if (currentTitle.isNullOrEmpty()) {
            _snackbarText.value = Event(R.string.empty_pomodoro_title)
            return
        }

        if (currentTag.isNullOrEmpty()) {
            _snackbarText.value = Event(R.string.empty_pomodoro_tag)
            return
        }

        if (currentGoalCount.isNullOrEmpty()) {
            _snackbarText.value = Event(R.string.empty_pomodoro_count_message)
            return
        }

        val goalCountNum = currentGoalCount.toInt()

        if (goalCountNum < 1) {
            _snackbarText.value = Event(R.string.wrong_pomodoro_goal_count)
            return
        }

        if (currentHasDuedate && currentDueDate == null) {
            _snackbarText.value = Event(R.string.empty_duedate_message)
            return
        }

        Timber.d("생성일: ${currentInitDate}, 목표일 ${currentDueDate}")

        if(currentHasDuedate) {
            createPomodoro(
                Pomodoro(
                    currentTitle, currentTag, goalCountNum,
                    0, currentDescription,currentHasDuedate, currentInitDate, currentDueDate))
        }
        else{
            createPomodoro(
                Pomodoro(currentTitle, currentTag, goalCountNum,
                    0, currentDescription, currentHasDuedate, currentInitDate))
        }




    }


    private fun createPomodoro(newPomodoro: Pomodoro) {

        viewModelScope.launch {
            pomodoroRepository.insert(newPomodoro)
            _pomodoroUpdated.value = Event(Unit)
        }
    }

    fun setStartDateText() : String = SimpleDateFormat("yyyy/MM/dd").format(Date(System.currentTimeMillis()))


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