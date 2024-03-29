package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentSettingBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.SpfUtils.clear
import com.mangpo.taste.util.SpfUtils.writeSpf
import com.mangpo.taste.view.model.TwoBtnDialog
import com.mangpo.taste.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>(FragmentSettingBinding::inflate) {
    override val viewModel: SettingViewModel by viewModels()

    private var dialogType: Int = -1

    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var alarmTimeDialogFragment: AlarmTimeDialogFragment

    override fun initAfterBinding() {
        initTwoBtnDialog()
        setAlarmTimeDialogFragment()
        setMyEventListener()
        observe()
    }

    private fun initTwoBtnDialog() {
        twoBtnDialogFragment = TwoBtnDialogFragment()
        twoBtnDialogFragment.setMyCallback(object : TwoBtnDialogFragment.MyCallback {
            override fun leftAction(action: String) { //아니요
                if (dialogType==0) {    //로그아웃

                } else {    //회원탈퇴

                }

                dialogType = -1 //초기화
            }

            override fun rightAction(action: String) {    //예
                if (dialogType==0) {    //로그아웃
                    goodBye()
                } else {    //회원탈퇴
                    viewModel.deleteUser(SpfUtils.getIntEncryptedSpf("userId"))
                }
            }
        })
    }

    private fun setAlarmTimeDialogFragment() {
        alarmTimeDialogFragment = AlarmTimeDialogFragment()

        alarmTimeDialogFragment.setCallback(object : AlarmTimeDialogFragment.Callback {
            override fun cancel() {
            }

            override fun complete(time: String) {
            }
        })
    }

    private fun setMyEventListener() {
        //뒤로가기 이미지뷰 클릭 리스너
        binding.settingBackIv.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.settingAlarmBtn.setOnClickListener {
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("android.provider.extra.APP_PACKAGE", requireActivity().packageName)
            startActivity(intent)
        }

        //비밀번호 재설정 버튼 클릭리스너
        binding.settingPwResettingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_pwResettingFragment)
        }

        //공지사항 텍스트뷰 클릭 리스너
        binding.settingNoticeTv.setOnClickListener {
            goUrlPage("https://5gaam.notion.site/a9d6cc445d4e4adab7bc50ab79969c7a")
        }

        //이용약관 텍스트뷰 클릭 리스너
        binding.settingTermsTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_termsFragment)
        }

        //오픈소스 라이선스 텍스트뷰 클릭 리스너
        binding.settingOpensourceTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_openSourceFragment)
        }

        //앱 리뷰 남기기 텍스트뷰 클릭 리스너
        binding.settingReviewTv.setOnClickListener {
            goUrlPage("https://play.google.com/store/apps/details?id=com.mangpo.taste")
            viewModel.checkThanks()
        }

        //공식 SNS 이동 텍스트뷰 클릭 리스너
        binding.settingSnsTv.setOnClickListener {
            goUrlPage("https://www.instagram.com/5gaam_app")
        }

        binding.settingInquiryRequestTv.setOnClickListener {
            val emailIntent: Intent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "message/rfc822"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.app_email)))
            startActivity(emailIntent)
        }

        //후원자 목록 텍스트뷰 클릭 리스너
        binding.settingSupportView.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_supportActivity)
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

    private fun goodBye() {
        clear()    //Spf 에 있는 모든 내용 초기화
        writeSpf("onBoarding", true)   //온보딩 화면은 봤었으니까 다시 설정해주기

        //OnBoardingActivity 의 맨 마지막 화면(로그인/회원가입)으로 이동
        val intent: Intent = Intent(requireActivity(), OnBoardingActivity::class.java)
        intent.putExtra("currentItem", 3)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        //초기화
        dialogType = -1
    }

    private fun observe() {
        viewModel.deleteUserResultCode.observe(viewLifecycleOwner, Observer {
            if (it==200) {
                goodBye()
            } else {

            }
        })
    }
}