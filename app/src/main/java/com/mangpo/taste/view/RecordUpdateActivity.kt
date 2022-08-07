package com.mangpo.taste.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mangpo.domain.model.RecordEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityRecordUpdateBinding
import com.mangpo.taste.util.convertDpToPx
import com.mangpo.taste.util.setSpannableText
import com.mangpo.taste.util.setting
import com.mangpo.taste.view.model.OneBtnDialog
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class RecordUpdateActivity : BaseActivity<ActivityRecordUpdateBinding>(ActivityRecordUpdateBinding::inflate), TextWatcher {
    private lateinit var oneBtnDialogFragment: OneBtnDialogFragment

    override fun initAfterBinding() {
        setMyEventListener()
        setOneBtnDialogFragment()

        bind(intent.getParcelableExtra<RecordEntity>("record")!!)   //인텐트를 통해 얻어낸 record 데이터 UI 바인딩
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.tasteRecordContentCntTv.text = "${p0?.length} / 100"
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun bind(record: RecordEntity) {
        val date: LocalDate = LocalDate.parse(record.date, DateTimeFormatter.ofPattern("yyyy.MM.dd"))

        binding.recordUpdateKeywordEt.setText(record.keyword)
        binding.tasteRecordContentEt.setText(record.content)
        binding.tasteRecordDateTv.text = record.date
        binding.tasteRecordContentCntTv.text = "${record.content?.length} / 100"

        when (record.taste) {
            0 -> {  //시각
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_rd2_38)
                setSpannableText("시각으로 감각한\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,", applicationContext, R.color.RD_2, 0, 2, binding.recordUpdateTitleTv)
                binding.recordUpdateSrb.setting(R.drawable.ic_star_blurred_rd_23, R.drawable.ic_star_fill_rd2_23, record.star)
                binding.recordUpdateSrbMsgTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.RD_2))
            }
            1 -> {  //청각
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_bu2_38)
                setSpannableText("청각으로 감각한\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,", applicationContext, R.color.BU_2, 0, 2, binding.recordUpdateTitleTv)
                binding.recordUpdateSrb.setting(R.drawable.ic_star_blurred_bu_23, R.drawable.ic_star_fill_bu2_23, record.star)
                binding.recordUpdateSrbMsgTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.BU_2))
            }
            2 -> {  //후각
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_gn2_38)
                setSpannableText("후각으로 감각한\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,", applicationContext, R.color.GN_2, 0, 2, binding.recordUpdateTitleTv)
                binding.recordUpdateSrb.setting(R.drawable.ic_star_blurred_gn_23, R.drawable.ic_star_fill_gn2_23, record.star)
                binding.recordUpdateSrbMsgTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.GN_3))
            }
            3 -> {  //미각
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_ye2_38)
                setSpannableText("미각으로 감각한\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,", applicationContext, R.color.YE_2, 0, 2, binding.recordUpdateTitleTv)
                binding.recordUpdateSrb.setting(R.drawable.ic_star_blurred_ye_23, R.drawable.ic_star_fill_ye2_23, record.star)
                binding.recordUpdateSrbMsgTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.YE_2))
            }
            4 -> {  //촉각
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_pu2_38)
                setSpannableText("촉각으로 감각한\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,", applicationContext, R.color.PU_2, 0, 2, binding.recordUpdateTitleTv)
                binding.recordUpdateSrb.setting(R.drawable.ic_star_blurred_pu_23, R.drawable.ic_star_fill_pu2_23, record.star)
                binding.recordUpdateSrbMsgTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.PU_2))
            }
            else -> {   //모르겠어요
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_bk_38)
                setSpannableText("감각이 정해지기 전인\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,", applicationContext, R.color.GY_04, 0, 2, binding.recordUpdateTitleTv)
                binding.recordUpdateSrb.setting(R.drawable.ic_star_blurred_gy_23, R.drawable.ic_star_fill_gy04_23, record.star)
                binding.recordUpdateSrbMsgTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.GY_04))
            }
        }
    }

    private fun setMyEventListener() {
        //뒤로가기 이미지뷰 클릭 리스너 등록
        binding.recordUpdateBackIv.setOnClickListener {
            onBackPressed()
        }

        //내용 EditText TextWatcher 등록
        binding.tasteRecordContentEt.addTextChangedListener(this)

        //키보드 감지해서 뷰 바꾸기
        KeyboardVisibilityEvent.setEventListener(
            this@RecordUpdateActivity,
            KeyboardVisibilityEventListener {
                if (it) {   //키보드 올라와 있을 때
                    binding.recordUpdateTitleTv.visibility = View.GONE  //타이틀 GONE

                    //키워드 EditText TopMargin 25dp
                    val layoutParams = binding.recordUpdateKeywordEt.layoutParams
                    (layoutParams as ConstraintLayout.LayoutParams).topMargin = convertDpToPx(applicationContext, 25)
                    binding.recordUpdateKeywordEt.layoutParams = layoutParams
                } else {    //키보드 내려와 있을 때
                    binding.recordUpdateTitleTv.visibility = View.VISIBLE   //타이틀 VISIBLE

                    //키워드 EditText TopMargin 134dp
                    val layoutParams = binding.recordUpdateKeywordEt.layoutParams
                    (layoutParams as ConstraintLayout.LayoutParams).topMargin = convertDpToPx(applicationContext, 134)
                    binding.recordUpdateKeywordEt.layoutParams = layoutParams
                }
            })

        //완료 버튼 클릭 리스너 등록
        binding.recordUpdateCompleteBtn.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putParcelable("data", OneBtnDialog(getString(R.string.title_update_complete), getString(R.string.msg_update_complete), getString(R.string.action_confirm), listOf(46, 10, 46, 12)))

            oneBtnDialogFragment.arguments = bundle
            oneBtnDialogFragment.show(supportFragmentManager, null)
        }
    }

    private fun setOneBtnDialogFragment() {
        oneBtnDialogFragment = OneBtnDialogFragment()
        oneBtnDialogFragment.setMyCallback(object: OneBtnDialogFragment.MyCallback {
            override fun end() {
                finish()
            }
        })
    }
}