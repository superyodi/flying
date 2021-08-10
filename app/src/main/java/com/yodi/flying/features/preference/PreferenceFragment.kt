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

        observeViewModel()


        return binding.root
    }


    private fun observeViewModel() {
        preferenceViewModel.needNumberPicker.observe(::getLifecycle) {
            openNumberPickerDialog()
        }
    }


    private fun openNumberPickerDialog() {
        val numberPickerDialog = NumberPickerDialog().getInstance()

        activity?.supportFragmentManager?.let { fragmentManager ->

            numberPickerDialog.show(fragmentManager, Constants.NUMBER_PICKER)

        }
    }
}

