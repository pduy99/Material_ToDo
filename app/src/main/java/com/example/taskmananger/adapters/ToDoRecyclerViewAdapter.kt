package com.example.taskmananger.adapters

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.agilie.swipe2delete.ModelOptions
import com.agilie.swipe2delete.SwipeToDeleteDelegate
import com.agilie.swipe2delete.interfaces.IAnimationUpdateListener
import com.agilie.swipe2delete.interfaces.IAnimatorListener
import com.agilie.swipe2delete.interfaces.ISwipeToDeleteAdapter
import com.agilie.swipe2delete.interfaces.ISwipeToDeleteHolder
import com.example.taskmananger.R
import com.example.taskmananger.databinding.LayoutListTodoBinding
import com.example.taskmananger.models.ToDoItem
import com.example.taskmananger.viewmodels.MainViewModel
import java.util.*

class ToDoRecyclerViewAdapter(var context: Context,
                              var listToDo : MutableList<ToDoItem>,
                              private val viewmodel : MainViewModel,
                              var onItemClick: (ToDoItem) -> Any)
    : RecyclerView.Adapter<ToDoRecyclerViewAdapter.ViewHolder>(),
    ISwipeToDeleteAdapter<Int, ToDoItem,ToDoRecyclerViewAdapter.ViewHolder>,
    IAnimationUpdateListener,
    IAnimatorListener{

    val swipeToDeleteDelegate = SwipeToDeleteDelegate(items = listToDo, swipeToDeleteAdapter = this)

    var animationEnabled = true
    var bottomContainer = true

    init{
        swipeToDeleteDelegate.deletingDuration = 3000
    }

    inner class ViewHolder(itemView : LayoutListTodoBinding) : RecyclerView.ViewHolder(itemView.root), ISwipeToDeleteHolder<Int> {
        var layoutListTodoBinding: LayoutListTodoBinding = itemView
        var progressBar = itemView.progressIndicator
        var undoContainer = itemView.undoContainer
        var deletedItemName = itemView.itemNameDeleted
        var undoData = itemView.undoData
        var undoButton = itemView.buttonUndo
        var itemContainer = itemView.itemContainer

        override val topContainer: View
            get() = if(pendingDelete && bottomContainer)
                        undoContainer
                    else
                        itemContainer

        override var key: Int = -1

        override var pendingDelete: Boolean = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutListTodoBinding : LayoutListTodoBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.layout_list_todo,parent,false)
        return ViewHolder(layoutListTodoBinding)
    }

    override fun getItemCount(): Int {
        return listToDo.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val toDoItem : ToDoItem = listToDo[position]
        swipeToDeleteDelegate.onBindViewHolder(holder, toDoItem.hashCode(),position)
        holder.layoutListTodoBinding.item = toDoItem
        holder.layoutListTodoBinding.viewmodel = viewmodel
    }

    override fun findItemPositionByKey(key: Int): Int = (0..listToDo.lastIndex).firstOrNull{
        listToDo[it].hashCode() == key} ?: -1

    override fun onBindCommonItem(holder: ViewHolder, key: Int, item: ToDoItem, position: Int) {
        holder.apply {
            itemContainer.visibility = View.VISIBLE
            undoData.visibility = View.GONE
            progressBar.visibility = View.GONE
            itemContainer.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

    override fun removeItem(key: Int) {
        swipeToDeleteDelegate.removeItem(key)
    }

    override fun onAnimationUpdated(animation: ValueAnimator?, options: ModelOptions<*>) {
       if(animationEnabled){
           val posX = animation?.animatedValue as Float
           swipeToDeleteDelegate.holders[options.key]?.progressBar?.x = posX
           options.posX = posX
       }
    }

    override fun onAnimationStart(animation: Animator?, options: ModelOptions<*>) {
        if(animationEnabled){
            swipeToDeleteDelegate.holders[options.key]?.progressBar?.visibility = View.VISIBLE
        }
    }

    override fun onAnimationEnd(animation: Animator?, options: ModelOptions<*>) {
        if(animationEnabled){
            swipeToDeleteDelegate.holders[options.key]?.progressBar?.animate()?.x(0.0f)?.withEndAction{
                swipeToDeleteDelegate.holders[options.key]?.progressBar?.visibility = View.GONE
            }
        }
    }

    override fun onBindPendingItem(holder: ViewHolder, key: Int, item: ToDoItem, position: Int) {
        holder.itemContainer.visibility = View.GONE
        if(bottomContainer){
            holder.apply {
                deletedItemName.text = "You have deleted ${item.title}"
                itemContainer.visibility = View.GONE
                undoContainer.visibility = View.VISIBLE
                if(animationEnabled){
                    progressBar.visibility = View.VISIBLE
                }
                undoButton.setOnClickListener {
                    swipeToDeleteDelegate.onUndo(key)
                }
            }
        }
    }
}