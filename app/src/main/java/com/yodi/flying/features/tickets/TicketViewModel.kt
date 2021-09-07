package com.yodi.flying.features.tickets


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yodi.flying.MainApplication
import com.yodi.flying.model.entity.Task
import com.yodi.flying.model.entity.Ticket
import com.yodi.flying.model.entity.TicketWithTasks
import com.yodi.flying.utils.convertDateTimeToString
import kotlinx.coroutines.launch
import java.util.*

class TicketViewModel (private val ticket: TicketWithTasks)  {





    val startedTimeText
        get() = run {
            "Departure " + convertDateTimeToString(ticket.startTime, "hh:mmaa", Locale.ENGLISH)
        }

    val finishedTimeText
        get() = run {
            "Arrival " + convertDateTimeToString(ticket.endTime, "hh:mmaa", Locale.ENGLISH)
        }


}


