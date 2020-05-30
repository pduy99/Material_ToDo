package com.example.taskmananger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmananger.R
import kotlinx.android.synthetic.main.layout_item_category_background.view.*

class ColorHorizontalSliderAdapter(var context: Context, var colors : IntArray):RecyclerView.Adapter<ColorHorizontalSliderAdapter.ViewHolder>() {
    var callback : CallBack? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindColor(resId: Int, callBack: CallBack?){
            itemView.roundCategoryBackgroundItem.setImageResource(resId)
            itemView.setOnClickListener {
                callBack!!.onItemClicked(resId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_category_background,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resId = colors[position]
        holder.bindColor(resId,callback)
    }

    interface CallBack{
        fun onItemClicked(resId:Int)
    }
}