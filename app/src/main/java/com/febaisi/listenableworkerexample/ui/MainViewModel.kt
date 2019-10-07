package com.febaisi.listenableworkerexample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import com.febaisi.listenableworkerexample.data.Repository

class MainViewModel (private val repository: Repository)
    : ViewModel() {

    fun getLocationWorkInfoLiveData(): LiveData<WorkInfo> {
        return repository.getLocationWorkInfoLiveData()
    }

}