package com.masterlibs.basestructure.view.activity

import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.BuildConfig
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.extentions.loadNative
import com.masterlibs.basestructure.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity(override val layoutId: Int = R.layout.activity_setting) : BaseActivity() {
    override fun initView() {
        loadNative(BuildConfig.native_setting, fr_ad)
    }

    override fun addEvent() {
        btn_setting_share.setOnClickListener {
            CommonUtils.getInstance().shareApp(this)
        }
        btn_setting_feed.setOnClickListener {
            CommonUtils.getInstance().support(this)
        }
        btn_setting_pri.setOnClickListener {
            CommonUtils.getInstance().showPolicy(this)
        }
        btn_setting_back.setOnClickListener {
            finish()
        }
    }
}