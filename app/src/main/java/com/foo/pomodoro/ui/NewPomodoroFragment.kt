package com.foo.pomodoro.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.foo.pomodoro.MainApplication
import com.foo.pomodoro.custom.TagPickerDialog
import com.foo.pomodoro.data.PomodoroRepository
import com.foo.pomodoro.databinding.FragmentNewPomodoroBinding
import com.foo.pomodoro.viewmodels.NewPomodoroViewModel
import com.foo.pomodoro.viewmodels.NewPomodoroViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale

class NewPomodoroFragment : Fragment(){

    private val TAG = "NewPomodoroFramgment"
    private lateinit var binding: FragmentNewPomodoroBinding
    private lateinit var repository: PomodoroRepository
    private var taskTag = ""



    private val viewmodel: NewPomodoroViewModel by viewModels {
        NewPomodoroViewModelFactory((activity?.application as MainApplication).pomodoroRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentNewPomodoroBinding.inflate(inflater, container, false)

        repository = (requireActivity().application as MainApplication).pomodoroRepository


        binding.btnTag.setOnClickListener {
            val tagPickerDialog = TagPickerDialog().getInstance()

            activity?.supportFragmentManager?.let { fragmentManager ->

                tagPickerDialog.setOnButtonClickedListener { it ->
                    if(!it.isNullOrEmpty()) {
                        binding.btnTag.text = it
                        taskTag = it
                    }

                }

                tagPickerDialog.show(fragmentManager, "tag picker dialog")
            }
        }

        binding.btnOneday.setOnClickListener {

            binding.groupDuedate.visibility = View.GONE

        }

        binding.btnEveryday.setOnClickListener {

            binding.groupDuedate.visibility = View.VISIBLE

        }

        binding.btnAddMemo.setOnClickListener {

            binding.taskDescription.visibility = View.VISIBLE
            binding.btnAddMemo.visibility = View.INVISIBLE

        }

        binding.addPomodoro.setOnClickListener { view ->


            // goal count 입력받는 count_dialog 보여줌
            viewmodel.savePomo(
                binding.taskTitle.text.toString(),
                taskTag,
                binding.taskGoalCount.text.toString(),
                false

            )

            viewmodel.pomodoroUpdatedEvent.observe(::getLifecycle) { it ->
                it.getContentIfNotHandled()?.let {

                    Toast.makeText(activity, "새 뽀모도로를 추가했습니다.", Toast.LENGTH_SHORT).show()
                    view.findNavController().navigate(com.foo.pomodoro.R.id.action_newPomodoroFragment_to_pomodoroListFragment)
                }
            }
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