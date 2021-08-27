package com.yodi.flying.features.setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener
import com.yodi.flying.MainActivity
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.databinding.ActivitySetupBinding
import com.yodi.flying.features.splash.SplashActivity
import com.yodi.flying.utils.Constants
import com.yodi.flying.utils.convertLongToString
import com.yodi.flying.utils.convertRulerValueToLong
import com.yodi.flying.utils.convertRulerValueToString
import timber.log.Timber


class SetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupBinding
    private val setupViewmodel: SetupViewModel by viewModels {
        SetupViewModelFactory(
            (application as MainApplication).userRepository,
            (application as MainApplication).tagRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup)
        binding.viewModel = setupViewmodel
        binding.lifecycleOwner = this@SetupActivity

        val userId = intent.getLongExtra(Constants.EXTRA_USER_ID, -1L)
        setupViewmodel.start(userId)


        observeViewModel()
        setTagChips()
        setupTimber()
        setProgressListener()

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
                    chekedTags.add(chip.text.toString())
                }
                setupViewmodel.checkedTags = chekedTags

            }
        }
        setupViewmodel.needUserGoalTime.observe(::getLifecycle) {
            binding.rulerPicker.run {
                setupViewmodel.userGoalTime = convertRulerValueToLong(this.currentValue, this.longIndicatorStep)
            }

        }
        setupViewmodel.navigateToHome.observe(::getLifecycle) {
            navigateToSplash()

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
            val chip = Chip(this)
                .apply {
                    this.text = tag
                    this.isCheckable = true
                    this.isChecked = false
                }
            chipGroup.addView(chip)
        }
    }

    private fun setProgressListener(){
        binding.rulerPicker.setValuePickerListener(object : RulerValuePickerListener {
            val intervalStep = binding.rulerPicker.longIndicatorStep
            override fun onValueChange(selectedValue: Int) {
                binding.textViewUserGoalTime.text = convertRulerValueToString(selectedValue, intervalStep )
            }
            override fun onIntermediateValueChange(selectedValue: Int) {
                binding.textViewUserGoalTime.text = convertRulerValueToString(selectedValue, intervalStep)
            }
        })
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }

    private fun navigateToSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }



}




