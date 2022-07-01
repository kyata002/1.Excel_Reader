package com.masterexcels.excelreader.extentions

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.masterexcels.excelreader.AdCache
import com.masterexcels.excelreader.BuildConfig

fun Context.loadInterAd(id: String) {
    Log.d("android_log", ":load $id");
    AdmobManager.getInstance().loadInterAds(this as Activity, id,
        object : AdCallback() {
            override fun interCallback(inter: InterstitialAd) {
                super.interCallback(inter)
                storeInter(id, inter)
            }

            override fun onAdFailedToLoad(i: LoadAdError) {
                storeInter(id, null)
            }
        })
}

private fun storeInter(id: String, inter: InterstitialAd?) {
    when (id) {
        BuildConfig.inter_read_file -> {
            AdCache.interReadFile = inter
        }
    }
}

fun Context.loadBanner(id: String) {
    AdmobManager.getInstance().loadBanner(this as Activity, id)
}


fun Context.loadNative(id: String, view: FrameLayout) {
    AdmobManager.getInstance()
        .loadNative(this, id, view)
}


fun Context.showInterAd(id: String) {
    Log.d("android_log", ":show $id");
    var interstitialAd: InterstitialAd? = null
    when (id) {
        BuildConfig.inter_read_file -> {
            interstitialAd = AdCache.interReadFile
        }
    }

    AdmobManager.getInstance().showInterstitial(
        this as Activity,
        interstitialAd, object : AdCallback() {
            override fun onAdClosed() {
                loadInterAd(id)
            }

            override fun onAdFailedToLoad(i: LoadAdError) {
                storeInter(id, null)
            }
        }
    )
}
