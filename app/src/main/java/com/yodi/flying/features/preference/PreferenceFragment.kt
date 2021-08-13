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

        preferenceViewModel.start()

        setViewEventListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        preferenceViewModel.updateUserData()
    }



    private fun setViewEventListener() {

        binding.setClickListener { view ->
            when (view) {
                binding.runningTimeButton ->
                    showNumberPickerDialog(Constants.RUNNING_TIME_FLAG,
                        preferenceViewModel.runningTime.value ?: 0)
                binding.longRestButton -> showNumberPickerDialog(Constants.LONG_REST_TIME_FLAG,
                    preferenceViewModel.longRestTime.value)
                binding.shortRestButton -> showNumberPickerDialog(Constants.SHORT_REST_TIME_FLAG,
                    preferenceViewModel.shortRestTime.value)
                binding.longTermButton -> showNumberPickerDialog(Constants.LONG_REST_TERM_FLAG,
                    preferenceViewModel.longRestTerm.value)

            }
        }


    }


    private fun showNumberPickerDialog(flag: String, value: Int?) {
        val numberPickerDialog = NumberPickerDialog().newInstance(flag, value)

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

