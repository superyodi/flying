package com.yodi.flying.features.tickets


import android.app.Application
import androidx.lifecycle.*
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.model.entity.Task
import com.yodi.flying.model.entity.Ticket
import com.yodi.flying.model.entity.TicketWithTasks
import com.yodi.flying.utils.*
import kotlinx.coroutines.launch
import java.util.*

class TicketViewModel (private val ticket: TicketWithTasks)  {

    val ticketBackgroundResource = when(ticket.depth) {
        0 -> R.drawable.ticket_1
        1 -> R.drawable.ticket_2
        2 -> R.drawable.ticket_3
        3 -> R.drawable.ticket_4
        4 -> R.drawable.ticket_5
        5 -> R.drawable.ticket_6
        else -> R.drawable.ticket_7

    }



    val currentCity = getCityFromDepth(ticket.depth)

    val nextCity = getNextCity(currentCity)

    val startedTimeText
        get() = run {
            "Departure " + convertDateTimeToString(ticket.startTime, "hh:mmaa", Locale.ENGLISH)
        }

    val finishedTimeText
        get() = run {
            "Arrival " + convertDateTimeToString(ticket.endTime, "hh:mmaa", Locale.ENGLISH)
        }


}


