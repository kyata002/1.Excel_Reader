package com.masterlibs.basestructure.view.dialog

import android.content.Context
import android.content.Intent
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import kotlinx.android.synthetic.main.dialog_filter.*

class FilterDialog(override val layoutId: Int = R.layout.dialog_filter) : BaseActivity() {
    companion object {
        var callback: OnActionCallback? = null

        fun start(context: Context, onActionCallback1: String, onActionCallback: OnActionCallback) {
            callback = onActionCallback
            context.startActivity(Intent(context, FilterDialog::class.java))
        }
        const val SORT_NAME= 0
        const val SORT_SIZE= 1
        const val SORT_CREATED_TIMED = 2
        var currentStatus : Int = -1
    }
    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }
    override fun initView() {
        radio_filter.setOnClickListener {

        }
        border_filter.setOnClickListener {
            finish()
        }
        when(currentStatus){
            0 -> {
                by_name.setButtonDrawable(R.drawable.ic_selected)
                by_size.setButtonDrawable(R.drawable.ic_unselect)
                by_created_time.setButtonDrawable(R.drawable.ic_unselect)
            }
            1 -> {
                by_name.setButtonDrawable(R.drawable.ic_unselect)
                by_size.setButtonDrawable(R.drawable.ic_selected)
                by_created_time.setButtonDrawable(R.drawable.ic_unselect)
            }
            2 -> {
                by_name.setButtonDrawable(R.drawable.ic_unselect)
                by_size.setButtonDrawable(R.drawable.ic_unselect)
                by_created_time.setButtonDrawable(R.drawable.ic_selected)
            }
        }
    }

    override fun addEvent() {
        by_name.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                callback?.callback("by_name")
                currentStatus = SORT_NAME
                finish()
            }

        }
        by_size.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                callback?.callback("by_size")
                currentStatus = SORT_SIZE
                finish()
            }
        }
        by_created_time.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                callback?.callback("by_created_time")
                currentStatus = SORT_CREATED_TIMED
                finish()
            }
        }

    }
}