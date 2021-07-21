package com.foo.pomodoro.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.foo.pomodoro.MainApplication
import com.foo.pomodoro.R
import com.foo.pomodoro.adapters.ItemSwipeHeplerCallback
import com.foo.pomodoro.adapters.PomodoroAdapter
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.data.TimerState
import com.foo.pomodoro.databinding.FragmentPomodoroListBinding
import com.foo.pomodoro.viewmodels.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import timber.log.Timber


class PomodoroListFragment: Fragment() {

    private lateinit var binding: FragmentPomodoroListBinding
    private lateinit var adapter: PomodoroAdapter


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


        adapter = PomodoroAdapter(isTimerRunning, runningPomodoroId)
        binding.pomodoroList.adapter = adapter

        setItemSwipeListener()
        subscribeUi(adapter, binding)


        pomoListViewModel.isTimerRunning.observe(::getLifecycle) {
            isTimerRunning = it

            if(isTimerRunning) {
                if(pomoListViewModel.runningPomodoroId != null) {
                    Timber.i("실행되는 뽀모도로" + pomoListViewModel.runningPomodoroId.toString())

                    runningPomodoroId = pomoListViewModel.runningPomodoroId ?: -1

                }
            }
            Timber.i("isTimerRunning: ${isTimerRunning}")


            adapter = PomodoroAdapter(isTimerRunning, runningPomodoroId)
            binding.pomodoroList.adapter = adapter
            subscribeUi(adapter, binding)

        }

        binding.addTask.setOnClickListener{


            it.findNavController().navigate(
                R.id.action_pomodoroListFragment_to_newPomodoroFragment,
                null,
                null
            )
        }
        return binding.root
    }


    private fun subscribeUi(adapter: PomodoroAdapter, binding: FragmentPomodoroListBinding) {
        pomoListViewModel.allPomos.observe(::getLifecycle)  { result ->
            binding.hasPomodoros = !result.isNullOrEmpty()
            adapter.submitList(result)
        }

    }



    private fun setItemSwipeListener() {
        val itemSwipeHeplerCallback: ItemSwipeHeplerCallback = object : ItemSwipeHeplerCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item: Pomodoro = adapter.currentList.get(position)

                pomoListViewModel.delete(item)

                val snackbar = Snackbar
                    .make(
                        binding.root,
                        "Item was removed from the list.",
                        Snackbar.LENGTH_LONG
                    )
                snackbar.setAction("UNDO") {
                    val deletedPomodoro = pomoListViewModel.deletedPomooro
                    if(deletedPomodoro != null) {
                        pomoListViewModel.insert(deletedPomodoro)
                    }
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()

            }
        }
        val itemTouchhelper = ItemTouchHelper(itemSwipeHeplerCallback)
        itemTouchhelper.attachToRecyclerView(binding.pomodoroList)
    }


}


