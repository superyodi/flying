package com.foo.pomodoro

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.foo.pomodoro.data.PomodoroRepository
import com.foo.pomodoro.databinding.FragmentNewPomodoroBinding
import com.foo.pomodoro.viewmodels.NewPomodoroViewModel
import com.foo.pomodoro.viewmodels.NewPomodoroViewModelFactory
import com.google.android.material.snackbar.Snackbar

class NewPomodoroFragment : Fragment(){

    private val TAG = "NewPomodoroFramgment"
    private lateinit var binding: FragmentNewPomodoroBinding
    private lateinit var repository: PomodoroRepository


    private val viewmodel: NewPomodoroViewModel by viewModels {
        NewPomodoroViewModelFactory((activity?.application as MainApplication).repository)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentNewPomodoroBinding.inflate(inflater, container, false)

        repository = (requireActivity().application as MainApplication).repository



        binding.addPomodoro.setOnClickListener {


            viewmodel.savePomo(
                binding.taskTitle.text.toString(),
                binding.taskDescription.text.toString(),
                binding.taskGoalcount.text.toString()

            )

            Toast.makeText(activity, "새 뽀모도로를 추가했습니다.", Toast.LENGTH_SHORT).show()

            it.findNavController().navigate(com.foo.pomodoro.R.id.action_newPomodoroFragment_to_view_pager_fragment)


        }

        viewmodel.snackbarMessage.observe(::getLifecycle) {
            it.getContentIfNotHandled()?.let {

                val message = getString(it)
                val snackBar = Snackbar.make(
                    requireActivity().findViewById(R.id.content),
                    message, Snackbar.LENGTH_LONG
                )
                snackBar.show()


            }


        }




        return binding.root
    }





}