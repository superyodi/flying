package com.yodi.flying.features.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yodi.flying.MainApplication
import com.yodi.flying.custom.NumberPickerDialog
import com.yodi.flying.databinding.FragmentPreferenceBinding
import com.yodi.flying.utils.Constants

class PreferenceFragment : Fragment() {

    private lateinit var binding: FragmentPreferenceBinding


    private val preferenceViewModel : PreferenceViewModel by viewModels {
        PreferenceViewModelFactory((activity?.application as MainApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreferenceBinding
            .inflate(inflater, container, false)
            .apply {
                viewModel = preferenceViewModel
                lifecycleOwner = viewLifecycleOwner
            }

        setViewEventListener()

        observeViewModel()


        return binding.root
    }


    private fun observeViewModel() {

        preferenceViewModel.longRestTermInput.observe(::getLifecycle) {
            preferenceViewModel.longRestTerm.value = it.toInt()
        }
        preferenceViewModel.longRestTimeInput.observe(::getLifecycle) {
            preferenceViewModel.longRestTime.value = it.toInt()
        }
        preferenceViewModel.shortRestTimeInput.observe(::getLifecycle) {
            preferenceViewModel.shortRestTime.value = it.toInt()
        }
        preferenceViewModel.runningTimeInput.observe(::getLifecycle) {
            preferenceViewModel.runningTime.value = it.toInt()
        }

    }

    private fun setViewEventListener() {

        binding.setClickListener { view ->
            when (view) {
                binding.runningTimeButton -> showNumberPickerDialog(Constants.RUNNING_TIME_FLAG)
                binding.longRestButton -> showNumberPickerDialog(Constants.LONG_REST_TIME_FLAG)
                binding.shortRestButton -> showNumberPickerDialog(Constants.SHORT_REST_TIME_FLAG)
                binding.longTermButton -> showNumberPickerDialog(Constants.LONG_REST_TERM_FLAG)


            }
        }


    }


    private fun showNumberPickerDialog(flag: String) {
        val numberPickerDialog = NumberPickerDialog().newInstance(flag)

        activity?.supportFragmentManager?.let { fragmentManager ->
            numberPickerDialog?.setOnButtonClickedListener { it ->
                setPreferenceValue(flag, it)
            }
            numberPickerDialog?.show(fragmentManager, Constants.NUMBER_PICKER)
        }

    }


    private fun setPreferenceValue(flag : String, value: Int) {
        when(flag) {
            Constants.RUNNING_TIME_FLAG -> preferenceViewModel.runningTime.value = value
            Constants.LONG_REST_TIME_FLAG -> preferenceViewModel.longRestTime.value = value
            Constants.SHORT_REST_TIME_FLAG -> preferenceViewModel.shortRestTime.value = value
            Constants.LONG_REST_TERM_FLAG -> preferenceViewModel.longRestTerm.value = value

        }

    }
}

