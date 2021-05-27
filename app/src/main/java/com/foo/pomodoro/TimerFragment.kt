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
import com.foo.pomodoro.data.PomodoroState.Companion.FINISHED
import com.foo.pomodoro.data.PomodoroState.Companion.FLYING
import com.foo.pomodoro.data.PomodoroState.Companion.LONG_BREAK
import com.foo.pomodoro.data.PomodoroState.Companion.NONE
import com.foo.pomodoro.data.PomodoroState.Companion.SHORT_BREAK
import com.foo.pomodoro.databinding.FragmentTimerBinding
import com.foo.pomodoro.service.TimerService
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


//        timerViewmodel.pomodoro.observe(::getLifecycle) { it ->
//            when(it.state) {
//                NONE, FLYING, FINISHED -> binding.timerState.text = "${it.nowCount}/${it.goalCount}"
//                SHORT_BREAK -> binding.timerState.text = "Short Break"
//                LONG_BREAK -> binding.timerState.text = "Long Break"
//                else -> binding.timerState.text = "상태 알 수 없음 "
//            }
//
//        }


//        timerViewmodel.timerState
//            .observe(::getLifecycle) { it ->
//            when(it) {
//                NONE, FLYING, FINISHED -> binding.timerState.text = "${timerViewmodel.timerNowCount.value}/${timerViewmodel.timerGoalCount.value}"
//                SHORT_BREAK -> binding.timerState.text = "Short Break"
//                LONG_BREAK -> binding.timerState.text = "Long Break"
//                else -> binding.timerState.text = "상태 알 수 없음 "
//            }
//
//        }

       timerViewmodel.timerNowCount
           .observe(::getLifecycle) { cnt ->

               if(cnt == 4) {
                   timerViewmodel.setPomodoroState(cnt)
               }

           }





        // Test 용
        binding.btnStart.setOnClickListener {

//            binding.stopLayout.visibility = View.GONE
//            binding.btnStop.visibility =View.VISIBLE

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

