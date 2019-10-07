package com.febaisi.listenableworkerexample

import android.app.Application
import com.febaisi.listenableworkerexample.data.Repository
import com.febaisi.listenableworkerexample.ui.CustomViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ListenableWorkerExampleApplication : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        /* lazy init to get context */
        bind() from singleton {
            Repository(
                applicationContext
            )
        }
        bind() from provider {
            CustomViewModelFactory(
                instance()
            )
        }
    }
}