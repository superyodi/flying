package com.yodi.flying.custom

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.model.entity.Tag
import com.yodi.flying.databinding.DialogTagPickerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class TagPickerDialog: BottomSheetDialogFragment() {

    private lateinit var binding : DialogTagPickerBinding
    private val viewmodel: TagViewModel by viewModels {
        TagViewModelFactory((activity?.application as MainApplication).tagRepository)
    }
    private var updateTagsFlag = true
    private lateinit var listener: TagPickerDialogListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogTagPickerBinding.inflate(inflater, container, false)

        binding.btnAddTag.setOnClickListener {

            val tagTitle = binding.newTag.text.toString()
            listener.onButtonClicked(tagTitle)
            viewmodel.addTag(tagTitle)
            dismiss()
        }

        subscribeUi()

        return binding.root

    }



    private fun subscribeUi() {
        viewmodel.allTags.observe(::getLifecycle)  { result ->
            if (updateTagsFlag) {
                setTag(result)
                updateTagsFlag = false
            }
        }

    }
    fun setOnButtonClickedListener(listener: (String) -> Unit) {
        this.listener = object : TagPickerDialogListener {
            override fun onButtonClicked(tag: String) {
                listener(tag)
            }
        }
    }

    private fun setTag(tagList: List<Tag>) {
        val chipGroup: ChipGroup = binding.chipGroup
        for (index in tagList.indices) {
            val tagName = tagList[index].tagName
            val chip = Chip(this.context)
            val paddingDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10f,
                resources.displayMetrics
            ).toInt()
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
            chip.text = tagName


            chip.setOnClickListener {
                binding.newTag.setText(tagName)
            }
            chipGroup.addView(chip)
        }
    }


    fun getInstance(): TagPickerDialog {
        return TagPickerDialog()
    }

    interface TagPickerDialogListener {
        fun onButtonClicked(tag: String)
    }




}





