package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityPreviewBinding
import com.mangpo.taste.util.*
import com.mangpo.taste.view.custom.EmojiInputFilter
import com.mangpo.taste.view.model.PreviewResource

class PreviewActivity : BaseActivity<ActivityPreviewBinding>(ActivityPreviewBinding::inflate), TextWatcher {
    private lateinit var iconCustomBottomSheetFragment: IconCustomBottomSheetFragment
    private lateinit var emojiInputFilter: EmojiInputFilter

    override fun initAfterBinding() {
        initIconCustomBottomSheetFragment()
        initEmojiFilter()

        binding.apply {
            activity = this@PreviewActivity
            content = intent.getParcelableExtra("content")
            resource = getResource(content!!.category)
            nickname = SpfUtils.getStrSpf("nickname")!!
            customType = 0
        }

        binding.previewCl.viewTreeObserver.addOnGlobalLayoutListener {
            if (intent.getIntExtra("action", 0)==0) {   //이미지 저장일 경우
                val bitmap = getBitmapFromView(binding.previewCl.measuredWidth, binding.previewCl.measuredHeight, binding.previewCl, ContextCompat.getColor(baseContext, R.color.GY_01))
                saveImage(bitmap!!, baseContext, getString(R.string.app_name)) { result, uri -> afterSaveImage(result) }
            }
        }

        binding.previewEmojiEt.filters = arrayOf(InputFilter.LengthFilter(2), emojiInputFilter)
        binding.previewEmojiEt.addTextChangedListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0!=null && p0.isNotBlank()) {
            binding.previewEmojiEt.background = null
        } else {
            binding.previewEmojiEt.background = ContextCompat.getDrawable(baseContext, R.drawable.ic_icon_custom_emoji_default)
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun getResource(category: String): PreviewResource {
        return when (category) {
            "SIGHT" -> PreviewResource(" '${getString(R.string.title_sight)}'", ContextCompat.getDrawable(baseContext, R.drawable.ic_5gaam_rd2)!!, ContextCompat.getDrawable(baseContext, R.drawable.ic_sight_character_72)!!, ContextCompat.getColor(baseContext, R.color.RD_2))
            "HEARING" -> PreviewResource(" '${getString(R.string.title_ear)}'", ContextCompat.getDrawable(baseContext, R.drawable.ic_5gaam_bu2)!!, ContextCompat.getDrawable(baseContext, R.drawable.ic_ear_character_72)!!, ContextCompat.getColor(baseContext, R.color.BU_2))
            "SMELL" -> PreviewResource(" '${getString(R.string.title_smell)}'", ContextCompat.getDrawable(baseContext, R.drawable.ic_5gaam_gn2)!!, ContextCompat.getDrawable(baseContext, R.drawable.ic_smell_character_72)!!, ContextCompat.getColor(baseContext, R.color.GN_3))
            "TASTE" -> PreviewResource(" '${getString(R.string.title_taste)}'", ContextCompat.getDrawable(baseContext, R.drawable.ic_5gaam_ye2)!!, ContextCompat.getDrawable(baseContext, R.drawable.ic_taste_character_72)!!, ContextCompat.getColor(baseContext, R.color.YE_3))
            "TOUCH" -> PreviewResource(" '${getString(R.string.title_touch)}'", ContextCompat.getDrawable(baseContext, R.drawable.ic_5gaam_pu2)!!, ContextCompat.getDrawable(baseContext, R.drawable.ic_touch_character_72)!!, ContextCompat.getColor(baseContext, R.color.PU_2))
            else -> PreviewResource("", ContextCompat.getDrawable(baseContext, R.drawable.ic_5gaam_gy04)!!, ContextCompat.getDrawable(baseContext, R.drawable.ic_question_character_72)!!, ContextCompat.getColor(baseContext, R.color.GY_04))
        }
    }

    private fun initIconCustomBottomSheetFragment() {
        iconCustomBottomSheetFragment = IconCustomBottomSheetFragment()
        iconCustomBottomSheetFragment.setCallbackListener(object : IconCustomBottomSheetFragment.CallbackListener {
            override fun typeSelected(type: Int) {
                binding.customType = type

                when (type) {
                    0 -> {

                    }
                    1 -> {
                        showKeyboard(binding.previewEmojiEt)
                    }
                    2 -> {

                    }
                }
            }
        })
    }

    private fun initEmojiFilter() {
        emojiInputFilter = EmojiInputFilter()
        emojiInputFilter.setCallbackListener(object : EmojiInputFilter.CallbackListener {
            override fun showToast(msg: String) {
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun afterSaveImage(result: Boolean) {
        if (!result) {
            showToast("이미지 저장 중 문제가 발생했습니다.")
        }

        finish()
    }

    fun showIconCustomBottomSheet() {
        val bundle: Bundle = Bundle()
        bundle.putInt("customType", binding.customType!!)

        iconCustomBottomSheetFragment.arguments = bundle
        iconCustomBottomSheetFragment.show(supportFragmentManager, null)
    }

    fun share() {
        val bitmap = getBitmapFromView(binding.previewCl.measuredWidth, binding.previewCl.measuredHeight, binding.previewCl, ContextCompat.getColor(baseContext, R.color.GY_01))
        saveImage(bitmap!!, baseContext, getString(R.string.app_name)) { result, uri ->
            if (result) {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "image/png"
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)

                startActivity(Intent.createChooser(sharingIntent, ""))
            } else {
                showToast("공유 중 문제가 발생했습니다.")
            }
        }
    }
}