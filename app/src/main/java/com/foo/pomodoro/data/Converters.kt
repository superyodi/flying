package com.foo.pomodoro.data

import androidx.room.TypeConverter
import com.google.gson.Gson


class Converters {
    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}
