package com.yodi.flying.features.preference

import androidx.lifecycle.*
import com.yodi.flying.model.repository.UserRepository

class PreferenceViewModel(private val userRepository: UserRepository) : ViewModel() {


    val shortRestTime = MutableLiveData<Int>()
    val longRestTime = MutableLiveData<Int>()
    val runningTime = MutableLiveData<Int>()
    val longRestTerm = MutableLiveData<Int>()

    val shortRestTimeInput = MutableLiveData<String>()
    val longRestTimeInput = MutableLiveData<String>()
    val runningTimeInput = MutableLiveData<String>()
    val longRestTermInput = MutableLiveData<String>()

    val runningTimeString : LiveData<String>
        get() = runningTime.map { "${it}분"}
    val longRestTimeString: LiveData<String>
        get() = longRestTime.map { "${it}분" }
    val shortRestTimeString: LiveData<String>
        get() = shortRestTime.map { "${it}분" }
    val longRestTermString: LiveData<String>
        get() = longRestTerm.map { "$it 뽀모도로" }


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