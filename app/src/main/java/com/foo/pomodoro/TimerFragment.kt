package com.foo.pomodoro

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foo.pomodoro.databinding.FragmentTimerBinding
import com.foo.pomodoro.service.TimerService
import com.foo.pomodoro.utils.ACTION_START
import com.foo.pomodoro.utils.EXTRA_TIMER_ID
import com.foo.pomodoro.viewmodels.TimerViewModel
import com.foo.pomodoro.viewmodels.TimerViewModelFactory
import java.util.*
import kotlin.concurrent.timer


class TimerFragment : Fragment(){

    private val TAG = "TimerFragment"
    private val args: TimerFragmentArgs by navArgs()
    private lateinit var binding : FragmentTimerBinding

    private val timerViewmodel: TimerViewModel by viewModels {
        TimerViewModelFactory((activity?.application as MainApplication).pomodoroRepository, args.pomoId)
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


       timerViewmodel.timerNowCount
           .observe(::getLifecycle) { cnt ->

               if(cnt == 4) {
                   timerViewmodel.setPomodoroState(cnt)
               }
           }



        // Test 용
        binding.btnStart.setOnClickListener {

            binding.stopLayout.visibility = View.GONE
            binding.btnStop.visibility =View.VISIBLE

            startForegroundService()

            timerViewmodel.plusTomatoCount()

            binding.timerText.text = timerViewmodel.timerNowCount.value.toString()

        }

        binding.btnStop.setOnClickListener {

            binding.btnStop.visibility = View.GONE
            binding.stopLayout.visibility = View.VISIBLE

            stopForegroundService()

        }

        return binding.root
    }

    private fun startTimerService() {

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

    private fun startForegroundService() {

        Intent(context, TimerService::class.java).run {
            this.action = ACTION_START
            this.putExtra(EXTRA_TIMER_ID, args.pomoId)

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) context?.startForegroundService(this)
            else context?.startService(this)
        }
    }

    private fun stopForegroundService() {
        Intent(context, TimerService::class.java).run {
            context?.stopService(this)
        }
    }


    companion object {
        // 타이머 연습
        const val RUNNING_TIME = 25
        const val SHORT_REST_TIME = 5
        const val LONG_REST_TIME = 15
    }


}

