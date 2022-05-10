package com.masterlibs.basestructure.utils;


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.documentmaster.documentscan.OnActionCallback
import com.docxmaster.docreader.base.BaseAdapter
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.utils.MyFile
//import com.masterlibs.basestructure.view.activity.DocReaderActivity
//import com.masterlibs.basestructure.view.activity.ReadFile
import com.masterlibs.basestructure.view.dialog.DeleteDialog
import com.masterlibs.basestructure.view.dialog.RenameDialog
import kotlinx.android.synthetic.main.dialog_delete.view.*
import kotlinx.android.synthetic.main.dialog_detail.view.*
//import kotlinx.android.synthetic.main.dialog_detail.view.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


 class FileAdapter(mList: ArrayList<MyFile>?, context: Context) :
    BaseAdapter<MyFile>(mList, context), Filterable {
//    var sizeOfFile: Float = 0f
    private var temp: ArrayList<MyFile> = mList!!
    override fun viewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false)
        return FViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("RestrictedApi")
    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder: FViewHolder = viewHolder as FViewHolder
        val myFile: MyFile = this.mList?.get(position)!!
        holder.img_view.setImageResource(R.drawable.ic_file_excel)
        holder.name_view.text = File(myFile.path).name
        holder.date_file.text = Date(File(myFile.path).lastModified()).toString()
//        sizeOfFile = ((File(myFile.path).length() / (1024.0 * 1024)).toFloat())

        if (!myFile.isFavorite){
            holder.favorite_checked.setButtonDrawable(R.drawable.ic_favorite)

        }
        else{
            holder.favorite_checked.setButtonDrawable(R.drawable.ic_favorite_true)

        }
//        holder.sizeFile.text = "%.2f Mb".format(sizeOfFile)
        holder.favorite_checked.setOnCheckedChangeListener { compoundButton, b ->
            myFile.isFavorite = b

            notifyDataSetChanged()
        }
//        holder.itemView.setOnClickListener {
//            showRead(myFile)
//        }


        holder.more_options.setOnClickListener {
            val pm = PopupMenu(context, holder.more_options)
            pm.menuInflater.inflate(R.menu.popup_menu_more, pm.menu)
            pm.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
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

    fun sortByNameAZ() {
        for (i in 0 until mList?.size!!) {
            for (j in i + 1 until mList?.size!!) {
                var n = 0
                if (File(mList!![i].path).name.length < File(mList!![j].path).name.length){
                    n = File(mList!![i].path).name.length
                }
                else{
                    n = File(mList!![j].path).name.length
                }
                for (k in 0 until n){
                    if (File(mList!![i].path).name[k] > File(mList!![j].path).name[k]) {
                        var a: MyFile = mList!![i]
                        mList!![i] = mList!![j]
                        mList!![j] = a
                    }
                }

            }

        }
        notifyDataSetChanged()
    }

    fun sortByNameZA() {

        for (i in 0 until mList?.size!!) {
            for (j in i + 1 until mList?.size!!) {
                var n = 0
                if (File(mList!![i].path).name.length < File(mList!![j].path).name.length){
                    n = File(mList!![i].path).name.length
                }
                else{
                    n = File(mList!![j].path).name.length
                }
                for (k in 0 until n){
                    if (File(mList!![i].path).name[k] < File(mList!![j].path).name[k]) {
                        var a: MyFile = mList!![i]
                        mList!![i] = mList!![j]
                        mList!![j] = a
                    }
                }

            }

        }
        notifyDataSetChanged()
    }

    fun sortBySize() {
        for (i in 0 until mList?.size!!) {
            for (j in i + 1 until mList?.size!!) {
                if (File(mList!![i].path).length() < File(mList!![j].path).length()) {
                    var a: MyFile = mList!![i]
                    mList!![i] = mList!![j]
                    mList!![j] = a
                }
            }
        }
        notifyDataSetChanged()
    }

    fun sortByDate() {

        for (i in 0 until mList?.size!!) {
            for (j in i + 1 until mList?.size!!) {
                if (Date(File(mList!![i].path).lastModified()).time < Date(File(mList!![j].path).lastModified()).time) {
                    var a: MyFile = mList!![i]
                    mList!![i] = mList!![j]
                    mList!![j] = a
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                var checked = false
                var text = p0.toString()
                mList = if (text.isEmpty()) {
                    temp
                } else {
                    var list: ArrayList<MyFile> = ArrayList()
                    temp.forEach {
                        for (i in 0 until text.toLowerCase().length){
                            if (File(it.path).name.toLowerCase().contains(text.toLowerCase()[i])) {
                                checked = true
                                continue
                            }
                            checked = false
                        }
                        if (checked){
                            list.add(it)
                        }
                    }
                    list
                }
                var filterResult = FilterResults()
                filterResult.values = mList
                return filterResult
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                mList = p1?.values as ArrayList<MyFile>
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

        init {
            img_view = itemView.findViewById(R.id.img_view_file)
            more_options = itemView.findViewById(R.id.more_options)
            name_view = itemView.findViewById(R.id.name_file)
            date_file = itemView.findViewById(R.id.date_file)
            favorite_checked = itemView.findViewById(R.id.favorite_checked)
        }
    }

    fun showDelete(myFile: MyFile) {
        val view = View.inflate(context,R.layout.dialog_delete,null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        val tvNameFile : TextView = view.findViewById(R.id.tvNameDelete)
        tvNameFile.text = "Are you sure you want to delete"+"\n"+ File(myFile.path).name
        val  dialog = builder.create()
        view.bt_cancel.setOnClickListener {

        }
        view.bt_delete.setOnClickListener {

        }
        dialog.show()

        DeleteDialog.start(context, myFile.path, object : OnActionCallback {
            override fun callback(key: String?, vararg data: Any?) {
                //var listFileAdapter : ListFileAdapter? = null
                when {
                    key.equals("delete") -> {
                        File(myFile.path).delete()
                        mList?.indexOf(myFile)?.let { notifyItemRemoved(it) }
                        mList?.remove(myFile)
                        //bắn thông báo có thằng myfile đc xóa cho thằng Main để update lại data
                    }
                    key.equals("cancel") -> {

                    }
                }
            }
        })
    }

    fun showDetail(myFile: MyFile) {
        val view = View.inflate(context, R.layout.dialog_detail, null)
        val builder = AlertDialog.Builder(context)
        val file = File(myFile.path)
        val sizeOfFile = (file.length() / (1024.0 * 1024))
        builder.setView(view)
        view.tvNameFile.text = file.name
        view.tvPathFile.text = file.path
        view.tvDateFile.text = Date(file.lastModified()).toString()
        view.tvSizeFile.text = "%.2f Mb".format(sizeOfFile)
        val dialog = builder.create()
        dialog.show()
//        DetailDialog.start(context, pathFile , object :OnActionCallback{
//            override fun callback(key: String?, vararg data: Any?) {
//                if (key.equals("ok")) {
//
//                }
//            }
//
//        })
    }

    fun showRename(myFile: MyFile) {
        RenameDialog.start(context, myFile.path, object : OnActionCallback {
            override fun callback(key: String?, vararg data: Any?) {
                val newName = data[0] as String
                val file = File(myFile.path)
                val newFile = File(file.parent + "/" + newName)
                file.renameTo(newFile)
                myFile.path=newFile.path
                notifyDataSetChanged()
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)
                    )
                )
            }

        })
    }

//    private fun showRead(myFile: MyFile) {
//        DocReaderActivity.start(context, myFile.path)
//    }


 }