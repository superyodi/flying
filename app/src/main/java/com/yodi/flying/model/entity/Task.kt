package com.yodi.flying.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey

// TaskForEachCity, 특정 도시에 있는 2시간동안 수행한 task들
@Entity(tableName = "tasks",
    primaryKeys = [ "userId", "date", "cityTime", "pomoId"])
data class Task(
    val userId : Long = 0L,
    val date :Long = 0L,
    val cityTime : Long = 0L,
    val title : String = "",
    val pomoId : Long = 0L
    // 실행 도중 pomodoro의 title이 바뀔 수 있으므로 처음 실행될때의 title을 기준으로 하지만 pomoId로 뽀모도로 구분

){
    var count : Int = 0
    var totalTime : Long = 0L
}
