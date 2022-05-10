package com.documentmaster.documentscan.extention

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd

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
//        BuildConfig.inter_orc -> {
//            AdCache.interOrc = inter
//        }
//        BuildConfig.inter_open_doc -> {
//            AdCache.interOpenDoc = inter
//        }
//        BuildConfig.inter_save_doc -> {
//            AdCache.interSaveDoc = inter
//        }
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
//        BuildConfig.inter_orc -> {
//            interstitialAd = AdCache.interOrc
//        }
//        BuildConfig.inter_open_doc -> {
//            interstitialAd = AdCache.interOpenDoc
//        }
//        BuildConfig.inter_save_doc -> {
//            interstitialAd = AdCache.interSaveDoc
//        }
    }


    AdmobManager.getInstance().showInterstitial(
        this as Activity,
        interstitialAd, object : AdCallback() {
            override fun onAdClosed() {
                loadInterAd(id)
            }

            override fun onAdFailedToLoad(i: LoadAdError?) {
                storeInter(id, null)
            }
        }
    )
}
