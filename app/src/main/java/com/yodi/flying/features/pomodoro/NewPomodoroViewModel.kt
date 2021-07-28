package com.yodi.flying.viewmodels

import androidx.lifecycle.*
import com.yodi.flying.utils.Event
import com.yodi.flying.R
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.repository.PomodoroRepository
import kotlinx.coroutines.launch
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

    private val pomodoro = MutableLiveData<Pomodoro>()


    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val tag = MutableLiveData<String>()
    val initDate = MutableLiveData<String>()
    val dueDate= MutableLiveData<String>()
    val hasDuedate = MutableLiveData<Boolean>()
    val goalCount = MutableLiveData<String>()

    var hasMemo = false


    private var isNewPomo: Boolean = false
    private var pomodoroCompleted = false

    private val _isDataLoaded = MutableLiveData<Boolean>()
    val isDataLoaded: LiveData<Boolean>
        get() =_isDataLoaded


    fun start(pomoId: Int) {
        _isDataLoaded.value = false

        if (pomoId == -1) {
            // No need to populate, it's a new task
            isNewPomo = true
            return
        }

        isNewPomo = false

        viewModelScope.launch {
            pomodoro.value = pomodoroRepository.getPomodoro(pomoId)
            _isDataLoaded.value = true
        }

    }
    fun setNewPomoData() {
        title.value = ""
        tag.value = "+ 태그"
        description.value = ""
        initDate.value = setStartDateText()
        hasDuedate.value = false

    }

    // 불러온 데이터 binding
    fun setEditPomoData() {

        pomodoro.value.let{
            if (it != null) {
                title.value = it.title
                description.value = it.description
                goalCount.value = it.goalCount.toString()
                hasDuedate.value = it.hasDuedate
                tag.value = it.tag
                initDate.value = it.initDate.replace('-','/')
                dueDate.value = it.dueDate?:""
                    .replace('-','/')

                hasMemo = !(description.value.isNullOrEmpty())
            }
        }


    }

    fun savePomodoro() {
        val currentTitle = title.value
        var currentDueDate= dueDate.value
        val currentGoalCount = goalCount.value
        val currentDescription = description.value ?: ""
        val currentTag = tag.value
        val currentHasDuedate = hasDuedate.value ?: false
        val currentInitDate = initDate.value ?: "0000/00/00"
            .replace('/','-')



        // check data validation
        if (currentTitle.isNullOrEmpty()) {
            _snackbarText.value = Event(R.string.empty_pomodoro_title)
            return
        }

        if (currentTag.isNullOrEmpty() || currentTag == "+ 태그") {
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

        if (!currentHasDuedate && !(currentDueDate.isNullOrEmpty())) {
            currentDueDate = ""
        }

        if(isNewPomo) {
            createPomodoro(
                Pomodoro(
                    currentTitle, currentTag, goalCountNum,
                    0, currentDescription, currentHasDuedate, currentInitDate, currentDueDate
                )
            )
        }
        else {
            viewModelScope.launch {
                pomodoro.value.let {
                    if (it != null) {
                        it.title = currentTitle
                        it.description = currentDescription
                        it.goalCount = currentGoalCount.toInt()
                        it.hasDuedate = currentHasDuedate
                        it.dueDate = currentDueDate
                        it.tag = currentTag

                        pomodoroRepository.update(it)
                        _pomodoroUpdated.value = Event(Unit)
                    }
                }
            }
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