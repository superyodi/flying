package com.yodi.flying.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yodi.flying.model.entity.User
import com.yodi.flying.model.repository.UserRepository
import com.yodi.flying.mvvm.SingleLiveEvent
import com.yodi.flying.utils.Constants
import kotlinx.coroutines.launch
import timber.log.Timber

class LogInViewModel(private val userRepository: UserRepository) : ViewModel() {

    val navigateToHome : SingleLiveEvent<Void> = SingleLiveEvent()
    val navigateToSetup : SingleLiveEvent<Void> = SingleLiveEvent()

    fun executeLogin(userId: Long) {

        Timber.d("executeLogin(), userId: $userId")

        val user = getUserWithId(userId)

        if(user == null) {
            Timber.i("go to SetUpActivity")
            navigateToSetup.call()
        }
        else {
            userRepository.setUserIdToPreferences(userId)
            Timber.i("go to MainActivity")
            userRepository.setUserIdToPreferences(userId)
            Constants.USER_ID = userId
            navigateToHome.call()
        }
    }


    private fun getUserWithId(userId: Long) : User? {
        var user : User? = null
        viewModelScope.launch {
            user = userRepository.getUser(userId)
        }
        return user
    }

}



class LogInViewModelFactory(val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LogInViewModel::class.java)) {
            LogInViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}