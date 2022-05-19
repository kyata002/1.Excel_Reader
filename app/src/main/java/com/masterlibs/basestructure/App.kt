package com.masterlibs.basestructure

import com.common.control.MyApplication
import com.common.control.manager.AppOpenManager
import com.masterlibs.basestructure.db.RoomDatabase
import com.masterlibs.basestructure.view.activity.SplashActivity

class App : MyApplication() {
    companion object {
        var instance: App? = null
        var database: RoomDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = RoomDatabase.getDatabase(instance)
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity::class.java)
    }


    override fun isPurchased(): Boolean {
        return BuildConfig.PURCHASED
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