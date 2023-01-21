package com.mangpo.taste.view

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mangpo.domain.model.authorizeNew.AuthorizeNewReqEntity
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityStartBinding
import com.mangpo.taste.viewmodel.StartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding, StartViewModel>(ActivityStartBinding::inflate) {
    override val viewModel: StartViewModel by viewModels()

    private lateinit var createUserReqEntity: CreateUserReqEntity

    override fun initAfterBinding() {
        binding.apply {
            activity = this@StartActivity
        }

        createUserReqEntity = intent.getParcelableExtra<CreateUserReqEntity>("newUser")!!

        observe()
    }

    private fun observe() {
        viewModel.createUserResult.observe(this, Observer {
            if (it) {
                viewModel.authorizeNew(AuthorizeNewReqEntity(createUserReqEntity.email, createUserReqEntity.password))
            } else {
                showToast("회원가입 중 문제가 발생했습니다. 다시 시도해 주세요.")
            }
        })

        viewModel.loginSuccess.observe(this, Observer {
            if (it) {   //로그인 성공
                startActivityWithClear(MainActivity::class.java)
            } else {    //로그인 실패
                showToast("회원가입 중 문제가 발생했습니다. 다시 시도해 주세요.")
            }
        })
    }

    fun createUser() {
        viewModel.createUser(createUserReqEntity)
    }
}