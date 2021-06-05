package com.foo.pomodoro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foo.pomodoro.databinding.ActivityMainBinding
import androidx.databinding.DataBindingUtil.setContentView
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        setupTimber()

    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }


}