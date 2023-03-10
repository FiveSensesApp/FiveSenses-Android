package com.mangpo.taste.view

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ActivityShareGuideBinding
import com.mangpo.taste.util.SpfUtils.writeSpf

class ShareGuideActivity : AppCompatActivity() {
    private lateinit var imgs: MutableList<Drawable?>

    private lateinit var binding: ActivityShareGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imgs = mutableListOf(ContextCompat.getDrawable(baseContext, R.drawable.share_guide1), ContextCompat.getDrawable(baseContext, R.drawable.share_guide2), ContextCompat.getDrawable(baseContext, R.drawable.share_guide3))

        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_guide)
        binding.apply {
            activity = this@ShareGuideActivity
            img = imgs.removeFirst()
        }


        setContentView(binding.root)
    }

    fun setImage() {
        if (imgs.isEmpty()) {
            writeSpf("shareGuide", true)

            val content = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra("content", ContentEntity::class.java)
            } else {
                intent.getParcelableExtra("content")
            }

            val intent = Intent(this, PreviewActivity::class.java)
            intent.putExtra("content", content)
            startActivity(intent)

            finish()
        } else {
            binding.img = imgs.removeFirst()
        }
    }
}