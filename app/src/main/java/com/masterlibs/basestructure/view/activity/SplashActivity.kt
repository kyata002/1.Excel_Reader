package com.masterlibs.basestructure.view.activity

import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R

class SplashActivity(override val layoutId: Int = R.layout.activity_splash) : BaseActivity() {
    override fun initView() {
        Handler().postDelayed(Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()},
            5000)

    }

    override fun addEvent() {
    }
}