package com.febaisi.listenableworkerexample.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager

class Repository (private val context: Context) {

    fun getLocationWorkInfoLiveData(): LiveData<WorkInfo> {
        Log.i(Repository::class.toString(), "Dispatch listenable worker")
        val workManagerInstance = WorkManager.getInstance(context)
        val locationListenableWorker = OneTimeWorkRequest.Builder(LocationListenableWorker::class.java).build()

        workManagerInstance.cancelAllWork()
        workManagerInstance.enqueueUniqueWork("LOCATION_WORKER", ExistingWorkPolicy.KEEP, locationListenableWorker)
        return workManagerInstance.getWorkInfoByIdLiveData(locationListenableWorker.id)
    }
}