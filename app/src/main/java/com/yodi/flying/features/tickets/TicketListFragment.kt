package com.yodi.flying.features.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.yodi.flying.MainActivity
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.adapters.TicketAdapter
import com.yodi.flying.databinding.FragmentPomodoroListBinding
import com.yodi.flying.databinding.FragmentTicketListBinding
import timber.log.Timber


class TicketListFragment : Fragment() {

    private lateinit var binding: FragmentTicketListBinding
    private lateinit var adapter: TicketAdapter

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
                viewModel = ticketListViewModel
                lifecycleOwner = viewLifecycleOwner
            }


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity != null) (activity as MainActivity).setToolBarTitle(getString(R.string.toolbar_title_ticket_list))
    }

    private fun observeViewModel() {


    }

    private fun subscribeUi(adapter: TicketAdapter, binding: FragmentPomodoroListBinding) {
        ticketListViewModel.allTickets.observe(::getLifecycle)  { result ->
            binding.hasPomodoros = !result.isNullOrEmpty()
            adapter.submitList(result)
        }
    }


//    private fun subscribeUi(adapter: PomodoroAdapter, binding: FragmentPomodoroListBinding) {
//        pomoListViewModel.allPomos.observe(::getLifecycle)  { result ->
//            binding.hasPomodoros = !result.isNullOrEmpty()
//            adapter.submitList(result)
//        }
//
//    }


}








