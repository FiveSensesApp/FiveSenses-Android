package com.mangpo.taste.view

import android.os.Build
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseNoVMActivity
import com.mangpo.taste.databinding.ActivityPreviewBinding
import com.mangpo.taste.view.adpater.PreviewVPAdapter
import com.mangpo.taste.view.custom.EmojiInputFilter
import com.mangpo.taste.view.model.PreviewResource

class PreviewActivity : BaseNoVMActivity<ActivityPreviewBinding>(ActivityPreviewBinding::inflate) {
    val shareBtnEnableStatus: MutableLiveData<Boolean> = MutableLiveData(true)

    private lateinit var emojiInputFilter: EmojiInputFilter
    private lateinit var previewVPAdapter: PreviewVPAdapter

    override fun initAfterBinding() {
        initEmojiFilter()
        initPreviewVPAdapter()

        binding.apply {
            activity = this@PreviewActivity
            lifecycleOwner = this@PreviewActivity
        }
    }

    private fun initPreviewVPAdapter() {
        val content = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("content", ContentEntity::class.java)
        } else {
            intent.getParcelableExtra("content")
        }

        if (content!=null) {
            previewVPAdapter = PreviewVPAdapter(this, content, getResource(content!!.category))
            binding.previewVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    binding.previewCharacterRb.isChecked = true

                    when (position) {
                        0 -> shareBtnEnableStatus.postValue(true)
                        1 -> shareBtnEnableStatus.postValue(previewVPAdapter.isBgSelected())
                    }
                }
            })
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

    private fun initEmojiFilter() {
        emojiInputFilter = EmojiInputFilter()
        emojiInputFilter.setCallbackListener(object : EmojiInputFilter.CallbackListener {
            override fun showToast(msg: String) {
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun share() {
        previewVPAdapter.share(binding.previewTl.selectedTabPosition)
    }

    fun onSplitTypeChanged(radioGroup: RadioGroup, id: Int) {
        when (id) {
            binding.previewCharacterRb.id -> previewVPAdapter.changeCustomType(binding.previewTl.selectedTabPosition, 0)
            binding.previewEmojiRb.id -> previewVPAdapter.changeCustomType(binding.previewTl.selectedTabPosition, 1)
            binding.previewTextRb.id -> previewVPAdapter.changeCustomType(binding.previewTl.selectedTabPosition, 2)
        }
    }
}