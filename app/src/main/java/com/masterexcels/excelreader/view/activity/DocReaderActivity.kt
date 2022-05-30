package com.masterexcels.excelreader.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.documentmaster.documentscan.OnActionCallback
import com.documentmaster.documentscan.extention.setUserProperty
import com.github.barteksc.pdfviewer.source.FileSource
import com.masterexcels.excelreader.BuildConfig
import com.masterexcels.excelreader.R
import com.masterexcels.excelreader.base.BaseDocActivity
import com.masterexcels.excelreader.extentions.loadBanner
import com.masterexcels.excelreader.utils.AppUtils
import com.masterexcels.excelreader.view.dialog.PasswordDialog
import com.shockwave.pdfium.PdfiumCore
import com.wxiwei.office.constant.MainConstant
import kotlinx.android.synthetic.main.activity_reader.*
import java.io.File
import java.io.IOException


class DocReaderActivity : BaseDocActivity() {
    private var path: String? = null

    @SuppressLint("SetTextI18n")
    override fun pageChanged(page: Int, pageCount: Int) {
        tv_page.text = "$page/$pageCount"
    }

    override fun errorLoadPdf(t: Throwable?) {
        val passDialog: PasswordDialog = PasswordDialog.newInstance()
        passDialog.setCallback(object : OnActionCallback {
            override fun callback(key: String?, vararg data: Any?) {
                try {
                    if (key != null && key == "cancel") {
                        finish()
                        return
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val pass = data[0] as String?
                if (!passwordCorrect(pass)) {
                    Toast.makeText(
                        this@DocReaderActivity,
                        "The password you entered is incorrect",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                readPdfFile(pass)
                passDialog.dismiss()
            }
        })
        passDialog.show(supportFragmentManager, null)
    }

    private fun passwordCorrect(pass: String?): Boolean {
        val docSource = FileSource(File(path))
        try {
            val pdfDocument = docSource.createDocument(this, PdfiumCore(this), pass)
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_reader
    }

    @SuppressLint("WrongViewCast")
    override fun getFrameLayoutDoc(): FrameLayout {
        return findViewById(R.id.fr_doc)
    }

    override fun initView() {
        path = intent.getStringExtra(MainConstant.INTENT_FILED_FILE_PATH)
        name_file_read.text = File(path!!).name
        loadBanner(BuildConfig.banner_doc)

        if (path!!.toLowerCase().endsWith(".xls") || path!!.toLowerCase().endsWith(".xlsx")) {
            tv_page.visibility = View.GONE
        }
    }

    override fun addEvent() {
        btn_back.setOnClickListener {
            finish()
        }
        btn_share.setOnClickListener {
            AppUtils.sharefile(File(path), this)
            setUserProperty("CLICK_Read_Share")
        }
    }


    companion object {
        fun start(context: Context, path: String?) {
            val starter = Intent(context, DocReaderActivity::class.java)
            starter.putExtra(MainConstant.INTENT_FILED_FILE_PATH, path)
            context.startActivity(starter)
        }
    }
}