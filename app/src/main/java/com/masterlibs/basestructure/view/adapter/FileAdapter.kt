package com.masterlibs.basestructure.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.docxmaster.docreader.base.BaseAdapter
import com.docxmaster.docreader.base.BaseViewHolder
import com.masterlibs.basestructure.R
import com.masterlibs.basestructure.model.FileModel

class FileAdapter(mList: MutableList<FileModel>?, context: Context) :
    BaseAdapter<FileModel>(mList, context) {

    override fun viewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return FileViewModel(
            LayoutInflater.from(context).inflate(R.layout.item_file, parent, false)
        )
    }

    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {

    }

    inner class FileViewModel(itemView: View) : BaseViewHolder(itemView) {
        override fun loadData(data: Any) {

        }

    }

}