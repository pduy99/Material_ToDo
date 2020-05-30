package com.example.taskmananger.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmananger.R
import com.example.taskmananger.databinding.LayoutListCategoryBinding
import com.example.taskmananger.models.Category

class CategoryRecylerViewAdapter(var listCategory:List<Category>, var listener : OnItemSelected): RecyclerView.Adapter<CategoryRecylerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView : LayoutListCategoryBinding): RecyclerView.ViewHolder(itemView.root){
        var layoutListCategoryBinding : LayoutListCategoryBinding = itemView;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutListCategoryBinding : LayoutListCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.layout_list_category,parent,false);
        val viewHolder = ViewHolder(layoutListCategoryBinding);
        return viewHolder;
    }

    override fun getItemCount(): Int {
        return listCategory.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category : Category = listCategory[position];
        holder.layoutListCategoryBinding.category = category;
        holder.itemView.setOnClickListener { listener.onItemSelected(position) }
        holder.itemView.setOnLongClickListener{ listener.onItemLongClick(position)}
    }

    interface OnItemSelected{
        fun onItemSelected(position: Int)
        fun onItemLongClick(position: Int) : Boolean
    }
}
