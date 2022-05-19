package com.masterexcels.excelreader.view.dialog

import android.content.Context
import android.content.Intent
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseActivity
import com.masterexcels.excelreader.R
import kotlinx.android.synthetic.main.dialog_permission.*

class PermissionDialog(override val layoutId: Int = R.layout.dialog_permission) : BaseActivity() {
    companion object {
        var callback: OnActionCallback? = null
        fun start(context: Context, path: String, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            context.startActivity(Intent(context, PermissionDialog::class.java))
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }
    override fun initView() {

    }

    override fun addEvent() {
        btn_deny.setOnClickListener {
            callback?.callback("deny")
            finish()
        }
        btn_allow.setOnClickListener {
            callback?.callback("allow")
            finish()
        }
    }
}