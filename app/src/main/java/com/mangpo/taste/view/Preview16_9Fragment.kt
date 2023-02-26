package com.mangpo.taste.view

import android.util.Log
import androidx.core.view.doOnLayout
import com.mangpo.taste.base.BaseNoVMFragment
import com.mangpo.taste.databinding.FragmentPreview169Binding

class Preview16_9Fragment : BaseNoVMFragment<FragmentPreview169Binding>(FragmentPreview169Binding::inflate) {
    override fun initAfterBinding() {
        binding.preview169Cl.doOnLayout {
            Log.d("Preview16_9Fragment", "width: ${it.measuredWidth}, height: ${it.measuredHeight}")
        }
    }

}