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
        //        binding.edtName.setSelectAllOnFocus(true);

//        binding.edtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    binding.edtName.setSelection(0, binding.edtName.getText().toString().length());
//                }
//            }
//        });
//        new Handler().postDelayed(() -> , 1000);
        edit_name.setSelection(0, file.name.length)
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

        bt_cancel.setOnClickListener {
            finish()
        }
    }
}