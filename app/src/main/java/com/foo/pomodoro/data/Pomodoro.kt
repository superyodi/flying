package com.foo.pomodoro.data

import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoros")
data class Pomodoro(
    var title: String = "",
    var description: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var goalCount: Int? = 0
    var nowCount: Int = 0

    var tags: List<String> ?= null


    constructor(
        title: String, description: String, id: Int,
        goalCount: Int?, nowCount: Int
    ) : this(title, description) {
        this.id = id
        this.goalCount = goalCount
        this.nowCount = nowCount
    }


    constructor(
        title: String, description: String,
        goalCount: Int?, nowCount: Int
    ) : this(title, description) {
        this.goalCount = goalCount
        this.nowCount = nowCount
    }

    constructor(
        title: String, description: String,
        goalCount: Int, nowCount: Int, tags: List<String>
    ) : this(
        title, description, goalCount, nowCount
    ) {
        this.tags = tags
    }

    val isEmpty
        get() = title.isEmpty() && description.isEmpty()

}


