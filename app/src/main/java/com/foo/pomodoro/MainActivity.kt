package com.foo.pomodoro

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.foo.pomodoro.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this, R.layout.activity_main)


        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_resource_return)
            it.setDisplayHomeAsUpEnabled(true)


        }
        toolbar.visibility = View.GONE

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        // set bottom view visibility depending on fragment's id
        navController.addOnDestinationChangedListener { _, destination, _ ->


            when(destination.id) {
                R.id.newPomodoroFragment -> {
                    navView.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                    toolbar.setBackgroundColor(Color.parseColor("#ff6037"))
                    toolbar.setTitleTextColor(Color.parseColor("#ffffff"))

                }
                R.id.timerFragment -> {
                    navView.visibility = View.GONE
                    toolbar.visibility = View.GONE


                }
                else -> {
                    navView.visibility = View.VISIBLE
                    toolbar.visibility = View.GONE
                }
            }
        }

        setupTimber()
    }

    fun setToolBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }





}