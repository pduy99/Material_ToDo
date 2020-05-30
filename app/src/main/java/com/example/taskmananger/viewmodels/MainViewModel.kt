package com.example.taskmananger.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.taskmananger.models.Category
import com.example.taskmananger.models.ToDoItem
import com.example.taskmananger.repositories.ListCategoryRepository
import com.example.taskmananger.repositories.ListToDoRepository
import com.example.taskmananger.viewmodels.base.BaseViewModel
import java.util.*

class MainViewModel : BaseViewModel() {
    private val listCategory = MutableLiveData<MutableList<Category>>()
    private val listToDoItem = MutableLiveData<MutableList<ToDoItem>>()

    private var listCategoryRepository = ListCategoryRepository.getInstance()

    fun getListCategory() : MutableLiveData<MutableList<Category>>{
        return listCategory
    }

    fun getListToDoItem() : MutableLiveData<MutableList<ToDoItem>>{
        return listToDoItem
    }

    fun setListToDoItemByCategory(categoryName: String){
        val category = getCategoryByName(categoryName)
        listToDoItem.value =  category!!.listItem
    }

    fun loadList(){
        listCategory.value = listCategoryRepository.loadListCategory()
        listToDoItem.value = mutableListOf()
    }

    fun saveListCategory(){
        listCategoryRepository.saveListCategory(listCategory.value!!)
    }

    fun addCategory(category: Category){
        val currentList = listCategory.value
        currentList!!.add(category)
        listCategory.postValue(currentList)
    }

    fun removeCategory(category: Category){
        val currentList = listCategory.value
        currentList!!.remove(category)
        listCategory.postValue(currentList)
    }

    fun removeCategoryAt(category: Int){
        val currentList = listCategory.value
        currentList!!.removeAt(category)
        listCategory.postValue(currentList)
    }

    fun getCategoryByName(categoryName : String) : Category?{
        val currentList = listCategory.value
        for(category in currentList!!){
            if(category.name == categoryName){
                return category
            }
        }
        return null;
    }

    fun getCategoryAt(position: Int) : Category{
        return listCategory.value!![position]
    }

    fun handleSetToDoDone(id : UUID){
        val currentList = listToDoItem.value!!
        currentList.forEach {
            if(it.id == id){
                it.isDone = !it.isDone
            }
        }
        listToDoItem.postValue(currentList)
    }

    fun addToDo(toDoItem: ToDoItem){
        val currentList = listToDoItem.value!!
        currentList.add(toDoItem)
        listToDoItem.postValue(currentList)
    }

    fun removeToDoAt(position : Int){
        listToDoItem.value!!.removeAt(position)
    }

}