package com.example.taskmananger.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.taskmananger.App


import com.example.taskmananger.R
import com.example.taskmananger.adapters.CategoryRecylerViewAdapter
import com.example.taskmananger.databinding.FragmentListCategoryBinding
import com.example.taskmananger.models.Category
import com.example.taskmananger.viewmodels.MainViewModel

class ListCategoryFragment : Fragment(), CategoryRecylerViewAdapter.OnItemSelected{

    lateinit var categoryRecyclerViewAdapter : CategoryRecylerViewAdapter
    lateinit var mainViewModel: MainViewModel
    lateinit var listCategoryFragmentBinding : FragmentListCategoryBinding
    lateinit var listener : OnClicked

    companion object{
        fun newInstance() : ListCategoryFragment {
            return ListCategoryFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClicked) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnClicked.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listCategoryFragmentBinding = FragmentListCategoryBinding.inflate(inflater,container,false).apply {
            activity?.let {
                mainViewModel = ViewModelProvider(it).get(MainViewModel::class.java)
            }
            lifecycleOwner = viewLifecycleOwner
        }

        listCategoryFragmentBinding.btnAddCategory.setOnClickListener {
            val category = Category("Untitled",R.drawable.icon_list,R.color.background_color,1)
            mainViewModel.addCategory(category)
            listener.fabAddCategoryClicked(category)
        }

        return listCategoryFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //listCategoryViewModel.loadList()

        initRecycler()
        setupObserver()
    }

    private fun initRecycler(){
        val listCategory = mainViewModel.getListCategory().value
        categoryRecyclerViewAdapter = CategoryRecylerViewAdapter(listCategory!!,this)
        listCategoryFragmentBinding.recylerviewCategory.layoutManager =  GridLayoutManager(App.appContext,2,GridLayoutManager.VERTICAL,false);
        listCategoryFragmentBinding.recylerviewCategory.adapter = categoryRecyclerViewAdapter
    }

    private fun setupObserver(){
        mainViewModel.getListCategory().observe(viewLifecycleOwner, Observer {
            mainViewModel.saveListCategory()
            categoryRecyclerViewAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_main,menu);
        return super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onItemSelected(position: Int) {
        val category = mainViewModel.getCategoryAt(position)
        listener.onCategorySelected(category)
    }

    override fun onItemLongClick(position: Int): Boolean {
        val category = mainViewModel.getListCategory().value!![position]
        if(category.type == 0){
            MaterialDialog(context!!).show {
                title(R.string.error)
                message(R.string.cannot_delete_default_category)
                positiveButton(R.string.ok)
            }
        }
        else {
            MaterialDialog(context!!).show {
                title(R.string.delete_category_title)
                message(R.string.delete_category_warning)
                positiveButton(R.string.delete){
                    mainViewModel.removeCategory(category)
                }
                negativeButton(R.string.cancel)
            }
        }
        return true
    }

    interface OnClicked {
        fun onCategorySelected(category: Category)
        fun fabAddCategoryClicked(category: Category)
    }

}
