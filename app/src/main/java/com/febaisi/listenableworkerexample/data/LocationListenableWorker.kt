package com.febaisi.listenableworkerexample.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.core.app.ActivityCompat
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class LocationListenableWorker(appContext: Context, workerParams: WorkerParameters) :
    ListenableWorker(appContext, workerParams) {

    companion object { // Also available to JAVA classes
        @JvmStatic
        val LOCATION_LAT = "LOCATION_LAT"

        @JvmStatic
        val LOCATION_LONG = "LOCATION_LONG"

        @JvmStatic
        val LOCATION_TIME = "LOCATION_TIME"
    }

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture { completer ->
            CoroutineScope(Default).launch {
                //Getting out of MAIN Thread to set up location listener
                //This scope will be finished after setting up the listeners (Not wait through the final response)
                getLocation(completer)
                Log.i(LocationListenableWorker::class.toString(), "Global score finished")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(completer: CallbackToFutureAdapter.Completer<Result>) {
        if (isPermissionGranted()) {
            LocationServices.getFusedLocationProviderClient(applicationContext)
                ?.lastLocation
                ?.addOnSuccessListener { location: Location? ->
                    //MAIN THREAD - MINIMAL WORK
                    //YOU MIGHT WANT TO PASS AN EXECUTOR TO THE ONSUCCESSLISTENER IF YOU HAVE A LOT OF WORK TO DO
                    if (location != null) {
                        completer.set(Result.success(createOutputData(location)))
                    } else {
                        Log.e(
                            LocationListenableWorker::class.toString(),
                            "Location null - Make sure you had used location before - Try in a real phone."
                        )
                        completer.set(Result.success(Data.EMPTY))
                    }
                }
                ?.addOnFailureListener { exception ->
                    Log.e(
                        LocationListenableWorker::class.toString(),
                        "Something went wrong getting the location -> $exception"
                    )
                    completer.set(Result.failure(Data.EMPTY))
                }
        }
    }

    private fun createOutputData(location: Location): Data {
        return Data.Builder()
            .putString(LOCATION_LAT, location.latitude.toString())
            .putString(LOCATION_LONG, location.longitude.toString())
            .putLong(LOCATION_TIME, location.time)
            .build()
    }

    private fun isPermissionGranted(): Boolean {
        return !(ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)
    }

}