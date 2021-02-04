package com.foo.pomodoro.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoros")
data class Pomodoro(
        @PrimaryKey @ColumnInfo(name = "id") val pomodoroId: String,
        var title: String,
        val description: String,
        var goalCount: Int,
        var nowCount: Int,

        val tags: ArrayList<String> = ArrayList<String>()



)
