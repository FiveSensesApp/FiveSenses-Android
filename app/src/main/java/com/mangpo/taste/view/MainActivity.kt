package com.mangpo.taste.view

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityMainBinding
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var mainNavHostFragment: NavHostFragment
    private lateinit var translateUp: Animation
    private lateinit var translateDown: Animation

    override fun initAfterBinding() {
        //애니메이션 초기화
        translateUp = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_up)
        translateDown = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_down)

        setMyEventListener()

        mainNavHostFragment = supportFragmentManager.findFragmentById(binding.mainFcv.id) as NavHostFragment
        val navController = mainNavHostFragment.findNavController()
        binding.mainBnv.setupWithNavController(navController)

        binding.mainBnv.background = null
    }

    override fun onBackPressed() {
        if (binding.mainRecordFcv.visibility==View.VISIBLE && binding.mainRecordFcv.findNavController().currentDestination?.id == R.id.ogamSelectFragment)  //바텀 시트 화면에서 오감 선택 화면이면 바텀 시트 내리기
            hideBottomSheet()
        else    //이외 경우는 뒤로가기
            super.onBackPressed()
    }

    private fun setMyEventListener() {
        //FloatingButton 클릭 리스너 -> 바텀 시트 화면이 닫혀 있을 때 -> 바텀 시트 화면 올리기(오감 시트 화면)
        binding.mainFab.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        mainViewModel.setRandomSloganIdx()  //OgamSelectFragment 의 슬로건 idx 를 랜덤하게 뽑기 위해 라이브데이터 사용

        binding.mainRecordFcv.visibility = View.VISIBLE //recordFcv VISIBLE
        binding.mainRecordFcv.startAnimation(translateUp)   //아래 -> 위로 올라오는 애니메이션
        binding.mainTransparentView.visibility = View.VISIBLE   //투명배경 VISIBLE
    }

    private fun hideBottomSheet() {
        binding.mainRecordFcv.visibility = View.INVISIBLE   //recordFcv INVISIBLE
        binding.mainRecordFcv.startAnimation(translateDown) //위 -> 아래로 내려가는 애니메이션
        binding.mainTransparentView.visibility = View.INVISIBLE   //투명배경 INVISIBLE
    }

    fun setRecordFcvTopMargin(margin: Int) {
        val params = binding.mainRecordFcv.layoutParams as ConstraintLayout.LayoutParams
        params.setMargins(0, margin, 0, 0)
        binding.mainRecordFcv.layoutParams = params
    }

    fun changeMenu(menu: Int) {
        binding.mainBnv.selectedItemId = menu
    }

    //기록하기 BottomSheet 의 visibility 를 확인하는 함수
    fun checkRecordFcvVisibility(): Int = binding.mainRecordFcv.visibility
}