package com.masterlibs.basestructure.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Handler
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import com.wxiwei.office.utils.RealPathUtil


class SplashActivity(override val layoutId: Int = R.layout.activity_splash) : BaseActivity() {
    override fun initView() {
        Handler().postDelayed(Runnable {
            val intent = intent
            val fileUri: Uri?
            if ("android.intent.action.VIEW" == intent.action) {
                val data: Uri? = intent.data
                fileUri = data
                if (data != null) {
                    val sb = StringBuilder()
                    sb.append(" fileUri = ")
                    sb.append(fileUri)
                    var filePath = RealPathUtil.getPathFromData(this, fileUri)
                    DocReaderActivity.start(this, filePath)
                    finish()
                }
                return@Runnable
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()},
            5000)

    }

    override fun addEvent() {
    }
}