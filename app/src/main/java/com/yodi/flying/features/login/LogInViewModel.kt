package com.yodi.flying.features.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yodi.flying.model.entity.User
import com.yodi.flying.model.repository.UserRepository
import com.yodi.flying.utils.Constants
import kotlinx.coroutines.launch
import timber.log.Timber

class LogInViewModel(application: Application, private val repository: UserRepository) : AndroidViewModel(application) {


    fun executeLogin(userId: Long) {

        val user = getUserWithId(userId)

        Constants.USER_ID = userId


        if(user == null) {
            Timber.i("go to SetUpActivity")
        }
        else {
            repository.setUserIdToPreferences(userId)
            Timber.i("go to MainActivity")
        }

    }

    private fun getUserWithId(userId: Long) : User? {
        var user : User? = null
        viewModelScope.launch {
            user = repository.getUser(userId)
        }
        return user
    }





}



class LogInViewModelFactory(val application: Application, val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LogInViewModel::class.java)) {
            LogInViewModel(application, repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}