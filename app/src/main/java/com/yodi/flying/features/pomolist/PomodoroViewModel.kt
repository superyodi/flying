package com.yodi.flying.features.pomolist

import com.yodi.flying.model.entity.Pomodoro

class PomodoroViewModel (pomodoro: Pomodoro)  {

    private val pomodoro = pomodoro

    val pomoId
        get() = pomodoro.id
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
    val pomoHasDuedateText
        get() = run {
            if(pomodoro.hasDuedate) "매일매일"
            else "오늘 하루만"
        }
    val progressText = "$pomoNowCount/$pomoGoalCount"




}


