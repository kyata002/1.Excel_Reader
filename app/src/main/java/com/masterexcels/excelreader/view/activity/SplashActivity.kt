package com.masterexcels.excelreader.view.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.documentmaster.documentscan.extention.setUserProperty
import com.docxmaster.docreader.base.BaseActivity
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.masterexcels.excelreader.App
import com.masterexcels.excelreader.BuildConfig
import com.masterexcels.excelreader.R
import com.masterexcels.excelreader.model.FileRecent
import com.masterexcels.excelreader.model.MyFile
import com.masterexcels.excelreader.utils.LoadFile
import com.wxiwei.office.utils.RealPathUtil
import java.io.File


@SuppressLint("CustomSplashScreen")
class SplashActivity(override val layoutId: Int = R.layout.activity_splash) : BaseActivity() {
    var check = false
    override fun initView() {
        Handler().postDelayed(
            {
                val interId =
                    if ("android.intent.action.VIEW" == intent.action)
                        BuildConfig.inter_open_other_app
                    else
                        BuildConfig.inter_splash
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
            200
        )
    }

    private fun startMain() {
        if ("android.intent.action.VIEW" == intent.action) {
            setUserProperty("OPEN_File_FromDevice")
            Toast.makeText(this, "OPEN_File_FromDevice", Toast.LENGTH_SHORT).show()
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
                MainActivity.start(this)
                DocReaderActivity.start(this, path)
                val typeList: ArrayList<String> = ArrayList()
                typeList.add("xlsx")
                typeList.add("xls")
                typeList.add("xlsm")
                typeList.add("xlsb")
                typeList.add("xlam")
                typeList.add("csv")
                typeList.forEach {
                    if(File(path).name.endsWith(it)){
                        check=true
                    }
                }
                if(check){
                    val hasFile = App.database?.recentDao()?.hasFile(path)
                    if (hasFile == true) {
                        App.database?.recentDao()?.delete(path)
                        App.database?.recentDao()?.add(FileRecent(path))
                    }else{
                        App.database?.recentDao()?.add(FileRecent(path))
                    }
                    check=false
                }
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