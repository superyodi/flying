package com.yodi.flying.features.preference

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yodi.flying.model.repository.UserRepository
import com.yodi.flying.mvvm.SingleLiveEvent
import timber.log.Timber

class PreferenceViewModel(private val userRepository: UserRepository) : ViewModel() {

    val needNumberPicker : SingleLiveEvent<Void> = SingleLiveEvent()

    fun onNumberPickerClicked(view: View) {
        Timber.d("onNumberPickerClicked")
        needNumberPicker.call()
    }

}




class PreferenceViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PreferenceViewModel::class.java)) {
            PreferenceViewModel(userRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}