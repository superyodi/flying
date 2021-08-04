package com.yodi.flying.features.setup

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yodi.flying.R
import com.yodi.flying.model.entity.Tag
import com.yodi.flying.model.repository.TagRepository
import com.yodi.flying.model.repository.UserRepository
import com.yodi.flying.mvvm.Event
import com.yodi.flying.mvvm.SingleLiveEvent
import com.yodi.flying.utils.Constants
import timber.log.Timber

class SetupViewModel(private val userRepository: UserRepository, private val tagRepository: TagRepository) : ViewModel() {

    val prevButtonClicked : SingleLiveEvent<Void> = SingleLiveEvent()
    val needCheckedTags : SingleLiveEvent<Void> = SingleLiveEvent()
    val navigateToHome : SingleLiveEvent<Void> = SingleLiveEvent()
    val userGoalTime = MutableLiveData<Long>()
    val userNickname = MutableLiveData<String>()
    val titleText =  MutableLiveData<String>()
    val subTitleText =  MutableLiveData<String>()
    val iconResource = MutableLiveData<Int>()


    val currentStageState = MutableLiveData(Constants.SETUP_STAGE_1)
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarText
    var checkedTags  = mutableListOf<String>()


    private fun goToNextStage() {
        when(currentStageState.value) {
            Constants.SETUP_STAGE_1 -> currentStageState.value = Constants.SETUP_STAGE_2
            Constants.SETUP_STAGE_2 -> currentStageState.value = Constants.SETUP_STAGE_3
            Constants.SETUP_STAGE_3 -> currentStageState.value = Constants.SETUP_STAGE_DONE
            else -> return

        }
    }

    fun onNextButtonClicked(view: View) {
        when(currentStageState.value) {
            Constants.SETUP_STAGE_1 -> validateUserNickname()
            Constants.SETUP_STAGE_2 -> {
                // List<Tag> 받아오기
                needCheckedTags.call()
                currentStageState.value = Constants.SETUP_STAGE_3
            }
            Constants.SETUP_STAGE_3 -> currentStageState.value = Constants.SETUP_STAGE_DONE
            else -> return

        }
    }

    fun onPrevButtonClicked(view: View) {
        when(currentStageState.value) {
            Constants.SETUP_STAGE_1 -> prevButtonClicked.call()
            Constants.SETUP_STAGE_2 -> currentStageState.value = Constants.SETUP_STAGE_1
            Constants.SETUP_STAGE_3 -> currentStageState.value = Constants.SETUP_STAGE_2
            else -> return
        }

    }

    fun getDefaultTags() = listOf(
        "프로젝트","공모전", "영어회화", "요가","런닝","홈트",
        "자격증","포트폴리오","독서"
    )

    private fun validateUserNickname() {
        val nickname = userNickname.value

        // 한글 길이 어떻게 check하는지 확인
        Timber.i("글자수: ${nickname?.length}, 닉네임: ${userNickname.value}")

        if (nickname.isNullOrEmpty()) {
            _snackbarText.value = Event(R.string.empty_user_nickname)
            return
        }

        if (nickname.length > 8) {
            _snackbarText.value = Event(R.string.wrong_user_nickname_length)
            return
        }
        goToNextStage()
    }

    fun setResourceAsStage(stage: String) {
        when(stage) {
            Constants.SETUP_STAGE_1 -> {
                titleText.value = Constants.SETUP_TITLE_1
                subTitleText.value = Constants.SETUP_SUB_TITLE_1
                iconResource.value = R.drawable.ic_setup_nickname
            }
            Constants.SETUP_STAGE_2 -> {
                val nickname = userNickname.value ?: ""
                titleText.value =  "요즘 ${nickname}님은\n어떤 것에 열중하고계신가요?"
                subTitleText.value = Constants.SETUP_SUB_TITLE_2
                iconResource.value = R.drawable.ic_setup_tag
            }
            Constants.SETUP_STAGE_3 -> {
                val nickname = userNickname.value ?: ""
                titleText.value = "${nickname}님의\n목표 집중시간을 알려주세요"
                subTitleText.value = Constants.SETUP_SUB_TITLE_3
                iconResource.value = R.drawable.ic_setup_time
            }
            else -> return
        }

    }


    private fun createNewUser() {


    }
}



class SetupViewModelFactory(private val userRepository: UserRepository, private val tagRepository: TagRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SetupViewModel::class.java)) {
            SetupViewModel(userRepository, tagRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}