package com.mangpo.taste.view.custom

import android.text.InputFilter
import android.text.Spanned

class EmojiInputFilter : InputFilter {
    private lateinit var callbackListener: CallbackListener

    interface CallbackListener {
        fun showToast(msg: String)
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        for (i in start until end) {
            val type = Character.getType(source[i]).toByte()
            if (type != Character.SURROGATE && type != Character.OTHER_SYMBOL) {
                callbackListener.showToast("이모지를 입력해주세요")
                return ""
            }
        }

        return source
    }

    fun setCallbackListener(callbackListener: CallbackListener) {
        this.callbackListener = callbackListener
    }
}