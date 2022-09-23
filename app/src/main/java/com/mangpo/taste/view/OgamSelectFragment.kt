package com.mangpo.taste.view

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentOgamSelectBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.model.OgamSelect
import com.mangpo.taste.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class OgamSelectFragment : BaseFragment<FragmentOgamSelectBinding>(FragmentOgamSelectBinding::inflate) {
    private val mainVm: MainViewModel by activityViewModels()

    override fun initAfterBinding() {
        binding.apply {
            fragment = this@OgamSelectFragment
            mainVm = this@OgamSelectFragment.mainVm
            lifecycleOwner = viewLifecycleOwner
        }

        //현재 날짜 보여주기
        val current = System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("M.d (E) a HH:mm", Locale.KOREA).format(current)
        binding.ogamSelectDateTv.text = simpleDateFormat

        observe()
    }

    private fun observe() {
        mainVm.getUserInfoResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                //hello 텍스트뷰 부분 텍스트 색상 변경
                val nickname = SpfUtils.getStrSpf("nickname")!!
                val ssb: SpannableStringBuilder = SpannableStringBuilder("${nickname}${getString(R.string.msg_hello)}")
                ssb.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.GY_04)), 0, nickname.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.ogamSelectHelloTv.text = ssb
            } else {
                SpfUtils.clear()
                requireActivity().finishAffinity()
            }
        })
    }

    fun goRecordFragment(type: Int) {
        var ogamSelect: OgamSelect = OgamSelect()

        when (type) {
            R.string.title_sight -> ogamSelect = OgamSelect(ContextCompat.getDrawable(requireContext(), R.drawable.ic_sight_character_40), getString(type), getString(R.string.title_taste_record_title), R.color.RD_2, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_rd_23), ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_rd2_23), ContextCompat.getColor(requireContext(), R.color.RD_2))
            R.string.title_ear -> ogamSelect = OgamSelect(ContextCompat.getDrawable(requireContext(), R.drawable.ic_ear_character_40), getString(type), getString(R.string.title_taste_record_title), R.color.BU_2, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_bu_23), ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_bu2_23), ContextCompat.getColor(requireContext(), R.color.BU_2))
            R.string.title_smell -> ogamSelect = OgamSelect(ContextCompat.getDrawable(requireContext(), R.drawable.ic_smell_character_40), getString(type), getString(R.string.title_taste_record_title), R.color.GN_2, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_gn_23), ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_gn2_23), ContextCompat.getColor(requireContext(), R.color.GN_3))
            R.string.title_taste -> ogamSelect = OgamSelect(ContextCompat.getDrawable(requireContext(), R.drawable.ic_taste_character_40), getString(type), getString(R.string.title_taste_record_title), R.color.YE_2, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_ye_23), ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_ye2_23), ContextCompat.getColor(requireContext(), R.color.YE_2))
            R.string.title_touch -> ogamSelect = OgamSelect(ContextCompat.getDrawable(requireContext(), R.drawable.ic_touch_character_40), getString(type), getString(R.string.title_taste_record_title), R.color.PU_2, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_pu_23), ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_pu2_23), ContextCompat.getColor(requireContext(), R.color.PU_2))
            R.string.title_question -> ogamSelect = OgamSelect(ContextCompat.getDrawable(requireContext(), R.drawable.ic_question_character_40), getString(R.string.title_sense), getString(R.string.title_taste_record_title_before), R.color.GY_04, ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_blurred_gy_23), ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_fill_gy04_23), ContextCompat.getColor(requireContext(), R.color.GY_04))
        }

        val action = OgamSelectFragmentDirections.actionOgamSelectFragmentToTasteRecordFragment(ogamSelect)
        findNavController().navigate(action)
    }
}