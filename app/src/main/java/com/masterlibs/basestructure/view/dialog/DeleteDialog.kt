package com.masterlibs.basestructure.view.dialog

import android.content.Context
import android.content.Intent
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import kotlinx.android.synthetic.main.dialog_delete.*

class DeleteDialog(override val layoutId: Int = R.layout.dialog_delete) : BaseActivity() {

    companion object {
        var callback: OnActionCallback? = null

        fun start(context: Context, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            context.startActivity(Intent(context, DeleteDialog::class.java))
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

    override fun initView() {

    }

    override fun addEvent() {
        bt_delete.setOnClickListener {
            callback?.callback(null)
            finish()
        }

        bt_cancel.setOnClickListener {
            finish()
        }
    }
}