package com.masterexcels.excelreader.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import com.documentmaster.documentscan.extention.setUserProperty
import com.masterexcels.excelreader.BuildConfig
import com.masterexcels.excelreader.R
import com.masterexcels.excelreader.extentions.loadBanner
import com.masterexcels.excelreader.utils.AppUtils
import com.wxiwei.office.constant.MainConstant
import com.wxiwei.office.officereader.BaseDocActivity
import kotlinx.android.synthetic.main.activity_reader.*
import java.io.File


class DocReaderActivity : BaseDocActivity() {
    private var path: String? = null
    @SuppressLint("SetTextI18n")
    override fun pageChanged(page: Int, pageCount: Int) {
        tv_page.text = "$page/$pageCount"
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