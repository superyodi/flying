package com.foo.pomodoro

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foo.pomodoro.data.TimerState
import com.foo.pomodoro.databinding.FragmentTimerBinding
import com.foo.pomodoro.service.TimerService
import com.foo.pomodoro.utils.*
import com.foo.pomodoro.viewmodels.TimerViewModel
import com.foo.pomodoro.viewmodels.TimerViewModelFactory
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timer


class TimerFragment : Fragment(){

    private val TAG = "TimerFragment"
    private val args: TimerFragmentArgs by navArgs()

    private var isTimerRunninng = false

    private lateinit var binding : FragmentTimerBinding


    private val timerViewmodel: TimerViewModel by viewModels {
        TimerViewModelFactory((activity?.application as MainApplication).pomodoroRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sendCommandToService(ACTION_INITIALIZE_DATA)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(isTimerRunninng){
                    requireActivity().finish()
                }else{
                    isEnabled = false
                    activity?.onBackPressed()
                    sendCommandToService( ACTION_CANCEL_AND_RESET )

                }
            }
        })





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


        // Test 용
        binding.btnStart.setOnClickListener {

            binding.stopLayout.visibility = View.GONE
            binding.btnStop.visibility =View.VISIBLE

            timerViewmodel.timerState.observe(::getLifecycle) {
                when(it) {
                    TimerState.EXPIRED -> {
                        sendCommandToService(ACTION_START)
                    }
                    TimerState.PAUSED -> {
                        sendCommandToService(ACTION_RESUME)
                    }
                }
            }

            isTimerRunninng = true

        }

        binding.btnStop.setOnClickListener {

            binding.btnStop.visibility = View.GONE
            binding.stopLayout.visibility = View.VISIBLE

            sendCommandToService(ACTION_PAUSE)

        }

        binding.btnInit.setOnClickListener {
            sendCommandToService(ACTION_CANCEL)
            isTimerRunninng = false
        }

        return binding.root
    }



    override fun onDestroy() {
        super.onDestroy()
        sendCommandToService(ACTION_CANCEL_AND_RESET)
    }

    private fun startForegroundService() {

        Intent(context, TimerService::class.java).run {
            this.action = ACTION_START
            this.putExtra(EXTRA_POMODORO_ID, args.pomoId)

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) context?.startForegroundService(this)
            else context?.startService(this)
        }
    }

    private fun stopForegroundService() {
        Intent(context, TimerService::class.java).run {
            context?.stopService(this)
        }
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

