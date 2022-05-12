package com.masterlibs.basestructure.view.dialog

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import kotlinx.android.synthetic.main.dialog_rename.*
import java.io.File

class RenameDialog(override val layoutId: Int = R.layout.dialog_rename) : BaseActivity() {
    private var extension: String? = null

    companion object {
        var callback: OnActionCallback? = null

        fun start(context: Context, path: String, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            val intent = Intent(context, RenameDialog::class.java)
            intent.putExtra("data", path)
            context.startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

    override fun initView() {
        val path = intent.getStringExtra("data")
        if (path == null) {
            finish()
            return
        }
        val file = File(path)
        if (!file.isDirectory) {
            extension = ".pdf"
        }
        edit_name.setText(file.name)
        if(!edit_name.text.isNullOrEmpty()){
                clear_bt_reanme.setImageResource(R.drawable.ic_clear_rename)
        }else{
            clear_bt_reanme.setImageResource(0)
        }
        edit_name.setSelection(0, file.name.length)
        rename_border.setOnClickListener {
            finish()
        }
        rename_dialog.setOnClickListener {

        }
    }

    override fun addEvent() {
        bt_save.setOnClickListener {
            val text = edit_name.text.toString().trim()
            if (TextUtils.isEmpty(text)) {
                edit_name.error = "Error"
                return@setOnClickListener
            }
            if (extension == null || text.endsWith(extension!!)) {
                callback?.callback(null, text)
                finish()
                return@setOnClickListener
            }
            callback?.callback(null, (text + extension))
            finish()
        }
        clear_bt_reanme.setOnClickListener {
            clear_bt_reanme.setImageResource(0)
            edit_name.setText("")
        }

        bt_cancel.setOnClickListener {
            finish()
        }
    }
}