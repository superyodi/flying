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
import com.foo.pomodoro.data.TimerState
import com.foo.pomodoro.databinding.FragmentPomodoroListBinding
import com.foo.pomodoro.viewmodels.*
import timber.log.Timber

class PomodoroListFragment: Fragment() {

    private lateinit var binding: FragmentPomodoroListBinding


    private val pomoListViewModel: PomoListViewModel by viewModels {
        PomoListViewModelFactory((activity?.application as MainApplication).pomodoroRepository)
    }
    private var isTimerRunning = false
    var runningPomodoroId  = -1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPomodoroListBinding.inflate(inflater, container, false)
        binding.hasPomodoros = true

        if(pomoListViewModel.timerState.value != null && pomoListViewModel.timerState.value != TimerState.EXPIRED) {
            isTimerRunning = true
        }

        var adapter = PomodoroAdapter(isTimerRunning, runningPomodoroId)
        binding.pomodoroList.adapter = adapter
        subscribeUi(adapter,binding)


        pomoListViewModel.isTimerRunning.observe(::getLifecycle) {
            isTimerRunning = it

            if(isTimerRunning) {
                if(pomoListViewModel.runningPomodoroId != null) {
                    Timber.i("실행되는 뽀모도로"+pomoListViewModel.runningPomodoroId.toString())

                    runningPomodoroId = pomoListViewModel.runningPomodoroId ?: -1

                }
            }
            Timber.i("isTimerRunning: ${isTimerRunning}")


            adapter = PomodoroAdapter(isTimerRunning, runningPomodoroId)
            binding.pomodoroList.adapter = adapter
            subscribeUi(adapter,binding)

        }

        binding.addTask.setOnClickListener{
            it.findNavController().navigate(R.id.action_view_pager_fragment_to_newPomodoroFragment)
        }
        return binding.root
    }

    private fun subscribeUi(adapter: PomodoroAdapter, binding: FragmentPomodoroListBinding) {
        pomoListViewModel.allPomos.observe(viewLifecycleOwner)  { result ->
            binding.hasPomodoros = !result.isNullOrEmpty()
            adapter.submitList(result)
        }

    }

}

