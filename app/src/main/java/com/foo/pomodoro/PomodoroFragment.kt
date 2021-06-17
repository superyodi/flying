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
import com.foo.pomodoro.custom.TagPickerDialog
import com.foo.pomodoro.data.TimerState
import com.foo.pomodoro.databinding.FragmentPomodoroBinding
import com.foo.pomodoro.viewmodels.*
import timber.log.Timber

class PomodoroFragment: Fragment() {

    private lateinit var binding: FragmentPomodoroBinding

    private val pomoListViewModel: PomoListViewModel by viewModels {
        PomoListViewModelFactory((activity?.application as MainApplication).pomodoroRepository)
    }

    private var isTimerRunning = false

    var runningPomodoro  = -1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPomodoroBinding.inflate(inflater, container, false)
        binding.hasPomodoros = true

        if(pomoListViewModel.timerState.value != null && pomoListViewModel.timerState.value != TimerState.EXPIRED) {
            isTimerRunning = true
        }

        pomoListViewModel.timerState.observe(::getLifecycle) {
            if(it == TimerState.EXPIRED) isTimerRunning = false

            if(isTimerRunning) {
                if(pomoListViewModel.runningPomodoroId != null) {
                    Timber.i("실행되는 뽀모도로"+pomoListViewModel.runningPomodoroId.toString())

                    runningPomodoro = pomoListViewModel.runningPomodoroId ?: -1


                }
            }
        }

        val adapter = PomodoroAdapter()
        binding.pomodoroList.adapter = adapter
        subscribeUi(adapter,binding)


        binding.addTask.setOnClickListener{
            it.findNavController().navigate(R.id.action_view_pager_fragment_to_newPomodoroFragment)
        }
        return binding.root
    }

    private fun subscribeUi(adapter: PomodoroAdapter, binding: FragmentPomodoroBinding) {
        pomoListViewModel.allPomos.observe(viewLifecycleOwner)  { result ->
            binding.hasPomodoros = !result.isNullOrEmpty()
            adapter.submitList(result)
        }

    }

    
}