package com.masterlibs.basestructure.view.popup

import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowManager
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R

class PopupMenu(override val layoutId: Int = R.layout.popup_menu_more_layout) : BaseActivity() {
    override fun initView() {
        var dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        var width = dm.widthPixels
        var height = dm.heightPixels

        window.setLayout((width*0.5f).toInt(), (height*0.5f).toInt())

        var params : WindowManager.LayoutParams = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = -20
        window.attributes = params
    }

    override fun addEvent() {
    }
}