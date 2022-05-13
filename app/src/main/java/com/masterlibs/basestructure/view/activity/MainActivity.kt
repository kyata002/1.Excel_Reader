package com.masterlibs.basestructure.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.control.utils.PermissionUtils
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseActivity
import com.masterlibs.basestructure.App
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.utils.FileAdapter
import com.masterlibs.basestructure.utils.LoadFile
import com.masterlibs.basestructure.model.MyFile
import com.masterlibs.basestructure.view.dialog.FilterDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity() {
    private var fileList: java.util.ArrayList<MyFile> = ArrayList()
    var fileadapter: FileAdapter? = null

    @SuppressLint("RestrictedApi")
    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(this)
        rcvExcel.layoutManager = linearLayoutManager
        executeLoadFile()
        updateStatus()
        fileList = fileListTemp
        fileadapter = FileAdapter(fileList, this)
        rcvExcel.adapter = fileadapter
        btn_setting.setOnClickListener {
            val back = Intent(this, SettingActivity::class.java)
            startActivity(back)
        }
        btn_allfile.setOnClickListener {

            clickAllAfile()
            btn_favourite.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
            btn_allfile.setTypeface(null, Typeface.BOLD)
        }
        clear_bt.setOnClickListener{
            search_bar.setText("")
            clear_bt.setImageResource(0)
        }
        btn_favourite.setOnClickListener {
            fileListTempFavourite = App.database?.favoriteDAO()?.list as java.util.ArrayList<MyFile>
            when (FilterDialog.currentStatus) {
                0 -> {
                    fileadapter?.sortByNameAZ(fileListTempFavourite)
                }
                1 -> {
                    fileadapter?.sortBySize(fileListTempFavourite)
                }
                2 -> {
                    fileadapter?.sortByDate(fileListTempFavourite)
                }
            }
            btn_favourite.setBackgroundResource(R.drawable.ic_bg_btn_yes)
            btn_favourite.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            btn_allfile.setTypeface(null, Typeface.NORMAL)
            btn_favourite.setTextColor(Color.parseColor("#ffffff"))
            btn_allfile.setBackgroundResource(R.drawable.ic_bg_btn_no)
            btn_allfile.setTextColor(Color.parseColor("#000000"))
            Thread {
                fileList = fileListTempFavourite
                runOnUiThread {
                    fileadapter?.updateList(fileList)
                    updateStatus()
                }
            }.start()
        }
        sort_btn.setOnClickListener {
            FilterDialog.start(this, "sort_dialog", object : OnActionCallback {
                override fun callback(key: String?, vararg data: Any?) {
                    if (key == "by_name") {
                        fileadapter?.sortByNameAZ(fileList)!!
                    }
                    if (key == "by_size") {
                        fileadapter?.sortBySize(fileList)!!
                    }
                    if (key == "by_created_time") {
                        fileadapter?.sortByDate(fileList)!!
                    }
                    fileadapter?.updateList(fileList)
                }

            })
        }

        search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                fileadapter?.filter?.filter(p0)
                clear_bt.setImageResource(R.drawable.ic_btn_clear)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        initReceiver()

    }

    private fun clickAllAfile() {
        fileListTemp = getFileList()
        btn_allfile.setBackgroundResource(R.drawable.ic_bg_btn_yes)
        btn_allfile.setTextColor(Color.parseColor("#ffffff"))
        btn_favourite.setBackgroundResource(R.drawable.ic_bg_btn_no)
        btn_favourite.setTextColor(Color.parseColor("#000000"))
        when (FilterDialog.currentStatus) {
            0 -> {
                fileadapter?.sortByNameAZ(fileListTemp)
            }
            1 -> {
                fileadapter?.sortBySize(fileListTemp)
            }
            2 -> {
                fileadapter?.sortByDate(fileListTemp)
            }
        }
        Thread {
            fileList = Companion.fileListTemp
            runOnUiThread {
                fileadapter?.updateList(fileList)
                updateStatus()
            }
        }.start()
    }

    private fun updateStatus() {
        if (fileList.size == 0) {
            no_file.setImageResource(R.drawable.ic_no_file)
            no_result_search.setImageResource(0)

        } else {
            no_file.setImageResource(0)
            no_result_search.setImageResource(0)
        }
    }

    private fun initReceiver() {
        val filter = IntentFilter();
        filter.addAction(UPDATE_SEARCH_HAVE_RESULT)
        filter.addAction(UPDATE_SEARCH)
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val action = p1?.action
                if (action == UPDATE_SEARCH) {
                    no_result_search.setImageResource(R.drawable.ic_no_result_search)
                    no_file.setImageResource(0)
                } else if (action == UPDATE_SEARCH_HAVE_RESULT) {
                    no_result_search.setImageResource(0)
                }
            }
        }, filter)
    }

    companion object {

        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        const val RQC_REQUEST_PERMISSION_ANDROID_11 = 333
        val UPDATE_SEARCH = "update_search"
        val UPDATE_SEARCH_HAVE_RESULT = "have_result"
        var fileListTemp: java.util.ArrayList<MyFile> = ArrayList()
        var fileListTempFavourite: java.util.ArrayList<MyFile> = ArrayList()
    }

    private fun getFileList(): ArrayList<MyFile> {
        var typeList: ArrayList<String> = ArrayList()
        var mlist: ArrayList<MyFile> = ArrayList()
        typeList.add("xlsx")
        typeList.add("xls")
        typeList.add("xlsm")
        typeList.add("xlsb")
        typeList.add("xlam")
        typeList.forEach {
            val tempList = LoadFile.loadFile(this, it)
            tempList.forEach { it1 ->
                mlist.add(MyFile(it1.path))
            }
        }
        return mlist
    }

    private fun executeLoadFile() {
        if (checkPermission()) {
            clickAllAfile()
        } else {
            requestPermission()
        }

    }


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    PERMISSIONS_STORAGE,
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        executeLoadFile()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        executeLoadFile()
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