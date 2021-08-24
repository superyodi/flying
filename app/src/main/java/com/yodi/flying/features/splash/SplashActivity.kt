package com.yodi.flying.features.splash

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yodi.flying.MainActivity
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.databinding.ActivitySetupBinding
import com.yodi.flying.databinding.ActivitySplashBinding
import com.yodi.flying.features.login.LogInActivity
import com.yodi.flying.features.setup.SetupViewModel
import com.yodi.flying.features.setup.SetupViewModelFactory
import com.yodi.flying.model.entity.Pomodoro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels {
        SplashViewModelFactory(
            (application as MainApplication).pomodoroRepository,
            (application as MainApplication).reportRepository
        )
    }
    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView< ActivitySplashBinding>(this, R.layout.activity_splash)

        setupTimber()

        val userId = splashViewModel.getUserId()

        if(userId == -1L || userId == 0L)
            navigateToLogIn()
        else {
            splashViewModel.initTodayData()
            navigateToHome()
        }



        binding.imageView.setOnClickListener {
            Timber.d("눌렀다!")
            splashViewModel.initTodayData()
        }


    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }

    private fun navigateToLogIn() {
        val intent = Intent(this, LogInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }


    private fun testData() {
        val repo = (application as MainApplication).pomodoroRepository
        val userId = repo.userId

        val demos : List<Pomodoro> = listOf(
            Pomodoro(
                userId, "test1", "test", 5, 0,
                "", false, 20210822,null
            ),
            Pomodoro(
                userId, "test2", "test", 6, 0,
                "", false, 20210824,null
            ),
            Pomodoro(
                userId, "test3", "test", 6, 0,
                "", true, 20210820,20210830
            ),
            Pomodoro(
                userId, "test4", "test", 6, 0,
                "", true, 20210820,20210823
            )
        )

        CoroutineScope(Dispatchers.IO).launch {
            for(pomo in demos){
                repo.insert(pomo)
            }
        }
    }

}