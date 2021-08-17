package com.yodi.flying.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yodi.flying.model.PomodoroState


@Entity(tableName = "pomodoros")
data class Pomodoro(
    var userId: Long = 0L,
    var title: String = "",
    var tag: String = "",
    var goalCount: Int = 0,
    var nowCount: Int = 0,
    var description: String = "",
    var hasDuedate: Boolean = false,
    var initDate: Long = 0L,
    var dueDate: Long? = null

) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
    var state = PomodoroState.NONE
    var leftTime = 0L
    var startedTime = 0L
    var ticketCount : Int = 0


    constructor(
        userId: Long, title: String, tag: String,
        goalCount: Int, nowCount: Int, description: String, hasDuedate: Boolean, initDate: Long
    ) : this(userId, title, tag, goalCount, nowCount, description, hasDuedate, initDate, null)


    val isEmpty
        get() = title.isEmpty() || tag.isEmpty() || goalCount == 0

}


