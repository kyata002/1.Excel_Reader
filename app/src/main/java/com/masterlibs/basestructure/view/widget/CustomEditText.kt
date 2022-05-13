package com.pdfreaderdreamw.pdfreader.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText : AppCompatEditText {
    private var mOnHideKeyboardListener: OnHideKeyboardListener? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )

    fun setOnHideKeyboardListener(onHideKeyboardListener: OnHideKeyboardListener?) {
        mOnHideKeyboardListener = onHideKeyboardListener
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            if (mOnHideKeyboardListener != null) {
                mOnHideKeyboardListener!!.onHideKeyboard()
            }
        }
        return super.onKeyPreIme(keyCode, event)
    }

    interface OnHideKeyboardListener {
        fun onHideKeyboard()
    }
}