package com.yodi.flying.features.tickets

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yodi.flying.MainActivity
import com.yodi.flying.R
import com.yodi.flying.adapters.ItemSwipeHeplerCallback
import com.yodi.flying.adapters.PomodoroAdapter
import com.yodi.flying.databinding.FragmentPomodoroListBinding
import com.yodi.flying.databinding.FragmentTicketListBinding
import com.yodi.flying.model.TimerState
import com.yodi.flying.model.entity.Pomodoro
import timber.log.Timber


class TicketListFragment : Fragment() {

    private lateinit var binding: FragmentTicketListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTicketListBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity != null) (activity as MainActivity).setToolBarTitle(getString(R.string.toolbar_title_ticket_list))
    }




//    private fun subscribeUi(adapter: PomodoroAdapter, binding: FragmentPomodoroListBinding) {
//        pomoListViewModel.allPomos.observe(::getLifecycle)  { result ->
//            binding.hasPomodoros = !result.isNullOrEmpty()
//            adapter.submitList(result)
//        }
//
//    }






}







