package com.example.taskmananger.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.example.taskmananger.R
import com.example.taskmananger.models.Category
import com.example.taskmananger.utils.openFragment
import com.example.taskmananger.viewmodels.MainViewModel
import com.example.taskmananger.viewmodels.ListToDoViewModel
import com.example.taskmananger.views.fragments.AddNewToDoFragment
import com.example.taskmananger.views.fragments.CategoryDetailFragment
import com.example.taskmananger.views.fragments.ListCategoryFragment

class MainActivity : AppCompatActivity(), ListCategoryFragment.OnClicked, CategoryDetailFragment.OnClicked {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var listToDoViewModel: ListToDoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        listToDoViewModel = ViewModelProvider(this).get(ListToDoViewModel::class.java)
        mainViewModel.loadList()
        listToDoViewModel.loadListToDo()

        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.root_layout,
                    ListCategoryFragment.newInstance(),"listCategory")
                .commit()
        }

        //If app is launched when user clicked on notification then navigate them to suitable fragment
        val destinationFragment = intent.getStringExtra("destinationFragment")
        if (destinationFragment != null) {
            val category: Category = mainViewModel.getCategoryByName(destinationFragment)!!
            openFragment(CategoryDetailFragment.newInstance(category), "categoryDetail")
        }

        //Hide status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
        openFragment(CategoryDetailFragment.newInstance(category), "categoryDetail")
    }

    override fun fabAddCategoryClicked(category: Category) {
        openFragment(CategoryDetailFragment.newInstance(category), "addNewCategory")
    }

    override fun fabAddToDoClicked(categoryName: String) {
        openFragment(AddNewToDoFragment.newInstance(categoryName), "addNewToDo")
    }

}
