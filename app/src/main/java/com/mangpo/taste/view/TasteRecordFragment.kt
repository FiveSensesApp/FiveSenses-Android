package com.mangpo.taste.view

import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private val navArgs: TasteRecordFragmentArgs by navArgs()

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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)    //뒤로가기 콜백 리스너 등록

        //키보드 감지해서 뷰 바꾸기
        setEventListener(requireActivity(), viewLifecycleOwner, KeyboardVisibilityEventListener {
            if (it) {   //키보드 올라와 있을 때
                (requireActivity() as MainActivity).setRecordFcvTopMargin(convertDpToPx(requireContext(), 43))  //높이 더 크게
                setRecordClMargin(convertDpToPx(requireContext(), 80))  //기록하기 topMargin 변경
                binding.tasteRecordTitleTv.visibility = View.GONE   //타이틀 GONE
            } else {    //키보드 내려와 있을 때
                (requireActivity() as MainActivity).setRecordFcvTopMargin(convertDpToPx(requireContext(), 136)) //높이 낮게
                setRecordClMargin(convertDpToPx(requireContext(), 172)) //기록하기 topMargin 변경
                binding.tasteRecordTitleTv.visibility = View.VISIBLE    //타이틀 VISIBLE
            }
        })

        //date 텍스트뷰에 오늘 날짜 보여주기
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val formatted = current.format(formatter)
        binding.tasteRecordDateTv.text = formatted

        setUIBySense(navArgs.sense) //감각별로 UI 변경해서 보여주기

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
                clear() //입력했던 내용 모두 초기화
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

    private fun setUIBySense(sense: Int) {
        when (sense) {
            R.string.title_sight -> {
                binding.tasteRecordTasteCharacterIv.setImageResource(R.drawable.ic_sight_character_40)

                val ssb: SpannableStringBuilder = SpannableStringBuilder("${getString(sense)}${getString(R.string.title_taste_record_title)}")
                ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.RD_2)), 0, getString(sense).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.tasteRecordTitleTv.text = ssb

                binding.tasteRecordSrb.setEmptyDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_rd_23)!!)
                binding.tasteRecordSrb.setFilledDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_rd2_23)!!)

                binding.tasteRecordSrbMsgTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.RD_2))
            }

            R.string.title_ear -> {
                binding.tasteRecordTasteCharacterIv.setImageResource(R.drawable.ic_ear_character_40)

                val ssb: SpannableStringBuilder = SpannableStringBuilder("${getString(sense)}${getString(R.string.title_taste_record_title)}")
                ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.BU_2)), 0, getString(sense).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.tasteRecordTitleTv.text = ssb

                binding.tasteRecordSrb.setEmptyDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_bu_23)!!)
                binding.tasteRecordSrb.setFilledDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_bu2_23)!!)

                binding.tasteRecordSrbMsgTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.BU_2))
            }

            R.string.title_smell -> {
                binding.tasteRecordTasteCharacterIv.setImageResource(R.drawable.ic_smell_character_40)

                val ssb: SpannableStringBuilder = SpannableStringBuilder("${getString(sense)}${getString(R.string.title_taste_record_title)}")
                ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.GN_2)), 0, getString(sense).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.tasteRecordTitleTv.text = ssb

                binding.tasteRecordSrb.setEmptyDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_gn_23)!!)
                binding.tasteRecordSrb.setFilledDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_gn2_23)!!)

                binding.tasteRecordSrbMsgTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.GN_3))
            }

            R.string.title_taste -> {
                binding.tasteRecordTasteCharacterIv.setImageResource(R.drawable.ic_taste_character_40)

                val ssb: SpannableStringBuilder = SpannableStringBuilder("${getString(sense)}${getString(R.string.title_taste_record_title)}")
                ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.YE_2)), 0, getString(sense).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.tasteRecordTitleTv.text = ssb

                binding.tasteRecordSrb.setEmptyDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_ye_23)!!)
                binding.tasteRecordSrb.setFilledDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_ye2_23)!!)

                binding.tasteRecordSrbMsgTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.YE_2))
            }

            R.string.title_touch -> {
                binding.tasteRecordTasteCharacterIv.setImageResource(R.drawable.ic_touch_character_40)

                val ssb: SpannableStringBuilder = SpannableStringBuilder("${getString(sense)}${getString(R.string.title_taste_record_title)}")
                ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.PU_2)), 0, getString(sense).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.tasteRecordTitleTv.text = ssb

                binding.tasteRecordSrb.setEmptyDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_pu_23)!!)
                binding.tasteRecordSrb.setFilledDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_pu2_23)!!)

                binding.tasteRecordSrbMsgTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.PU_2))
            }

            R.string.title_sense -> {
                binding.tasteRecordTasteCharacterIv.setImageResource(R.drawable.ic_question_character_40)

                val ssb: SpannableStringBuilder = SpannableStringBuilder("${getString(sense)}${getString(R.string.title_taste_record_title_before)}")
                ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.GY_04)), 0, getString(sense).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.tasteRecordTitleTv.text = ssb

                binding.tasteRecordSrb.setEmptyDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_gy_23)!!)
                binding.tasteRecordSrb.setFilledDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_gy04_23)!!)

                binding.tasteRecordSrbMsgTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.GY_04))
            }
        }
    }

    //작성했던 내용 모두 초기화하는 함수
    private fun clear() {
        binding.tasteRecordKeywordEt.text.clear()
        binding.tasteRecordSrb.rating = 0.0f
        binding.tasteRecordContentEt.text.clear()
    }
}