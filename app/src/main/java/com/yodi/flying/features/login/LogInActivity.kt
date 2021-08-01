package com.yodi.flying.features.login

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.databinding.ActivityLoginBinding
import com.yodi.flying.viewmodels.PomoListViewModel
import com.yodi.flying.viewmodels.PomoListViewModelFactory
import timber.log.Timber


class LogInActivity : AppCompatActivity(){

    private lateinit var binding : ActivityLoginBinding
    private val logInViewModel: LogInViewModel by viewModels {
        LogInViewModelFactory(application ,(application as MainApplication).userRepository)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupTimber()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.btnStartWithKakao.setOnClickListener{
            val userId = getKakaoId(this)
            logInViewModel.executeLogin(userId)

        }

    }

    private fun getKakaoId(context : Context) : Long {

        var userID = 0L

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if(error != null) {
                Timber.e("로그인 실패, $error")
            }
            else if (token != null) {
                Timber.i("로그인 성공 ${token.accessToken}")
            }
        }

        if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback) // 카카오앱으로 로그인
        }
        else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback) // 카카오 계정으로 로그인
        }
        UserApiClient.instance.me { user, error ->
            user?.let {
                userID = user.id
                Timber.i("카카오 아이: ${user.id}")
            }
            error?.let {
                Timber.i("정보를 가져올 수 없습니다. ")
            }
        }

        return userID
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}