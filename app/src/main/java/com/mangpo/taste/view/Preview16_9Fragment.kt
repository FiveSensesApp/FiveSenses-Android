package com.mangpo.taste.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseNoVMFragment
import com.mangpo.taste.databinding.FragmentPreview169Binding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.util.checkPermission
import com.mangpo.taste.util.getBitmapFromView
import com.mangpo.taste.util.saveImage
import com.mangpo.taste.view.custom.EmojiInputFilter
import com.mangpo.taste.view.model.PreviewResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Preview16_9Fragment(private val content: ContentEntity, private val resource: PreviewResource) : BaseNoVMFragment<FragmentPreview169Binding>(FragmentPreview169Binding::inflate), TextWatcher {
    val type: MutableLiveData<Int> = MutableLiveData(0)

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri!=null) {
                binding.preview169Iv.setImageURI(uri)
            }

            bgUri = uri
            (requireActivity() as PreviewActivity).shareBtnEnableStatus.postValue(bgUri!=null)
        }

    private var bgUri: Uri? = null

    private lateinit var emojiInputFilter: EmojiInputFilter

    override fun initAfterBinding() {
        initEmojiFilter()

        binding.preview169EmojiEt.filters = arrayOf(InputFilter.LengthFilter(2), emojiInputFilter)
        binding.preview169EmojiEt.addTextChangedListener(this)

        binding.apply {
            fragment = this@Preview16_9Fragment
            lifecycleOwner = viewLifecycleOwner
            this.content = this@Preview16_9Fragment.content
            this.resource = this@Preview16_9Fragment.resource
            this.nickname = SpfUtils.getStrSpf("nickname")!!
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0!=null && p0.isNotBlank()) {
            binding.preview169EmojiEt.background = null
        } else {
            binding.preview169EmojiEt.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_custom_emoji_default)
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun showGallery(isGrated: Boolean) {
        if (isGrated) {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun initEmojiFilter() {
        emojiInputFilter = EmojiInputFilter()
        emojiInputFilter.setCallbackListener(object : EmojiInputFilter.CallbackListener {
            override fun showToast(msg: String) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun changeUI(type: Int) {
        this.type.postValue(type)
    }

    fun addBackground() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            checkPermission(lifecycleScope, Manifest.permission.WRITE_EXTERNAL_STORAGE, "갤러리 접근을 위해 저장소 접근 권한이 필요합니다. 권한을 허용해주세요.") { showGallery(it) }
        } else {
            checkPermission(lifecycleScope, Manifest.permission.READ_MEDIA_IMAGES, "갤러리 접근을 위해 저장소 접근 권한이 필요합니다. 권한을 허용해주세요.") { showGallery(it) }
        }
    }

    fun isBgSelected(): Boolean = bgUri!=null

    fun share() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.preview169PlusIb.visibility = View.INVISIBLE

            delay(100L)

            val bitmap = getBitmapFromView(binding.preview169Cv.width, binding.preview169Cv.height, binding.preview169Cv, ContextCompat.getColor(requireContext(), R.color.WH))
            saveImage(bitmap!!, requireContext(), getString(R.string.app_name)) { result, uri ->
                if (result) {
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "image/png"
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)

                    binding.preview169PlusIb.visibility = View.VISIBLE

                    startActivity(Intent.createChooser(sharingIntent, ""))
                } else {
                    showToast("공유 중 문제가 발생했습니다.")
                }
            }
        }
    }
}