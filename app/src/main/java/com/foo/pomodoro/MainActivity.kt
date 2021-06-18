package com.foo.pomodoro

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.foo.pomodoro.databinding.ActivityMainBinding
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        // set bottom view visibility depending on fragment's id
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when(destination.id) {
                R.id.newPomodoroFragment, R.id.timerFragment-> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }
        }

        setupTimber()
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }

}


