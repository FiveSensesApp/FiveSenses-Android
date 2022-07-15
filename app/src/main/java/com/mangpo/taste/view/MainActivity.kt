package com.mangpo.taste.view

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
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
        binding.mainRecordFcv.visibility = View.VISIBLE //recordFcv VISIBLE
        binding.mainRecordFcv.startAnimation(translateUp)   //아래 -> 위로 올라오는 애니메이션
        binding.mainTransparentView.visibility = View.VISIBLE   //투명배경 VISIBLE
        hideStatusBar() //상태바 숨기기
    }

    private fun hideBottomSheet() {
        binding.mainRecordFcv.visibility = View.INVISIBLE   //recordFcv INVISIBLE
        binding.mainRecordFcv.startAnimation(translateDown) //위 -> 아래로 내려가는 애니메이션
        binding.mainTransparentView.visibility = View.INVISIBLE   //투명배경 INVISIBLE
        transparentStatusBar()  //상태바 투명하게
    }

    fun changeFABIcon(icon: Int) {
        binding.mainFab.setImageDrawable(ContextCompat.getDrawable(applicationContext, icon)) //FAB 아이콘 변경
    }

    fun setRecordFcvTopMargin(margin: Int) {
        val params = binding.mainRecordFcv.layoutParams as ConstraintLayout.LayoutParams
        params.setMargins(0, margin, 0, 0)
        binding.mainRecordFcv.layoutParams = params
    }

    fun changeMenu(menu: Int) {
        binding.mainBnv.selectedItemId = menu
    }
}