package com.mangpo.taste.view

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentSettingBinding
import com.mangpo.taste.util.SpfUtils.clearEncryptedSpf
import com.mangpo.taste.view.model.TwoBtnDialog

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    private var dialogType: Int = -1

    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var alarmTimeDialogFragment: AlarmTimeDialogFragment

    override fun initAfterBinding() {
        initTwoBtnDialog()
        setAlarmTimeDialogFragment()
        setMyEventListener()

        binding.settingAlarmTimeTv.isEnabled = binding.settingAlarmSettingSb.isChecked
    }

    private fun initTwoBtnDialog() {
        twoBtnDialogFragment = TwoBtnDialogFragment()
        twoBtnDialogFragment.setMyCallback(object : TwoBtnDialogFragment.MyCallback {
            override fun leftAction() { //아니요
                if (dialogType==0) {    //로그아웃

                } else {    //회원탈퇴

                }

                dialogType = -1 //초기화
            }

            override fun rightAction() {    //예
                clearEncryptedSpf()    //jwt 초기화
                dialogType = -1 //초기화
                findNavController().navigate(R.id.action_settingFragment_to_loginActivity)  //LoginActivity 로 이동
            }
        })
    }

    private fun setAlarmTimeDialogFragment() {
        alarmTimeDialogFragment = AlarmTimeDialogFragment()

        alarmTimeDialogFragment.setCallback(object : AlarmTimeDialogFragment.Callback {
            override fun cancel() {
            }

            override fun complete(time: String) {
                binding.settingAlarmTimeTv.text = time
            }
        })
    }

    private fun setMyEventListener() {
        //뒤로가기 이미지뷰 클릭 리스너
        binding.settingBackIv.setOnClickListener {
            requireActivity().onBackPressed()
        }

        //알람 설정 스위치버튼 체크리스너
        binding.settingAlarmSettingSb.setOnCheckedChangeListener { compoundButton, b ->
            binding.settingAlarmTimeTv.isEnabled = b
        }

        //알람 시간 텍스트뷰 클릭 리스너
        binding.settingAlarmTimeTv.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putString("time", binding.settingAlarmTimeTv.text.toString())

            alarmTimeDialogFragment.arguments = bundle
            alarmTimeDialogFragment.show(requireActivity().supportFragmentManager, null)
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
            dialogType = 0

            val bundle: Bundle = Bundle()
            bundle.putParcelable("data", TwoBtnDialog(getString(R.string.action_logout), getString(R.string.msg_logout), getString(R.string.action_no), getString(R.string.action_yes), R.drawable.bg_gy01_12))

            twoBtnDialogFragment.arguments = bundle
            twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
        }

        //회원탈퇴 텍스트뷰 클릭리스너
        binding.settingWithdrawalTv.setOnClickListener {
            dialogType = 1

            val bundle: Bundle = Bundle()
            bundle.putParcelable("data", TwoBtnDialog(getString(R.string.msg_really_withdrawal), getString(R.string.msg_all_records_lost), getString(R.string.action_no), getString(R.string.action_yes), R.drawable.bg_gy01_12))

            twoBtnDialogFragment.arguments = bundle
            twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
        }
    }
}