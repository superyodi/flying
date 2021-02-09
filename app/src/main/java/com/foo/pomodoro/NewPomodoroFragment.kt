package com.foo.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.foo.pomodoro.databinding.FragmentNewPomodoroBinding

class NewPomodoroFragment : Fragment(){

    private lateinit var binding: FragmentNewPomodoroBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPomodoroBinding.inflate(inflater, container, false)


        binding.addPomodoro.setOnClickListener {
            Toast.makeText(activity, "새 뽀모도로를 추가했습니다.", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }




}