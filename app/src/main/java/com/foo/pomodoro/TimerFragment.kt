package com.foo.pomodoro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.foo.pomodoro.databinding.FragmentTimerBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer
import kotlin.properties.Delegates


class TimerFragment : Fragment(){



    // 간단한 타이머 화면
    // 25분 run
    // 5분 rest
    private val TAG = "TimerFragment"
    private lateinit var binding: FragmentTimerBinding
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

        binding = FragmentTimerBinding.inflate(inflater, container, false)

        binding.buttonStart.setOnClickListener {

            binding.stopLayout.visibility = View.GONE
            binding.runningLayout.visibility = View.VISIBLE

            startTimer()

        }

        binding.buttonStop.setOnClickListener {

            binding.runningLayout.visibility = View.GONE
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

