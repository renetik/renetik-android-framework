package renetik.android.core.base

import androidx.appcompat.app.AppCompatActivity

class CSTestApplication : CSApplication<AppCompatActivity>() {
    override val activityClass = AppCompatActivity::class
}