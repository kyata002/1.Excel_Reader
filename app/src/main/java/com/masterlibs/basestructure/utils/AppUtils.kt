package com.masterlibs.basestructure.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import com.masterlibs.basestructure.model.MyFile
import java.io.File

class AppUtils {
    companion object {
        fun sharefile(file: File, context:Context){
            val intentShareFile = Intent(Intent.ACTION_SEND)
            val fileWithinMyDir: File = File(file.path)
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            if (fileWithinMyDir.exists()) {
                intentShareFile.type = "application/xlsx"
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileWithinMyDir))
                intentShareFile.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "Sharing File..."
                )
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...")
                context.startActivity(Intent.createChooser(intentShareFile, "Share File"))
            }
        }
    }
}