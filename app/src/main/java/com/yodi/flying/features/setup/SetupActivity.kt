package com.yodi.flying.features.setup

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.databinding.ActivitySetupBinding
import com.yodi.flying.utils.Constants
import timber.log.Timber

class SetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupBinding
    private val setupViewmodel: SetupViewModel by viewModels {
        SetupViewModelFactory((application as MainApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup)
        binding.viewModel = setupViewmodel
        binding.lifecycleOwner = this@SetupActivity

        observeViewModel()
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
}




