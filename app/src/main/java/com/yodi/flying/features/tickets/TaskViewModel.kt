package com.yodi.flying.features.tickets

import androidx.room.Entity
import com.yodi.flying.model.entity.Task
import com.yodi.flying.utils.getFormattedTotalTime

class TaskViewModel(private val task : Task) {


    val title
        get() = task.title
    val count
        get() = "x${task.count}"
    val totalTime
        get() = getFormattedTotalTime(task.totalTime)

}