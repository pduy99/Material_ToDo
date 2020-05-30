package com.example.taskmananger.viewmodels.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    val isEmpty = MutableLiveData<Boolean>().apply { value = false }
    val isLoading = MutableLiveData<Boolean>().apply { value = false }
}