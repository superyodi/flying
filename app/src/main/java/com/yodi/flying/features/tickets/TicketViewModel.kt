package com.yodi.flying.features.tickets


import com.yodi.flying.model.entity.Ticket
import com.yodi.flying.utils.convertDateTimeToLong
import com.yodi.flying.utils.convertDateTimeToString
import com.yodi.flying.utils.convertDateToString
import com.yodi.flying.utils.convertLongToString
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class TicketViewModel (ticket: Ticket)  {


    private val ticket = ticket

    val startedTimeText
        get() = run {
            "Departure " + convertDateTimeToString(ticket.startTime, "hh:mmaa", Locale.ENGLISH)
        }

    val finishedTimeText
        get() = run {
            "Arrival " + convertDateTimeToString(ticket.endTime, "hh:mmaa", Locale.ENGLISH)
        }


}


