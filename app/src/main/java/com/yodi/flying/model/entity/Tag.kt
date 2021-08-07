package com.yodi.flying.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tags", primaryKeys = ["tagName", "userId"])
data class Tag(
    val tagName: String = "",
    val userId : Long = 0L
)

