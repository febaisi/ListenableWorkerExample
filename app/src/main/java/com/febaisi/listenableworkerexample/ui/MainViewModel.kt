package com.febaisi.listenableworkerexample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import com.febaisi.listenableworkerexample.data.LocationListenableWorker
import com.febaisi.listenableworkerexample.data.Repository

class MainViewModel (private val repository: Repository)
    : ViewModel() {

    private val buttonEvent1 = MutableLiveData<String>()
    private val _lastRequestTime = MutableLiveData<String>()
    private val _lastLocation = MutableLiveData<String>()
    val lastRequestTime = _lastRequestTime as LiveData<String>
    val lastLocation = _lastLocation as LiveData<String>

    val workStateInfo: LiveData<String> = Transformations.switchMap(
        buttonEvent1,
        ::mapLocationWork
    )

    private fun mapLocationWork(name: String) =  Transformations.map(repository.getLocationWorkInfoLiveData()) { input: WorkInfo ->
        _lastRequestTime.value = ""
        _lastLocation.value =  ""

        when {
            input.state == WorkInfo.State.RUNNING -> "Working"
            input.state == WorkInfo.State.ENQUEUED -> "Enqueued"
            input.state == WorkInfo.State.SUCCEEDED -> {
                val outputData = input.outputData
                _lastRequestTime.value = convertTime(outputData.getLong(LocationListenableWorker.LOCATION_TIME, 0))
                _lastLocation.value =  "${outputData.getString(LocationListenableWorker.LOCATION_LAT)},${outputData.getString(LocationListenableWorker.LOCATION_LONG)}"
                "Work succeeded"
            }
            else -> "Something went wrong"
        }
    }


    fun getLocationWorkInfoLiveData() {
        buttonEvent1.value = "" // Fire map events
    }

}