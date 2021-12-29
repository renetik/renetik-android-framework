package renetik.android.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import renetik.android.R
import renetik.android.framework.event.CSEventRegistration
import renetik.android.framework.event.CSMultiEventRegistration
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.lang.property.toggle
import renetik.android.framework.view.adapter.CSClickAdapter
import renetik.android.primitives.isFalse
import renetik.android.primitives.isTrue
import renetik.kotlin.isNull

fun <T : View> View.findView(@IdRes id: Int): T? = findViewById(id)
fun View.view(@IdRes id: Int) = findView<View>(id)!!
fun View.editText(@IdRes id: Int) = findView<EditText>(id)!!
fun View.textView(@IdRes id: Int) = findView<TextView>(id)!!
fun View.scrollView(@IdRes id: Int) = findView<ScrollView>(id)!!
fun View.horizontalScroll(@IdRes id: Int) = findView<HorizontalScrollView>(id)!!
fun View.listView(@IdRes id: Int) = findView<ListView>(id)!!
fun View.radio(@IdRes id: Int) = findView<RadioButton>(id)!!
fun View.datePicker(@IdRes id: Int) = findView<DatePicker>(id)!!
fun View.numberPicker(@IdRes id: Int) = findView<NumberPicker>(id)!!
fun View.frame(@IdRes id: Int) = findView<FrameLayout>(id)!!
fun View.viewGroup(@IdRes id: Int) = findView<ViewGroup>(id)!!
fun View.linear(@IdRes id: Int) = findView<LinearLayout>(id)!!
fun View.group(@IdRes id: Int) = findView<ViewGroup>(id)!!
fun View.spinner(@IdRes id: Int) = findView<Spinner>(id)!!
fun View.search(@IdRes id: Int) = findView<SearchView>(id)!!
fun View.button(@IdRes id: Int) = findView<Button>(id)!!
fun View.compound(@IdRes id: Int) = findView<CompoundButton>(id)!!
fun View.checkBox(@IdRes id: Int) = findView<CheckBox>(id)!!
fun View.switch(@IdRes id: Int) = findView<Switch>(id)!!
fun View.timePicker(@IdRes id: Int) = findView<TimePicker>(id)!!
fun View.webView(@IdRes id: Int) = findView<WebView>(id)!!
fun View.imageView(@IdRes id: Int) = findView<ImageView>(id)!!
fun View.swipeRefresh(@IdRes id: Int) = findView<SwipeRefreshLayout>(id)!!
fun View.seekBar(@IdRes id: Int) = findView<SeekBar>(id)!!
fun View.toolbar(@IdRes id: Int) = findView<Toolbar>(id)!!

fun <T : View> T.enabledIf(condition: Boolean) = apply { isEnabled = condition }

fun <T : View> T.disabledIf(condition: Boolean) = apply { isEnabled = !condition }
fun View.disabledIf(property: CSEventProperty<Boolean>) = disabledIf(property) { it }
fun <T> View.disabledIf(property: CSEventProperty<T>,
                        condition: (T) -> Boolean): CSEventRegistration {
    disabledIf(condition(property.value))
    return property.onChange { disabledIf(condition(property.value)) }
}

fun <T, V> View.disabledIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                           condition: (T, V) -> Boolean): CSEventRegistration {
    fun update() = disabledIf(condition(property1.value, property2.value))
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T : View> T.enabled() = apply { isEnabled = true }

fun <T : View> T.disabled() = apply { isEnabled = false }

val <T : View> T.superview get() = parent as? View

val <T : View> T.parentView get() = parent as? View

fun <T : View> T.removeFromSuperview() = apply { (parent as? ViewGroup)?.remove(this) }

fun <T : View> View.findViewRecursive(id: Int): T? = findView(id)
    ?: parentView?.findViewRecursive(id)

fun <T : View> T.onClick(onClick: (view: T) -> Unit) =
    apply { setOnClickListener(CSClickAdapter { onClick(this) }) }

fun <T : View> T.createBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    Canvas(bitmap).apply {
        background?.draw(this) ?: this.drawColor(Color.WHITE)
        draw(this)
    }
    return bitmap
}

fun <T : Any> View.propertyWithTag(@IdRes key: Int, onCreate: () -> T): T {
    @Suppress("UNCHECKED_CAST")
    var value = getTag(key) as? T
    if (value.isNull) {
        value = onCreate()
        setTag(key, value)
    }
    return value!!
}

fun View.getRectangleOnScreen(location: IntArray, rectangle: Rect) {
    getLocationOnScreen(location)
    rectangle.set(location[0], location[1], location[0] + width, location[1] + height)
}

fun <T> View.modelProperty(): CSEventProperty<T?> =
    propertyWithTag(R.id.ViewModelTag) { property(null) }

fun <T> View.model(value: T?) = apply { modelProperty<T?>().value(value) }
fun <T> View.model(): T? = modelProperty<T?>().value

fun View.id(value: Int) = apply { id = value }

fun View.pressedIf(value: Boolean) {
    isPressed = value
}

fun View.selectedIf(value: Boolean) {
    isSelected = value
}

fun View.activated(value: Boolean = true) {
    isActivated = value
}

fun View.selectIf(property: CSEventProperty<Boolean>) = selectIf(property, true)

fun View.toggleAsTrue(property: CSEventProperty<Boolean>)
        : CSEventRegistration {
    onClick { property.toggle() }
    return selectedIf(property) { it.isTrue }
}

fun View.toggleAsFalse(property: CSEventProperty<Boolean>)
        : CSEventRegistration {
    onClick { property.toggle() }
    return selectedIf(property) { it.isFalse }
}

fun <T> View.selectIf(property: CSEventProperty<T>, value: T)
        : CSEventRegistration {
    onClick { property.value = value }
    return selectedIf(property) { it == value }
}

fun <T> View.selectedIf(property: CSEventProperty<T>,
                        condition: (T) -> Boolean): CSEventRegistration {
    selectedIf(condition(property.value))
    return property.onChange { selectedIf(condition(property.value)) }
}

fun View.selectedIf(property: CSEventProperty<Boolean>): CSEventRegistration {
    return selectedIf(property) { it.isTrue }
}

fun <T> View.activateIf(property: CSEventProperty<T>, value: T)
        : CSEventRegistration {
    onClick { property.value = value }
    return activatedIf(property) { it == value }
}

fun <T> View.activatedIf(property: CSEventProperty<T>,
                         condition: (T) -> Boolean): CSEventRegistration {
    activated(condition(property.value))
    return property.onChange { activated(condition(property.value)) }
}

fun View.activatedIf(property: CSEventProperty<Boolean>) = activatedIf(property) { it }

fun <T> View.activatedIf(property1: CSEventProperty<T>, property2: CSEventProperty<*>,
                         condition: (T) -> Boolean) =
    activatedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.activatedIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                            condition: (T, V) -> Boolean): CSEventRegistration {
    fun update() = activated(condition(property1.value, property2.value))
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T> View.selectedIf(property1: CSEventProperty<T>, property2: CSEventProperty<*>,
                        condition: (T) -> Boolean) =
    selectedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.selectedIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                           condition: (T, V) -> Boolean): CSEventRegistration {
    fun update() = selectedIf(condition(property1.value, property2.value))
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

fun <T> View.pressedIf(property1: CSEventProperty<T>, property2: CSEventProperty<*>,
                        condition: (T) -> Boolean) =
    pressedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.pressedIf(property1: CSEventProperty<T>, property2: CSEventProperty<V>,
                           condition: (T, V) -> Boolean): CSEventRegistration {
    fun update() = pressedIf(condition(property1.value, property2.value))
    update()
    return CSMultiEventRegistration(
        property1.onChange { update() },
        property2.onChange { update() })
}

@Suppress("DEPRECATION")
fun View.enterFullScreen() {
    systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
}


@Suppress("DEPRECATION")
fun View.exitFullscreen() {
    systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
}

fun View.onLayoutChange(function: () -> Unit): CSEventRegistration {
    val listener = OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> function() }
    addOnLayoutChangeListener(listener)
    return object : CSEventRegistration {
        override var isActive = true
        override fun cancel() = removeOnLayoutChangeListener(listener)
    }
}

fun View.onScrollChange(function: (view: View) -> Unit) =
    eventScrollChange.listen(function)

val View.eventScrollChange
    @RequiresApi(Build.VERSION_CODES.M)
    get() = propertyWithTag(R.id.ViewEventOnScrollChangeTag) {
        event<View>().also { setOnScrollChangeListener { _, _, _, _, _ -> it.fire(this) } }
    }