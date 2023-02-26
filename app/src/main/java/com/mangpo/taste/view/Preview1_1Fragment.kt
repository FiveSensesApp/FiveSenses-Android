package com.mangpo.taste.view

import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.MutableLiveData
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseNoVMFragment
import com.mangpo.taste.databinding.FragmentPreview11Binding
import com.mangpo.taste.util.*
import com.mangpo.taste.view.custom.EmojiInputFilter
import com.mangpo.taste.view.model.PreviewResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Preview1_1Fragment(private val content: ContentEntity, private val resource: PreviewResource) : BaseNoVMFragment<FragmentPreview11Binding>(FragmentPreview11Binding::inflate), TextWatcher {
    val type: MutableLiveData<Int> = MutableLiveData(0)

    private var clWidth = 0
    private var clHeight = 0

    private lateinit var emojiInputFilter: EmojiInputFilter

    override fun initAfterBinding() {
        initEmojiFilter()

        binding.preview11EmojiEt.filters = arrayOf(InputFilter.LengthFilter(2), emojiInputFilter)
        binding.preview11EmojiEt.addTextChangedListener(this)

        binding.apply {
            fragment = this@Preview1_1Fragment
            lifecycleOwner = viewLifecycleOwner
            this.content = this@Preview1_1Fragment.content
            this.resource = this@Preview1_1Fragment.resource
            this.nickname = SpfUtils.getStrSpf("nickname")!!
        }

        if (content.content==null || content.content!!.isEmpty()) {
            binding.preview11ContentTv.visibility = View.GONE
            (binding.preview11Cv.layoutParams as ViewGroup.MarginLayoutParams).let {
                it.topMargin = convertDpToPx(requireContext(), 82)
                it.bottomMargin = convertDpToPx(requireContext(), 129)
                it.leftMargin = convertDpToPx(requireContext(), 70)
                it.rightMargin = convertDpToPx(requireContext(), 70)
            }
            (binding.preview11Cv.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "2:1"
        } else {
            binding.preview11ContentTv.visibility = View.VISIBLE
            (binding.preview11Cv.layoutParams as ViewGroup.MarginLayoutParams).let {
                it.topMargin = convertDpToPx(requireContext(), 28)
                it.bottomMargin = convertDpToPx(requireContext(), 80)
            }
            (binding.preview11Cv.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "1:1"
        }

        binding.preview11Cv.doOnLayout {
            clWidth = it.measuredWidth
            clHeight = it.measuredHeight

            if (content.content==null || content.content!!.isEmpty()) {
                binding.preview115gaamIv.updateLayoutParams {
                    this.width = (clWidth*0.21).toInt()
                    this.height = (clHeight*0.14).toInt()
                }
                (binding.preview115gaamIv.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.topMargin = (clHeight*0.15).toInt()
                    it.rightMargin = (clWidth*0.04).toInt()
                }

                binding.preview11CharacterIv.updateLayoutParams {
                    this.width = (clWidth*0.21).toInt()
                    this.height = (clHeight*0.38).toInt()
                }
                (binding.preview11CharacterIv.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.topMargin = (clHeight*0.28).toInt()
                    it.leftMargin = (clWidth*0.03).toInt()
                }

                binding.preview11EmojiEt.updateLayoutParams {
                    this.width = (clWidth*0.21).toInt()
                    this.height = (clHeight*0.38).toInt()
                }
                (binding.preview11EmojiEt.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.topMargin = (clHeight*0.28).toInt()
                    it.leftMargin = (clWidth*0.03).toInt()
                }

                binding.preview11KeywordTv.updateLayoutParams {
                    this.height = (clHeight*0.25).toInt()
                }
                (binding.preview11KeywordTv.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.topMargin = (clHeight*0.36).toInt()
                    it.leftMargin = (clWidth*0.22).toInt()
                    it.rightMargin = (clWidth*0.04).toInt()
                }

                (binding.preview11CreatedDateTv.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.leftMargin = (clWidth*0.06).toInt()
                    it.bottomMargin = (clHeight*0.16).toInt()
                }

                (binding.preview11InfoTv1.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.bottomMargin = (clHeight*0.16).toInt()
                }

                (binding.preview11InfoTv2.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.bottomMargin = (clHeight*0.16).toInt()
                }

                (binding.preview11InfoTv3.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.bottomMargin = (clHeight*0.16).toInt()
                }

                (binding.preview11InfoTv4.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.rightMargin = (clHeight*0.07).toInt()
                    it.bottomMargin = (clHeight*0.16).toInt()
                }
            } else {
                binding.preview115gaamIv.updateLayoutParams {
                    this.width = (clWidth*0.2).toInt()
                    this.height = (clHeight*0.08).toInt()
                }
                (binding.preview115gaamIv.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.topMargin = (clHeight*0.08).toInt()
                    it.rightMargin = (clWidth*0.04).toInt()
                }

                binding.preview11CharacterIv.updateLayoutParams {
                    this.width = (clWidth*0.21).toInt()
                    this.height = (clHeight*0.21).toInt()
                }
                (binding.preview11CharacterIv.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.topMargin = (clHeight*0.15).toInt()
                    it.leftMargin = (clWidth*0.03).toInt()
                }

                binding.preview11EmojiEt.updateLayoutParams {
                    this.width = (clWidth*0.21).toInt()
                    this.height = (clHeight*0.21).toInt()
                }
                (binding.preview11EmojiEt.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.topMargin = (clHeight*0.15).toInt()
                    it.leftMargin = (clWidth*0.03).toInt()
                }

                binding.preview11KeywordTv.updateLayoutParams {
                    this.height = (clHeight*0.14).toInt()
                }
                (binding.preview11KeywordTv.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.topMargin = (clHeight*0.2).toInt()
                    it.leftMargin = (clWidth*0.22).toInt()
                    it.rightMargin = (clWidth*0.04).toInt()
                }

                (binding.preview11ContentTv.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.topMargin = (clHeight*0.39).toInt()
                    it.leftMargin = (clWidth*0.04).toInt()
                    it.rightMargin = (clWidth*0.04).toInt()
                    it.bottomMargin = (clHeight*0.2).toInt()
                }

                (binding.preview11CreatedDateTv.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.leftMargin = (clWidth*0.06).toInt()
                    it.bottomMargin = (clHeight*0.09).toInt()
                }

                (binding.preview11InfoTv1.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.bottomMargin = (clHeight*0.09).toInt()
                }

                (binding.preview11InfoTv2.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.bottomMargin = (clHeight*0.09).toInt()
                }

                (binding.preview11InfoTv3.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.bottomMargin = (clHeight*0.09).toInt()
                }

                (binding.preview11InfoTv4.layoutParams as ViewGroup.MarginLayoutParams).let {
                    it.rightMargin = (clHeight*0.07).toInt()
                    it.bottomMargin = (clHeight*0.09).toInt()
                }
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0!=null && p0.isNotBlank()) {
            binding.preview11EmojiEt.background = null
        } else {
            binding.preview11EmojiEt.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_custom_emoji_default)
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun initEmojiFilter() {
        emojiInputFilter = EmojiInputFilter()
        emojiInputFilter.setCallbackListener(object : EmojiInputFilter.CallbackListener {
            override fun showToast(msg: String) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setCharacterUI() {
        (binding.preview11KeywordTv.layoutParams as ViewGroup.MarginLayoutParams).let {
            it.leftMargin = (clWidth*0.22).toInt()
            it.rightMargin = (clWidth*0.04).toInt()
        }
    }

    private fun setEmojiUI() {
        (binding.preview11KeywordTv.layoutParams as ViewGroup.MarginLayoutParams).let {
            it.leftMargin = (clWidth*0.22).toInt()
            it.rightMargin = (clWidth*0.04).toInt()
        }
    }

    private fun setTextUI() {
        (binding.preview11KeywordTv.layoutParams as ViewGroup.MarginLayoutParams).let {
            it.leftMargin = (clWidth*0.04).toInt()
            it.rightMargin = (clWidth*0.04).toInt()
        }
    }

    fun share() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(100L)

            val bitmap = getBitmapFromView(clWidth, clHeight, binding.preview11Cv, ContextCompat.getColor(requireContext(), R.color.WH))
            saveImage(bitmap!!, requireContext(), getString(R.string.app_name)) { result, uri ->
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

    fun changeUI(type: Int) {
        when (type) {
            0 -> setCharacterUI()
            1 -> setEmojiUI()
            else -> setTextUI()
        }

        this.type.postValue(type)
    }
}