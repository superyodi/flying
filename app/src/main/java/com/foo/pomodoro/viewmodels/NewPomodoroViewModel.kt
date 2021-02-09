package com.foo.pomodoro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foo.pomodoro.Event
import com.foo.pomodoro.R
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.PomodoroRepository

class NewPomodoroViewModel(
    private val pomodoroRepository: PomodoroRepository
) : ViewModel() {

    val TAG = "NewPomodoroViewModel"

    val title = MutableLiveData<String>()

    val description = MutableLiveData<String>()

    val goalCount = MutableLiveData<String>()

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

    fun start(pomodoroId: Int?) {
        this.pomodoroId = pomodoroId
        if (pomodoroId == null) {
            isNewPomo = true
            return
        }
    }

    internal suspend fun savePomo() {
        val currentTitle = title.value
        val currentDescription = description.value
        val currentGoalCount: Int? = goalCount.value?.toInt()

        if (currentTitle == null || currentDescription == null) {
            _snackbarText.value =  Event(R.string.empty_pomodoro_message)
            return
        }
        if (Pomodoro(currentTitle, currentDescription ?: "").isEmpty) {
            _snackbarText.value =  Event(R.string.empty_pomodoro_message)
            return
        }

        val currentPomoId = pomodoroId
        if (isNewPomo || currentPomoId == null) {
            createPomodoro(Pomodoro(currentTitle, currentDescription,currentGoalCount, 0))
        }
    }

    private suspend fun createPomodoro(newPomodoro: Pomodoro) {
        pomodoroRepository.insert(newPomodoro)
        _pomodoroUpdated.value = Event(Unit)
    }







}