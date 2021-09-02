package com.yodi.flying.features.pomodoro


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.aminography.primedatepicker.picker.theme.base.ThemeFactory
import com.google.android.material.snackbar.Snackbar
import com.yodi.flying.MainActivity
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.custom.NumberPickerDialog
import com.yodi.flying.custom.TagPickerDialog
import com.yodi.flying.databinding.FragmentNewPomodoroBinding
import com.yodi.flying.model.repository.PomodoroRepository
import com.yodi.flying.utils.Constants
import com.yodi.flying.utils.convertDateToLong


import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class NewPomodoroFragment : Fragment() {

    private lateinit var binding: FragmentNewPomodoroBinding
    private lateinit var repository: PomodoroRepository
    private val args: NewPomodoroFragmentArgs by navArgs()


    private var isNewPomodoro = false
    private var dueDate : Long? = null

    private val viewmodel: NewPomodoroViewModel by viewModels {
        NewPomodoroViewModelFactory((activity?.application as MainApplication).pomodoroRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)

        if(args.pomoId == -1L) {
            isNewPomodoro = true
        }

        if (activity != null) (activity as MainActivity).setToolBarTitle(setToolbarTitle())



    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPomodoroBinding.inflate(inflater, container, false)
            .apply {
                viewModel = viewmodel
                lifecycleOwner = viewLifecycleOwner
            }

        repository = (requireActivity().application as MainApplication).pomodoroRepository

        Timber.i("${args.pomoId}")
        viewmodel.start(args.pomoId)



        if (isNewPomodoro) viewmodel.setNewPomoData()

        else {
            viewmodel.isDataLoaded.observe(::getLifecycle) { isDataLoaded ->
                if (isDataLoaded) {
                    viewmodel.setEditPomoData()
                    setPomodoroView()
                }
            }
        }

        setPomodoroEventListener()
        return binding.root
    }


    private fun getDefaultTheme(): ThemeFactory {
        return object : LightThemeFactory() {
            override val pickedDayBackgroundShapeType: BackgroundShapeType
                get() = BackgroundShapeType.CIRCLE

        }
    }

    private val singleDayPickCallback = SingleDayPickCallback { singleDay ->

        dueDate = convertDateToLong(singleDay.getTime())
        Toast.makeText(activity, "$dueDate", Toast.LENGTH_SHORT).show()

        binding.dueDate.text = singleDay.shortDateString
    }

    private fun setPomodoroView() {
        if(viewmodel.hasMemo) {
            binding.btnAddMemo.visibility = View.GONE
            binding.taskDescription.visibility = View.VISIBLE

        }
        else {
            binding.btnAddMemo.visibility = View.VISIBLE
            binding.taskDescription.visibility = View.GONE

        }

        if(viewmodel.hasDuedate.value == true) {
            binding.groupDuedate.visibility = View.VISIBLE
            binding.btnEveryday.isChecked = true

        }
        else {
            binding.groupDuedate.visibility = View.GONE
            binding.btnOneday.isChecked = true
        }

    }

    private fun setPomodoroEventListener() {

        binding.btnTag.setOnClickListener {
            val tagPickerDialog = TagPickerDialog().getInstance()

            activity?.supportFragmentManager?.let { fragmentManager ->

                tagPickerDialog.setOnButtonClickedListener { it ->
                    if(it.isNotEmpty()) {
                        viewmodel.tag.value = it
                    }
                }
                tagPickerDialog.show(fragmentManager, Constants.TAG_PICKER)
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

            activity?.supportFragmentManager?.let { view -> datePicker.show(view, Constants.DATE_PICKER) }

            datePicker.setDayPickCallback(singleDayPickCallback)
        }

        binding.btnOneday.setOnClickListener {

            viewmodel.hasDuedate.postValue(false)
            binding.groupDuedate.visibility = View.GONE
        }

        binding.btnEveryday.setOnClickListener {

            viewmodel.hasDuedate.postValue(true)
            binding.groupDuedate.visibility = View.VISIBLE
        }

        binding.btnAddMemo.setOnClickListener {

            binding.taskDescription.visibility = View.VISIBLE
            binding.btnAddMemo.visibility = View.INVISIBLE

        }

        binding.addPomodoro.setOnClickListener {
            showNumberPickerDialog(viewmodel.goalCount)
        }

        viewmodel.pomodoroUpdatedEvent.observe(::getLifecycle) { it ->

            val msg = if(isNewPomodoro) "새 뽀모도로가 생성되었습니다." else "뽀모도로가 수정되었습니다."
            Toast.makeText(requireActivity().applicationContext, msg, Toast.LENGTH_SHORT).show()
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(R.id.action_newPomodoroFragment_to_pomodoroListFragment)
            }
        }
        viewmodel.snackbarMessage.observe(::getLifecycle) {
            it.getContentIfNotHandled()?.let { msg ->

                val message = getString(msg)
                val snackBar = Snackbar.make(
                    requireActivity().findViewById(R.id.content),
                    message, Snackbar.LENGTH_SHORT
                )
                snackBar.show()
            }
        }

    }

    private fun showNumberPickerDialog(value : Int) {
        val numberPickerDialog = NumberPickerDialog().newInstance( Constants.GOAL_COUNT_FLAG, value)

        activity?.supportFragmentManager?.let { fragmentManager ->
            numberPickerDialog?.setOnButtonClickedListener { it ->
                viewmodel.goalCount = it
                viewmodel.savePomodoro()
            }
            numberPickerDialog?.show(fragmentManager, Constants.NUMBER_PICKER)
        }
    }



    private fun setToolbarTitle() : String {

        if (isNewPomodoro) return "New Task"

        val taskDate = SimpleDateFormat("MM/dd").format(Date(System.currentTimeMillis()))
        return taskDate.plus(" Task")

    }




}


