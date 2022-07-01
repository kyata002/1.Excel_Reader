package com.masterexcels.excelreader

import com.common.control.MyApplication
import com.common.control.manager.AppOpenManager
import com.masterexcels.excelreader.db.RoomDatabase
import com.masterexcels.excelreader.view.activity.SplashActivity

class App : MyApplication() {
    companion object {
        var instance: App? = null
        var database: RoomDatabase? = null
    }

    override fun onApplicationCreate() {
        instance = this
        database = RoomDatabase.getDatabase(instance)
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity::class.java)
    }

    override fun hasAds(): Boolean {
        return true
    }

    override fun isShowDialogLoadingAd(): Boolean {
       return false
    }

    override fun isShowAdsTest(): Boolean {
        return BuildConfig.DEBUG || BuildConfig.TEST_AD
    }

    override fun enableAdsResume(): Boolean {
        return true
    }

    override fun getOpenAppAdId(): String? {
        return BuildConfig.open_app
    }
}