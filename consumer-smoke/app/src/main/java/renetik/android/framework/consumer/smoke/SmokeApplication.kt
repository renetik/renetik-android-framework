package renetik.android.framework.consumer.smoke

import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.runBlocking
import renetik.android.core.android.content.dpToPixel
import renetik.android.core.android.content.temporaryFolder
import renetik.android.core.android.graphics.CSColor
import renetik.android.core.android.graphics.setAlpha
import renetik.android.core.base.CSApplication
import renetik.android.core.java.io.createFileAndDirs
import renetik.android.core.java.io.write
import renetik.android.core.kotlin.primitives.leaveEndOfLength
import renetik.android.core.lang.atomic.CSAtomic.Companion.atomic
import renetik.android.core.lang.atomic.CSProducerConsumerData
import renetik.android.core.lang.result.CSResult
import renetik.android.core.lang.variable.CSVariable.Companion.variable
import renetik.android.core.lang.variable.plusAssign
import renetik.android.core.math.CSPoint
import java.io.File

class SmokeApplication : CSApplication<AppCompatActivity>() {

    private var state by atomic("created")
    override val activityClass = AppCompatActivity::class

    override fun onCreate() {
        super.onCreate()

        state = "started".leaveEndOfLength(7)
        check(state == "started")
        check(dpToPixel(1) >= 0)
        check(CSColor(Color.RED).id == CSColor(Color.RED).toHex())
        check(Color.RED.setAlpha(0.5f) != Color.RED)

        val rect = Rect(0, 0, 1, 1)
        val point = CSPoint(1, 2)
        check(rect.width() == point.x)

        val variable = variable<Float?>(1f)
        variable += 2f
        check(variable.value == 3f)

        val data = CSProducerConsumerData { mutableListOf<String>() }
        data.prepare(mutableListOf("prepared"))
        data.swap()
        check(data.active.single() == "prepared")

        val folder = temporaryFolder()
        File(folder, "smoke.txt").createFileAndDirs().write("ok")

        runBlocking {
            CSResult.failure<Unit>("expected").ifFailure {
                check(it.message == "expected")
            }
        }
    }
}
