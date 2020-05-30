package com.example.taskmananger.viewmodels

import androidx.lifecycle.MutableLiveData
import com.example.taskmananger.models.Category
import com.example.taskmananger.models.ToDoItem
import com.example.taskmananger.repositories.ListCategoryRepository
import com.example.taskmananger.repositories.ListToDoRepository
import com.example.taskmananger.viewmodels.base.BaseViewModel
import java.text.FieldPosition

class ListToDoViewModel : BaseViewModel() {
    val listItem = MutableLiveData<MutableList<ToDoItem>>()
    private lateinit var listToDoRepository : ListToDoRepository

    fun loadListToDo(){
        listToDoRepository = ListToDoRepository.getInstance()
        listItem.value = listToDoRepository.getToDoList()
    }

    fun saveListToDo(){
        listToDoRepository = ListToDoRepository.getInstance()
        listToDoRepository.saveToDoList(listItem.value!!)
    }

    fun addToDo(item : ToDoItem){
        val currentList = listItem.value
        currentList!!.add(item)
        listItem.postValue(currentList)
    }

    fun removeToDo(item : ToDoItem){
        val curentList = listItem.value
        curentList!!.remove(item)
        listItem.postValue(curentList)
    }

    fun filterByCategory(categoryName : String) : List<ToDoItem>{
        val resultList = mutableListOf<ToDoItem>()
        val currentList = listItem.value
        for(item in currentList!!){
            if(item.category == categoryName){
                resultList.add(item)
            }
        }
        return resultList
    }

}