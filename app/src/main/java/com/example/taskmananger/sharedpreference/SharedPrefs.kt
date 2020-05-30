package com.example.taskmananger.sharedpreference

import android.content.ContentProviderOperation
import android.content.Context
import android.content.SharedPreferences
import com.example.taskmananger.models.Category
import com.example.taskmananger.models.ToDoItem
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.Exception

object SharedPrefs {
    private const val NAME = "com.example.taskmanager.ListData_Prefs"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences : SharedPreferences

    //List of specific preferences
    private val LIST_ITEM = Pair("LIST_ITEM", mutableListOf<ToDoItem>())
    private val LIST_CATEGORY = Pair("LIST_CATEGORY", mutableListOf<Category>())

    fun init(context: Context){
        preferences = context.getSharedPreferences(NAME,MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor)->Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var listToDO : MutableList<ToDoItem>
        get(){
            return try{
                val jsonString = preferences.getString(LIST_ITEM.first,null)
                val turnsType = object : TypeToken<MutableList<ToDoItem>>() {}.type
                GsonBuilder().create().fromJson(jsonString, turnsType)
            }catch (ex : Exception){
                ex.printStackTrace()
                mutableListOf()
            }
        }

        set(value){
            val jsonString = GsonBuilder().create().toJson(value)
            preferences.edit{
                it.putString(LIST_ITEM.first,jsonString)
            }
        }

    var listCategory : MutableList<Category>
        get(){
            return try{
                val jsonString = preferences.getString(LIST_CATEGORY.first,null)
                val turnsType = object : TypeToken<MutableList<Category>>() {}.type
                GsonBuilder().create().fromJson(jsonString,turnsType)
            }catch (ex : Exception){
                ex.printStackTrace()
                mutableListOf()
            }
        }

        set(value){
            val jsonString = GsonBuilder().create().toJson(value)
            preferences.edit{
                it.putString(LIST_CATEGORY.first,jsonString)
            }
        }
}