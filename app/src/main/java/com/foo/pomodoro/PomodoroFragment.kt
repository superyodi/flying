package com.foo.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe

import androidx.navigation.findNavController
import com.foo.pomodoro.adapters.DemoTomatoAdapter
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.databinding.FragmentPomodoroBinding
import com.foo.pomodoro.viewmodels.NewPomodoroViewModel
import com.foo.pomodoro.viewmodels.NewPomodoroViewModelFactory
import com.foo.pomodoro.viewmodels.PomodoroViewModel
import com.foo.pomodoro.viewmodels.PomodoroViewModelFactory

class PomodoroFragment: Fragment() {

    private lateinit var binding: FragmentPomodoroBinding
    private val viewmodel: PomodoroViewModel by viewModels {
        PomodoroViewModelFactory((activity?.application as MainApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPomodoroBinding.inflate(inflater, container, false)


        binding.hasPomodoros = true

        val adapter = DemoTomatoAdapter()
        binding.pomodoroList.adapter = adapter
        subscribeUi(adapter,binding)


        binding.addTask.setOnClickListener{
            it.findNavController().navigate(R.id.action_view_pager_fragment_to_newPomodoroFragment)
        }

        binding.showTimerButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_view_pager_fragment_to_timerFragment)
        }

        return binding.root
    }

    private fun subscribeUi(adapter: DemoTomatoAdapter, binding: FragmentPomodoroBinding) {
        viewmodel.allPomos.observe(viewLifecycleOwner)  { result ->
            binding.hasPomodoros = !result.isNullOrEmpty()
            adapter.submitList(result)
        }


    }

}

