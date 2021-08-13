package com.yodi.flying.features.preference

import androidx.lifecycle.*
import com.yodi.flying.model.entity.User
import com.yodi.flying.model.repository.UserRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class PreferenceViewModel(private val userRepository: UserRepository) : ViewModel() {



    val userNickname = MutableLiveData<String>()
    val shortRestTime = MutableLiveData<Int>()
    val longRestTime = MutableLiveData<Int>()
    val runningTime = MutableLiveData<Int>()
    val longRestTerm = MutableLiveData<Int>()

    val isAutoBreakMode = MutableLiveData<Boolean>()// 자동으로 뽀모도로 넘김
    val isAutoSkipMode = MutableLiveData<Boolean>() // 자동으로 휴식 시작
    val isNonBreakMode = MutableLiveData<Boolean>()

    private var userId : Long = 0L
    private val currentUser = MutableLiveData<User>()



  /*
    val shortRestTimeInput = MutableLiveData<String>()
    val longRestTimeInput = MutableLiveData<String>()
    val runningTimeInput = MutableLiveData<String>()
    val longRestTermInput = MutableLiveData<String>()
  */

    val runningTimeString : LiveData<String>
        get() = runningTime.map { "${it}분"}
    val longRestTimeString: LiveData<String>
        get() = longRestTime.map { "${it}분" }
    val shortRestTimeString: LiveData<String>
        get() = shortRestTime.map { "${it}분" }
    val longRestTermString: LiveData<String>
        get() = longRestTerm.map { "$it 뽀모도로" }


    fun start() {

        setUserData()

    }

    private fun setUserData() = viewModelScope.launch {
        userId = userRepository.getUserIdFromPreferences()
        currentUser.value = userRepository.getUser(userId)
        currentUser.value?.let {
            shortRestTime.value = TimeUnit.MILLISECONDS.toMinutes(it.shortBreakTime).toInt()

            Timber.d("shortRestTime: $shortRestTime")
            longRestTime.value = TimeUnit.MILLISECONDS.toMinutes(it.longBreakTime).toInt()
            runningTime.value = TimeUnit.MILLISECONDS.toMinutes(it.runningTime).toInt()
            longRestTerm.value = it.longBreakTerm

            isAutoBreakMode.value = it.isAutoBreakMode
            isAutoSkipMode.value = it.isAutoSkipMode
            isNonBreakMode.value = it. isNonBreakMode
            userNickname.value = it.nickname
        }

    }

   fun updateUserData() = viewModelScope.launch {
        currentUser.value?.let {
            it.shortBreakTime = TimeUnit.MINUTES.toMillis(shortRestTime.value!!.toLong())
            it.longBreakTime = TimeUnit.MINUTES.toMillis(longRestTime.value!!.toLong())
            it.runningTime = TimeUnit.MINUTES.toMillis(runningTime.value!!.toLong())
            it.longBreakTerm = longRestTerm.value!!
            it.isAutoBreakMode = isAutoBreakMode.value!!
            it.isAutoSkipMode = isAutoSkipMode.value!!
            it.isNonBreakMode = isNonBreakMode.value!!

        }

       currentUser.value?.let { userRepository.update(it) }
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