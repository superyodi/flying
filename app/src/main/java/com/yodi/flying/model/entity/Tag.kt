package com.yodi.flying.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey
    val tagName: String = "",
    val userId : Long = 0L
)

