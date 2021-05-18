package com.foo.pomodoro

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
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


            viewModel = timerViewmodel
            lifecycleOwner = viewLifecycleOwner


        }



        timerViewmodel.pomodoro.observe(::getLifecycle) { it ->

            Log.d(TAG, it.state.toString())

            when(it.state) {
                0,1,4 -> binding.timerState.text = "${it.nowCount}/${it.goalCount}"
                2 -> binding.timerState.text = "Short Break"
                3 -> binding.timerState.text = "Long Break"
                else -> binding.timerState.text = "상태 알 수 없음 "
            }
            
        }

        binding.btnStart.setOnClickListener {

            binding.stopLayout.visibility = View.GONE
            binding.btnStop.visibility =View.VISIBLE

            startTimer()
        }

        binding.btnStop.setOnClickListener {

            binding.btnStop.visibility = View.GONE
            binding.stopLayout.visibility = View.VISIBLE
            stopTimer()

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

