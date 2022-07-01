package com.masterexcels.excelreader.view.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.documentmaster.documentscan.extention.setUserProperty
import com.docxmaster.docreader.base.BaseActivity
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.masterexcels.excelreader.BuildConfig
import com.masterexcels.excelreader.R
import com.wxiwei.office.utils.RealPathUtil


@SuppressLint("CustomSplashScreen")
class SplashActivity(override val layoutId: Int = R.layout.activity_splash) : BaseActivity() {
    override fun initView() {
        Handler().postDelayed(
            {
                val interId =
                    if ("android.intent.action.VIEW" == intent.action) BuildConfig.inter_open_other_app else BuildConfig.inter_splash
                AdmobManager.getInstance()
                    .loadInterAds(this, interId, object : AdCallback() {
                        override fun interCallback(interstitialAd: InterstitialAd?) {
                            super.interCallback(interstitialAd)
                            AdmobManager.getInstance()
                                .showInterstitial(this@SplashActivity, interstitialAd, this)
                        }

                        override fun onAdClosed() {
                            super.onAdClosed()
                            startMain()
                        }

                        override fun onAdFailedToLoad(i: LoadAdError) {
                            super.onAdFailedToLoad(i)
                            startMain()
                        }
                    })
            },
            2000
        )
    }

    private fun startMain() {
        if ("android.intent.action.VIEW" == intent.action) {
            setUserProperty("OPEN_File_FromDevice")
            val fileUri: Uri?
            val data: Uri? = intent.data
            fileUri = data
            if (data != null) {
                var path = RealPathUtil.getPathFromData(this, fileUri)
                if (TextUtils.isEmpty(path)) {
                    path = intent.dataString
                    val indexOf = path!!.indexOf(":")
                    if (indexOf > 0) {
                        path = path!!.substring(indexOf + 3)
                    }
                    path = Uri.decode(path)
                }
                if (!TextUtils.isEmpty(path) && path!!.contains("/raw:")) {
                    val str = path
                    path = str!!.substring(str.indexOf("/raw:") + 5)
                }
                DocReaderActivity.start(this, path)
            }
            finish()
            return
        }
        MainActivity.start(this)
        finish()
    }

    override fun addEvent() {
    }
}