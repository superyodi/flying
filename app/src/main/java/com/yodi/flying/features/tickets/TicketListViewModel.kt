package com.yodi.flying.features.tickets

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.yodi.flying.features.pomolist.PomoListViewModel
import com.yodi.flying.model.entity.Pomodoro
import com.yodi.flying.model.entity.Ticket
import com.yodi.flying.model.repository.PomodoroRepository
import com.yodi.flying.model.repository.TicketRepository
import timber.log.Timber

class TicketListViewModel (private val ticketRepository: TicketRepository) : ViewModel() {

    val allTickets : LiveData<List<Ticket>> = ticketRepository.getTickets().asLiveData()


    init {
        Timber.d("connected")
        Timber.d("${ticketRepository.todayDate }")
        allTickets.value?.let {

            Timber.d("ticket: ${it[0].startTime}")

        }

    }
}

class TicketListViewModelFactory(private val ticketRepository: TicketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(TicketListViewModel::class.java)) {
            TicketListViewModel(ticketRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}