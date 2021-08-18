package com.yodi.flying.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag_with_time")
data class TagWithTime(
    @PrimaryKey
    val tagName: String = ""

) {
    var time: Long = 0L
    var date : Long = 0L
}
