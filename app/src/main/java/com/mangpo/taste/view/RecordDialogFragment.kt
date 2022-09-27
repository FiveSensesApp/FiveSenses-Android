package com.mangpo.taste.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentRecordDialogBinding
import com.mangpo.taste.util.DialogFragmentUtils
import com.mangpo.taste.util.fadeIn
import com.mangpo.taste.util.fadeOut
import com.mangpo.taste.util.setNavigationResult
import com.mangpo.taste.view.model.RecordDetailResource
import com.mangpo.taste.view.model.TwoBtnDialog

class RecordDialogFragment : DialogFragment() {
    private val args: RecordDialogFragmentArgs by navArgs()

    private var updateFlag: Boolean = false

    private lateinit var binding: FragmentRecordDialogBinding
    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var updateCompleteLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                updateFlag = true   //기록 업데이트 된 상태로 플래그 변경

                //업데이트 후 전달 받은 수정된 기록 데이터(UpdatePostResEntity)를 ContentEntity 로 매핑해서 데이터 바인딩 실행
                val updatedPost: UpdatePostResEntity = result.data!!.getParcelableExtra<UpdatePostResEntity>("updatedPost")!!
                binding.content = ContentEntity(updatedPost.id, updatedPost.category, updatedPost.keyword, updatedPost.star, updatedPost.content, updatedPost.createdDate.split("T")[0])
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordDialogBinding.inflate(inflater, container, false)
        binding.apply {
            fragment = this@RecordDialogFragment
            content = args.content
            resource = setRecordDetailResource(args.content)
        }

        dialog?.setCancelable(false)    //외부 화면 눌러서 다이얼로그 사라지는거 막기

        //모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        initTwoBtnDialog()

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
            override fun leftAction() { //삭제하기
                binding.recordDialogBlurredView.visibility = View.INVISIBLE

                this@RecordDialogFragment.setNavigationResult("contentId", binding.content!!.id)    //이전 프래그먼트한테 recordId 넘겨주기
                dismiss()   //프래그먼트 종료
            }

            override fun rightAction() {    //뒤로가기
                binding.recordDialogBlurredView.visibility = View.INVISIBLE
            }
        })
    }

    private fun setRecordDetailResource(contentEntity: ContentEntity): RecordDetailResource {
        val context = requireContext()

        return when (contentEntity.category) {
            "SIGHT" -> {  //시각
                RecordDetailResource(ContextCompat.getColor(context, R.color.RD_2), ContextCompat.getDrawable(context, R.drawable.ic_sight_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_rd2_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_rd2_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_rd2_23))
            }
            "HEARING" -> {  //청각
                RecordDetailResource(ContextCompat.getColor(context, R.color.BU_2), ContextCompat.getDrawable(context, R.drawable.ic_ear_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_bu2_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_bu2_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_bu2_23))
            }
            "SMELL" -> {  //후각
                RecordDetailResource(ContextCompat.getColor(context, R.color.GN_3), ContextCompat.getDrawable(context, R.drawable.ic_smell_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_gn3_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_gn2_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_gn2_23))
            }
            "TASTE" -> {  //미각
                RecordDetailResource(ContextCompat.getColor(context, R.color.YE_2), ContextCompat.getDrawable(context, R.drawable.ic_taste_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_ye2_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_ye2_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_ye2_23))
            }
            "TOUCH" -> {  //촉각
                RecordDetailResource(ContextCompat.getColor(context, R.color.PU_2), ContextCompat.getDrawable(context, R.drawable.ic_touch_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_pu2_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_pu2_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_pu2_23))
            }
            else -> {   //모르겠어요
                RecordDetailResource(ContextCompat.getColor(context, R.color.GY_04), ContextCompat.getDrawable(context, R.drawable.ic_question_character_72), ContextCompat.getDrawable(context, R.drawable.ic_more_gy04_44), ContextCompat.getDrawable(context, R.drawable.ic_star_empty_gy04_23), ContextCompat.getDrawable(context, R.drawable.ic_star_fill_gy04_23))
            }
        }
    }

    fun close() {
        this@RecordDialogFragment.setNavigationResult("updateFlag", this.updateFlag)    //이전 프래그먼트한테 updateFlag 넘겨주기
        dialog?.dismiss()
    }

    fun clickMoreIv() {
        if (binding.recordDialogMenuCl.visibility==View.VISIBLE)    //더보기 이미지뷰 VISIBLE 이면
            fadeOut(requireContext(), binding.recordDialogMenuCl)   //메뉴 레이아웃 fadeOut
        else    //더보기 이미지뷰 INVISIBLE 이면
            fadeIn(requireContext(), binding.recordDialogMenuCl)    //메뉴 레이아웃 fadeIn
    }

    fun clickUpdateClickView(contentEntity: ContentEntity) {
        fadeOut(requireContext(), binding.recordDialogMenuCl)   //메뉴 레이아웃 fadeOut

        val intent = Intent(requireContext(), RecordUpdateActivity::class.java)
        intent.putExtra("content", contentEntity)
        updateCompleteLauncher.launch(intent)
    }

    fun clickDeleteClickView(contentEntity: ContentEntity) {
        if (contentEntity.content==null)
            binding.recordDialogBlurredView.setBackgroundResource(R.drawable.ic_delete_bg_small)
        else
            binding.recordDialogBlurredView.setBackgroundResource(R.drawable.ic_delete_bg_big)

        fadeOut(requireContext(), binding.recordDialogMenuCl)   //메뉴 레이아웃 fadeOut
        binding.recordDialogBlurredView.visibility = View.VISIBLE   //블러처리 화면 VISIBLE

        //삭제 관련 TwoBtnDialog 띄우기
        val bundle: Bundle = Bundle()
        bundle.putParcelable("data", TwoBtnDialog(getString(R.string.msg_really_delete), getString(R.string.msg_cannot_recover), getString(R.string.action_delete_long), getString(R.string.action_go_back), null))

        twoBtnDialogFragment.arguments = bundle
        twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
    }
}