package com.documentmaster.documentscan.extention

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.common.control.dialog.RateAppDialog
import com.common.control.interfaces.RateCallback
import com.common.control.utils.CommonUtils
import com.common.control.utils.RatePrefUtils
import com.google.firebase.analytics.FirebaseAnalytics

fun View.disableFocus() {
    this.isFocusableInTouchMode = false
    this.isFocusable = false
}

fun View.enableFocus() {
    this.isFocusableInTouchMode = true
    this.isFocusable = true
}

fun View.showKeyboard(context: Context) {
    this.requestFocus()
    val imm: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun Context.logEvent(eventName: String) {
    val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    val bundle = Bundle()
    mFirebaseAnalytics.logEvent(eventName, bundle)
}

fun Context.setUserProperty(param: String) {
    val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    mFirebaseAnalytics.setUserProperty(param, param)
}


fun Context.showRate(isFinish: Boolean) {
    val dialog = RateAppDialog(this)
    dialog.setCallback(object : RateCallback {
        override fun onMaybeLater() {
            if (isFinish) {
                RatePrefUtils.increaseCountRate(this@showRate)
                (this@showRate as Activity).finish()
            }
        }

        override fun onSubmit(review: String) {
            Toast.makeText(
                this@showRate,
                "Thank you for reviewing...",
                Toast.LENGTH_SHORT
            ).show()
            RatePrefUtils.setRated(this@showRate)
            if (isFinish) {
                (this@showRate as Activity).finish()
            }
        }

        override fun onRate() {
            CommonUtils.getInstance().rateApp(this@showRate)
            RatePrefUtils.setRated(this@showRate)
        }
    })
    dialog.show()
}