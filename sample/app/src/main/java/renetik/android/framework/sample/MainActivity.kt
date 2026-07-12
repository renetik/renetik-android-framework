package renetik.android.framework.sample

import android.os.Bundle
import androidx.core.view.WindowCompat
import renetik.android.controller.base.CSViewActivity
import renetik.android.core.android.content.res.isDarkMode

class MainActivity : CSViewActivity<MainView>() {
    override fun createView() = MainView(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = !resources.configuration.isDarkMode
        setContentView()
    }
}
