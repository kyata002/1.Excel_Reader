package com.masterlibs.basestructure.utils;


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseAdapter
import com.masterlibs.basestructure.App
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.model.MyFile
import com.masterlibs.basestructure.view.activity.DocReaderActivity
import com.masterlibs.basestructure.view.activity.MainActivity
//import com.masterlibs.basestructure.view.activity.DocReaderActivity
//import com.masterlibs.basestructure.view.activity.ReadFile
import com.masterlibs.basestructure.view.dialog.DeleteDialog
import com.masterlibs.basestructure.view.dialog.DetailDialog
import com.masterlibs.basestructure.view.dialog.RenameDialog
import kotlinx.android.synthetic.main.dialog_rename.*
//import kotlinx.android.synthetic.main.dialog_detail.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FileAdapter(mList: ArrayList<MyFile>?, context: Context) :
    BaseAdapter<MyFile>(mList, context), Filterable {
    //    var sizeOfFile: Float = 0f

    private var temp: ArrayList<MyFile> = ArrayList()
    override fun viewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false)
        return FViewHolder(view)
    }

    fun updateList(list: ArrayList<MyFile>) {
        this.mList = list

        temp = this.mList as ArrayList<MyFile>
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("RestrictedApi")
    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder: FViewHolder = viewHolder as FViewHolder
        val myFile: MyFile = this.mList?.get(position)!!
        var extension:String? = null
        holder.img_view.setImageResource(R.drawable.ic_xlsx)
        val file = File(myFile.path)
        if (!file.isDirectory) {
            if (file.path.endsWith(".xls"!!))
                extension = ".xls"
            if (file.path.endsWith(".xlsm"!!))
                extension = ".xlsm"
            if (file.path.endsWith(".xlsb"!!))
                extension = ".xlsb"
            if (file.path.endsWith(".xlsx"!!))
                extension = ".xlsx"
            if (file.path.endsWith(".xlam"!!))
                extension = ".xlam"
            if (file.path.endsWith(".csv"!!))
                extension = ".csv"
        }
        var name = file.name
        name = name.replace(extension!!, "")
        holder.name_view.text =name
        var datefile: SimpleDateFormat = SimpleDateFormat("hh:mm aa, dd MMMM yyyy")
        holder.date_file.text = datefile.format(Date(File(myFile.path).lastModified()))
        var sizeOfFile = ((File(myFile.path).length() / (1024.0)).toFloat())
        holder.size_file.text = "%.2f KB".format(sizeOfFile)
        if (position == mList?.size!! - 1) {
            holder.bottom_line.setImageResource(0)
        } else {
            holder.bottom_line.setImageResource(R.drawable.ic_linesperate)
        }
        if (!checkFavourite(myFile.path)) {
            holder.favorite_checked.setButtonDrawable(R.drawable.ic_favorite)
        } else {
            holder.favorite_checked.setButtonDrawable(R.drawable.ic_favorite_true)
        }
//        holder.sizeFile.text = "%.2f Mb".format(sizeOfFile)
        holder.favorite_checked.setOnCheckedChangeListener { compoundButton, b ->
            myFile.isFavorite = b
            if (b && !checkFavourite(myFile.path)) {
                App.database?.favoriteDAO()?.add(myFile)
            } else {
                App.database?.favoriteDAO()?.delete(myFile.path)
            }
            MainActivity.fileListTempFavourite =
                App.database?.favoriteDAO()?.list as java.util.ArrayList<MyFile>
            notifyDataSetChanged()
        }
        holder.itemView.setOnClickListener {
            showRead(myFile)
        }


        holder.more_options.setOnClickListener {
            val pm = PopupMenu(context, holder.more_options)
            pm.menuInflater.inflate(R.menu.popup_menu_more, pm.menu)
            pm.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.share -> AppUtils.sharefile(File(myFile.path),context)
                    R.id.delete -> showDelete(myFile)
                    R.id.rename -> showRename(myFile)
                    R.id.detail -> showDetail(myFile)
                }
                true
            })
//            pm.show()
            val ph = MenuPopupHelper(context, pm.menu as MenuBuilder, holder.more_options)
            ph.setForceShowIcon(true)
            ph.show()
        }
    }

    private fun checkFavourite(path: String?): Boolean {
        return App.database?.favoriteDAO()?.getFile(path) != null
    }

    fun sortByNameAZ(list: ArrayList<MyFile>) {
        for (i in 0 until list.size!!) {
            for (j in i + 1 until list.size!!) {
                if (File(list[i].path).name.toLowerCase() > File(list[j].path).name.toLowerCase()) {
                    val tempFile: MyFile = list[i]
                    list[i] = list[j]
                    list[j] = tempFile
                }

            }

        }
    }

    fun sortBySize(list: ArrayList<MyFile>) {
        for (i in 0 until list.size!!) {
            for (j in i + 1 until list.size!!) {
                if (File(list[i].path).length() < File(list[j].path).length()) {
                    var a: MyFile = list[i]
                    list[i] = list[j]
                    list[j] = a
                }
            }
        }

    }

    fun sortByDate(list: ArrayList<MyFile>) {
        for (i in 0 until list.size!!) {
            for (j in i + 1 until list.size!!) {
                if (Date(File(list[i].path).lastModified()).time < Date(File(list[j].path).lastModified()).time) {
                    var a: MyFile = list[i]
                    list[i] = list[j]
                    list[j] = a
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            var listFile: ArrayList<MyFile> = ArrayList()
            override fun performFiltering(p0: CharSequence?): FilterResults {
                var list: ArrayList<MyFile> = ArrayList()
                var text = p0.toString()
                if (text.isEmpty()) {
                    listFile = temp
                }
                else if (text[0] != ' ') {
                    temp?.forEach {
                        if (File(it.path).name.toLowerCase().contains(text.toLowerCase())) {
                            list.add(it)
                        }
                    }
                    listFile = list

                }

                var filterResult = FilterResults()
                filterResult.values = listFile
                return filterResult
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                mList = p1?.values as ArrayList<MyFile>
                if (mList!!.size == 0) {
                    context.sendBroadcast(Intent(MainActivity.UPDATE_SEARCH))
                } else {
                    context.sendBroadcast(Intent(MainActivity.UPDATE_SEARCH_HAVE_RESULT))
                }
                notifyDataSetChanged()
            }

        }
    }

    inner class FViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img_view: ImageView

        val more_options: Button
        val name_view: TextView
        val date_file: TextView
        val favorite_checked: CheckBox
        val size_file: TextView
        var bottom_line: ImageView

        init {
            img_view = itemView.findViewById(R.id.img_view_file)
            more_options = itemView.findViewById(R.id.more_options)
            name_view = itemView.findViewById(R.id.name_file)
            date_file = itemView.findViewById(R.id.date_file)
            favorite_checked = itemView.findViewById(R.id.favorite_checked)
            size_file = itemView.findViewById(R.id.size_file)
            bottom_line = itemView.findViewById(R.id.bottom_line)
        }
    }

    fun showDelete(myFile: MyFile) {

        DeleteDialog.start(context, File(myFile.path).name, object : OnActionCallback {
            override fun callback(key: String?, vararg data: Any?) {
                when {
                    key.equals("delete") -> {
                       val b= File(myFile.path).delete()
                        mList?.indexOf(myFile)?.let { notifyItemRemoved(it) }
                        mList?.remove(myFile)
                        App.database?.favoriteDAO()?.delete(myFile.path)
                        notifyDataSetChanged()
                    }
                    key.equals("cancel") -> {

                    }
                }
            }
        })

    }




    fun showDetail(myFile: MyFile) {
        DetailDialog.start(context, myFile.path, object : OnActionCallback {
            override fun callback(key: String?, vararg data: Any?) {
                if (key.equals("ok")) {

                }
            }

        })
    }

    fun showRename(myFile: MyFile) {
        RenameDialog.start(context, myFile.path, object : OnActionCallback {
            override fun callback(key: String?, vararg data: Any?) {
                val newName = data[0] as String
                val file = File(myFile.path)
                val newFile = File(file.parent + "/" + newName)
                App.database?.favoriteDAO()?.delete(myFile.path)
                file.renameTo(newFile)
                myFile.path = newFile.path
                notifyDataSetChanged()
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)
                    )
                )
            }

        })
    }

    private fun showRead(myFile: MyFile) {
        DocReaderActivity.start(context, myFile.path)
    }


}