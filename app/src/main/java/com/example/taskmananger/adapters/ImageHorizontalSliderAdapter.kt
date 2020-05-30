package com.example.taskmananger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmananger.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_category_background.view.*

class ImageHorizontalSliderAdapter(var context : Context, var images : IntArray) : RecyclerView.Adapter<ImageHorizontalSliderAdapter.ViewHolder>() {
    var callback : CallBack? = null

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindImage(resId : Int, callBack : CallBack?){
            Picasso.get().load(resId).resize(75,75).into(itemView.roundCategoryBackgroundItem)
            itemView.setOnClickListener {
                callBack!!.onItemClicked(resId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView : View = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_category_background,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resId = images[position]
        holder.bindImage(resId,callback)
    }

    interface CallBack{
        fun onItemClicked(resId: Int);
    }
}