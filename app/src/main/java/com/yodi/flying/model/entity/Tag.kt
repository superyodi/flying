package com.yodi.flying.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey
    var tagName: String = ""
) {


    var id: Int = 0

}


