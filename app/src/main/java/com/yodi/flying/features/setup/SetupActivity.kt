package com.yodi.flying.features.setup

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.databinding.ActivitySetupBinding
import com.yodi.flying.model.entity.Tag
import com.yodi.flying.utils.Constants
import timber.log.Timber

class SetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupBinding
    private val setupViewmodel: SetupViewModel by viewModels {
        SetupViewModelFactory(
            (application as MainApplication).userRepository,
            (application as MainApplication).tagRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup)
        binding.viewModel = setupViewmodel
        binding.lifecycleOwner = this@SetupActivity

        observeViewModel()
        setTagChips()
        setupTimber()

    }

    private fun observeViewModel() {

        setupViewmodel.currentStageState.observe(this) { stage ->

            setViewVisibility(stage)
            setupViewmodel.setResourceAsStage(stage)
        }

        setupViewmodel.snackbarMessage.observe(::getLifecycle) {
            it.getContentIfNotHandled()?.let { msg ->

                val message = getString(msg)
                val snackBar = Snackbar.make(
                    binding.root,
                    message, Snackbar.LENGTH_SHORT
                )
                snackBar.show()
            }
        }

        setupViewmodel.prevButtonClicked.observe(::getLifecycle) {
            super.onBackPressed()
        }

        setupViewmodel.needCheckedTags.observe(::getLifecycle) {
            var chekedTags = mutableListOf<String>()

            binding.contentStage2.let { group ->

                for (chipId in group.checkedChipIds) {
                    val chip = findViewById<Chip>(chipId)
                    Timber.d(chip.text.toString())
                    chekedTags.add(chip.text.toString())
                }
                setupViewmodel.checkedTags = chekedTags

            }
        }

    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun setViewVisibility(stage: String) {

        when(stage) {
            Constants.SETUP_STAGE_1 -> {
                binding.stage1.isActivated = true
                binding.stage2.isActivated = false
                binding.stage3.isActivated = false

                binding.contentStage1.visibility = View.VISIBLE
                binding.contentStage2.visibility = View.GONE
                binding.contentStage3.visibility = View.GONE

            }
            Constants.SETUP_STAGE_2 -> {
                binding.stage1.isActivated = true
                binding.stage2.isActivated = true
                binding.stage3.isActivated = false

                binding.contentStage1.visibility = View.GONE
                binding.contentStage2.visibility = View.VISIBLE
                binding.contentStage3.visibility = View.GONE

            }
            Constants.SETUP_STAGE_3 -> {
                binding.stage1.isActivated = true
                binding.stage2.isActivated = true
                binding.stage3.isActivated = true

                binding.contentStage1.visibility = View.GONE
                binding.contentStage2.visibility = View.GONE
                binding.contentStage3.visibility = View.VISIBLE

            }

        }

    }

    private fun setTagChips() {
        val chipGroup : ChipGroup = binding.contentStage2
        val defaultTags = setupViewmodel.getDefaultTags()

        for(tag in defaultTags) {
            Timber.d(tag)
            val chip = Chip(this)
                .apply {
                    this.text = tag
                    this.isCheckable = true
                    this.isChecked = false
                }
            chipGroup.addView(chip)
        }


    }
}




