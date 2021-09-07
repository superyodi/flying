package com.yodi.flying.model.entity

data class TicketWithTasks(
    val date : Long = 0L,
    val startTime: Long = 0L,
    val depth : Int = 0, // city 단계
    var endTime : Long? = null,
    val tasks : List<Task>

)