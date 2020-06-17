package com.example.taskmananger.views.fragments

import android.app.AlarmManager
import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.taskmananger.R
import com.example.taskmananger.models.ToDoItem
import com.example.taskmananger.viewmodels.MainViewModel
import androidx.lifecycle.Observer
import com.example.taskmananger.App
import com.example.taskmananger.utils.AlarmUtil
import kotlinx.android.synthetic.main.fragment_add_new_to_do.*
import kotlinx.android.synthetic.main.layout_date_time_picker.*
import java.util.*

class AddNewToDoFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var categoryName : String
    private lateinit var  alarmManager : AlarmManager

    companion object {
        private const val CATEGORY_NAME = "CATEGORY_NAME"
        fun newInstance(categoryName : String) : AddNewToDoFragment{
            val args = Bundle()
            args.putString(CATEGORY_NAME, categoryName)
            val fragment = AddNewToDoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_add_new_to_do,container,false)
        categoryName = arguments!!.getString(CATEGORY_NAME) as String
        activity?.let {
            mainViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()

        (spinner_category.editText as AutoCompleteTextView).setText(categoryName)
        spinner_category.isActivated = false
        alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //Set date and time for ToDo
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            // Appear TimePicker right after user has selected date
            val timePicker = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
                edittext_ToDoDateTime.setText("$dayOfMonth/${monthOfYear+1}/$year  $hour:$minute") //Display Selected date and time in textbox
            }
            TimePickerDialog(context, timePicker, hour, minute, true).show()
        }, year, month, day)

        //When user click on edittext_ToDoDateTime, a DatePickerDialog will appear for user to select
        edittext_ToDoDateTime.setOnClickListener {
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()
        }

        switch_remind.setOnCheckedChangeListener{ _, isCheck->
            if(isCheck){
                val toast : Toast = Toast.makeText(context,"Remind enable", Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                val toast : Toast = Toast.makeText(context,"Remind disable", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        button_add_new_ToDo.setOnClickListener {
            val category:String = (spinner_category.editText as? AutoCompleteTextView)?.text.toString()
            val title : String = edittext_ToDoName.text.toString()
            val description : String = edittext_ToDoDescription.text.toString()
            val stringDueDate :String = edittext_ToDoDateTime.text.toString()
            if(validateAddNewForm(category,title,stringDueDate)) {
                val newDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val dueDate : Date = newDateFormat.parse(stringDueDate)
                val place : String = edittext_ToDoPlace.text.toString()
                val hasRemider : Boolean = switch_remind.isChecked
                val newToDo = ToDoItem(title, category, description, hasRemider,null, dueDate, place)
                mainViewModel.addToDo(newToDo)
                if(hasRemider){
                    AlarmUtil.createAlarm(activity!!.applicationContext,newToDo,alarmManager)
                    Log.d("ALARM", "Created alarm ${newToDo.title}")
                }
                activity!!.supportFragmentManager.popBackStack()
            }
        }

        button_back_arrow.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
    }

    private fun validateAddNewForm(category:String,
                                   title:String,
                                   dueDate:String):Boolean{
        if(category.isEmpty()){
            val toast : Toast = Toast.makeText(context,
                "Please choose a category", Toast.LENGTH_SHORT)
            toast.show()
            return false
        }
        if(title.isEmpty()){
            val toast : Toast = Toast.makeText(context,
                "Title can not be empty", Toast.LENGTH_SHORT)
            toast.show()
            return false
        }
        if(dueDate.isEmpty()){
            val toast : Toast = Toast.makeText(context,
                "Please select a DateTime", Toast.LENGTH_SHORT)
            toast.show()
            return false
        }
        return true
    }

    private fun setupObserver(){
        mainViewModel.getListCategory().observe(viewLifecycleOwner, Observer {
            mainViewModel.saveListCategory()
        })
    }
}

