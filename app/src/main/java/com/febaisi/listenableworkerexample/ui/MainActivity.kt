package com.febaisi.listenableworkerexample.ui

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.WorkInfo
import com.febaisi.listenableworkerexample.R
import com.febaisi.listenableworkerexample.data.LocationListenableWorker
import com.febaisi.listenableworkerexample.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CustomViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        requestFullLocationPermission()
    }

    private fun initUI() {
        val mainViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MainViewModel::class.java)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {
            this.lifecycleOwner = this@MainActivity
            this.viewmodel = mainViewModel
        }

        buttonRequestLocation.setOnClickListener {
            mainViewModel.getLocationWorkInfoLiveData().observe(this, Observer { workInfo ->
                if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                    val outputData = workInfo.outputData
                    tvLocationDirection.text = "${outputData.getString(LocationListenableWorker.LOCATION_LAT)},${outputData.getString(
                        LocationListenableWorker.LOCATION_LONG)}"
                    tvLocationTimeStamp.text = convertTime(outputData.getLong(
                        LocationListenableWorker.LOCATION_TIME, 0))
                } else if (workInfo?.state == WorkInfo.State.FAILED) {
                    tvLocationDirection.text = getString(R.string.something_went_wrong)
                    tvLocationTimeStamp.text = getString(R.string.check_logs)
                }
            })
        }
    }

    private fun convertTime(longDate: Long) = SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Date(longDate))

    private fun requestFullLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            1)
    }
}
