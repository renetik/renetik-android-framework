package renetik.android.ui.extensions.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.Color.WHITE
import android.graphics.Rect
import android.os.Build
import android.os.SystemClock.uptimeMillis
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.obtain
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import renetik.android.core.kotlin.isNull
import renetik.android.core.kotlin.primitives.isFalse
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.core.lang.catchAll
import renetik.android.core.lang.variable.toggle
import renetik.android.event.CSEvent
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.fire
import renetik.android.event.property.CSActionInterface
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.property.onChange
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.start
import renetik.android.ui.view.adapter.CSClickAdapter

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

fun View.seekBar(@IdRes id: Int) = findView<SeekBar>(id)!!
fun View.progress(@IdRes id: Int) = findView<ProgressBar>(id)!!
fun View.toolbar(@IdRes id: Int) = findView<Toolbar>(id)!!


fun View.disabledIf(property: CSProperty<Boolean>) = disabledIf(property) { it }
fun <T> View.disabledIf(property: CSProperty<T>,
                        condition: (T) -> Boolean): CSRegistration {
    disabledIf(condition(property.value))
    return property.onChange { disabledIf(condition(property.value)) }
}

fun <T, V> View.disabledIf(property1: CSProperty<T>, property2: CSProperty<V>,
                           condition: (T, V) -> Boolean): CSRegistration {
    fun update() = disabledIf(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

fun <T : View> T.enabled(enabled: Boolean = true) = enabledIf(enabled)
fun <T : View> T.enabledIf(condition: Boolean) = apply { isEnabled = condition }
fun <T : View> T.disabled(value: Boolean = true) = disabledIf(value)
fun <T : View> T.disabledIf(condition: Boolean) = apply { isEnabled = !condition }
val <T : View> T.superview get() = parent as? View
val <T : View> T.parentView get() = parent as? View
fun <T : View> T.removeFromSuperview() = apply { (parent as? ViewGroup)?.remove(this) }

fun <T : View> View.findViewRecursive(id: Int): T? = findView(id)
    ?: parentView?.findViewRecursive(id)

fun <T : View> T.onClick(onClick: (view: T) -> Unit) =
    apply { setOnClickListener(CSClickAdapter { onClick(this) }) }

fun <T : View> T.onLongClick(onClick: (view: T) -> Unit) =
    apply { setOnLongClickListener { onClick(this); true } }

fun <T : View> T.createBitmap(): Bitmap {
    val bitmap = createBitmap(width, height, ARGB_8888)
    Canvas(bitmap).apply {
        background?.draw(this) ?: this.drawColor(WHITE)
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

fun <T> View.modelProperty(): CSProperty<T?> =
    propertyWithTag(renetik.android.ui.R.id.ViewModelTag) { property(null) }

fun <T> View.model(value: T?) = apply { modelProperty<T?>().value(value) }
fun <T> View.model(): T? = modelProperty<T?>().value
fun View.id(value: Int) = apply { id = value }
fun View.pressedIf(value: Boolean) = apply { isPressed = value }
fun View.selected(value: Boolean = true) = selectedIf(value)
fun View.selectedIf(value: Boolean) = apply { isSelected = value }
fun View.activated(value: Boolean = true) = activatedIf(value)
fun View.activatedIf(value: Boolean) = apply { isActivated = value }
fun View.selectIf(property: CSProperty<Boolean>) = selectIf(property, true)
fun View.onClick(action: CSActionInterface) = onClick { action.toggle() }

fun View.toggleSelectedAsTrue(property: CSProperty<Boolean>)
        : CSRegistration {
    onClick { property.toggle() }
    return selectedIf(property) { it.isTrue }
}

fun View.toggleActiveAsTrue(property: CSProperty<Boolean>)
        : CSRegistration {
    onClick { property.toggle() }
    return activatedIf(property) { it.isTrue }
}

fun View.toggleAsFalse(property: CSProperty<Boolean>)
        : CSRegistration {
    onClick { property.toggle() }
    return selectedIf(property) { it.isFalse }
}

fun <T> View.selectIf(property: CSProperty<T>, value: T)
        : CSRegistration {
    onClick { property.value = value }
    return selectedIf(property) { it == value }
}

inline fun <T> View.selectedIf(property: CSProperty<T>,
                               crossinline condition: (T) -> Boolean): CSRegistration {
    selected(condition(property.value))
    return property.onChange { selected(condition(property.value)) }
}

fun View.selectedIf(property: CSProperty<Boolean>): CSRegistration {
    return selectedIf(property) { it.isTrue }
}

fun <T> View.activateIf(property: CSProperty<T>, value: T)
        : CSRegistration {
    onClick { property.value = value }
    return activatedIf(property) { it == value }
}

inline fun <T> View.activatedIf(property: CSProperty<T>,
                                crossinline condition: (T) -> Boolean): CSRegistration {
    activated(condition(property.value))
    return property.onChange { activated(condition(property.value)) }
}

fun View.activatedIf(property: CSProperty<Boolean>) = activatedIf(property) { it }

inline fun <T> View.activatedIf(property1: CSProperty<T>, property2: CSProperty<*>,
                                crossinline condition: (T) -> Boolean) =
    activatedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.activatedIf(property1: CSProperty<T>, property2: CSProperty<V>,
                            condition: (T, V) -> Boolean): CSRegistration {
    fun update() = activated(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

fun <T, V, X> View.activatedIf(property1: CSProperty<T>,
                               property2: CSProperty<V>,
                               property3: CSProperty<X>,
                               condition: (T, V, X) -> Boolean): CSRegistration {
    fun update() = activated(condition(property1.value, property2.value, property3.value))
    update()
    return CSRegistration(property1.onChange(::update),
        property2.onChange(::update), property3.onChange(::update)
    )
}

fun <T, V, X, Y> View.activatedIf(property1: CSProperty<T>,
                                  property2: CSProperty<V>,
                                  property3: CSProperty<X>,
                                  property4: CSProperty<Y>,
                                  condition: (T, V, X, Y) -> Boolean): CSRegistration {
    fun update() = activated(condition(property1.value, property2.value,
        property3.value, property4.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update),
        property3.onChange(::update), property4.onChange(::update)
    )
}

inline fun <T> View.selectedIf(property1: CSProperty<T>, property2: CSProperty<*>,
                               crossinline condition: (T) -> Boolean) =
    selectedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.selectedIf(property1: CSProperty<T>, property2: CSProperty<V>,
                           condition: (T, V) -> Boolean): CSRegistration {
    fun update() = selected(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
}

fun <T, V, X> View.selectedIf(property1: CSProperty<T>,
                              property2: CSProperty<V>,
                              property3: CSProperty<X>,
                              condition: (T, V, X) -> Boolean): CSRegistration {
    fun update() = selected(condition(property1.value, property2.value, property3.value))
    update()
    return CSRegistration(property1.onChange(::update),
        property2.onChange(::update), property3.onChange(::update)
    )
}

fun <T, V, X, Y> View.selectedIf(property1: CSProperty<T>,
                                 property2: CSProperty<V>,
                                 property3: CSProperty<X>,
                                 property4: CSProperty<Y>,
                                 condition: (T, V, X, Y) -> Boolean): CSRegistration {
    fun update() = selected(condition(property1.value, property2.value,
        property3.value, property4.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update),
        property3.onChange(::update), property4.onChange(::update)
    )
}


fun <T> View.pressedIf(property1: CSProperty<T>, property2: CSProperty<*>,
                       condition: (T) -> Boolean) =
    pressedIf(property1, property2) { first, _ -> condition(first) }

fun <T, V> View.pressedIf(property1: CSProperty<T>, property2: CSProperty<V>,
                          condition: (T, V) -> Boolean): CSRegistration {
    fun update() = pressedIf(condition(property1.value, property2.value))
    update()
    return CSRegistration(property1.onChange(::update), property2.onChange(::update))
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

inline fun View.onLayoutChange(crossinline function: () -> Unit): CSRegistration {
    val listener = OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> function() }
    return CSRegistration(
        onResume = { addOnLayoutChangeListener(listener) },
        onPause = { removeOnLayoutChangeListener(listener) }).start()
}

fun View.onScrollChange(function: (view: View) -> Unit) =
    eventScrollChange.listen(function)

val View.eventScrollChange
    @RequiresApi(Build.VERSION_CODES.M)
    get() = propertyWithTag(renetik.android.ui.R.id.ViewEventOnScrollTag) {
        event<View>().also { setOnScrollChangeListener { _, _, _, _, _ -> it.fire(this) } }
    }

@SuppressLint("ClickableViewAccessibility")
inline fun <T : View> T.onDoubleTap(crossinline function: (T) -> Unit) = apply {
    val view = this
    val detector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            function(view)
            return true
        }

        override fun onDown(e: MotionEvent): Boolean = true
    })
    setOnTouchListener { _, event -> detector.onTouchEvent(event) }
}

fun View.action(action: CSEvent<Unit>) = onClick { action.fire() }

fun View.onDestroy() {
    catchAll { setOnClickListener(null) }
    catchAll { setOnLongClickListener(null) }
    (this as? ViewGroup)?.children?.forEach(View::onDestroy)
}

fun View.performTouchDown() = dispatchTouchEvent(obtain(uptimeMillis(),
    uptimeMillis() + 700, ACTION_DOWN, 0f, 0f, 0))

val View.firstChild get() = (this as? ViewGroup)?.firstChild
val View.lastChild get() = (this as? ViewGroup)?.lastChild