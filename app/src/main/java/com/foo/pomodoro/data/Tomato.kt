package com.foo.pomodoro.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey


// 임시
data class Tomato(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var tomatoId: Long = 0
)
