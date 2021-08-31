package com.yodi.flying.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "tickets", primaryKeys = ["userId", "date", "startTime"])
data class Ticket(
    val userId : Long = 0L,
    val date : Long = 0L,
    val startTime: Long = 0L,
    val depth : Int = 0 // city 단계
)
{
    var endTime : Long? = null
}
