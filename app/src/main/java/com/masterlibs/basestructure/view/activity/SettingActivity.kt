package com.masterlibs.basestructure.view.activity

import android.content.Intent
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity(override val layoutId: Int = R.layout.activity_setting) : BaseActivity() {
    override fun initView() {
        btn_setting_back.setOnClickListener {
            finish()
        }
    }

    override fun addEvent() {
    }
}