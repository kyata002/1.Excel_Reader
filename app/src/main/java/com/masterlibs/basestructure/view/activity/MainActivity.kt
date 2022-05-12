package com.masterlibs.basestructure.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
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
    var fileadapter: FileAdapter? = null

    @SuppressLint("RestrictedApi")
    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(this)
        rcvExcel.layoutManager = linearLayoutManager
        if(getFileList().size!=0){
            executeLoadFile()
            rcvExcel.adapter = fileadapter
        }else{
            executeLoadFile()
            rcvExcel.adapter = fileadapter
            no_file.setImageResource(R.drawable.ic_no_file)
            no_result_search.setImageResource(0)
        }
        btn_setting.setOnClickListener {
            val back = Intent(this, SettingActivity::class.java)
            startActivity(back)
        }
        Thread(Runnable {
            btn_allfile.setOnClickListener {
                btn_allfile.setBackgroundResource(R.drawable.ic_bg_btn_yes)
                btn_allfile.setTextColor(Color.parseColor("#ffffff"))
                btn_favourite.setBackgroundResource(R.drawable.ic_bg_btn_no)
                btn_favourite.setTextColor(Color.parseColor("#000000"))
                if(getFileList().size==0){
                    no_file.setImageResource(R.drawable.ic_no_file)
                    no_result_search.setImageResource(0)
                    executeLoadFile()
                    rcvExcel.adapter = fileadapter
                    Thread.sleep(10)
                }else{
                    executeLoadFile()
                    rcvExcel.adapter = fileadapter
                    no_file.setImageResource(0)
                    no_result_search.setImageResource(0)
                    Thread.sleep(10)
                }
            }
        }).start()

        Thread(Runnable {
            btn_favourite.setOnClickListener {
                btn_favourite.setBackgroundResource(R.drawable.ic_bg_btn_yes)
                btn_favourite.setTextColor(Color.parseColor("#ffffff"))
                btn_allfile.setBackgroundResource(R.drawable.ic_bg_btn_no)
                btn_allfile.setTextColor(Color.parseColor("#000000"))
                if(App.database?.favoriteDAO()?.list?.size==0){
                    no_file.setImageResource(R.drawable.ic_no_file)
                    no_result_search.setImageResource(0)
                    fileadapter =
                        FileAdapter(App.database?.favoriteDAO()?.list as ArrayList<MyFile>?, this)
                    rcvExcel.adapter = fileadapter
                }else{
                    fileadapter =
                        FileAdapter(App.database?.favoriteDAO()?.list as ArrayList<MyFile>?, this)
                    rcvExcel.adapter = fileadapter
                    no_file.setImageResource(0)
                    no_result_search.setImageResource(0)
                    Thread.sleep(10)
                }
            }
        }).start()
        sort_btn.setOnClickListener {
            FilterDialog.start(this, "sort_dialog", object : OnActionCallback{
                override fun callback(key: String?, vararg data: Any?) {
                    if (key == "by_name"){
                        fileadapter?.sortByNameAZ()
                    }
                    if (key == "by_size"){
                        fileadapter?.sortBySize()
                    }
                    if (key == "by_created_time"){
                        fileadapter?.sortByDate()
                    }
                }

            })
        }
        search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                fileadapter?.filter?.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        initReceiver()

    }

    private fun initReceiver() {
        val filter = IntentFilter();
        filter.addAction(UPDATE_SEARCH_HAVE_RESULT)
        filter.addAction(UPDATE_SEARCH)
        registerReceiver(object :BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                val action=p1?.action
                if (action == UPDATE_SEARCH){
                    no_result_search.setImageResource(R.drawable.ic_no_result_search)
                    no_file.setImageResource(0)
                }else if(action == UPDATE_SEARCH_HAVE_RESULT){
                    no_result_search.setImageResource(0)

                }
            }
        },filter)
    }

    companion object {

        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        const val RQC_REQUEST_PERMISSION_ANDROID_11 = 333
        val UPDATE_SEARCH = "update_search"
        val UPDATE_SEARCH_HAVE_RESULT = "have_result"

    }

    private fun getFileList(): ArrayList<MyFile> {
        var type:String
        var typeList : ArrayList<String> = ArrayList()
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
            fileadapter = FileAdapter(getFileList(), this)
            return
        }
        requestPermission()
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