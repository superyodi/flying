package com.yodi.flying.features.tickets


import com.yodi.flying.model.entity.Ticket
import com.yodi.flying.utils.convertLongToString
import java.util.*

class TicketViewModel (ticket: Ticket)  {

    /*
    data class Ticket(
    val userId : Long = 0L,
    val date : Long = 0L,
    val startTime: Long = 0L,
    val depth : Int = 0 // city 단계
)
{
    var endTime : Long = 0L
}

     */

    private val ticket = ticket

    val startTimeText
        get() = run {
            "START "
            convertLongToString(ticket.startTime, "HH:mm", Locale.ENGLISH)
        }



}


