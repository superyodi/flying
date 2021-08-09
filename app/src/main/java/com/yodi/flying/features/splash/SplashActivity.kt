package com.yodi.flying.features.splash

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yodi.flying.MainActivity
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.databinding.ActivitySetupBinding
import com.yodi.flying.databinding.ActivitySplashBinding
import com.yodi.flying.features.login.LogInActivity
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView< ActivitySplashBinding>(this, R.layout.activity_splash)

        setupTimber()

        val userId = (application as MainApplication).userRepository.getUserIdFromPreferences()

        Timber.d("Splash 호출")
        if(userId == -1L || userId == 0L)
            navigateToLogIn()
        else
            navigateToHome()

    }

    private fun navigateToHome() {
        Timber.d("setup --> home")
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

}