package com.masterlibs.basestructure.view.dialog

import android.content.Context
import android.content.Intent
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import kotlinx.android.synthetic.main.dialog_delete.*
import org.apache.http.conn.ssl.StrictHostnameVerifier
import java.io.File

class DeleteDialog(override val layoutId: Int = R.layout.dialog_delete) : BaseActivity() {

    companion object {
        var callback: OnActionCallback? = null
        var nameFile:String = ""
        fun start(context: Context, path: String, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            context.startActivity(Intent(context, DeleteDialog::class.java))
            nameFile = File(path).name
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

    override fun initView() {
        tvNameDelete.text =  nameFile
        border_delete.setOnClickListener {
            finish()
        }
        border_delete_dialog.setOnClickListener {

        }

    }

    override fun addEvent() {
        bt_delete.setOnClickListener {
            callback?.callback("delete")
            finish()
        }

        bt_cancel.setOnClickListener {
            finish()
        }
    }
}