package com.mangpo.taste.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentPwResettingBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.matchRegex
import com.mangpo.taste.view.model.OneBtnDialog
import com.mangpo.taste.viewmodel.PwResettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PwResettingFragment : BaseFragment<FragmentPwResettingBinding>(FragmentPwResettingBinding::inflate), TextWatcher {
    private val pwResettingVm: PwResettingViewModel by viewModels()

    private lateinit var oneBtnDialogFragment: OneBtnDialogFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragment = this@PwResettingFragment
            vm = this@PwResettingFragment.pwResettingVm
            lifecycleOwner = viewLifecycleOwner
            pwConditionsVisibility = View.VISIBLE
            pwNotMatchVisibility = View.INVISIBLE
            completeBtnEnableStatus = false
        }

        observe()
    }

    override fun initAfterBinding() {
        setMyEventListener()
        setOneBtnDialogFragment()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when {
            binding.pwResettingOldPwEt.isFocused -> {

            }
            binding.pwResettingNewPwEt.isFocused -> checkNewPw()
            else -> checkNewPw()
        }

        validate()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setMyEventListener() {
        //TextWatcher 등록
        binding.pwResettingOldPwEt.addTextChangedListener(this)
        binding.pwResettingNewPwEt.addTextChangedListener(this)
        binding.pwResettingReenterNewPwEt.addTextChangedListener(this)
    }

    private fun setOneBtnDialogFragment() {
        oneBtnDialogFragment = OneBtnDialogFragment()
        oneBtnDialogFragment.setMyCallback(object : OneBtnDialogFragment.MyCallback {
            override fun end() {
                SpfUtils.clear()    //Spf 에 있는 모든 내용 초기화
                SpfUtils.writeSpf("onBoarding", true)   //온보딩 화면은 봤었으니까 다시 설정해주기
                (requireActivity() as SettingActivity).startActivityWithClear(LoginActivity::class.java)
            }
        })
    }

    //비밀번호 유효성 검사(영문, 숫자, 특수문자 중복 2개 이상 && 10~20자)
    private fun isRegularPW(password: String): Boolean {
        return matchRegex(password, "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{10,30}\$".toRegex())
    }

    //새로운 비밀번호랑 새로운 비밀번호 재입력이 같은지 체크해서 뷰 업데이트
    private fun checkNewPw() {
        if (isRegularPW(binding.pwResettingNewPwEt.text.toString()) && isRegularPW(binding.pwResettingReenterNewPwEt.text.toString())) {   //새 비밀번호나 새 비밀번호 재입력의 정규식 통과
            if (binding.pwResettingNewPwEt.text.toString() != binding.pwResettingReenterNewPwEt.text.toString()) {   //새 비밀번호 != 새 비밀번호 재입력
                binding.apply {
                    pwNotMatchVisibility = View.VISIBLE
                    pwConditionsVisibility = View.INVISIBLE
                }
            } else {    //새 비밀번호 == 새 비밀번호 재입력
                binding.apply {
                    pwNotMatchVisibility = View.INVISIBLE
                    pwConditionsVisibility = View.VISIBLE
                }
            }
        } else {    //새 비밀번호, 새 비밀번호 재입력 모든 유효성 검사 통과
            binding.apply {
                pwNotMatchVisibility = View.INVISIBLE
                pwConditionsVisibility = View.VISIBLE
            }
        }
    }

    /*
    완료 버튼 활성화를 위한 유효성 검사
    1. 기존 비밀번호 입력 됐으면
    2. 새로운 비밀번호 입력 됐으면
    3. 새로운 비밀번호의 유효성 검사가 통과 됐으면(영문, 숫자, 특수문자 중 2개 이상 && 10자~20자)
    4. 새로운 비밀번호 재입력이 입력 됐으면
    5. 새로운 비밀번호 텍스트와 새로운 비밀번호 재입력 텍스트가 같으면
    완료 버튼 활성화
    */
    private fun validate() {
        binding.completeBtnEnableStatus = binding.pwResettingOldPwEt.text.isNotBlank() &&
                binding.pwResettingNewPwEt.text.isNotBlank() &&
                isRegularPW(binding.pwResettingNewPwEt.text.toString()) &&
                binding.pwResettingReenterNewPwEt.text.isNotBlank() &&
                binding.pwResettingNewPwEt.text.toString() == binding.pwResettingReenterNewPwEt.text.toString()
    }

    private fun observe() {
        pwResettingVm.toast.observe(viewLifecycleOwner, Observer {
            val toast = it.getContentIfNotHandled()

            if (toast!=null) {
                showToast(toast)
            }
        })

        pwResettingVm.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                (requireActivity() as SettingActivity).showLoading()
            } else {
                (requireActivity() as SettingActivity).hideLoading()
            }
        })

        pwResettingVm.changePasswordResult.observe(viewLifecycleOwner, Observer {
            if (it==200) {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", OneBtnDialog(getString(R.string.title_pw_resetting_complete), getString(R.string.msg_go_to_login), getString(R.string.action_see_you_again), listOf(26, 10, 26, 12)))

                oneBtnDialogFragment.arguments = bundle
                oneBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
            }
        })
    }

    fun changePassword(oldPw: String, newPw: String) {
        (requireActivity() as SettingActivity).hideKeyboard(requireView())
        pwResettingVm.changePassword(oldPw, newPw)
    }

    fun back() {
        findNavController().popBackStack()
    }
}