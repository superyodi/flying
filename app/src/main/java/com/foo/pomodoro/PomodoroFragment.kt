package com.foo.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe

import androidx.navigation.findNavController
import com.foo.pomodoro.adapters.PomodoroAdapter
import com.foo.pomodoro.databinding.FragmentPomodoroBinding
import com.foo.pomodoro.viewmodels.*

class PomodoroFragment: Fragment() {

    private lateinit var binding: FragmentPomodoroBinding
    private val viewmodel: PomoListViewModel by viewModels {
        PomoListViewModelFactory((activity?.application as MainApplication).pomodoroRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPomodoroBinding.inflate(inflater, container, false)
        binding.hasPomodoros = true

        val adapter = PomodoroAdapter()
        binding.pomodoroList.adapter = adapter
        subscribeUi(adapter,binding)


        binding.addTask.setOnClickListener{
            it.findNavController().navigate(R.id.action_view_pager_fragment_to_newPomodoroFragment)
        }

        return binding.root
    }

    private fun subscribeUi(adapter: PomodoroAdapter, binding: FragmentPomodoroBinding) {
        viewmodel.allPomos.observe(viewLifecycleOwner)  { result ->
            binding.hasPomodoros = !result.isNullOrEmpty()
            adapter.submitList(result)
        }


    }

}

