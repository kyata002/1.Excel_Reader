package com.masterlibs.basestructure

import com.google.android.gms.ads.interstitial.InterstitialAd

class AdCache {
    companion object {
        var interOpenDoc: InterstitialAd? = null
        var interSaveDoc: InterstitialAd? = null
        var interOrc: InterstitialAd? = null
    }
}