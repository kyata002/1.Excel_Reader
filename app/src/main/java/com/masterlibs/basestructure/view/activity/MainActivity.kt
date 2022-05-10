package com.masterlibs.basestructure.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.control.utils.PermissionUtils
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.utils.FileAdapter
import com.masterlibs.basestructure.utils.LoadFile
import com.masterlibs.basestructure.utils.MyFile
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_file.*

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity() {
    var mlist: ArrayList<MyFile> = ArrayList()
    var fileadapter:FileAdapter? = null
    @SuppressLint("RestrictedApi")
    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(this)
        rcvExcel.layoutManager = linearLayoutManager
        mlist = getFileList("xlsx")
        btn_allfile.setOnClickListener {
            executeLoadFile();
            rcvExcel.adapter = fileadapter
            btn_favourite.background(R.drawable.ic_btn_nofavourite)
            btn_allfile.background(R.drawable.ic_btn_allfile)
        }
        btn_favourite.setOnClickListener{
            btn_allfile.background(R.drawable.ic_btn_noallfile)
            btn_favourite.background(R.drawable.ic_btn_favourite)
        }


    }
    companion object {
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        const val RQC_REQUEST_PERMISSION_ANDROID_11 = 333

    }
    private fun getFileList( type: String): ArrayList<MyFile> {
        if (type == "xlsx"){
            val tempList = LoadFile.loadFile(this, type)
            tempList.forEach {
                mlist.add(MyFile(it.path))
            }
        }
        return mlist
    }

    private fun executeLoadFile() {
        if (checkPermission()) {
            fileadapter = FileAdapter(mlist, this)
            return
        }
        requestPermission()
    }


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS_STORAGE,
                    PermissionUtils.RQC_REQUEST_PERMISSION_ANDROID_BELOW
                )
            }
            return
        }
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data = Uri.parse(String.format("package:%s", this.packageName))
            this.startActivityForResult(intent, RQC_REQUEST_PERMISSION_ANDROID_11)
        } catch (e: Exception) {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            this.startActivityForResult(intent, RQC_REQUEST_PERMISSION_ANDROID_11)
        }
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(PERMISSIONS_STORAGE[0]) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(PERMISSIONS_STORAGE[1]) == PackageManager.PERMISSION_GRANTED
            }
            return true
        }
        return Environment.isExternalStorageManager()
    }

    override fun addEvent() {
    }
}

private fun ImageButton.background(icBtnNofavourite: Int) {

}
