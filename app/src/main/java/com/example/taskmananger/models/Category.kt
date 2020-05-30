package com.example.taskmananger.models

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Category(var name: String, var icon: Int, var background: Int, var type: Int) : Serializable{
    var listItem : MutableList<ToDoItem> = mutableListOf()
    var id: UUID = UUID.randomUUID()

    object UIUtil {
        @BindingAdapter("android:imageUrl")
        @JvmStatic
        fun loadImage(view: View, imageID: Int) {
            val imageView: ImageView = view as ImageView
            imageView.setImageDrawable(ContextCompat.getDrawable(view.context, imageID));
        }

        @BindingAdapter("android:numberOfTask")
        @JvmStatic
        fun getStringNumberOfTask(view : View, list: MutableList<*>){
            val listSize = list.size
            val textView = view as TextView
            if(listSize > 1){
                textView.text = "$listSize tasks"
            }
            else{
                textView.text = "$listSize task"
            }
        }
    }
}