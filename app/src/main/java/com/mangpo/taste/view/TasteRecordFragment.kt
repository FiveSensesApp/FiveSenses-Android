package com.mangpo.taste.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentTasteRecordBinding
import com.mangpo.taste.util.setSpannableText
import com.mangpo.taste.view.model.OgamSelect
import com.mangpo.taste.view.model.TwoBtnDialog
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TasteRecordFragment : BaseFragment<FragmentTasteRecordBinding>(FragmentTasteRecordBinding::inflate), TextWatcher {
    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private val navArgs: TasteRecordFragmentArgs by navArgs()

    private var isComplete: Boolean = false

    var isKeyboardUp: Boolean = false
    var date: String = ""
    var ogamSelect: OgamSelect = OgamSelect()

    override fun initAfterBinding() {
        binding.apply {
            fragment = this@TasteRecordFragment
        }

        ogamSelect = navArgs.sense

        //뒤로가기 콜백 리스너
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.tasteRecordBlurredView.visibility = View.VISIBLE    //투명뷰 VISIBLE

                //다시 선택하시겠습니까? TwoBtnDialog 띄우기
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", TwoBtnDialog(getString(R.string.msg_select_again), getString(R.string.msg_not_save), getString(R.string.action_keep_writing), getString(R.string.action_go_back), null))

                twoBtnDialogFragment.arguments = bundle
                twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)    //뒤로가기 콜백 리스너 등록

        initDialog()

        //date 텍스트뷰에 오늘 날짜 보여주기
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        date = LocalDateTime.now().format(formatter)
        binding.invalidateAll()

        setSpannableText("${ogamSelect.sense1}${ogamSelect.sense2}", requireContext(), ogamSelect.senseTextColor, 0, ogamSelect.sense1.length, binding.tasteRecordTitleTv)

        //키보드 감지해서 뷰 바꾸기
        setEventListener(requireActivity(), viewLifecycleOwner, KeyboardVisibilityEventListener {
            if (it) {   //키보드 올라와 있을 때
                (requireActivity() as MainActivity).setRecordFcvTopMargin(43)  //높이 더 크게
            } else {    //키보드 내려와 있을 때
                (requireActivity() as MainActivity).setRecordFcvTopMargin(136) //높이 낮게
            }

            isKeyboardUp = it
            binding.invalidateAll()
        })

        setMyEventListener()
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.tasteRecordContentCntTv.text = "${p0?.length} / 100"
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun initDialog() {
        twoBtnDialogFragment = TwoBtnDialogFragment()
        twoBtnDialogFragment.setMyCallback(object : TwoBtnDialogFragment.MyCallback {
            override fun leftAction() {
                if (isComplete) {   //기록 완료 -> 계속 쓰기
                    binding.tasteRecordBlurredView.visibility = View.INVISIBLE  //투명뷰 INVISIBLE
                    clear() //입력했던 내용 모두 초기화
                } else {    //다시 선택하시겠습니까? -> 계속 쓰기
                    binding.tasteRecordBlurredView.visibility = View.INVISIBLE    //투명뷰 INVISIBLE
                }

                isComplete = false
            }

            override fun rightAction() {
                if (isComplete) {   //기록 완료 -> 보관함 가기
                    findNavController().popBackStack()  //뒤로 가기 -> OgamSelectFragment
                    requireActivity().onBackPressed()   //뒤로 가기 -> MainActivity
                    (requireActivity() as MainActivity).changeMenu(R.id.feedFragment)   //보관함 화면으로 이동 -> FeedFragment
                } else {    //다시 선택하시겠습니까? -> 뒤로 가기
                    findNavController().popBackStack()  //뒤로 가기
                }

                isComplete = false
            }
        })
    }

    private fun setMyEventListener() {
        //content EditText 글자수 세기 위해 TextWatcher 등록
        binding.tasteRecordContentEt.addTextChangedListener(this)

        //뒤로가기 아이콘 이미지뷰 클릭 리스너
        binding.tasteRecordBackIv.setOnClickListener {
            binding.tasteRecordBlurredView.visibility = View.VISIBLE    //투명뷰 VISIBLE

            //다시 선택하시겠습니까? TwoBtnDialog 띄우기
            val bundle: Bundle = Bundle()
            bundle.putParcelable("data", TwoBtnDialog(getString(R.string.msg_select_again), getString(R.string.msg_not_save), getString(R.string.action_keep_writing), getString(R.string.action_go_back), null))

            twoBtnDialogFragment.arguments = bundle
            twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
        }

        //FloatingActionButton 클릭 리스너
        binding.tasteRecordFab.setOnClickListener {
            if (validate()) {   //유효성 검사 통과
                binding.tasteRecordEssentialErrTv.visibility = View.INVISIBLE   //에러 메시지 텍스트뷰 INVISIBLE
                binding.tasteRecordBlurredView.visibility = View.VISIBLE    //투명뷰 VISIBLE

                isComplete = true

                //기록 완료 TwoBtnDialog 띄우기
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", TwoBtnDialog(getString(R.string.title_record_complete), getString(R.string.msg_taste_input_complete), getString(R.string.action_keep_writing), getString(R.string.action_go_locker), null))

                twoBtnDialogFragment.arguments = bundle
                twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
            } else {    //유효성 검사 실패
                binding.tasteRecordEssentialErrTv.visibility = View.VISIBLE //에러 메시지 텍스트뷰 VISIBLE
                binding.tasteRecordBlurredView.visibility = View.INVISIBLE  //투명뷰 INVISIBLE
            }
        }
    }

    private fun validate(): Boolean = !(binding.tasteRecordKeywordEt.text.isBlank() || binding.tasteRecordSrb.rating == 0f)

    //작성했던 내용 모두 초기화하는 함수
    private fun clear() {
        binding.tasteRecordKeywordEt.text.clear()
        binding.tasteRecordSrb.rating = 0.0f
        binding.tasteRecordContentEt.text.clear()
    }
}