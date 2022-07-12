package com.masterexcels.excelreader.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.control.manager.AdmobManager
import com.documentmaster.documentscan.OnActionCallback
import com.documentmaster.documentscan.extention.setUserProperty
import com.github.barteksc.pdfviewer.source.FileSource
import com.masterexcels.excelreader.BuildConfig
import com.masterexcels.excelreader.R
import com.masterexcels.excelreader.model.MySlide
import com.masterexcels.excelreader.utils.CommonUtils
import com.masterexcels.excelreader.utils.NotificationUtil
import com.masterexcels.excelreader.view.adapter.SlideAdapter
import com.shockwave.pdfium.PdfiumCore
import com.wxiwei.office.constant.MainConstant
import com.wxiwei.office.officereader.BaseDocActivity
import com.wxiwei.office.pg.control.Presentation
import com.wxiwei.office.ss.control.ExcelView
import com.wxiwei.office.wp.control.Word
import com.wxiwei.office.wp.scroll.ScrollBarView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_reader.*
import kotlinx.android.synthetic.main.activity_reader.fr_ad
import java.io.File
import java.io.IOException


open class DocReaderActivity : BaseDocActivity() {
    private lateinit var adapter: SlideAdapter
    private var path: String? = null

    val list: MutableList<MySlide?> = ArrayList<MySlide?>()
    private var slideView: Presentation? = null
    private var currentPage = 0


    companion object {
        fun start(context: Context, path: String?) {
            val starter = Intent(context, DocReaderActivity::class.java)
            starter.putExtra(MainConstant.INTENT_FILED_FILE_PATH, path)
            context.startActivity(starter)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_reader
    }

    override fun getFrameLayoutDoc(): FrameLayout? {
        return fr_doc
    }

    override fun getScrollBarView(): ScrollBarView {
        return scrollView
    }


    protected override fun initView(path: String) {
        this.path = path
        AdmobManager.getInstance().loadBanner(this, BuildConfig.banner_doc)
        val title = findViewById<TextView>(R.id.tv_title)
        adapter = SlideAdapter(list, this@DocReaderActivity)
        adapter?.mCallback = object : OnActionCallback {
            override fun callback(key: String?, vararg data: Any?) {
                val slide: MySlide? = data[0] as MySlide?
                val index = list.indexOf(slide)
                gotoSlide(index)
                updateList(index)
            }
        }
        rvSlide.layoutManager = LinearLayoutManager(
            this@DocReaderActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvSlide.adapter = adapter

//        ConstraintLayout layoutToolbar = findViewById(R.id.layoutToolbar);
//        Category category = (Ca tegory) getIntent().getSerializableExtra("cat");
//        if (category != null) {
//            String colorHex = category.getColorTint();
//            tvPage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorHex)));
//            layoutToolbar.setBackgroundColor(Color.parseColor(colorHex));
//            getWindow().setStatusBarColor(Color.parseColor(colorHex));
//        }
        title.text = File(path).name
        //        if (path.toLowerCase().endsWith(".pdf")) {
////            tvPage.setVisibility(View.VISIBLE);
//            btPrint.setVisibility(View.VISIBLE);
//        } else {
//            btPrint.setVisibility(View.GONE);
//        }
        if (path.toLowerCase().endsWith(".xls") || path.toLowerCase().endsWith(".xlsx")) {
            tv_page.visibility = View.GONE
        }
        addEvent()
    }

    protected override fun showPageToast() {
        tvScrollPage!!.visibility = View.VISIBLE
        tv_page.visibility = View.GONE
    }

    protected override fun hidePageToast() {
        tvScrollPage!!.visibility = View.GONE
        tv_page.visibility = View.VISIBLE
    }

    private fun addEvent() {
        findViewById<View>(R.id.iv_back).setOnClickListener {
                v: View? -> onBackPressed()

        }
        findViewById<View>(R.id.bt_share).setOnClickListener {
                v: View? ->shareFileRead()
        }
    }

    fun shareFileRead(){
        setUserProperty("CLICK_Read_Share")
        Toast.makeText(this, "CLICK_Read_Share", Toast.LENGTH_SHORT).show()
        CommonUtils.getInstance().shareFile(this, File(path))
    }

    private fun updateList(index: Int) = try {
        val currentSelected = getCurrentSelected()
        list[currentSelected]?.isSelected = false
        list[index]?.isSelected = true
        adapter.notifyItemChanged(currentSelected)
        adapter.notifyItemChanged(index)
        rvSlide!!.scrollToPosition(index)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun getCurrentSelected(): Int {
        val pos = 0
        for (sl in list) {
            if (sl?.isSelected == true) {
                return list.indexOf(sl)
            }
        }
        return pos
    }

    private var handler: Handler? = null
    private val timeHideZoomRd =
        Runnable { tvZoom!!.visibility = View.GONE }

    @SuppressLint("SetTextI18n")
    override fun changeZoom(percent: Int) {
        tvZoom!!.visibility = View.VISIBLE
        tvZoom!!.text = "$percent %"
        if (handler == null) {
            handler = Handler()
        }
        handler!!.removeCallbacks(timeHideZoomRd)
        handler!!.postDelayed(timeHideZoomRd, 500)
    }

    override fun onError(errorCode: Int) {
//        if (errorCode == ERROR_PDF) {
//            PasswordDialog passDialog = PasswordDialog.newInstance();
//            passDialog.setCallback((key, data) -> {
//                if (key != null && key.equals("cancel")) {
//                    finish();
//                    return;
//                }
//                String pass = (String) data;
//                if (!passwordCorrect(pass)) {
//                    Toast.makeText(DocReaderActivity.this, "The password you entered is incorrect", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                readPdfFile(pass);
//                passDialog.dismiss();
//            });
//            passDialog.show(getSupportFragmentManager(), null);
//        }
    }

    @SuppressLint("SetTextI18n")
    override fun pageChanged(page: Int, pageCount: Int) {
        currentPage = page
        val currentPage = "$page/$pageCount"
        tvScrollPage!!.text = currentPage
        updateList(page - 1)
        tv_page.text = currentPage
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun openFileFinish() {
        super.openFileFinish()
        try {
            slideView = getPPTView()
            val count = slideView!!.slideCount
            if (count == 0) {
                return
            }
            //            btSlideShow.setVisibility(View.VISIBLE);
            fr_slide!!.visibility = View.VISIBLE
            list.clear()
            for (i in 0 until count) {
                list.add(MySlide(null))
            }
            list[0]?.setSelected(true)
            adapter.setViewSlide(slideView)
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getThumbnail(): Bitmap? {
        val view = control.view
        try {
            if (view is ExcelView) {
                val sheetView = (control.view as ExcelView).sheetView
                return sheetView.getThumbnail(sheetView.currentSheet, 500, 500, 1.0f)
            }
            if (view is Word) {
                return (control.view as Word).pageToImage(currentPage)
            }
            if (view is Presentation) {
                return getPPTView().slideToImage(currentPage)
            }
            if (filePath.toLowerCase().endsWith(".pdf")) {
                try {
                    return getPdfView().getCacheManager().getThumbnails().get(0).getRenderedBitmap()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun loadBitmapFromView(v: View): Bitmap? {
        val b = Bitmap.createBitmap(
            v.layoutParams.width,
            v.layoutParams.height,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }

    protected override fun hideZoomToast() {
        tvZoom!!.visibility = View.GONE
    }

    private fun passwordCorrect(pass: String): Boolean {
        val docSource = FileSource(File(path))
        try {
            docSource.createDocument(this, PdfiumCore(this), pass)
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }
    override fun onBackPressed() {
        MainActivity.isShowRate = true
        val bitmap = getThumbnail()
        if (bitmap == null) {
            super.onBackPressed()
            return
        }
        val intent = Intent(this, SplashActivity::class.java)
//        intent.putExtra("android.intent.action.VIEW", BuildConfig.inter_open_other_app)
        intent.action="android.intent.action.VIEW"
        intent.data = Uri.fromFile(File(path))
        NotificationUtil.getInstance().showNotification(
            this,
            "You have read this file recently \uD83D\uDCDA",
            "Check now \uD83D\uDC49",
            intent,
            bitmap
        )
//        fr_ad.visibility = View.VISIBLE
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        slideView = null
    }
}