package com.masterlibs.basestructure.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.FrameLayout
import com.masterlibs.basestructure.BuildConfig
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.extentions.loadBanner
import com.masterlibs.basestructure.utils.AppUtils
import com.wxiwei.office.constant.MainConstant
import com.wxiwei.office.officereader.BaseDocActivity
import kotlinx.android.synthetic.main.activity_reader.*
import java.io.File


class DocReaderActivity : BaseDocActivity() {
    private var path: String? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_reader
    }

    @SuppressLint("WrongViewCast")
    override fun getFrameLayoutDoc(): FrameLayout {
        return findViewById(R.id.app_frame)
    }

    override fun initView() {
        path = intent.getStringExtra(MainConstant.INTENT_FILED_FILE_PATH)
        name_file_read.text = File(path!!).name
        loadBanner(BuildConfig.banner_doc)
    }

    override fun addEvent() {
        btn_back.setOnClickListener {
            finish()
        }
        btn_share.setOnClickListener {
            AppUtils.sharefile(File(path), this)

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