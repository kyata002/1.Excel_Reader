package com.masterexcels.excelreader.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.docxmaster.docreader.base.BaseAdapter
import com.docxmaster.docreader.base.BaseViewHolder
import com.masterexcels.excelreader.databinding.ItemSlideBinding
import com.masterexcels.excelreader.model.MySlide
import com.wxiwei.office.pg.control.Presentation

class SlideAdapter(mList: List<MySlide?>?, context: Context?) :
    BaseAdapter<MySlide?>(mList as MutableList<MySlide?>?, context!!) {
    private var presentation: Presentation? = null
    override fun viewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return SlideViewHolder(
            ItemSlideBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val holder = viewHolder as SlideViewHolder?
        holder!!.onBind(mList!![position]!!)
    }

    fun setViewSlide(presentation: Presentation?) {
        this.presentation = presentation
    }

    private inner class SlideViewHolder(private val binding: ItemSlideBinding) : BaseViewHolder(
        binding.root
    ) {
        @SuppressLint("SetTextI18n")
        fun onBind(slide: MySlide) {
            itemView.tag = slide
            if (slide.bitmap == null && presentation != null) {
                val bitmap = presentation!!.slideToImage(mList!!.indexOf(slide) + 1)
                slide.bitmap = bitmap
            }
            Glide.with(context).asBitmap().load(slide.bitmap).into(binding.ivThumb)
            binding.tvPage.text = "" + (mList!!.indexOf(slide) + 1)
            if (slide.isSelected) {
                binding.viewSelect.visibility = View.VISIBLE
            } else {
                binding.viewSelect.visibility = View.GONE
            }
        }

        init {
            itemView.setOnClickListener { v: View? -> mCallback!!.callback(null, itemView.tag) }
        }

        override fun loadData(data: Any) {

        }
    }
}