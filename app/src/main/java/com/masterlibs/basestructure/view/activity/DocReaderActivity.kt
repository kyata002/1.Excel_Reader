package com.masterlibs.basestructure.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.print.PrintManager
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.print.PrintHelper
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.view.adapter.PrintDocumentAdapter
import com.shockwave.pdfium.PdfPasswordException
import com.tom_roush.pdfbox.pdmodel.interactive.action.PDWindowsLaunchParams
import com.wxiwei.office.constant.MainConstant
import com.wxiwei.office.officereader.BaseDocActivity
import kotlinx.android.synthetic.main.activity_reader.*
import java.io.File
import java.io.IOException


class DocReaderActivity : BaseDocActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_reader
    }

    @SuppressLint("WrongViewCast")
    override fun getFrameLayoutDoc(): FrameLayout {
        return findViewById(R.id.app_frame)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun printPdfFile(context: Context, uri: Uri?) {
        try {
//            PdfiumCore(context).newDocument(
//                uri?.let {
//                    context.contentResolver.openFileDescriptor(
//                        it,
//                        PDPageLabelRange.STYLE_ROMAN_LOWER
//                    )
//                }
//            )
            if (PrintHelper.systemSupportsPrint()) {
                @SuppressLint("WrongConstant") val printManager =
                    context.getSystemService(PDWindowsLaunchParams.OPERATION_PRINT) as PrintManager
                val sb = StringBuilder()
                sb.append(context.getString(R.string.app_name))
                sb.append(" Document")
                val sb2 = sb.toString()
                if (printManager != null) {
                    printManager.print(sb2, PrintDocumentAdapter(context, uri), null)
                    return
                }
                return
            }
//            Toast.makeText(
//                context,
//                context.getString(R.string.device_does_not_support_printing),
//                Toast.LENGTH_LONG
//            ).show()
        } catch (e: PdfPasswordException) {
//            Toast.makeText(
//                context,
//                context.getString(R.string.cant_print_password_protected_pdf),
//                Toast.LENGTH_LONG
//            ).show()
            e.printStackTrace()
        } catch (e2: IOException) {
//            Toast.makeText(
//                context,
//                context.getString(R.string.cannot_print_malformed_pdf),
//                Toast.LENGTH_LONG
//            ).show()
            e2.printStackTrace()
        } catch (e3: Exception) {
//            Toast.makeText(
//                context,
//                context.getString(R.string.cannot_print_unknown_error),
//                Toast.LENGTH_LONG
//            ).show()
            e3.printStackTrace()
        }
    }

    override fun initView() {
        //val title = findViewById<TextView>(R.id.title)
        val path = intent.getStringExtra(MainConstant.INTENT_FILED_FILE_PATH)
        name_file_read.text = File(path).name
        btn_back.setOnClickListener {
//            val back = Intent(this, MainActivity::class.java)
//            startActivity(back)
            finish()
        }
        btn_print.setOnClickListener {
            printPdfFile(this, Uri.fromFile(File(path)))
        }
        btn_share.setOnClickListener {
            val intentShareFile = Intent(Intent.ACTION_SEND)
            val fileWithinMyDir: File = File(path)
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            if (fileWithinMyDir.exists()) {
                intentShareFile.type = "application/xlsx"
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileWithinMyDir))
                intentShareFile.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "Sharing File..."
                )
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...")
                startActivity(Intent.createChooser(intentShareFile, "Share File"))
            }

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