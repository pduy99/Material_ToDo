package com.example.taskmananger.repositories

import com.example.taskmananger.models.ToDoItem
import com.example.taskmananger.sharedpreference.SharedPrefs

class ListToDoRepository {
    companion object {
        private var INSTANCE: ListToDoRepository? = null
        fun getInstance() = INSTANCE
            ?: ListToDoRepository().also {
                INSTANCE = it
            }
    }

    fun saveToDoList(listToDo : MutableList<ToDoItem>){
        SharedPrefs.listToDO = listToDo
    }

    fun getToDoList() : MutableList<ToDoItem>{
        return SharedPrefs.listToDO
    }
}