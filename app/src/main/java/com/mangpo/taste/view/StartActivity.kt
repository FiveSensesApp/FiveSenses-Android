package com.mangpo.taste.view

import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityStartBinding
import com.mangpo.taste.viewmodel.StartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding>(ActivityStartBinding::inflate) {
    private val startVm: StartViewModel by viewModels()

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

        startVm.createUserResult.observe(this, Observer {
            if (it) {
                startNextActivity(MainActivity::class.java)
            }
        })
    }

    fun createUser() {
        startVm.createUser(createUserReqEntity)
    }
}