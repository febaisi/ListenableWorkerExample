package com.febaisi.listenableworkerexample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.febaisi.listenableworkerexample.data.Repository

class CustomViewModelFactory(private val repository: Repository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}