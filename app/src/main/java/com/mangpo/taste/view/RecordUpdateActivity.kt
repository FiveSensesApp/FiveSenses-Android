package com.mangpo.taste.view

import com.mangpo.domain.model.RecordEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityRecordUpdateBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class RecordUpdateActivity : BaseActivity<ActivityRecordUpdateBinding>(ActivityRecordUpdateBinding::inflate) {
    override fun initAfterBinding() {
        //인텐트를 통해 얻어낸 record 데이터 UI 바인딩
        bind(intent.getParcelableExtra<RecordEntity>("record")!!)
    }

    private fun bind(record: RecordEntity) {
        val date: LocalDate = LocalDate.parse(record.date, DateTimeFormatter.ofPattern("yyyy.MM.dd"))

        when (record.taste) {
            0 -> {  //시각
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_rd2_38)
                binding.recordUpdateTitleTv.text = "시각으로 감각한\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,"
            }
            1 -> {  //청각
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_bu2_38)
                binding.recordUpdateTitleTv.text = "청각으로 감각한\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,"
            }
            2 -> {  //후각
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_gn2_38)
                binding.recordUpdateTitleTv.text = "후각으로 감각한\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,"
            }
            3 -> {  //미각
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_ye2_38)
                binding.recordUpdateTitleTv.text = "미각으로 감각한\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,"
            }
            4 -> {  //촉각
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_pu2_38)
                binding.recordUpdateTitleTv.text = "촉각으로 감각한\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,"
            }
            else -> {   //모르겠어요
                binding.recordUpdateBackIv.setImageResource(R.drawable.ic_back_bk_38)
                binding.recordUpdateTitleTv.text = "감각이 정해지기 전인\n${date.monthValue}월 ${date.dayOfMonth}일의 취향 수정,"
            }
        }
    }
}