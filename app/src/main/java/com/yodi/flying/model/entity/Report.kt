package com.yodi.flying.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList


@Entity(tableName = "reports", primaryKeys = ["date", "userId"])
data class Report(
    val userId : Long = 0L,
    val date: Long = 0L
)
{
    var totalTime = 0L
    var cityDepth = 0 // 오늘 여행한 나라들이 몇개인지

}












