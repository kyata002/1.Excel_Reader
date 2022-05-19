package com.docxmaster.docreader.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        initLanguage()
        initView()
        addEvent()
    }

    private fun initLanguage() {}

    protected abstract val layoutId: Int
    protected abstract fun initView()
    protected abstract fun addEvent()


}