package com.masterlibs.basestructure.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.control.utils.PermissionUtils
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.utils.FileAdapter
import com.masterlibs.basestructure.utils.LoadFile
import com.masterlibs.basestructure.utils.MyFile
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity() {
    var fileadapter:FileAdapter? = null
    @SuppressLint("RestrictedApi")
    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(this)
        rcvExcel.layoutManager = linearLayoutManager
        executeLoadFile()
        rcvExcel.adapter = fileadapter
        sort_btn.setOnClickListener {
            val pm = PopupMenu(this, sort_btn)
            pm.menuInflater.inflate(R.menu.popup_sort, pm.menu)
            pm.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.a_to_z -> fileadapter?.sortByNameAZ()
                    R.id.z_to_a -> fileadapter?.sortByNameZA()
                    R.id.by_size -> fileadapter?.sortBySize()
                    R.id.by_date -> fileadapter?.sortByDate()
                }
                true
            })
//            pm.show()
            val ph = MenuPopupHelper(this, pm.menu as MenuBuilder, sort_btn)
            ph.setForceShowIcon(true)
            ph.show()
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
        var mlist : ArrayList<MyFile> = ArrayList()
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
            fileadapter = FileAdapter(getFileList("xlsx"), this)
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