package com.foo.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.foo.pomodoro.adapters.DemoTomatoAdapter
import com.foo.pomodoro.data.Pomodoro
import com.foo.pomodoro.databinding.FragmentPomodoroBinding

class PomodoroFragment: Fragment() {

    private lateinit var binding: FragmentPomodoroBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPomodoroBinding.inflate(inflater, container, false)
        val adapter = DemoTomatoAdapter()

        binding.hasPomodoros = true

        binding.pomodoroList.adapter = adapter


        binding.addTask.setOnClickListener{
            it.findNavController().navigate(R.id.action_view_pager_fragment_to_newPomodoroFragment)
        }

        subscribeUi(adapter, binding)

        return binding.root
    }

    private fun subscribeUi(adapter: DemoTomatoAdapter, binding: FragmentPomodoroBinding) {

        val demoPomo = listOf(

            Pomodoro(

                "pomo1",
                "des1",
                5,
                3,
                arrayListOf("공부", "스터디")
            ),
            Pomodoro(
                "pomo2",
                "des2",
                5,
                3,
                arrayListOf("공부", "스터디")
            ),
            Pomodoro(
                "pomo3",
                "des3",
                8,
                3,
                arrayListOf("공부", "스터디")
            ),
            Pomodoro(
                "pomo4",
                "des4",
                9,
                9,
                arrayListOf("공부", "스터디")
            ),
            Pomodoro(
                "pomo5",
                "des5",
                5,
                3,
                arrayListOf("스터디")
            )
        )

        adapter.submitList(demoPomo)

    }




}

