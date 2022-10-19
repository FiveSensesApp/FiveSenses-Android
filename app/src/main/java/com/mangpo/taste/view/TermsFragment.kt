package com.mangpo.taste.view

import androidx.navigation.fragment.findNavController
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentTermsBinding

class TermsFragment : BaseFragment<FragmentTermsBinding>(FragmentTermsBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()
    }

    private fun setMyEventListener() {
        //뒤로가기 이미지뷰 클릭 리스너
        binding.termsBackIv.setOnClickListener {
            findNavController().popBackStack()
        }

        //서비스 이용약관 텍스트뷰 클릭 리스너
        binding.termsTermsOfServiceTv.setOnClickListener {
            val action = TermsFragmentDirections.actionTermsFragmentToTermsBottomSheetFragment(0)
            findNavController().navigate(action)
        }

        //개인정보 처리 방침 텍스트뷰 클릭 리스너
        binding.termsPrivacyPolicyTv.setOnClickListener {
            val action = TermsFragmentDirections.actionTermsFragmentToTermsBottomSheetFragment(1)
            findNavController().navigate(action)
        }

        //마케팅 정보 수신동의 텍스트뷰 클릭 리스너
        binding.titleConsentingMarketingInformation.setOnClickListener {
            val action = TermsFragmentDirections.actionTermsFragmentToTermsBottomSheetFragment(2)
            findNavController().navigate(action)
        }
    }
}