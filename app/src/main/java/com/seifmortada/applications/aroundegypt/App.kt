package com.seifmortada.applications.aroundegypt

import android.app.Application
import com.seifmortada.applications.aroundegypt.core.di.coreModule
import com.seifmortada.applications.aroundegypt.core.di.networkModule
import com.seifmortada.applications.aroundegypt.detail.di.detailModule
import com.seifmortada.applications.aroundegypt.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(coreModule, homeModule, networkModule, detailModule)
        }
    }
}