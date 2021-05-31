package com.example.movie.untils

import android.app.Application
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var instance: App
            private set

    }
    override fun onCreate(){
        super.onCreate()
        instance =this
    }
}