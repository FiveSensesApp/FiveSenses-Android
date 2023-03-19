package com.mangpo.taste.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mangpo.taste.R
import com.mangpo.taste.databinding.ActivityUpdateEventBinding

class UpdateEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_event)
        binding.apply {
            activity = this@UpdateEventActivity
        }

        setContentView(binding.root)
    }

    fun goInstagram() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/5gaam_app"))
        startActivity(intent)
    }
}