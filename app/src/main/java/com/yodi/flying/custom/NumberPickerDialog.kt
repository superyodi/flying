package com.yodi.flying.custom


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yodi.flying.R
import com.yodi.flying.databinding.DialogNumberPickerBinding
import com.yodi.flying.utils.Constants
import timber.log.Timber


class NumberPickerDialog: BottomSheetDialogFragment() {

    private lateinit var binding: DialogNumberPickerBinding
    private lateinit var listener: NumberPickerDialogListener
    private var flag = "flag"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        flag = requireArguments().getString(Constants.EXTRA_NUMBER_PICKER_ID, "EXTRA_NUMBER_PICKER_ID")
        Timber.d("number picker : $flag")


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogNumberPickerBinding.inflate(inflater, container, false)

        binding.addButton.setOnClickListener {
            val selectedVal = binding.numberPicker.value
            Timber.d("selectedVal : $selectedVal")
            listener.onButtonClicked(selectedVal)
            dismiss()
        }
        binding.cancelButton.setOnClickListener {

            dismiss()
        }

        return binding.root
    }


    private fun setNumberRange(flag: String) {

    }

    fun setOnButtonClickedListener(listener: (Int) -> Unit) {
        this.listener = object : NumberPickerDialogListener {
            override fun onButtonClicked(selectedVal: Int) {
                listener(selectedVal)
            }
        }
    }



    fun newInstance(flag: String): NumberPickerDialog? {
        val numberPickerDialog = NumberPickerDialog()
        val args = Bundle()
        args.putString(Constants.EXTRA_NUMBER_PICKER_ID, flag)
        numberPickerDialog.arguments = args
        return numberPickerDialog
    }

}

interface NumberPickerDialogListener {
    fun onButtonClicked(selectedVal: Int)
}


