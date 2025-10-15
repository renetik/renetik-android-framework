package renetik.android.controller.base

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import renetik.android.core.kotlin.className
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSHasContext
import renetik.android.event.common.destruct
import renetik.android.event.fire
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.CSRegistrationsMap
import renetik.android.ui.extensions.fixInputMethodManagerLeak
import renetik.android.ui.protocol.CSViewInterface
import renetik.android.ui.protocol.CSVisibility

abstract class CSActivity : AppCompatActivity(), CSVisibility, CSHasContext, CSViewInterface {
    val onActivityResult = event<CSActivityResult>()
    val onRequestPermissionsResult = event<CSRequestPermissionResult>()

    //CSVisibility
    override val isVisible = property(true)

    //CSViewInterface
    override val view: View get() = window.decorView

    //CSHasContext
    override val context: Context get() = this

    override fun recreate() {
        destruct()
        super.recreate()
    }

    final override val registrations = CSRegistrationsMap(className)
    final override val eventDestruct = event<Unit>()
    override val isDestructed: Boolean get() = registrations.isCanceled
    override fun onDestruct() {
        registrations.cancel()
        eventDestruct.fire().clear()
        logInfo()
        fixInputMethodManagerLeak()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (!isDestructed) destruct()
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResult.fire(CSActivityResult(requestCode, resultCode, data))
        super.onActivityResult(requestCode, resultCode, data)
        logInfo("$requestCode $resultCode $data")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult.fire(
            CSRequestPermissionResult(requestCode, permissions, grantResults)
        )
        logInfo("$requestCode $permissions $grantResults")
    }
}
