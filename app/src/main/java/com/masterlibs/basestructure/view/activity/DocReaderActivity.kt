package com.masterlibs.basestructure.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.FrameLayout
import com.masterlibs.basestructure.R
import com.wxiwei.office.constant.MainConstant
import com.wxiwei.office.officereader.BaseDocActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_reader.*
import java.io.File

class DocReaderActivity : BaseDocActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_reader
    }

    @SuppressLint("WrongViewCast")
    override fun getFrameLayoutDoc(): FrameLayout {
        return findViewById(R.id.app_frame)
    }

    override fun initView() {
        //val title = findViewById<TextView>(R.id.title)
        val path = intent.getStringExtra(MainConstant.INTENT_FILED_FILE_PATH)
        name_file_read.text = File(path).name
        btn_back.setOnClickListener {
            val back = Intent(this, MainActivity::class.java)
            startActivity(back)
        }
    }

    override fun addEvent() {
    }


    companion object {
        fun start(context: Context, path: String?) {
            val starter = Intent(context, DocReaderActivity::class.java)
            starter.putExtra(MainConstant.INTENT_FILED_FILE_PATH, path)
            context.startActivity(starter)
        }
    }
}