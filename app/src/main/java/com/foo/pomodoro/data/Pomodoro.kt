package com.foo.pomodoro.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "pomodoros")
data class Pomodoro(
    var title: String = "",
    var tag: String = "",
    var goalCount: Int = 0,
    var nowCount: Int = 0,
    var description: String = "",
    var hasDuedate: Boolean = false,
    var initDate: String = "",
    var dueDate: String? = null

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var state = PomodoroState.NONE
    var leftTime = 0L


    constructor(
        title: String, tag: String,
        goalCount: Int, nowCount: Int, description: String, hasDuedate: Boolean, initDate: String
    ) : this(title, tag, goalCount, nowCount, description, hasDuedate, initDate, null)







    val isEmpty
        get() = title.isEmpty() || tag.isEmpty() || goalCount == 0

}


