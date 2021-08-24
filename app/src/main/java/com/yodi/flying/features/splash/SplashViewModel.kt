package com.yodi.flying.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yodi.flying.model.repository.*
import com.yodi.flying.utils.convertDateTimeToLong
import com.yodi.flying.utils.convertDateToLong
import kotlinx.coroutines.launch
import timber.log.Timber

import java.util.*

class SplashViewModel (
    private val pomodoroRepository: PomodoroRepository,
    private val reportRepository: ReportRepository ) : ViewModel() {

    fun getUserId() = pomodoroRepository.userId


    fun initTodayData() {
        val todayDate = convertDateToLong(Date())
        if(todayDate == reportRepository.todayDate) return
        val todayTime = convertDateTimeToLong(Date())


        // 새벽 5시 전일때 어제 날짜가 저장되어있다면 return
        if(todayTime < (todayDate * 10L + 5L)) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterday = convertDateToLong(calendar.time)
            Timber.d("어제: $yesterday")

            if(yesterday == reportRepository.todayDate) return
        }

        viewModelScope.launch {
            insertReport(todayDate)
            updateTodayPomodoros(todayDate)
        }

    }


    private suspend fun insertReport(date : Long) {
        reportRepository.insertReport(date)
        reportRepository.setTodayDateToPreferences(date)
    }

    private suspend fun updateTodayPomodoros(date: Long) {
        pomodoroRepository.updateTodayPomodoros(date)
    }

}



class SplashViewModelFactory(private val pomodoroRepository: PomodoroRepository, private val reportRepository: ReportRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            SplashViewModel(pomodoroRepository, reportRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}