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
    var hasDuedate: Boolean = false,
    var description: String = "",
    var dueDate: Date? = null

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var state = PomodoroState.NONE
    var leftTime = 0L




    constructor(
        title: String, tag: String,
        goalCount: Int, nowCount: Int, hasDuedate: Boolean
    ) : this(title, tag, goalCount, nowCount, hasDuedate, "", null)


    constructor(
        title: String, tag: String,
        goalCount: Int, nowCount: Int, hasDuedate: Boolean, dueDate: Date
    ) : this(title, tag, goalCount, nowCount, hasDuedate, "")


    constructor(
        title: String, tag: String,
        goalCount: Int, nowCount: Int, hasDuedate: Boolean, description: String
    ) : this(title, tag, goalCount, nowCount, hasDuedate, description, null)



    val isEmpty
        get() = title.isEmpty() || tag.isEmpty() || goalCount == 0

}


