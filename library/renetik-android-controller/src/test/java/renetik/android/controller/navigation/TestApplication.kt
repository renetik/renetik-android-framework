package renetik.android.controller.navigation

import android.app.Application

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setTheme(androidx.appcompat.R.style.Theme_AppCompat)
    }
}