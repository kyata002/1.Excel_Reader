package com.masterexcels.excelreader.view.activity

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
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.control.dialog.RateAppDialog
import com.common.control.interfaces.RateCallback
import com.common.control.utils.PermissionUtils
import com.documentmaster.documentscan.OnActionCallback
import com.documentmaster.documentscan.extention.hide
import com.documentmaster.documentscan.extention.setUserProperty
import com.documentmaster.documentscan.extention.show
import com.docxmaster.docreader.base.BaseActivity
import com.masterexcels.excelreader.AdCache
import com.masterexcels.excelreader.App
import com.masterexcels.excelreader.BuildConfig
import com.masterexcels.excelreader.R
import com.masterexcels.excelreader.extentions.loadInterAd
import com.masterexcels.excelreader.extentions.loadNative
import com.masterexcels.excelreader.model.FileRecent
import com.masterexcels.excelreader.model.MyFile
import com.masterexcels.excelreader.utils.CommonUtils
import com.masterexcels.excelreader.utils.LoadFile
import com.masterexcels.excelreader.utils.SharePreferenceUtils
import com.masterexcels.excelreader.view.adapter.FileAdapter
import com.masterexcels.excelreader.view.dialog.FilterDialog
import com.masterexcels.excelreader.view.dialog.PermissionDialog
import com.pdfreaderdreamw.pdfreader.view.widget.CustomEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity() {
    private var fileList: java.util.ArrayList<MyFile> = ArrayList()
    private var fileAdapter: FileAdapter? = null
    var status = 1
    private val dm = DisplayMetrics()

    @SuppressLint("RestrictedApi")
    override fun initView() {
        windowManager.defaultDisplay.getMetrics(dm)
        width = dm.xdpi.roundToInt()
        height = dm.ydpi.roundToInt()
        cancelSearch()
        btn_favourite.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
        val linearLayoutManager = LinearLayoutManager(this)
        rcvExcel.layoutManager = linearLayoutManager
        executeLoadFile()
        updateStatus(status)
        fileList = fileListTemp
        fileAdapter = FileAdapter(
            fileList  /* = java.util.ArrayList<com.masterexcels.excelreader.model.FileRecent> */,
            this
        )
        rcvExcel.adapter = fileAdapter

        initReceiver()
        loadNative(BuildConfig.native_home, fr_ad)

    }

    @SuppressLint("SetTextI18n")
    override fun addEvent() {
        btn_setting.setOnClickListener {
            val back = Intent(this, SettingActivity::class.java)
            startActivity(back)
            setUserProperty("CLICK_Main_Settings")
            Toast.makeText(this, "CLICK_Main_Settings", Toast.LENGTH_SHORT).show()
        }
        btn_allfile.setOnClickListener {
            clickAllAfile()
            btn_favourite.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
            btn_recent.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
            btn_allfile.setTypeface(null, Typeface.BOLD)
        }

        btn_favourite.setOnClickListener {
            val status = 2
            fileListTempFavourite = App.database?.favoriteDAO()?.list as java.util.ArrayList<MyFile>
            when (FilterDialog.currentStatus) {
                0 -> {
                    fileAdapter?.sortByNameAZ(fileListTempFavourite)
                }
                1 -> {
                    fileAdapter?.sortBySize(fileListTempFavourite)
                }
                2 -> {
                    fileAdapter?.sortByDate(fileListTempFavourite)
                }
            }
            btn_favourite.setBackgroundResource(R.drawable.ic_bg_btn_yes)
            btn_favourite.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            btn_favourite.setTextColor(Color.parseColor("#ffffff"))
            btn_allfile.setTypeface(null, Typeface.NORMAL)
            btn_allfile.setBackgroundResource(R.drawable.bg_btn)
            btn_allfile.setTextColor(Color.parseColor("#838388"))
            btn_recent.setTypeface(null, Typeface.NORMAL)
            btn_recent.setBackgroundResource(R.drawable.bg_btn)
            btn_recent.setTextColor(Color.parseColor("#838388"))
            Thread {
                fileList = fileListTempFavourite
                runOnUiThread {
                    fileAdapter?.updateList(fileList)
                    updateStatus(status)
                }
            }.start()
        }
        btn_recent.setOnClickListener {
            status = 3
            btn_recent.setBackgroundResource(R.drawable.bg_btn_yes)
            btn_recent.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            btn_recent.setTextColor(Color.parseColor("#ffffff"))
            btn_allfile.setTypeface(null, Typeface.NORMAL)
            btn_allfile.setBackgroundResource(R.drawable.bg_btn)
            btn_allfile.setTextColor(Color.parseColor("#838388"))
            btn_favourite.setTypeface(null, Typeface.NORMAL)
            btn_favourite.setBackgroundResource(R.drawable.bg_btn)
            btn_favourite.setTextColor(Color.parseColor("#838388"))
            Thread {
                fileList.clear()
                fileRecentList = App.database?.recentDao()?.list!!
                fileRecentList.forEach {
                    val myFile = MyFile(it.path)
                    fileList.add(myFile)
                }
                runOnUiThread {
                    fileAdapter?.updateList(fileList/* = java.util.ArrayList<com.masterexcels.excelreader.model.FileRecent> */)
                }
                updateStatus(status)
            }.start()
        }
        sort_btn.setOnClickListener {
            FilterDialog.start(this, "sort_dialog", object : OnActionCallback {
                override fun callback(key: String?, vararg data: Any?) {
                    if (key == "by_name") {
                        fileAdapter?.sortByNameAZ(fileList)!!
                    }
                    if (key == "by_size") {
                        fileAdapter?.sortBySize(fileList)!!
                    }
                    if (key == "by_created_time") {
                        fileAdapter?.sortByDate(fileList)!!
                    }
                    fileAdapter?.updateList(fileList)
                }

            })
        }

        search_bar.setOnClickListener {
            fr_ad.visibility = View.GONE
            clickSearch()
            search_bar.isFocusableInTouchMode = true
            search_bar.isFocusable = true
            showKeyboard(search_bar)
        }
        search_bt.setOnClickListener {
            search_bar.isFocusableInTouchMode = true
            search_bar.isFocusable = true
            showKeyboard(search_bar)
        }
        search_bar.setOnFocusChangeListener { _, b ->
            run {
                if (b) {
                    search_bar.hint = ""
                    search_bt_back.setImageResource(R.drawable.ic_btn_back)
                    search_bt.setImageResource(0)

                } else {
                    search_bt.setImageResource(R.drawable.ic_btn_share2)
                    search_bt_back.setImageResource(0)
                    search_bar.hint = "Search files"
                }
            }
        }

        search_bar.setOnHideKeyboardListener(object : CustomEditText.OnHideKeyboardListener {
            override fun onHideKeyboard() {
                search_bar.isFocusableInTouchMode = false
                search_bar.isFocusable = false
                search_bt_back.setImageResource(R.drawable.ic_btn_back)
                search_bt.setImageResource(0)
            }

        })
        search_bt_back.setOnClickListener {
            search_bar.setText("")
            fr_ad.visibility = View.VISIBLE
            showAllbtn()
            clickAllAfile()
            search_bt_back.setImageResource(0)
            search_bt.setImageResource(R.drawable.ic_btn_share2)
            cancelSearch()
        }
        search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                fileAdapter?.filter?.filter(p0)
                if (p0!!.isNotEmpty()) {
                    clear_bt.setImageResource(R.drawable.ic_btn_clear)
                    setUserProperty("SEARCH_File")
                    Toast.makeText(this@MainActivity, "SEARCH_File", Toast.LENGTH_SHORT).show()
                } else {
                    clear_bt.setImageResource(0)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        clear_bt.setOnClickListener {
            search_bar.text = null
            clear_bt.setImageResource(0)
        }

    }

    private fun cancelSearch() {
        search_bar.hint = "Search files"
        search_bar.isFocusableInTouchMode = false
        search_bar.isFocusable = false
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(search_bar.windowToken, 0)
    }


    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun clickAllAfile() {
        val status = 1
        fileListTemp = getFileList()
        btn_allfile.setBackgroundResource(R.drawable.ic_bg_btn_yes)
        btn_allfile.setTextColor(Color.parseColor("#ffffff"))
        btn_favourite.setBackgroundResource(R.drawable.bg_btn)
        btn_favourite.setTextColor(Color.parseColor("#838388"))
        btn_recent.setBackgroundResource(R.drawable.bg_btn)
        btn_recent.setTextColor(Color.parseColor("#838388"))
        when (FilterDialog.currentStatus) {
            0 -> {
                fileAdapter?.sortByNameAZ(fileListTemp)
            }
            1 -> {
                fileAdapter?.sortBySize(fileListTemp)
            }
            2 -> {
                fileAdapter?.sortByDate(fileListTemp)
            }
        }
        Thread {
            fileList = fileListTemp
            runOnUiThread {
                fileAdapter?.updateList(fileList)
                updateStatus(status)
            }
        }.start()
    }

    private fun updateStatus(int: Int) {
        runOnUiThread {
            if (fileList.size == 0 && int == 2) {
                no_file.setImageResource(R.drawable.ic_no_file_favourite2)
                no_result_search.setImageResource(0)

            }
            if (fileList.size == 0 && int == 3) {
                no_file.setImageResource(R.drawable.ic_nofile_recent)
                no_result_search.setImageResource(0)

            }
            if (fileList.size == 0 && int == 1) {
                no_file.setImageResource(0)
                no_result_search.setImageResource(R.drawable.ic_no_all_file)
            }
            if (fileList.size != 0) {
                no_file.setImageResource(0)
                no_result_search.setImageResource(0)
            }
        }
    }

    private fun initReceiver() {
        val filter = IntentFilter()
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
        var isShowRate = false
        var width = 0
        var height = 0
        const val RQC_REQUEST_PERMISSION_ANDROID_11 = 333
        const val UPDATE_SEARCH = "update_search"
        const val UPDATE_SEARCH_HAVE_RESULT = "have_result"
        var fileListTemp = ArrayList<MyFile>()
        var fileListTempFavourite: java.util.ArrayList<MyFile> = ArrayList()
        var fileRecentList: List<FileRecent> = ArrayList()

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }

    private fun getFileList(): ArrayList<MyFile> {
        val typeList: ArrayList<String> = ArrayList()
        val mlist: ArrayList<MyFile> = ArrayList()
        typeList.add("xlsx")
        typeList.add("xls")
        typeList.add("xlsm")
        typeList.add("xlsb")
        typeList.add("xlam")
        typeList.add("csv")
        typeList.forEach {
            val tempList = LoadFile.loadFile(this, it)
            tempList.forEach { it1 ->
                mlist.add(MyFile(it1.path))
            }
        }
        return mlist
    }

    override fun onResume() {
        super.onResume()
        if (AdCache.interReadFile == null) {
            loadInterAd(BuildConfig.inter_read_file)
        }
    }

    @SuppressLint("MissingPermission")
    private fun executeLoadFile() {
        if (checkPermission()) {
            clickAllAfile()
            setUserProperty("ACCEPT_File_Permission")
            Toast.makeText(this, "ACCEPT_File_Permission", Toast.LENGTH_SHORT).show()
        } else {
            PermissionDialog.start(this, "permission", object : OnActionCallback {
                override fun callback(key: String?, vararg data: Any?) {
                    if (key == "deny") {
                        finish()
                    } else if (key == "allow") {
                        requestPermission()
                    }
                }

            })

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

    override fun onBackPressed() {
        search_bar.setText("")
        fr_ad.visibility = View.VISIBLE
        showAllbtn()
        clickAllAfile()
        search_bt_back.setImageResource(0)
        search_bt.setImageResource(R.drawable.ic_btn_share2)
        if (SharePreferenceUtils.shouldShowRatePopup(this)) {
            showRateDialog(true)
            return
        }
        executeBack()
    }

    var doubleBackToExitPressedOnce = false

    private fun executeBack() {
        if (doubleBackToExitPressedOnce) {
            SharePreferenceUtils.increaseCountRate(this@MainActivity)
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.click_back_again), Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }


    fun showRateDialog(isFinish: Boolean) {
        val dialog = RateAppDialog(this)
        dialog.setCallback(object : RateCallback {
            override fun onMaybeLater() {
                if (isFinish) {
                    SharePreferenceUtils.increaseCountRate(this@MainActivity)
                    finishAffinity()
                }
            }

            override fun onSubmit(review: String?) {
                Toast.makeText(this@MainActivity, R.string.thank_you, Toast.LENGTH_SHORT).show()
                SharePreferenceUtils.setRated(this@MainActivity)
                if (isFinish) {
                    finishAffinity()
                }
            }

            override fun onRate() {
                CommonUtils.getInstance().rateApp(this@MainActivity)
                SharePreferenceUtils.setRated(this@MainActivity)
            }
        })
        dialog.show()
    }

    fun clickSearch() {
        clickAllAfile()
        btn_favourite.hide()
        btn_recent.hide()
        sort_btn.hide()
        btn_allfile.show()

    }

    fun showAllbtn() {
        btn_favourite.show()
        btn_recent.show()
        sort_btn.show()
        btn_allfile.show()
    }


}