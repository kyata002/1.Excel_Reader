package com.masterlibs.basestructure.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Handler
import com.docxmaster.docreader.base.BaseActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.masterlibs.basestructure.R
import com.wxiwei.office.utils.RealPathUtil


class SplashActivity(override val layoutId: Int = R.layout.activity_splash) : BaseActivity() {
    var internAds : InterstitialAd? = null
    var intent1 : Intent? = null
    override fun initView() {
        loadInternAds()
        intent1 = Intent(this, MainActivity::class.java)
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
            startActivity(intent1)
            finish()
            },
            5000)

    }

    private fun loadInternAds() {
        val requestAds = AdRequest.Builder().build()
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", requestAds, object :
            InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                internAds = null
            }

            override fun onAdLoaded(p0: InterstitialAd) {
                super.onAdLoaded(p0)
                internAds = p0
            }
        })

    }
    override fun addEvent() {
    }
}