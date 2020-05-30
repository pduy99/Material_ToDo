package com.example.taskmananger.repositories

import com.example.taskmananger.R
import com.example.taskmananger.models.Category
import com.example.taskmananger.sharedpreference.SharedPrefs

class ListCategoryRepository {
    companion object {
        private var INSTANCE: ListCategoryRepository? = null
        fun getInstance() = INSTANCE
            ?: ListCategoryRepository().also {
                INSTANCE = it
            }
    }

    fun loadListCategory() : MutableList<Category>{
        val resultList = SharedPrefs.listCategory
        if(resultList.size == 0) {
            resultList.add(Category("My Day", R.drawable.icon_sun,R.drawable.category_myday_background,0));
            resultList.add(Category("Important", R.drawable.icon_star,R.drawable.category_important_background,0));
            resultList.add(Category("Work", R.drawable.icon_suitcase, R.drawable.category_work_background,0));
            resultList.add(Category("Study", R.drawable.icon_book, R.drawable.category_study_background,0));
            resultList.add(Category("Home", R.drawable.icon_home,R.drawable.category_home_background,0));
            resultList.add(Category("Travel", R.drawable.icon_plane,R.drawable.category_travel_background,0));
            resultList.add(Category("Love", R.drawable.icon_heart,R.drawable.category_love_background,0));
        }
        return resultList
    }

    fun saveListCategory(listCategory: MutableList<Category>){
        SharedPrefs.listCategory = listCategory
    }
}