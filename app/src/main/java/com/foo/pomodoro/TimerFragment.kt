package com.foo.pomodoro

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foo.pomodoro.databinding.FragmentTimerBinding
import com.foo.pomodoro.viewmodels.TimerViewModel
import com.foo.pomodoro.viewmodels.TimerViewModelFactory
import java.util.*
import kotlin.concurrent.timer


class TimerFragment : Fragment(){

    private val TAG = "TimerFragment"
    private val avgs: TimerFragmentArgs by navArgs()
    private lateinit var binding : FragmentTimerBinding

    private val timerViewmodel: TimerViewModel by viewModels {
        TimerViewModelFactory((activity?.application as MainApplication).pomodoroRepository, avgs.pomoId)
    }

    private lateinit var handler : Handler
    private lateinit var timerTask : Timer
    private var nowMin = 0
    private var nowSecond = 0


    var isRunning = false



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentTimerBinding>(

            inflater,
            R.layout.fragment_timer,
            container,
            false
        ).apply {

//            timerViewmodel.initPomodoro()
            viewModel = timerViewmodel
            lifecycleOwner = viewLifecycleOwner



            btnStart.setOnClickListener {

                stopLayout.visibility = View.GONE
                btnStop.visibility =View.VISIBLE




//                startTimer()
            }

            btnStop.setOnClickListener {

                btnStop.visibility = View.GONE
                stopLayout.visibility = View.VISIBLE
                stopTimer()

            }

        }


        return binding.root
    }


    private fun stopTimer() {
        timerTask.cancel()
    }

    private fun startTimer() {

        nowMin = Companion.RUNNING_TIME

        timerTask = timer (period = 1000, initialDelay = 1000)
        {

            if (nowSecond==0 && nowMin==0)
            {
                println("\n타이머 종료")
                cancel()
            }
            if (nowSecond == 0)
            {
                nowMin--
                nowSecond = 60
            }
            nowSecond--

            activity?.runOnUiThread {
                binding.timerText.text = "${nowMin} : ${nowSecond}"

            }
        }
    }

    companion object {
        // 타이머 연습
        const val RUNNING_TIME = 25
        const val SHORT_REST_TIME = 5
        const val LONG_REST_TIME = 15
    }


}

