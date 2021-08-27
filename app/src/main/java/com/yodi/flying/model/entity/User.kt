package com.yodi.flying.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Long = 0L,
    var nickname : String = "",
    var goalTime : Long = 0L
)

{
    var runningTime  = 1500000L //25min
    var shortBreakTime = 300000L //5min
    var longBreakTime = 900000L //15min
    var longBreakTerm = 4
    var isAutoBreakMode = false // 자동으로 뽀모도로 넘김
    var isAutoSkipMode = false // 자동으로 휴식 시작
    var isNonBreakMode = false // 휴식없이 무한 질주
    var totalTime = 0L
    var totalDate = 0


}




