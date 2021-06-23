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
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.aminography.primedatepicker.picker.theme.base.ThemeFactory
import com.foo.pomodoro.MainApplication
import com.foo.pomodoro.custom.TagPickerDialog
import com.foo.pomodoro.data.PomodoroRepository
import com.foo.pomodoro.databinding.FragmentNewPomodoroBinding
import com.foo.pomodoro.viewmodels.NewPomodoroViewModel
import com.foo.pomodoro.viewmodels.NewPomodoroViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import timber.log.Timber
import java.util.*


class NewPomodoroFragment : Fragment() {

    private val TAG = "NewPomodoroFramgment"
    private lateinit var binding: FragmentNewPomodoroBinding
    private lateinit var repository: PomodoroRepository


    private var taskTag = ""
    private var hasDueDate = false
    private var initDate : String? = null
    private var dueDate : String? = null


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

        binding.enddateLayout.setOnClickListener{


            val today = CalendarFactory.newInstance(CalendarType.CIVIL, Locale.KOREAN)
            val theme = getDefaultTheme()


            val datePicker = PrimeDatePicker.bottomSheetWith(today)
                .pickSingleDay(singleDayPickCallback)
                .minPossibleDate(today)
                .applyTheme(theme)
                .build()

            activity?.supportFragmentManager?.let { view -> datePicker.show(view, DATE_PICKER_TAG) }

            datePicker.setDayPickCallback(singleDayPickCallback)
        }

        binding.btnOneday.setOnClickListener {

            binding.groupDuedate.visibility = View.GONE
            hasDueDate = false

        }

        binding.btnEveryday.setOnClickListener {

            binding.groupDuedate.visibility = View.VISIBLE
            hasDueDate = true

        }


        binding.btnAddMemo.setOnClickListener {

            binding.taskDescription.visibility = View.VISIBLE
            binding.btnAddMemo.visibility = View.INVISIBLE

        }

        binding.addPomodoro.setOnClickListener { view ->


            // TODO("goal count 입력받는 dialog 보여줌 ")

            viewmodel.savePomo(
                binding.taskTitle.text.toString(),
                taskTag,
                binding.taskGoalCount.text.toString(),
                hasDueDate,
                dueDate
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

    private fun getDefaultTheme(): ThemeFactory {
        return object : LightThemeFactory() {
            override val pickedDayBackgroundShapeType: BackgroundShapeType
                get() = BackgroundShapeType.CIRCLE

        }
    }

    private val singleDayPickCallback = SingleDayPickCallback { singleDay ->
        dueDate = singleDay.shortDateString
        binding.dueDate.text = dueDate
    }




    companion object {
        const val DATE_PICKER_TAG = "PrimeDatePickerBottomSheet"
    }



}


