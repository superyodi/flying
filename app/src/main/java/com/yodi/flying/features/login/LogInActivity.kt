package com.yodi.flying.features.login

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
import com.yodi.flying.databinding.ActivityLoginBinding
import com.yodi.flying.features.setup.SetupActivity
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
            loginKakaoTalk()

            Timber.d("getKakaoId(): $userId")

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


    // DI 라이브러리 추가 후 login repository로 이동할 예정


    private fun loginKakaoTalk() {
        val isKakaoTalkLoginAvailable = UserApiClient.instance.isKakaoTalkLoginAvailable(this)
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if(error != null) {
                Timber.e("로그인 실패, $error")
            }
            else if (token != null) {
                Timber.i("로그인 성공 ${token.accessToken}")
                loadUserId()
            }
        }

        if(isKakaoTalkLoginAvailable) {
            UserApiClient.instance.loginWithKakaoTalk(
                this,
                callback = callback
            )
        }
        else {
            UserApiClient.instance.loginWithKakaoAccount(
                this,
                callback = callback
            )
        }

    }

    private fun loadUserId()  {
        UserApiClient.instance.me { user, error ->
            user?.let {
                Timber.i("카카오 아이디: ${user.id}")
                userId = user.id
                logInViewModel.executeLogin(userId)
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


    private fun navigateToSetup() {
        val intent = Intent(this, SetupActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(com.yodi.flying.utils.Constants.EXTRA_USER_ID, userId)
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