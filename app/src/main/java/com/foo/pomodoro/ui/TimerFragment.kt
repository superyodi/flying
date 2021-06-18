package com.foo.pomodoro.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foo.pomodoro.MainApplication
import com.foo.pomodoro.R
import com.foo.pomodoro.TimerFragmentArgs
import com.foo.pomodoro.data.PomodoroState
import com.foo.pomodoro.data.TimerState
import com.foo.pomodoro.databinding.FragmentTimerBinding
import com.foo.pomodoro.service.TimerService
import com.foo.pomodoro.utils.*
import com.foo.pomodoro.viewmodels.TimerViewModel
import com.foo.pomodoro.viewmodels.TimerViewModelFactory
import timber.log.Timber
import java.util.*


class TimerFragment : Fragment(){

    private val TAG = "TimerFragment"
    private val args: TimerFragmentArgs by navArgs()

    private var bound: Boolean = false

    private lateinit var binding : FragmentTimerBinding


    private val timerViewmodel: TimerViewModel by viewModels {
        TimerViewModelFactory((activity?.application as MainApplication).pomodoroRepository)
    }

    /*This acts as a dummy to trigger onBind/onRebind/onUnbind in TimerService*/
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {}
        override fun onServiceDisconnected(className: ComponentName) {}
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i("OnCreate")
        Timber.i("timer state - time: ${timerViewmodel.timerState.value}")

        sendCommandToService(ACTION_INITIALIZE_DATA)


    }

    override fun onStart() {
        super.onStart()

        Timber.i("onStop")
        Timber.i("timer state - time: ${timerViewmodel.timerState.value}, bound: ${bound}")
        Intent(context, TimerService::class.java).also { intent ->
            context?.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
        bound = true
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        Timber.i("onStop")
        Timber.i("timer state - time: ${timerViewmodel.timerState.value}, bound: ${bound}")
        if (bound) {
            //Timber.d("Trying to unbind service")
            context?.unbindService(mConnection)
            bound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(timerViewmodel.timerState.value == TimerState.EXPIRED) sendCommandToService(
            ACTION_CANCEL_AND_RESET)
    }

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

        // UI Setting according to timerState
        timerViewmodel.timerState.observe(::getLifecycle) { it ->

            when(it) {
                TimerState.RUNNING -> {
                    binding.stopLayout.visibility = View.GONE
                    binding.btnStop.visibility = View.VISIBLE
                }

                TimerState.EXPIRED , TimerState.PAUSED-> {
                    binding.stopLayout.visibility = View.VISIBLE
                    binding.btnStop.visibility = View.GONE
                }
            }
        }

        // UI Setting according to pomodoroState
        timerViewmodel.pomodoroState.observe(::getLifecycle) {
            Timber.i("pomodoro state - time: ${it}")

            when(it) {
                PomodoroState.NONE, PomodoroState.FLYING, PomodoroState.FINISHED -> {

                    val nowCount = timerViewmodel.timerNowCount
                    val goalCount = timerViewmodel.timerGoalCount

                    binding.timerState.text = "${nowCount}/${goalCount}"
                }

                PomodoroState.SHORT_BREAK -> {
                    binding.timerState.text = "Meal time"

                }

                PomodoroState.LONG_BREAK -> {
                    binding.timerState.text = "Stop over"
                }
            }
        }


        binding.btnStart.setOnClickListener {
            when(timerViewmodel.timerState.value) {
                TimerState.EXPIRED -> sendCommandToService(ACTION_START)
                TimerState.PAUSED -> sendCommandToService(ACTION_RESUME)
            }

        }

        binding.btnStop.setOnClickListener {

            if(timerViewmodel.timerState.value == TimerState.RUNNING) sendCommandToService(ACTION_PAUSE)
        }

        binding.btnInit.setOnClickListener {

            sendCommandToService(ACTION_CANCEL)
        }

        return binding.root
    }


    private fun sendCommandToService(action: String) {
        Intent(context, TimerService::class.java).also {
            it.action = action

            Timber.d("sendCommandService - Action: $action - ID: $args.pomoId")
            if (action == ACTION_INITIALIZE_DATA) {
                it.putExtra(EXTRA_POMODORO_ID, args.pomoId)
            }
            context?.startService(it)
        }
    }

}

