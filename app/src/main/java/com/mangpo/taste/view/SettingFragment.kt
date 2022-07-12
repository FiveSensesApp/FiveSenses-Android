package com.mangpo.taste.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentSettingBinding

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()

        binding.settingAlarmTimeTv.isEnabled = binding.settingAlarmSettingSb.isChecked
    }

    private fun setMyEventListener() {
        //뒤로가기 이미지뷰 클릭 리스너
        binding.settingBackIv.setOnClickListener {
            findNavController().popBackStack()
        }

        //알람 설정 스위치버튼 체크리스너
        binding.settingAlarmSettingSb.setOnCheckedChangeListener { compoundButton, b ->
            binding.settingAlarmTimeTv.isEnabled = b
        }

        //알람 시간 텍스트뷰 클릭 리스너
        binding.settingAlarmTimeTv.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToAlarmTimeDialogFragment(binding.settingAlarmTimeTv.text.toString())
            findNavController().navigate(action)
        }

        //비밀번호 재설정 버튼 클릭리스너
        binding.settingPwResettingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_pwResettingFragment)
        }

        //이용약관 텍스트뷰 클릭 리스너
        binding.settingTermsTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_termsFragment)
        }

        //오픈소스 라이선스 텍스트뷰 클릭 리스너
        binding.settingOpensourceTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_openSourceFragment)
        }

        //로그아웃 텍스트뷰 클릭리스너
        binding.settingLogoutTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_logoutDialogFragment)
        }

        //회원탈퇴 텍스트뷰 클릭리스너
        binding.settingWithdrawalTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_withdrawalDialogFragment)
        }
    }
}