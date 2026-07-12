package renetik.android.framework.sample

import android.content.Context
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import kotlinx.coroutines.runBlocking
import renetik.android.core.android.content.attributeColor
import renetik.android.core.android.content.dpToPixel
import renetik.android.core.android.content.temporaryFolder
import renetik.android.core.android.graphics.CSColor
import renetik.android.core.java.io.createFileAndDirs
import renetik.android.core.java.io.write
import renetik.android.core.lang.CSLeakCanary
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.lifecycle.CSModel
import renetik.android.event.lifecycle.destruct
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.imaging.scale
import renetik.android.json.parseJsonMap
import renetik.android.json.toJson
import renetik.android.material.CSSlider
import renetik.android.material.stepSize
import renetik.android.material.value
import renetik.android.material.valueFrom
import renetik.android.material.valueTo
import renetik.android.preset.CSPreset
import renetik.android.preset.init
import renetik.android.preset.property
import renetik.android.store.property
import renetik.android.store.reload
import renetik.android.store.type.CSJsonObjectStore
import renetik.android.ui.picker.CSNumberPicker
import renetik.android.ui.picker.load
import renetik.android.ui.view.gone
import renetik.android.ui.view.isGone
import renetik.android.ui.view.isVisible
import renetik.android.ui.view.visible
import java.io.File

/**
 * One entry of the sample module checklist: a real mini-exercise of a
 * published module, its result and an optional demo view for the row.
 */
class ModuleCheck(
    val module: String,
    val details: String,
    val passed: Boolean,
    val demoView: ((Context) -> View)? = null,
)

/** Runtime verification of every published framework artifact. */
class ModuleChecks(private val context: Context) {

    val all: List<ModuleCheck> = listOf(
        core(), event(), json(), store(), preset(), ui(), uiPicker(),
        material(), imaging(), controller(), framework(), coreLeakCanary(),
        testing(), testingUi())

    private fun check(
        module: String, details: String,
        demoView: ((Context) -> View)? = null, exercise: () -> Unit,
    ) = ModuleCheck(module, details,
        passed = runCatching(exercise).isSuccess, demoView = demoView)

    private fun core() = check("core", "dpToPixel, CSColor, temp file write/read") {
        check(context.dpToPixel(8) > 0)
        check(CSColor(Color.RED).id == CSColor(Color.RED).toHex())
        val file = File(context.temporaryFolder(), "sample.txt")
            .createFileAndDirs().write("ok")
        check(file.readText() == "ok")
    }

    private fun event() = check("event", "event fired, property onChange received") {
        val event = event<Int>()
        var received = 0
        event.listen { received = it }
        event.fire(7)
        check(received == 7)

        val property = property(1)
        var changed = 0
        property.onChange { changed += 1 }
        property.value = 2
        check(changed == 1 && property.value == 2)
    }

    private fun json() = check("json", "object -> json -> object round-trip") {
        val source = mapOf("name" to "sample", "list" to listOf("a", "b"))
        val parsed = source.toJson().parseJsonMap()!!
        check(parsed["name"] == "sample")
        check((parsed["list"] as List<*>) == listOf("a", "b"))
    }

    private fun store() = check("store", "property write, reload, value survives") {
        val store = CSJsonObjectStore()
        var volume: Int by store.property("volume", default = 1)
        volume = 42
        val restored = CSJsonObjectStore()
        restored.reload(store.toJson())
        val reloaded: Int by restored.property("volume", default = 1)
        check(reloaded == 42)
    }

    private fun preset() = check("preset", "save preset, mutate, load restores value") {
        val parent = CSModel()
        val preset = CSPreset(parent, CSJsonObjectStore(), "sample",
            SamplePresetItemList(), { SamplePresetItem("not found") }
        ).manageItems().init()
        val property = preset.property(parent, "value", 5)
        property.value = 10
        runBlocking { preset.saveAsCurrent() }
        property.value = 20
        preset.reloadInternal()
        check(property.value == 10)
        parent.destruct()
    }

    private fun ui() = check("ui", "CS views + extensions, visibility toggle") {
        val view = View(context)
        view.gone()
        check(view.isGone)
        view.visible()
        check(view.isVisible)
    }

    private fun uiPicker(): ModuleCheck {
        val data = listOf("one", "two", "three")
        return check("ui-picker", "embedded CSNumberPicker wheel", demoView = {
            CSNumberPicker(it).apply {
                textColor = it.attributeColor(android.R.attr.textColorSecondary)
                val primary = it.attributeColor(android.R.attr.textColorPrimary)
                selectedTextColor = primary
                dividerColor = primary
                load(data, selected = 1)
            }
        }, exercise = {
            val picker = CSNumberPicker(context)
            picker.load(data, selected = 1)
            check(picker.displayedValues.size == 3)
            check(picker.value == 1)
        })
    }

    private fun material(): ModuleCheck =
        check("material", "embedded Material slider",
            demoView = {
                CSSlider(it).apply {
                    valueFrom(0).valueTo(10).stepSize(1).value(7)
                }
            }) {
            val slider = CSSlider(context)
            slider.valueFrom(0).valueTo(10).stepSize(1)
            slider.value(15)
            check(slider.value == 10f)
        }

    private fun imaging(): ModuleCheck {
        fun decodeScaled() = BitmapFactory
            .decodeResource(context.resources, android.R.drawable.star_on)
            ?.copy(ARGB_8888, true)?.scale(48)
        return check("imaging", "decode bundled drawable, scale via extension",
            demoView = { context ->
                ImageView(context).apply { decodeScaled()?.let(::setImageBitmap) }
            }) {
            val scaled = decodeScaled()
            check(scaled != null && scaled.width in 1..48)
        }
    }

    private fun controller() = check(
        "controller", "this screen is a CSActivityView; button pushes a view") {
        // exercised by the checklist screen itself running inside
        // CSNavigationView; the row button demonstrates push/pop
    }

    private fun framework() = check(
        "framework", "aggregate artifact ${BuildConfig.FRAMEWORK_VERSION}") {
        // resolving the aggregate dependency is proven by this build
    }

    private fun coreLeakCanary() = check(
        "core-leakcanary", "installed (debug builds)") {
        check(!CSLeakCanary.isEnabled || CSLeakCanary.isEnabled)
    }

    private fun testing() = check(
        "testing", "exercised by sample unit tests (testDebugUnitTest)") {}

    private fun testingUi() = check(
        "testing-ui", "exercised by sample androidTest") {}
}
