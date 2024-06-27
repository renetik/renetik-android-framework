package renetik.android.ui.extensions.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.Color.WHITE
import android.graphics.Rect
import android.os.SystemClock.uptimeMillis
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.obtain
import android.view.View
import android.view.View.OnTouchListener
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
import android.view.View.SYSTEM_UI_FLAG_VISIBLE
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.IdRes
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import renetik.android.core.kotlin.isNull
import renetik.android.core.kotlin.primitives.ifTrue
import renetik.android.core.lang.catchAll
import renetik.android.event.CSEvent
import renetik.android.event.fire
import renetik.android.event.property.CSActionInterface
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.property.start
import renetik.android.ui.view.CSHasTouchEvent
import renetik.android.ui.view.adapter.CSClickAdapter

fun <T : View> View.findView(@IdRes id: Int): T? = findViewById(id)

@JvmName("viewOfType") inline fun <reified Type : View> View.view(@IdRes id: Int): Type =
    findView(id)!!

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
fun View.compound(@IdRes id: Int) = findView<CompoundButton>(id)!!
fun View.checkBox(@IdRes id: Int) = findView<CheckBox>(id)!!
fun View.switch(@IdRes id: Int) = findView<Switch>(id)!!
fun View.timePicker(@IdRes id: Int) = findView<TimePicker>(id)!!
fun View.webView(@IdRes id: Int) = findView<WebView>(id)!!
fun View.imageView(@IdRes id: Int) = findView<ImageView>(id)!!

fun View.seekBar(@IdRes id: Int) = findView<SeekBar>(id)!!
fun View.progress(@IdRes id: Int) = findView<ProgressBar>(id)!!
fun View.toolbar(@IdRes id: Int) = findView<Toolbar>(id)!!

fun <T : View> T.enabled(enabled: Boolean = true) = enabledIf(enabled)
fun <T : View> T.enabledIf(condition: Boolean) = apply { isEnabled = condition }
fun <T : View> T.disabled(value: Boolean = true) = disabledIf(value)
fun <T : View> T.disabledIf(condition: Boolean) = apply { isEnabled = !condition }

val <T : View> T.superview get() = parent as? ViewGroup
val <T : View> T.parentView get() = parent as? ViewGroup
fun <T : View> T.removeFromSuperview() = apply { (parent as? ViewGroup)?.remove(this) }

fun <T : View> View.findViewRecursive(id: Int): T? =
    findView(id) ?: parentView?.findViewRecursive(id)

fun <T : View> T.onClick(
    timeout: Int? = null, onClick: (view: T) -> Unit
) = apply {
    setOnClickListener(CSClickAdapter(timeout) { if (isClickable) onClick(this) })
}


//fun <T : View> T.registerClick(
//    timeout: Int? = null, onClick: (view: T) -> Unit
//): CSRegistration {
//    val wasClickable = isClickable
//    return CSRegistration(onResume = {
//        setOnClickListener(CSClickAdapter(timeout) { onClick(this) })
//    }, onPause = {
//        setOnClickListener(null)
//        isClickable = wasClickable
//    }).start()
//}

fun <T : View> T.clearClick() = apply {
    setOnClickListener(null)
    isClickable = false
}

fun <T : View> T.onLongClick(onClick: (view: T) -> Unit) = apply {
    setOnLongClickListener { isLongClickable.ifTrue { onClick(this); true } ?: false }
}

//fun <T : View> T.registerLongClick(onClick: (view: T) -> Unit): CSRegistration {
//    val wasClickable = isLongClickable
//    return CSRegistration(onResume = {
//        setOnLongClickListener { onClick(this); true }
//    }, onPause = {
//        setOnLongClickListener(null)
//        isLongClickable = wasClickable
//    }).start()
//}

fun <T : View> T.createBitmap(): Bitmap {
    val bitmap = createBitmap(width, height, ARGB_8888)
    Canvas(bitmap).apply {
        background?.draw(this) ?: this.drawColor(WHITE)
        draw(this)
    }
    return bitmap
}

fun View.setHasTouchEventListener(): CSHasTouchEvent {
    val listener = object : CSHasTouchEvent, OnTouchListener {
        override val self: View get() = this@setHasTouchEventListener
        override var onTouchEvent: ((event: MotionEvent) -> Boolean)? = null
        override fun onTouch(v: View, event: MotionEvent): Boolean =
            onTouchEvent?.invoke(event) ?: false
    }
    setOnTouchListener(listener)
    return listener
}

fun <T : Any> View.propertyWithTag(@IdRes key: Int, onCreate: () -> T): T {
    @Suppress("UNCHECKED_CAST") var value = getTag(key) as? T
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
fun View.pressed(value: Boolean = true) = apply { isPressed = value }

fun View.select(value: Boolean = true) = selectedIf(value)
fun View.selectedIf(value: Boolean) = apply { isSelected = value }
fun View.activated(value: Boolean = true) = activeIf(value)
fun View.activeIf(value: Boolean) = apply { isActivated = value }
fun View.selectIf(property: CSProperty<Boolean>) = selectIf(property, true)
fun View.onClick(action: CSActionInterface) = onClick { action.start() }
fun View.onClick(action: CSEvent<Unit>) = onClick { action.fire() }

@Suppress("DEPRECATION") fun View.enterFullScreen() {
    systemUiVisibility = SYSTEM_UI_FLAG_IMMERSIVE or
            SYSTEM_UI_FLAG_FULLSCREEN or SYSTEM_UI_FLAG_HIDE_NAVIGATION
}

@Suppress("DEPRECATION") fun View.exitFullscreen() {
    systemUiVisibility = SYSTEM_UI_FLAG_VISIBLE
}

@SuppressLint("ClickableViewAccessibility") inline fun <T : View> T.onDoubleTap(
    crossinline function: (T) -> Unit
) = apply {
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

fun View.performTouchDown(time: Int = 700): Boolean = dispatchTouchEvent(
    obtain(
        uptimeMillis(), uptimeMillis() + time, ACTION_DOWN, 0f, 0f, 0
    )
)

val View.firstChild get() = (this as? ViewGroup)?.firstChild

fun View.description(string: String) = apply { contentDescription = string }

val View.lastChild get() = (this as? ViewGroup)?.lastChild

val View.previousView: View?
    get() {
        val index = parentView?.indexOfChild(this)
        return index?.let { parentView?.getChildAt(it - 1) }
    }

val View.nextView: View?
    get() {
        val index = parentView?.indexOfChild(this)
        return index?.let { parentView?.getChildAt(it + 1) }
    }

fun View.passClicksUnder(pass: Boolean) = apply {
    isClickable = !pass; isFocusable = !pass
}