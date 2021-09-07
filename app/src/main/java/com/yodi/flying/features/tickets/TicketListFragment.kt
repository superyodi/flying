package com.yodi.flying.features.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yodi.flying.MainActivity
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.adapters.TicketAdapter
import com.yodi.flying.databinding.FragmentTicketListBinding
import com.yodi.flying.model.entity.Task
import timber.log.Timber


class TicketListFragment : Fragment() {

    private lateinit var binding: FragmentTicketListBinding
    private lateinit var adapter: TicketAdapter
    private lateinit var taks : List<Task>

    private val ticketListViewModel: TicketListViewModel by viewModels {
        TicketListViewModelFactory(
            (activity?.application as MainApplication).ticketRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTicketListBinding
            .inflate(inflater, container, false)
            .apply {
                hasTickets = true
                viewModel = ticketListViewModel
                lifecycleOwner = viewLifecycleOwner
            }

        adapter = TicketAdapter()
        binding.ticketList.adapter = adapter
        subscribeUi(adapter, binding)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null) (activity as MainActivity).setToolBarTitle(getString(R.string.toolbar_title_ticket_list))
    }


    private fun subscribeUi(adapter: TicketAdapter, binding: FragmentTicketListBinding) {
        ticketListViewModel.allTickets.observe(::getLifecycle) { tickets ->
            Timber.i("첫번째 시작 시간: ${tickets[0].startTime}")
            binding.hasTickets = !tickets.isNullOrEmpty()
            adapter.submitList(tickets)

        }


    }



}








