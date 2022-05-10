package com.masterlibs.basestructure

import com.common.control.MyApplication
import com.masterlibs.basestructure.db.RoomDatabase

class App : MyApplication() {
    companion object {
        var instance: App? = null
        var database: RoomDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = RoomDatabase.getDatabase(instance)
    }


    override fun isPurchased(): Boolean {
        return BuildConfig.PURCHASED
    }

    override fun isShowAdsTest(): Boolean {
        return BuildConfig.DEBUG || BuildConfig.TEST_AD
    }

    override fun enableAdsResume(): Boolean {
        return false
    }

    override fun getOpenAppAdId(): String? {
        return null
    }
}