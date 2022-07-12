package com.mangpo.taste.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mangpo.taste.base.BaseActivity
import com.mangpo.taste.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var navHostFragment: NavHostFragment

    override fun initAfterBinding() {
        navHostFragment = supportFragmentManager.findFragmentById(binding.mainFcv.id) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.mainBnv.setupWithNavController(navController)

        binding.mainBnv.background = null
    }

}