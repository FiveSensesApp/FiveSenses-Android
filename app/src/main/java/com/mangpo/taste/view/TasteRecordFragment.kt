package com.mangpo.taste.view

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentTasteRecordBinding
import com.mangpo.taste.util.convertDpToPx
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TasteRecordFragment : BaseFragment<FragmentTasteRecordBinding>(FragmentTasteRecordBinding::inflate), TextWatcher {
    private lateinit var selectAgainDialogFragment: SelectAgainDialogFragment
    private lateinit var recordCompleteDialogFragment: RecordCompleteDialogFragment
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun initAfterBinding() {
        initDialog()
        setMyEventListener()

        //뒤로가기 콜백 리스너
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.tasteRecordBlurredView.visibility = View.VISIBLE    //투명뷰 VISIBLE
                selectAgainDialogFragment.show(requireActivity().supportFragmentManager, null)  //다시 선택하시겠습니까? 다이얼로그 띄우기
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        //date 텍스트뷰에 오늘 날짜 보여주기
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val formatted = current.format(formatter)
        binding.tasteRecordDateTv.text = formatted

        //키보드 감지해서 뷰 바꾸기
        setEventListener(requireActivity(), viewLifecycleOwner, KeyboardVisibilityEventListener {
            if (it) {   //키보드 올라와 있을 때
                (requireActivity() as MainActivity).setRecordFcvTopMargin(convertDpToPx(requireContext(), 43))  //높이 더 크게
                setRecordClMargin(convertDpToPx(requireContext(), 80))  //기록하기 topMargin 변경
                binding.tasteRecordTitleTv.visibility = View.GONE   //타이틀 GONE
            } else {    //키보드 내려와 있을 때
                (requireActivity() as MainActivity).setRecordFcvTopMargin(convertDpToPx(requireContext(), 123)) //높이 낮게
                setRecordClMargin(convertDpToPx(requireContext(), 172)) //기록하기 topMargin 변경
                binding.tasteRecordTitleTv.visibility = View.VISIBLE    //타이틀 VISIBLE
            }
       })
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
        selectAgainDialogFragment = SelectAgainDialogFragment()
        selectAgainDialogFragment.setMyClickCallback(object : SelectAgainDialogFragment.MyClickCallback {
            override fun keep() {   //계속 쓰기
                binding.tasteRecordBlurredView.visibility = View.INVISIBLE    //투명뷰 INVISIBLE
            }

            override fun back() {   //뒤로 가기
                findNavController().popBackStack()
            }
        })

        //기록 완료 다이얼로그
        recordCompleteDialogFragment = RecordCompleteDialogFragment()
        recordCompleteDialogFragment.setMyCallback(object: RecordCompleteDialogFragment.MyCallback {
            override fun keep() {   //계속 쓰기
                binding.tasteRecordBlurredView.visibility = View.INVISIBLE  //투명뷰 INVISIBLE
            }

            override fun complete() {   //보관함 가기
                findNavController().popBackStack()  //뒤로 가기 -> OgamSelectFragment
                requireActivity().onBackPressed()   //뒤로 가기 -> MainActivity
                (requireActivity() as MainActivity).changeMenu(R.id.feedFragment)   //보관함 화면으로 이동 -> FeedFragment
            }
        })
    }

    private fun setMyEventListener() {
        //content EditText 글자수 세기 위해 TextWatcher 등록
        binding.tasteRecordContentEt.addTextChangedListener(this)

        //뒤로가기 아이콘 이미지뷰 클릭 리스너
        binding.tasteRecordBackIv.setOnClickListener {
            binding.tasteRecordBlurredView.visibility = View.VISIBLE    //투명뷰 VISIBLE
            selectAgainDialogFragment.show(requireActivity().supportFragmentManager, null)  //다시 선택하시겠습니까? 다이얼로그 띄우기
        }

        //FloatingActionButton 클릭 리스너
        binding.tasteRecordFab.setOnClickListener {
            if (validate()) {   //유효성 검사 통과
                binding.tasteRecordEssentialErrTv.visibility = View.INVISIBLE   //에러 메시지 텍스트뷰 INVISIBLE
                binding.tasteRecordBlurredView.visibility = View.VISIBLE    //투명뷰 VISIBLE
                recordCompleteDialogFragment.show(requireActivity().supportFragmentManager, null)  //기록 완료 다이얼로그 띄우기
            } else {    //유효성 검사 실패
                binding.tasteRecordEssentialErrTv.visibility = View.VISIBLE //에러 메시지 텍스트뷰 VISIBLE
                binding.tasteRecordBlurredView.visibility = View.INVISIBLE  //투명뷰 INVISIBLE
            }
        }
    }

    private fun setRecordClMargin(topMargin: Int) {
        val params = binding.tasteRecordRecordCl.layoutParams as ConstraintLayout.LayoutParams
        params.setMargins(0, topMargin, 0, 0)
        binding.tasteRecordRecordCl.layoutParams = params
    }

    private fun validate(): Boolean = !(binding.tasteRecordKeywordEt.text.isBlank() || binding.tasteRecordSrb.rating == 0f)
}