package com.yodi.flying.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Long = 0L
) {
    var nickname : String = ""
}




