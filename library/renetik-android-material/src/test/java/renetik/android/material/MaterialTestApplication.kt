package renetik.android.material

import android.app.Application

class MaterialTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setTheme(com.google.android.material.R.style.Theme_MaterialComponents_DayNight)
    }
}
