package com.foo.pomodoro.viewmodels

import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foo.pomodoro.data.Pomodoro

class DemoTomatoViewModel (pomodoro: Pomodoro)  {


    // Rest of the ViewModel...




    private val pomodoro = pomodoro


    val pomoTitle
        get() = pomodoro.title
    val pomoDescription
        get() = pomodoro.description
    val pomoGoalCount
        get() = pomodoro.goalCount
    val pomoNowCount
        get() = pomodoro.nowCount
    val pomoTags
        get() = pomodoro.tags


}


