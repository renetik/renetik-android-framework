package renetik.android.framework.sample

import android.os.Bundle
import renetik.android.controller.base.CSViewActivity

class MainActivity : CSViewActivity<MainView>() {
    override fun createView() = MainView(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
    }
}
