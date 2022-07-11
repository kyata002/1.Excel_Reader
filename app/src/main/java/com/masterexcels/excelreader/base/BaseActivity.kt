package com.docxmaster.docreader.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics

abstract class BaseActivity : AppCompatActivity() {
    val firebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        initLanguage()
        initView()
        addEvent()
    }
    open fun setUserProperties(properties: String?) {
        try {
            properties?.let { firebaseAnalytics?.setUserProperty(it, properties) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initLanguage() {}

    protected abstract val layoutId: Int
    protected abstract fun initView()
    protected abstract fun addEvent()


}