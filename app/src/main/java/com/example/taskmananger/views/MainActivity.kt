package com.example.taskmananger.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.example.taskmananger.R
import com.example.taskmananger.models.Category
import com.example.taskmananger.viewmodels.MainViewModel
import com.example.taskmananger.viewmodels.ListToDoViewModel
import com.example.taskmananger.views.fragments.AddNewToDoFragment
import com.example.taskmananger.views.fragments.CategoryDetailFragment
import com.example.taskmananger.views.fragments.ListCategoryFragment

class MainActivity : AppCompatActivity(), ListCategoryFragment.OnClicked, CategoryDetailFragment.OnClicked {

    lateinit var mainViewModel: MainViewModel
    lateinit var listToDoViewModel: ListToDoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Hide status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        listToDoViewModel = ViewModelProviders.of(this).get(ListToDoViewModel::class.java)
        mainViewModel.loadList()
        listToDoViewModel.loadListToDo()

        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.root_layout,
                    ListCategoryFragment.newInstance(),"listCategory")
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        //Hide status bar when resume activity
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main,menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCategorySelected(category: Category) {
        val categoryDetailFragment = CategoryDetailFragment.newInstance(category)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,R.anim.slide_in_right,R.anim.slide_out_right)
            .replace(R.id.root_layout, categoryDetailFragment, "categoryDetail")
            .addToBackStack(null)
            .commit()
    }

    override fun fabAddCategoryClicked(category: Category) {
        val categoryDetailFragment = CategoryDetailFragment.newInstance(category)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,R.anim.slide_in_right,R.anim.slide_out_right)
            .replace(R.id.root_layout, categoryDetailFragment, "addNewCategory")
            .addToBackStack(null)
            .commit()
    }

    override fun fabAddToDoClicked(categoryName : String) {
        val addNewToDoFragment = AddNewToDoFragment.newInstance(categoryName)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,R.anim.slide_in_right,R.anim.slide_out_right)
            .replace(R.id.root_layout,addNewToDoFragment,"addNewToDo")
            .addToBackStack(null)
            .commit()
    }

}
