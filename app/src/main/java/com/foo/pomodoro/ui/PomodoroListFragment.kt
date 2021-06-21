package com.foo.pomodoro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.foo.pomodoro.MainApplication
import com.foo.pomodoro.R
import com.foo.pomodoro.adapters.PomodoroAdapter
import com.foo.pomodoro.data.TimerState
import com.foo.pomodoro.databinding.FragmentPomodoroListBinding
import com.foo.pomodoro.viewmodels.*
import com.google.android.material.transition.MaterialElevationScale
import timber.log.Timber

class PomodoroListFragment: Fragment() {

    private lateinit var binding: FragmentPomodoroListBinding


    private val pomoListViewModel: PomoListViewModel by viewModels {
        PomoListViewModelFactory((activity?.application as MainApplication).pomodoroRepository)
    }
    private var isTimerRunning = false
    var runningPomodoroId  = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()

        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }



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

            exitTransition = MaterialElevationScale(false).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }

            val extras = FragmentNavigatorExtras(it to "shared_element_container")
            it.findNavController().navigate(R.id.action_pomodoroListFragment_to_newPomodoroFragment, null, null, extras)
        }
        return binding.root
    }

    private fun subscribeUi(adapter: PomodoroAdapter, binding: FragmentPomodoroListBinding) {
        pomoListViewModel.allPomos.observe(::getLifecycle)  { result ->
            binding.hasPomodoros = !result.isNullOrEmpty()
            adapter.submitList(result)
        }

    }

}


