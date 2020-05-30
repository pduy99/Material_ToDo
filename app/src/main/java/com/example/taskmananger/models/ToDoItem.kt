package com.example.taskmananger.models

import android.graphics.Paint
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.TextView
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import com.example.taskmananger.BR
import java.io.Serializable
import java.util.*

class ToDoItem : Serializable, BaseObservable {
    var title : String
    var hasReminder: Boolean
    var color:Int
    var description:String
    var category:String
    var place:String
    var id : UUID
    var createDate: Date
    var dueDate:Date

    @Bindable
    var isDone:Boolean
        @Bindable
        get
        set(value) {
            field = value
            notifyPropertyChanged(BR.isDone)
        }

    init {
        title =""
        category = ""
        hasReminder = false
        isDone = false
        color = 1677725
        description = ""
        place = ""
        id = UUID.randomUUID()
        createDate  = Date()
        dueDate = Date()
    }

    constructor(title:String,category: String, description:String, reminder:Boolean, color: Int?, dueDate:Date, place:String){
        this.title = title
        this.category = category
        this.hasReminder = reminder
        this.color = color?:1677725
        this.description = description
        this.dueDate = dueDate
        this.place = place
        id = UUID.randomUUID()
        createDate = Date()
        isDone = false
    }

    object UIUtil{
        @BindingAdapter("android:DateFormat")
        @JvmStatic
        fun DateToStringFormat(view : View, date:Date){
            val textView : TextView = view as TextView
            val simpleDateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm a")
            textView.text = simpleDateFormat.format(date)
        }

        @BindingAdapter("android:strikeThrough")
        @JvmStatic
        fun setTextStrikeThrough(view : View, hasDone : Boolean){
            val textView : TextView = view as TextView
            if(hasDone){
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            else{
                textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

    }
}