package com.foo.pomodoro.viewmodels

import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foo.pomodoro.data.Pomodoro

class PomodoroViewModel (pomodoro: Pomodoro)  {





    private val pomodoro = pomodoro


    val pomoTitle
        get() = pomodoro.title
    val pomoTag
        get() = pomodoro.tag
    val pomoDescription
        get() = pomodoro.description
    val pomoGoalCount
        get() = pomodoro.goalCount
    val pomoNowCount
        get() = pomodoro.nowCount
    val pomoHasDuedate
        get() = pomodoro.hasDuedate


}


