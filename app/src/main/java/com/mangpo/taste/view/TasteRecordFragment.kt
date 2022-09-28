package com.mangpo.taste.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mangpo.domain.model.createPost.CreatePostReqEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentTasteRecordBinding
import com.mangpo.taste.util.setSpannableText
import com.mangpo.taste.view.model.OgamSelect
import com.mangpo.taste.view.model.TwoBtnDialog
import com.mangpo.taste.viewmodel.MainViewModel
import com.mangpo.taste.viewmodel.TasteRecordViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class TasteRecordFragment : BaseFragment<FragmentTasteRecordBinding>(FragmentTasteRecordBinding::inflate), TextWatcher {
    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private val navArgs: TasteRecordFragmentArgs by navArgs()
    private val tasteRecordVm: TasteRecordViewModel by viewModels()
    private val mainVm: MainViewModel by activityViewModels()
    private val bundle: Bundle = Bundle()

    private var isComplete: Boolean = false

    var isKeyboardUp: Boolean = false
    var date: String = ""
    var ogamSelect: OgamSelect = OgamSelect()
    var createPostValidation: Boolean = true
    var isDialogShown: Boolean = false

    override fun initAfterBinding() {
        binding.apply {
            fragment = this@TasteRecordFragment
        }

        ogamSelect = navArgs.sense

        initDialog()

        //뒤로가기 콜백 리스너
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)    //뒤로가기 콜백 리스너 등록

        //date 텍스트뷰에 오늘 날짜 보여주기
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        date = LocalDateTime.now().format(formatter)
        binding.invalidateAll()

        //감각별로 다른 타이틀 텍스트뷰 보여주기
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
        observe()
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
                //다이얼로그 화면 뒤에 투명 뷰 없애기
                isDialogShown = false
                binding.invalidateAll()

                if (isComplete) {   //기록 완료 -> 계속 쓰기
                    clear() //입력했던 내용 모두 초기화
                }

                isComplete = false  //기록 완료가 끝났으니까 플래그를 False 로 설정
            }

            override fun rightAction() {
                //다이얼로그 화면 뒤에 투명 뷰 없애기
                isDialogShown = false
                binding.invalidateAll()

                findNavController().popBackStack()  //뒤로 가기 -> 다시 기록하기 Bottom Sheet 가 올라왔을 때 OgamSelectFragment 가 보일 수 있도록 뒤로 가놓기

                if (isComplete) {   //기록 완료 -> 보관함 가기
                    mainVm.setIsTasteRecordShown(false) //TasteRecordFragment 닫기
                }

                isComplete = false  //기록 완료가 끝났으니까 플래그를 False 로 설정
            }
        })
    }

    private fun setMyEventListener() {
        //content EditText 글자수 세기 위해 TextWatcher 등록
        binding.tasteRecordContentEt.addTextChangedListener(this)
    }

    private fun validate(): Boolean = !(binding.tasteRecordKeywordEt.text.isBlank() || binding.tasteRecordSrb.rating==0f)

    //작성했던 내용 모두 초기화하는 함수
    private fun clear() {
        binding.tasteRecordKeywordEt.text.clear()
        binding.tasteRecordSrb.rating = 0f
        binding.tasteRecordContentEt.text.clear()
    }

    private fun showDialog(bundle: Bundle) {
        twoBtnDialogFragment.arguments = bundle
        twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
    }

    private fun observe() {
        tasteRecordVm.toast.observe(viewLifecycleOwner, Observer {
            val msg = it.getContentIfNotHandled()

            if (msg!=null) {
                showToast(msg)
            }
        })

        tasteRecordVm.createPostResult.observe(viewLifecycleOwner, Observer {
            if (it) {   //기록 저장 성공
                mainVm.setIsRecordComplete(true)

                isComplete = true   //기록 완료일 때 뜰 다이얼로그를 보여주기 위한 플래그 설정
                isDialogShown = true    //다이얼로그가 뜰 때 보여지는 투명뷰를 VISIBLE 로 변경하기 위한 플래그 설정
                binding.invalidateAll()

                //기록 완료 TwoBtnDialog 띄우기
                bundle.putParcelable("data", TwoBtnDialog(getString(R.string.title_record_complete), getString(R.string.msg_taste_input_complete), getString(R.string.action_keep_writing), getString(R.string.action_go_locker), null))
                showDialog(bundle)
            } else {    //기록 저장 실패
                mainVm.setIsRecordComplete(false)
                showToast("기록 저장 중 문제가 발생했습니다.")
            }
        })
    }

    //뒤로가기 다이얼로그 띄우기
    fun showBackDialog() {
        isDialogShown = true
        binding.invalidateAll()

        //다시 선택하시겠습니까? TwoBtnDialog 띄우기
        bundle.putParcelable("data", TwoBtnDialog(getString(R.string.msg_select_again), getString(R.string.msg_not_save), getString(R.string.action_keep_writing), getString(R.string.action_go_back), null))
        showDialog(bundle)
    }

    fun createPost() {
        createPostValidation = validate()

        if (createPostValidation) { //유효성 검사 성공 -> 요청 데이터 만들기
            var category: String = ""
            when (ogamSelect.sense1) {
                // touch, sight, smell, taste, hearing, ambiguous
                getString(R.string.title_sight) -> category = "SIGHT"
                getString(R.string.title_ear) -> category = "HEARING"
                getString(R.string.title_smell) -> category = "SMELL"
                getString(R.string.title_taste) -> category = "TASTE"
                getString(R.string.title_touch) -> category = "TOUCH"
                getString(R.string.title_sense) -> category = "AMBIGUOUS"
            }

            var content: String? = null
            if (binding.tasteRecordContentEt.text.isNotBlank()) {
                content = binding.tasteRecordContentEt.text.replace("\\n".toRegex(), " ").trim()
            }

            val createPostReqEntity: CreatePostReqEntity = CreatePostReqEntity(category, content, binding.tasteRecordKeywordEt.text.toString(), binding.tasteRecordSrb.rating.toInt())
            tasteRecordVm.createPost(createPostReqEntity)
        }

        binding.invalidateAll()
    }
}