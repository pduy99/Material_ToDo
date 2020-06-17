package com.example.taskmananger.views.fragments

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.taskmananger.R
import com.example.taskmananger.models.Category
import com.example.taskmananger.viewmodels.MainViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import java.io.Serializable
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.example.taskmananger.adapters.ColorHorizontalSliderAdapter
import com.example.taskmananger.adapters.ImageHorizontalSliderAdapter
import com.example.taskmananger.adapters.ToDoRecyclerViewAdapter
import com.example.taskmananger.utils.AlarmUtil
import com.example.taskmananger.utils.Constant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.ClassCastException
import java.lang.Exception

class CategoryDetailFragment : Fragment(){

    private lateinit var mainViewModel: MainViewModel
    private lateinit var toolbar : Toolbar
    private lateinit var collapsingToolbarLayout : CollapsingToolbarLayout
    private lateinit var imageHorizontalSliderAdapter : ImageHorizontalSliderAdapter
    private lateinit var colorHorizontalSliderAdapter: ColorHorizontalSliderAdapter
    private lateinit var iconHorizontalSliderAdapter: ImageHorizontalSliderAdapter
    private lateinit var listToDoRecyclerViewAdapter: ToDoRecyclerViewAdapter
    private lateinit var categoryListToDoRecyclerView : RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryBackgroundPickerLayout : LinearLayoutManager
    private lateinit var categoryIconPickerLayout : GridLayoutManager
    private lateinit var fabAddNewToDo : FloatingActionButton
    private lateinit var alarmManager: AlarmManager
    lateinit var categoryDetailLayout : CoordinatorLayout
    lateinit var category: Category
    private lateinit var listener : OnClicked

    companion object {
        private const val CATEGORY = "Category"
        fun newInstance(category: Category): CategoryDetailFragment {
            val args = Bundle()
            args.putSerializable(CATEGORY, category as Serializable)
            val fragment =
                CategoryDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnClicked) listener = context else{
            throw ClassCastException("$context must implement OnClicked")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_category_detail,container,false)
        category = arguments!!.getSerializable(CATEGORY) as Category
        categoryDetailLayout = view.findViewById(R.id.category_detail_layout)
        categoryListToDoRecyclerView = view.findViewById(R.id.category_list_todo_recyclerview)
        toolbar = view.findViewById(R.id.category_detail_toolbar)
        activity?.let {
            mainViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
        }
        mainViewModel.setListToDoItemByCategory(category.name)
        collapsingToolbarLayout = view.findViewById(R.id.category_detail_collapsetoolbar)
        fabAddNewToDo = view.findViewById(R.id.btnAddToDo)
        alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout(category)
        setupObserver()
        initRecyclerView()

        if(category.name == "Untitled") {
            createNewCategory()
        }

        fabAddNewToDo.setOnClickListener {
            listener.fabAddToDoClicked(category.name)
        }

    }

    private fun initRecyclerView(){
        listToDoRecyclerViewAdapter = ToDoRecyclerViewAdapter(context!!,mainViewModel.getListToDoItem().value!!,mainViewModel){
            if(it.hasReminder) {
                AlarmUtil.cancelAlarm(context!!, it, alarmManager)
                Toast.makeText(context,"Deleted reminder ${it.title}",Toast.LENGTH_SHORT).show()
            }
        }
        listToDoRecyclerViewAdapter.swipeToDeleteDelegate.pending = true
        categoryListToDoRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        categoryListToDoRecyclerView.adapter = listToDoRecyclerViewAdapter
        val itemTouchHelper = ItemTouchHelper(listToDoRecyclerViewAdapter.swipeToDeleteDelegate.itemTouchCallBack)
        itemTouchHelper.attachToRecyclerView(categoryListToDoRecyclerView)
    }

    private fun setupObserver(){
        mainViewModel.getListCategory().observe(viewLifecycleOwner, Observer {
            mainViewModel.saveListCategory()
        })

        mainViewModel.getListToDoItem().observe(viewLifecycleOwner, Observer {
            mainViewModel.saveListCategory()
            listToDoRecyclerViewAdapter.notifyDataSetChanged()
        })
    }

    private fun setupLayout(category: Category){
        //Setup the Toolbar
        collapsingToolbarLayout.title = category.name
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setLogo(category.icon)
            (activity as AppCompatActivity).supportActionBar?.setDisplayUseLogoEnabled(true)
        }
        //Setup background
        try{
            val color = ContextCompat.getColor(context!!,category.background)
            categoryDetailLayout.setBackgroundColor(color)
        }catch(exception : Exception){
            categoryDetailLayout.setBackgroundResource(category.background)
        }
    }

    //Show dialog to create new category
    private fun createNewCategory(){
        val dialog = MaterialDialog(context!!).show {
            title(R.string.new_catergory)
            noAutoDismiss()
            setOnCancelListener {
                activity!!.supportFragmentManager.popBackStack()
                //Already add to listCategory when create fragment
                //Need remove it when cancel add new category
                mainViewModel.removeCategory(category)
            }
            cancelOnTouchOutside(false)
            customView(
                R.layout.dialog_add_new_category,
                scrollable = true,
                horizontalPadding = true
            )
            positiveButton(R.string.create_category) { dialog ->
                val categoryName = (dialog.getCustomView()
                    .findViewById(R.id.editext_new_category_name) as EditText).text.toString()
                if (categoryName == "") {
                    Toast.makeText(
                        activity,
                        "Name cannot empty !",
                        Toast.LENGTH_SHORT
                    ).show()
                    noAutoDismiss()
                }
                else{
                    //Already add to listCategory when create fragment
                    //If call mainViewModel.add will leads to add duplicate category
                    category.name = categoryName
                    collapsingToolbarLayout.title = categoryName
                    (activity as AppCompatActivity).supportActionBar?.setLogo(category.icon)
                    dismiss()
                }
            }
            negativeButton(R.string.cancel){
                dismiss()
                //Already add to listCategory when create fragment
                //Need remove it when cancel add new category
                mainViewModel.removeCategory(category)
                activity!!.supportFragmentManager.popBackStack()
            }
        }

        //category background recyclerview in dialog
        imageHorizontalSliderAdapter = ImageHorizontalSliderAdapter(context!!, Constant.intArrayCategoryBackground).apply {
            callback = object :ImageHorizontalSliderAdapter.CallBack{
                override fun onItemClicked(resId: Int) {
                    categoryDetailLayout.setBackgroundResource(resId)
                    category.background = resId
                }
            }
        }

        colorHorizontalSliderAdapter = ColorHorizontalSliderAdapter(context!!,Constant.intArrayColor).apply{
            callback =  object : ColorHorizontalSliderAdapter.CallBack{
                override fun onItemClicked(resId: Int) {
                    val color = ContextCompat.getColor(context,resId)
                    categoryDetailLayout.setBackgroundColor(color)
                    category.background = resId
                }

            }
        }

        iconHorizontalSliderAdapter = ImageHorizontalSliderAdapter(context!!,Constant.intArrayIcon).apply {
            callback = object : ImageHorizontalSliderAdapter.CallBack{
                override fun onItemClicked(resId: Int) {
                    dialog.getCustomView().findViewById<ImageButton>(R.id.button_insert_icon).setImageResource(resId)
                    category.icon = resId
                }
            }
        }

        categoryBackgroundPickerLayout = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL,false)
        categoryIconPickerLayout = GridLayoutManager(context!!,5,GridLayoutManager.VERTICAL,false)
        recyclerView = dialog.getCustomView().findViewById(R.id.category_image_background_recyclerview)
        recyclerView.layoutManager = categoryBackgroundPickerLayout
        recyclerView.adapter = imageHorizontalSliderAdapter

        // Change data set in recyclerview
        dialog.getCustomView().findViewById<Button>(R.id.button_image_background).setOnClickListener {
            if(recyclerView.adapter != imageHorizontalSliderAdapter){
                recyclerView.layoutManager = categoryBackgroundPickerLayout
                recyclerView.adapter = imageHorizontalSliderAdapter
            }
        }
        dialog.getCustomView().findViewById<Button>(R.id.button_color_background).setOnClickListener {
            if(recyclerView.adapter != colorHorizontalSliderAdapter){
                recyclerView.layoutManager = categoryBackgroundPickerLayout
                recyclerView.adapter = colorHorizontalSliderAdapter
            }
        }
        dialog.getCustomView().findViewById<ImageButton>(R.id.button_insert_icon).setOnClickListener{
            if(recyclerView.adapter != iconHorizontalSliderAdapter){
                recyclerView.layoutManager = categoryIconPickerLayout
                recyclerView.adapter = iconHorizontalSliderAdapter
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            activity!!.supportFragmentManager.popBackStack()
            Toast.makeText(context,"Popback", Toast.LENGTH_LONG)
        }
        return super.onOptionsItemSelected(item)
    }

    interface OnClicked{
        fun fabAddToDoClicked(categoryName : String)
    }
}
