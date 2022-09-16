package com.mangpo.taste.view

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityMainBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.SpfUtils.getIntEncryptedSpf
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val mainVm: MainViewModel by viewModels()

    private lateinit var mainNavHostFragment: NavHostFragment
    private lateinit var translateUp: Animation
    private lateinit var translateDown: Animation

    var recordFcvVisibility: Int = View.INVISIBLE
    var recordFcvMarginTop: Int = 136

    override fun initAfterBinding() {
        binding.apply {
            activity = this@MainActivity
            vm = mainVm
            lifecycleOwner = this@MainActivity
        }

        //애니메이션 초기화
        translateUp = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_up)
        translateDown = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_down)

        //Navigation - Bottom Navigation 세팅
        mainNavHostFragment = supportFragmentManager.findFragmentById(binding.mainFcv.id) as NavHostFragment
        val navController = mainNavHostFragment.findNavController()
        binding.mainBnv.setupWithNavController(navController)
        binding.mainBnv.background = null

        observe()

        mainVm.getUserInfo(getIntEncryptedSpf("userId"))    //사용자 정보 조회
    }

    override fun onBackPressed() {
        if (binding.mainRecordFcv.visibility==View.VISIBLE && binding.mainRecordFcv.findNavController().currentDestination?.id == R.id.ogamSelectFragment)  //바텀 시트 화면에서 오감 선택 화면이면 바텀 시트 내리기
            hideBottomSheet()
        else    //이외 경우는 뒤로가기
            super.onBackPressed()
    }

    private fun hideBottomSheet() {
        recordFcvVisibility = View.INVISIBLE
        binding.invalidateAll()
        binding.mainRecordFcv.startAnimation(translateDown)   //아래 -> 위로 올라오는 애니메이션
    }

    private fun observe() {
        mainVm.toast.observe(this, Observer {
            val msg = it.getContentIfNotHandled()

            if (msg!=null)
                showToast(msg)
        })

        mainVm.getUserInfoResult.observe(this, Observer {
            if (!it) {
                SpfUtils.clear()
                finishAffinity()
            }
        })

        mainVm.getUserInfoResult.observe(this, Observer {
            if (!it) {
                SpfUtils.clear()
                finishAffinity()
            }
        })
    }

    fun showBottomSheet() {
        mainVm.setRandomSloganIdx()  //OgamSelectFragment 의 슬로건 idx 를 랜덤하게 뽑기 위해 라이브데이터 사용

        recordFcvVisibility = View.VISIBLE
        binding.invalidateAll()
        binding.mainRecordFcv.startAnimation(translateUp)   //아래 -> 위로 올라오는 애니메이션
    }

    fun setRecordFcvTopMargin(margin: Int) {
        recordFcvMarginTop = margin
        binding.invalidateAll()
    }

    fun changeMenu(menu: Int) {
        binding.mainBnv.selectedItemId = menu
    }

    //기록하기 BottomSheet 의 visibility 를 확인하는 함수
    fun checkRecordFcvVisibility(): Int = binding.mainRecordFcv.visibility
}