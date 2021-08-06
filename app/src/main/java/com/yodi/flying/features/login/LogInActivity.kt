package com.yodi.flying.features.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.yodi.flying.MainActivity
import com.yodi.flying.MainApplication
import com.yodi.flying.R
import com.yodi.flying.custom.WaitingDialog
import com.yodi.flying.databinding.ActivityLoginBinding
import com.yodi.flying.features.setup.SetupActivity
import kotlinx.coroutines.*
import timber.log.Timber


class LogInActivity : AppCompatActivity(){

    private lateinit var binding : ActivityLoginBinding
    private val logInViewModel: LogInViewModel by viewModels {
        LogInViewModelFactory((application as MainApplication).userRepository)
    }
    private var userId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.btnStartWithKakao.setOnClickListener{

            getKakaoId(this)
            Timber.d("getKakaoId(): $userId")
            logInViewModel.executeLogin(userId)
        }

        setupTimber()
        observeViewModel()


    }
    private fun observeViewModel() {
        logInViewModel.navigateToSetup.observe(::getLifecycle) {
            navigateToSetup()
        }
        logInViewModel.navigateToHome.observe(::getLifecycle) {
            navigateToHome()
        }
    }


    private fun getKakaoId(context : Context) : Long {



        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if(error != null) {
                Timber.e("로그인 실패, $error")
            }
            else if (token != null) {
                Timber.i("로그인 성공 ${token.accessToken}")
                UserApiClient.instance.me { user, error ->
                    user?.let {
                        Timber.i("카카오 아이디: ${user.id}")
                        userId = user.id

                    }
                    error?.let {
                        Timber.i("정보를 가져올 수 없습니다. ")

                        Snackbar.make(
                            binding.root,
                            getString(R.string.kakao_login_fail),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(
                context,
                callback = callback
            ) // 카카오앱으로 로그인
        }
        else {
            UserApiClient.instance.loginWithKakaoAccount(
                context,
                callback = callback
            ) // 카카오 계정으로 로그인
        }

        return userId
    }

    private fun navigateToSetup() {
        val intent = Intent(this, SetupActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}