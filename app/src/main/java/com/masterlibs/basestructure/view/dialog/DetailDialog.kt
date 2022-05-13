package com.masterlibs.basestructure.view.dialog

import android.content.Context
import android.content.Intent
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import kotlinx.android.synthetic.main.dialog_detail.*

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DetailDialog(override val layoutId: Int = R.layout.dialog_detail) : BaseActivity() {

    companion object {
        var callback: OnActionCallback? = null

        fun start(context: Context, pathFile:String, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            val intent = Intent(context, DetailDialog::class.java)
            val file = File(pathFile)
            var sizeOfFile = ((File(pathFile).length() / (1024.0)).toFloat())
            var datefile: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            intent.putExtra("name",file.name )
            intent.putExtra("path",file.path)
            intent.putExtra("date", datefile.format(Date(File(pathFile).lastModified())))
            intent.putExtra("size","%.2f KB".format(sizeOfFile))
            context.startActivity(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

    override fun initView() {
        val name = intent.getStringExtra("name")
        val path = intent.getStringExtra("path")
        val date = intent.getStringExtra("date")
        val size = intent.getStringExtra("size")
        tvNameFile.text = name
        tvPathFile.text = path
        tvDateFile.text = date
        tvSizeFile.text = size
        border_detail.setOnClickListener {

        }
        border_dialog.setOnClickListener {
            finish()
        }

    }

    override fun addEvent() {

        detail_btn.setOnClickListener {
            callback?.callback("ok")
            finish()
        }
    }
}