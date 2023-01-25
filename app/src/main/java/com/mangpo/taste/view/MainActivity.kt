package com.mangpo.taste.view

import android.Manifest
import android.os.Build
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityMainBinding
import com.mangpo.taste.util.SpfUtils.clear
import com.mangpo.taste.util.SpfUtils.getIntEncryptedSpf
import com.mangpo.taste.util.SpfUtils.writeSpf
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {
    override val viewModel: MainViewModel by viewModels()

    private lateinit var mainNavHostFragment: NavHostFragment
    private lateinit var translateUp: Animation
    private lateinit var translateDown: Animation

    var recordFcvVisibility: Int = View.INVISIBLE
    var recordFcvMarginTop: Int = 136

    override fun initAfterBinding() {
        binding.apply {
            activity = this@MainActivity
            vm = viewModel
            lifecycleOwner = this@MainActivity
        }

        //Android 13 이상부턴 알람 권한 체크 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAlarmPermission()
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

        viewModel.getUserInfo(getIntEncryptedSpf("userId"))    //사용자 정보 조회
    }

    override fun onBackPressed() {
        if (binding.mainRecordFcv.visibility==View.VISIBLE) {   //기록하기 바텀 시트가 올라와 있을 때
            if (binding.mainRecordFcv.findNavController().currentDestination?.id == R.id.ogamSelectFragment) {  //바텀 시트 화면에서 오감 선택 화면이면 바텀 시트 내리기
                viewModel.setIsTasteRecordShown(false)
            } else {    //바텀 시트 화면에서 오감 선택 화면이 아니면 뒤로가기
                super.onBackPressed()
            }
        } else {    //기록하기 바텀 시트가 올라와 있지 않을 때 앱 종료
            super.onBackPressed()
        }
    }

    private fun checkAlarmPermission() {
        com.mangpo.taste.util.checkPermission(
            lifecycleScope,
            Manifest.permission.POST_NOTIFICATIONS,
            "푸시 알림 권한을 거부하셨어요. 나중에도 [설정-알림설정]에서 알림을 변경할 수 있어요!"
        ) {}
    }

    private fun hideBottomSheet() {
        recordFcvVisibility = View.INVISIBLE
        binding.invalidateAll()
        binding.mainRecordFcv.startAnimation(translateDown)   //아래 -> 위로 올라오는 애니메이션
    }

    private fun showBottomSheet() {
        viewModel.setRandomSloganIdx()  //OgamSelectFragment 의 슬로건 idx 를 랜덤하게 뽑기 위해 라이브데이터 사용

        recordFcvVisibility = View.VISIBLE
        binding.invalidateAll()
        binding.mainRecordFcv.startAnimation(translateUp)   //아래 -> 위로 올라오는 애니메이션
    }

    private fun observe() {
        viewModel.getUserInfoResult.observe(this, Observer {
            //유저 정보 조회 실패 -> 401, 500 ~~~ (대부분 401일걸?)
            if (!it) {
                clear()    //Spf 초기화
                writeSpf("onBoarding", true)   //온보딩 화면은 봤었으니까 다시 설정해주기
                startActivityWithClear(LoginActivity::class.java)   //다시 로그인하라고 로그인 액티비티로 이동
            }
        })

        viewModel.isTasteRecordShown.observe(this, Observer {
            val isTasteRecordShown = it.getContentIfNotHandled()

            if (isTasteRecordShown!=null) {
                if (isTasteRecordShown) {   //기록 화면을 올리고 싶다
                    showBottomSheet()   //기록 화면을 올린다
                    viewModel.setIsRecordComplete(false)   //기록하러 가는 중이니까 기록 완료 플래그를 false 로 변경
                } else {    //기록 화면을 내리고 싶다
                    hideBottomSheet()   //기록 화면을 내린다
                    if (binding.mainBnv.selectedItemId==R.id.feedFragment) {
                        viewModel.setCallGetPostsFlag(viewModel.getIsRecordComplete())    //TimelineFragment, ByScoreFragment, BySenseFragment, ByCalendarFragment 에게 getPosts API 를 호출할지 말지 선택할 수 있는 플래그 변수 postValue
                    } else {
                        viewModel.setCallGetStatFlag(viewModel.getIsRecordComplete()) //AnalysisFragment 에게 getStat API 를 호출할지 결정할 수 있도록 하는 Flag 변수
                    }
                }
            }
        })
    }

    fun setRecordFcvTopMargin(margin: Int) {
        recordFcvMarginTop = margin
        binding.invalidateAll()
    }

    fun changeNavigationMenu(menuId: Int) {
        binding.mainBnv.selectedItemId = menuId
    }
}