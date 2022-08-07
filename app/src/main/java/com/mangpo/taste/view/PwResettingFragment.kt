package com.mangpo.taste.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentPwResettingBinding
import com.mangpo.taste.view.model.OneBtnDialog
import java.util.regex.Pattern

class PwResettingFragment : BaseFragment<FragmentPwResettingBinding>(FragmentPwResettingBinding::inflate), TextWatcher {
    private lateinit var oneBtnDialogFragment: OneBtnDialogFragment

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

        //완료 텍스트뷰 클릭 리스너
        binding.pwResettingCompleteBtn.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putParcelable("data", OneBtnDialog(getString(R.string.title_pw_resetting_complete), getString(R.string.msg_go_to_login), getString(R.string.action_see_you_again), listOf(26, 10, 26, 12)))

            oneBtnDialogFragment.arguments = bundle
            oneBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
        }

        //뒤로가기 이미지뷰 클릭 리스너
        binding.pwResettingBackIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setOneBtnDialogFragment() {
        oneBtnDialogFragment = OneBtnDialogFragment()
        oneBtnDialogFragment.setMyCallback(object : OneBtnDialogFragment.MyCallback {
            override fun end() {
                (requireActivity() as SettingActivity).startActivityWithClear(LoginActivity::class.java)
            }
        })
    }

    //비밀번호 유효성 검사(영문, 숫자, 특수문자 중복 2개 이상 && 10~20자)
    private fun isRegularPW(password: String): Boolean {
        val pwPattern1 = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{10,20}$" // 영문, 숫자
        val pwPattern2 = "^(?=.*[0-9])(?=.*[$@$!%*#?&.])[[0-9]$@$!%*#?&.]{10,20}$" // 숫자, 특수문자
        val pwPattern3 = "^(?=.*[A-Za-z])(?=.*[$@$!%*#?&.])[A-Za-z$@$!%*#?&.]{10,20}$" // 영문, 특수문자
        val pwPattern4 = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{10,20}$" // 영문, 숫자, 특수문자

        return (Pattern.matches(pwPattern1, password) ||
                Pattern.matches(pwPattern2, password) ||
                Pattern.matches(pwPattern3, password) ||
                Pattern.matches(pwPattern4, password))
    }

    //새로운 비밀번호랑 새로운 비밀번호 재입력이 같은지 체크해서 뷰 업데이트
    private fun checkNewPw() {
        if (!isRegularPW(binding.pwResettingNewPwEt.text.toString())) { //영문, 숫자, 특수문자 중 2개, 10자 이상
            binding.pwResettingPwNotMatchTv.visibility = View.INVISIBLE
            binding.pwResettingPwConditionsTv.visibility = View.VISIBLE
        } else if (binding.pwResettingNewPwEt.text.toString() == binding.pwResettingReenterNewPwEt.text.toString()) {   //새 비밀번호 == 새 비밀번호 재입력
            binding.pwResettingPwNotMatchTv.visibility = View.INVISIBLE
            binding.pwResettingPwConditionsTv.visibility = View.VISIBLE
        } else {    //그 이외
            binding.pwResettingPwNotMatchTv.visibility = View.VISIBLE
            binding.pwResettingPwConditionsTv.visibility = View.INVISIBLE
        }
    }

    /*
    완료 버튼 활성화를 위한 유효성 검사
    1. 기존 비밀번호 입력 됐으면
    2. "기존 비밀번호를 확인해주세요." 텍스트뷰가 보이지 않으면
    3. 새로운 비밀번호 입력 됐으면
    4. 새로운 비밀번호의 유효성 검사가 통과 됐으면(영문, 숫자, 특수문자 중 2개 이상 && 10자~20자)
    5. 새로운 비밀번호 재입력이 입력 됐으면
    6. 새로운 비밀번호 텍스트와 새로운 비밀번호 재입력 텍스트가 같으면
    완료 버튼 활성화
    */
    private fun validate() {
        binding.pwResettingCompleteBtn.isEnabled = binding.pwResettingOldPwEt.text.isNotBlank() &&
                binding.pwResettingCheckOldPwTv.visibility == View.INVISIBLE &&
                binding.pwResettingNewPwEt.text.isNotBlank() &&
                isRegularPW(binding.pwResettingNewPwEt.text.toString()) &&
                binding.pwResettingReenterNewPwEt.text.isNotBlank() &&
                binding.pwResettingNewPwEt.text.toString() == binding.pwResettingReenterNewPwEt.text.toString()
    }
}