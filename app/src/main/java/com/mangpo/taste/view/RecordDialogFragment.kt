package com.mangpo.taste.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.mangpo.domain.model.RecordEntity
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ItemRecordDetailBinding
import com.mangpo.taste.util.DialogFragmentUtils
import com.mangpo.taste.util.fadeIn
import com.mangpo.taste.util.fadeOut
import com.mangpo.taste.view.model.TwoBtnDialog

class RecordDialogFragment : DialogFragment() {
    private val args: RecordDialogFragmentArgs by navArgs()

    private lateinit var binding: ItemRecordDetailBinding
    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var recordEntity: RecordEntity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemRecordDetailBinding.inflate(inflater, container, false)

        dialog?.setCancelable(false)    //외부 화면 눌러서 다이얼로그 사라지는거 막기

        //모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        recordEntity = args.record.record!!

        binding.recordDetailCloseIv.visibility = View.VISIBLE   //닫기 아이콘 VISIBLE

        initTwoBtnDialog()
        setMyEventListener()
        bind()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //넓이 0.83%
        DialogFragmentUtils.resizeWidth(
            requireContext(),
            this@RecordDialogFragment,
            0.89f
        )
    }

    private fun initTwoBtnDialog() {
        twoBtnDialogFragment = TwoBtnDialogFragment()
        twoBtnDialogFragment.setMyCallback(object : TwoBtnDialogFragment.MyCallback {
            override fun leftAction() {
            }

            override fun rightAction() {
            }
        })
    }

    private fun setMyEventListener() {
        //닫기 이미지뷰 클릭 리스너 -> 다이얼로그 닫기
        binding.recordDetailCloseIv.setOnClickListener {
            dialog?.dismiss()
        }

        //더보기 이미지뷰 클릭 리스너
        binding.recordDetailMoreIv.setOnClickListener {
            if (binding.recordDetailMenuCl.visibility==View.VISIBLE)    //더보기 이미지뷰 VISIBLE 이면
                fadeOut(requireContext(), binding.recordDetailMenuCl)   //메뉴 레이아웃 fadeOut
            else    //더보기 이미지뷰 INVISIBLE 이면
                fadeIn(requireContext(), binding.recordDetailMenuCl)    //메뉴 레이아웃 fadeIn
        }

        //수정 클릭뷰 클릭 리스너
        binding.recordDetailUpdateClickView.setOnClickListener {
            fadeOut(requireContext(), binding.recordDetailMenuCl)   //메뉴 레이아웃 fadeOut

            //RecordUpdateActivity 로 이동
            val intent: Intent = Intent(requireContext(), RecordUpdateActivity::class.java)
            intent.putExtra("record", args.record.record!!)
            startActivity(intent)
        }

        //삭제 클릭뷰 클릭 리스너
        binding.recordDetailDeleteClickView.setOnClickListener {
            fadeOut(requireContext(), binding.recordDetailMenuCl)   //메뉴 레이아웃 fadeOut
            binding.recordDetailBlurredView.visibility = View.VISIBLE   //블러처리 화면 VISIBLE

            //삭제 관련 TwoBtnDialog 띄우기
            val bundle: Bundle = Bundle()
            bundle.putParcelable("data", TwoBtnDialog(getString(R.string.msg_really_delete), getString(R.string.msg_cannot_recover), getString(R.string.action_delete_long), getString(R.string.action_go_back), true))

            twoBtnDialogFragment.arguments = bundle
            twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
        }
    }

    private fun bind() {
        binding.recordDetailKeywordTv.text = recordEntity.keyword
        if (recordEntity.content==null)
            binding.recordDetailContentTv.visibility = View.GONE
        else {
            binding.recordDetailContentTv.visibility = View.VISIBLE
            binding.recordDetailContentTv.text = recordEntity.content
        }
        binding.recordDetailDateTv.text = recordEntity.date
        binding.recordDetailSrb.rating = recordEntity.star

        when (recordEntity.taste) {
            0 -> {
                binding.recordDetailMoreIv.setImageResource(R.drawable.ic_more_rd2_44)
                binding.recordDetailCharacterIv.setImageResource(R.drawable.ic_sight_character_72)
                binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.RD_2))
                binding.recordDetailSrb.setFilledDrawableRes(R.drawable.ic_star_fill_rd2_23)
                binding.recordDetailSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_rd2_23)
            }
            1 -> {
                binding.recordDetailMoreIv.setImageResource(R.drawable.ic_more_bu2_44)
                binding.recordDetailCharacterIv.setImageResource(R.drawable.ic_ear_character_72)
                binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.BU_2))
                binding.recordDetailSrb.setFilledDrawableRes(R.drawable.ic_star_fill_bu2_23)
                binding.recordDetailSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_bu2_23)
            }
            2 -> {
                binding.recordDetailMoreIv.setImageResource(R.drawable.ic_more_gn3_44)
                binding.recordDetailCharacterIv.setImageResource(R.drawable.ic_smell_character_72)
                binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.GN_3))
                binding.recordDetailSrb.setFilledDrawableRes(R.drawable.ic_star_fill_gn2_23)
                binding.recordDetailSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_gn2_23)
            }
            3 -> {
                binding.recordDetailMoreIv.setImageResource(R.drawable.ic_more_ye2_44)
                binding.recordDetailCharacterIv.setImageResource(R.drawable.ic_taste_character_72)
                binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.YE_2))
                binding.recordDetailSrb.setFilledDrawableRes(R.drawable.ic_star_fill_ye2_23)
                binding.recordDetailSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_ye2_23)
            }
            4 -> {
                binding.recordDetailMoreIv.setImageResource(R.drawable.ic_more_pu2_44)
                binding.recordDetailCharacterIv.setImageResource(R.drawable.ic_touch_character_72)
                binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.PU_2))
                binding.recordDetailSrb.setFilledDrawableRes(R.drawable.ic_star_fill_pu2_23)
                binding.recordDetailSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_pu2_23)
            }
            5 -> {
                binding.recordDetailMoreIv.setImageResource(R.drawable.ic_more_gy04_44)
                binding.recordDetailCharacterIv.setImageResource(R.drawable.ic_question_character_72)
                binding.recordDetailDateTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.GY_04))
                binding.recordDetailSrb.setFilledDrawableRes(R.drawable.ic_star_fill_gy04_23)
                binding.recordDetailSrb.setEmptyDrawableRes(R.drawable.ic_star_empty_gy04_23)
            }
        }
    }
}