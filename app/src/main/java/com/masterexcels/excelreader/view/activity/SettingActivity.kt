package com.masterexcels.excelreader.view.activity

import android.widget.Toast
import com.common.control.dialog.RateAppDialog
import com.common.control.interfaces.RateCallback
import com.docxmaster.docreader.base.BaseActivity
import com.masterexcels.excelreader.BuildConfig
import com.masterexcels.excelreader.R
import com.masterexcels.excelreader.extentions.loadNative
import com.masterexcels.excelreader.utils.CommonUtils
import com.masterexcels.excelreader.utils.SharePreferenceUtils
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
        btn_setting_rate.setOnClickListener {
            showRateDialog(true)
        }
        btn_setting_back.setOnClickListener {
            finish()
        }
    }
    fun showRateDialog(isFinish: Boolean) {
        val dialog = RateAppDialog(this)
        dialog.setCallback(object : RateCallback {
            override fun onMaybeLater() {
                if (isFinish) {
                    SharePreferenceUtils.increaseCountRate(this@SettingActivity)
                    finishAffinity()
                }
            }

            override fun onSubmit(review: String?) {
                Toast.makeText(this@SettingActivity, R.string.thank_you, Toast.LENGTH_SHORT).show()
                SharePreferenceUtils.setRated(this@SettingActivity)
                if (isFinish) {
                    finishAffinity()
                }
            }

            override fun onRate() {
                CommonUtils.getInstance().rateApp(this@SettingActivity)
                SharePreferenceUtils.setRated(this@SettingActivity)
            }
        })
        dialog.show()
    }
}