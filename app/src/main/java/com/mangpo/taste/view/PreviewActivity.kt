package com.mangpo.taste.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseNoVMActivity
import com.mangpo.taste.databinding.ActivityPreviewBinding
import com.mangpo.taste.util.*
import com.mangpo.taste.view.adpater.PreviewVPAdapter
import com.mangpo.taste.view.custom.EmojiInputFilter
import com.mangpo.taste.view.model.PreviewResource

class PreviewActivity : BaseNoVMActivity<ActivityPreviewBinding>(ActivityPreviewBinding::inflate), TextWatcher {
    private lateinit var iconCustomBottomSheetFragment: IconCustomBottomSheetFragment
    private lateinit var emojiInputFilter: EmojiInputFilter
    private lateinit var previewVPAdapter: PreviewVPAdapter

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            binding.backgroundUri = uri
        }

    override fun initAfterBinding() {
        initIconCustomBottomSheetFragment()
        initEmojiFilter()
        initPreviewVPAdapter()

        binding.apply {
            activity = this@PreviewActivity
            previewClMarginTop = 163
            customType = 0
            content = intent.getParcelableExtra("content")
            resource = getResource(content!!.category)
            nickname = SpfUtils.getStrSpf("nickname")!!
        }

        /*binding.previewCl.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (intent.getIntExtra("action", 0)==0) {   //이미지 저장일 경우
                    val bitmap = getBitmapFromView(binding.previewCl.measuredWidth, binding.previewCl.measuredHeight, binding.previewCl, ContextCompat.getColor(baseContext, R.color.GY_01))
                    saveImage(bitmap!!, baseContext, getString(R.string.app_name)) { result, uri -> afterSaveImage(result) }
                }

                binding.previewCl.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding.previewEmojiEt.filters = arrayOf(InputFilter.LengthFilter(2), emojiInputFilter)
        binding.previewEmojiEt.addTextChangedListener(this)*/
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        /*if (p0!=null && p0.isNotBlank()) {
            binding.previewEmojiEt.background = null
        } else {
            binding.previewEmojiEt.background = ContextCompat.getDrawable(baseContext, R.drawable.ic_icon_custom_emoji_default)
        }*/
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun initPreviewVPAdapter() {
        val content = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("content", ContentEntity::class.java)
        } else {
            intent.getParcelableExtra("content")
        }
        if (content!=null) {
            previewVPAdapter = PreviewVPAdapter(this, content, getResource(content!!.category))
            binding.previewVp.apply {
                this.adapter = previewVPAdapter
                isUserInputEnabled = false
            }
        }

        TabLayoutMediator(binding.previewTl, binding.previewVp) { tab, position ->
            when (position) {
                0 -> tab.text = "1:1"
                1 -> tab.text = "16:9"
            }

        }.attach()
    }

    private fun getResource(category: String): PreviewResource {
        return when (category) {
            "SIGHT" -> PreviewResource(" '${getString(R.string.title_sight)}'", ContextCompat.getDrawable(baseContext, R.drawable.ic_5gaam_rd2)!!, ContextCompat.getDrawable(baseContext, R.drawable.ic_sight_character_72)!!, ContextCompat.getColor(baseContext, R.color.RD_2))
            "HEARING" -> PreviewResource(" '${getString(R.string.title_ear)}'", ContextCompat.getDrawable(baseContext, R.drawable.ic_5gaam_bu2)!!, ContextCompat.getDrawable(baseContext, R.drawable.ic_ear_character_72)!!, ContextCompat.getColor(baseContext, R.color.BU_2))
            "SMELL" -> PreviewResource(" '${getString(R.string.title_smell)}'", ContextCompat.getDrawable(baseContext, R.drawable.ic_5gaam_gn)!!, ContextCompat.getDrawable(baseContext, R.drawable.ic_smell_character_72)!!, ContextCompat.getColor(baseContext, R.color.GN_3))
            "TASTE" -> PreviewResource(" '${getString(R.string.title_taste)}'", ContextCompat.getDrawable(baseContext, R.drawable.ic_5gaam_ye)!!, ContextCompat.getDrawable(baseContext, R.drawable.ic_taste_character_72)!!, ContextCompat.getColor(baseContext, R.color.YE_3))
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
//                        showKeyboard(binding.previewEmojiEt)
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
        if (result) {
            showToast(getString(R.string.msg_success_save_image))
        } else {
            showToast("이미지 저장 중 문제가 발생했습니다.")
        }

        finish()
    }

    private fun showGallery(isGrated: Boolean) {
        if (isGrated) {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    fun showIconCustomBottomSheet() {
        val bundle: Bundle = Bundle()
        bundle.putInt("customType", binding.customType!!)

        iconCustomBottomSheetFragment.arguments = bundle
        iconCustomBottomSheetFragment.show(supportFragmentManager, null)
    }

    fun share() {
        previewVPAdapter.share(binding.previewTl.selectedTabPosition)
        /*CoroutineScope(Dispatchers.Main).launch {
            binding.isSharing = true
//            binding.previewClMarginTop = convertPxToDp(baseContext, (getDeviceHeight() - binding.previewCl.measuredHeight) / 2)

            delay(100L)

            val bitmap = getBitmapFromView(getDeviceWidth(), getDeviceHeight(), binding.root, ContextCompat.getColor(baseContext, R.color.WH))
            saveImage(bitmap!!, baseContext, getString(R.string.app_name)) { result, uri ->
                if (result) {
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "image/png"
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)

                    startActivity(Intent.createChooser(sharingIntent, ""))
                } else {
                    showToast("공유 중 문제가 발생했습니다.")
                }

                binding.apply {
                    isSharing = false
                    previewClMarginTop = 163
                }
            }
        }*/
    }

    fun addBackground() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            checkPermission(lifecycleScope, Manifest.permission.WRITE_EXTERNAL_STORAGE, "갤러리 접근을 위해 저장소 접근 권한이 필요합니다. 권한을 허용해주세요.") { showGallery(it) }
        } else {
            checkPermission(lifecycleScope, Manifest.permission.READ_MEDIA_IMAGES, "갤러리 접근을 위해 저장소 접근 권한이 필요합니다. 권한을 허용해주세요.") { showGallery(it) }
        }
    }

    fun onSplitTypeChanged(radioGroup: RadioGroup, id: Int) {
        when (id) {
            binding.previewCharacterRb.id -> previewVPAdapter.changeCustomType(binding.previewTl.selectedTabPosition, 0)
            binding.previewEmojiRb.id -> previewVPAdapter.changeCustomType(binding.previewTl.selectedTabPosition, 1)
            binding.previewTextRb.id -> previewVPAdapter.changeCustomType(binding.previewTl.selectedTabPosition, 2)
        }
    }
}