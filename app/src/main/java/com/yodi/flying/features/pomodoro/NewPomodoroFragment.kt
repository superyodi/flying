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

        Timber.d("user id: ${(activity?.application as MainApplication).sharedPreferences.getLong(Constants.PREF_USER_ID)}")

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
        }
        else {
            binding.groupDuedate.visibility = View.GONE
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
            binding.btnOneday.setBackgroundResource(R.drawable.chip_bright_orange_clicked)
            binding.btnEveryday.setBackgroundResource(R.drawable.chip_bright_orange_unclicked)
            binding.btnEveryday.setTextColor(Color.parseColor("#dbcfc7"))
            binding.btnOneday.setTextColor(Color.parseColor("#ff8e71"))


        }

        binding.btnEveryday.setOnClickListener {

            viewmodel.hasDuedate.postValue(true)

            binding.groupDuedate.visibility = View.VISIBLE
            binding.btnEveryday.setBackgroundResource(R.drawable.chip_bright_orange_clicked)
            binding.btnOneday.setBackgroundResource(R.drawable.chip_bright_orange_unclicked)
            binding.btnOneday.setTextColor(Color.parseColor("#dbcfc7"))
            binding.btnEveryday.setTextColor(Color.parseColor("#ff8e71"))

        }

        binding.btnAddMemo.setOnClickListener {

            binding.taskDescription.visibility = View.VISIBLE
            binding.btnAddMemo.visibility = View.INVISIBLE

        }

        binding.addPomodoro.setOnClickListener { view ->
            viewmodel.savePomodoro()

            // TODO("goal count 입력받는 dialog 보여줌 ")
            viewmodel.pomodoroUpdatedEvent.observe(::getLifecycle) { it ->
                it.getContentIfNotHandled()?.let {
                    view.findNavController().navigate(R.id.action_newPomodoroFragment_to_pomodoroListFragment)
                }
            }
        }

        viewmodel.snackbarMessage.observe(::getLifecycle) {
            it.getContentIfNotHandled()?.let { msg ->

                val message = getString(msg)
                val snackBar = Snackbar.make(
                    requireActivity().findViewById(R.id.content),
                    message, Snackbar.LENGTH_LONG
                )
                snackBar.show()
            }
        }

    }

    private fun setToolbarTitle() : String {

        if (isNewPomodoro) return "New Task"

        val taskDate = SimpleDateFormat("MM/dd").format(Date(System.currentTimeMillis()))
        return taskDate.plus(" Task")

    }









}


