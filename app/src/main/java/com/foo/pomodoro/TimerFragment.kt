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


class TimerFragment : Fragment(){


    // 간단한 타이머 화면
    // 25분 run
    // 5분 rest
    private lateinit var binding: FragmentTimerBinding
    private lateinit var handler : Handler




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTimerBinding.inflate(inflater, container, false)
        handler = Handler(Looper.getMainLooper())

        val runnable = object:Runnable {
            public override fun run() {
                val cal = Calendar.getInstance()
                val sdf = SimpleDateFormat("mm:ss")
                val strTime = sdf.format(cal.getTime())

                binding.timerText.setText(strTime)
            }
        }

        class NewRunnable : Runnable {
            override fun run() {
                while (true) {
                    try {
                        Thread.sleep(1000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    handler.post(runnable)
                }
            }
        }



        binding.buttonStart.setOnClickListener {

            // change format mm:ss -> ss
            val nr = NewRunnable()
            val t = Thread(nr)
            t.start()


        }
        return binding.root
    }



}

