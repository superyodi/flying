package com.yodi.flying.features.tickets

import androidx.lifecycle.*
import com.yodi.flying.model.entity.Ticket
import com.yodi.flying.model.entity.TicketWithTasks
import com.yodi.flying.model.repository.TicketRepository
import com.yodi.flying.mvvm.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class TicketListViewModel (private val ticketRepository: TicketRepository) : ViewModel() {


    private val _allTickets = MutableLiveData<List<TicketWithTasks>>()
    val allTickets : LiveData<List<TicketWithTasks>>
     get() = _allTickets

    init {
        viewModelScope.launch {
            _allTickets.value = ticketRepository.getTicketsWithTasks()
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