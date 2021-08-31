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
    val pomoId : Long = 0L,
    var count : Int = 0,
    var totalTime : Long = 0L
)
{
    constructor(
        userId: Long, date: Long, cityTime: Long, pomoId: Long
    ) : this(userId, date, cityTime, pomoId, 0, 0L)

}
