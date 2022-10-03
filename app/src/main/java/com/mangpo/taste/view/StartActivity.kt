package com.mangpo.taste.view

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mangpo.domain.model.authorize.AuthorizeReqEntity
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityStartBinding
import com.mangpo.taste.viewmodel.LoginViewModel
import com.mangpo.taste.viewmodel.StartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding>(ActivityStartBinding::inflate) {
    private val startVm: StartViewModel by viewModels()
    private val loginVm: LoginViewModel by viewModels()

    private lateinit var createUserReqEntity: CreateUserReqEntity

    override fun initAfterBinding() {
        binding.apply {
            activity = this@StartActivity
        }

        createUserReqEntity = intent.getParcelableExtra<CreateUserReqEntity>("newUser")!!

        observe()
    }

    private fun observe() {
        startVm.toast.observe(this, Observer {
            val msg = it.getContentIfNotHandled()

            if (msg!=null)
                showToast(msg)
        })

        loginVm.toast.observe(this, Observer {
            val msg = it.getContentIfNotHandled()

            if (msg!=null) {
                showToast(msg)
            }
        })

        startVm.createUserResult.observe(this, Observer {
            if (it) {
                loginVm.authorize(AuthorizeReqEntity(createUserReqEntity.email, createUserReqEntity.password))
            } else {
                showToast("회원가입 중 문제가 발생했습니다. 다시 시도해 주세요.")
            }
        })

        loginVm.loginSuccess.observe(this, Observer {
            if (it) {   //로그인 성공
                startActivityWithClear(MainActivity::class.java)
            } else {    //로그인 실패
                showToast("회원가입 중 문제가 발생했습니다. 다시 시도해 주세요.")
            }
        })
    }

    fun createUser() {
        startVm.createUser(createUserReqEntity)
    }
}