package com.mangpo.taste.view

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mangpo.domain.model.authorizeNew.AuthorizeNewReqEntity
import com.mangpo.domain.model.createUser.CreateUserReqEntity
import com.mangpo.taste.R
import com.mangpo.taste.base2.BaseActivity
import com.mangpo.taste.databinding.ActivityStartBinding
import com.mangpo.taste.viewmodel.StartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding>(R.layout.activity_start) {
    private val startVm: StartViewModel by viewModels()

    private lateinit var createUserReqEntity: CreateUserReqEntity

    override fun initAfterBinding() {
        binding.apply {
            activity = this@StartActivity
            this.startVm = this@StartActivity.startVm
        }
        setCommonObserver(listOf(startVm))

        createUserReqEntity = intent.getParcelableExtra<CreateUserReqEntity>("newUser")!!

        observe()
    }

    private fun observe() {
        startVm.createUserResult.observe(this, Observer {
            if (it) {
                startVm.authorizeNew(AuthorizeNewReqEntity(createUserReqEntity.email, createUserReqEntity.password))
            } else {
                showToast("회원가입 중 문제가 발생했습니다. 다시 시도해 주세요.")
            }
        })

        startVm.loginSuccess.observe(this, Observer {
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