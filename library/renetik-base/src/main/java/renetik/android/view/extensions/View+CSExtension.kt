package renetik.android.view.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import renetik.android.java.extensions.isNull
import renetik.android.java.extensions.primitives.isTrue

fun <T : View> View.findView(@IdRes id: Int): T? = findViewById(id)
fun View.view(@IdRes id: Int) = findView<View>(id)!!

@Deprecated("To be removed", ReplaceWith("view(id)"))
fun View.simpleView(@IdRes id: Int) = view(id)

fun View.editText(@IdRes id: Int) = findView<EditText>(id)!!
fun View.textView(@IdRes id: Int) = findView<TextView>(id)!!
fun View.scrollView(@IdRes id: Int) = findView<ScrollView>(id)!!
fun View.horizontalScroll(@IdRes id: Int) = findView<HorizontalScrollView>(id)!!
fun View.listView(@IdRes id: Int) = findView<ListView>(id)!!
fun View.radio(@IdRes id: Int) = findView<RadioButton>(id)!!
fun View.radioGroup(@IdRes id: Int) = findView<RadioGroup>(id)!!
fun View.datePicker(@IdRes id: Int) = findView<DatePicker>(id)!!
fun View.numberPicker(@IdRes id: Int) = findView<NumberPicker>(id)!!
fun View.frame(@IdRes id: Int) = findView<FrameLayout>(id)!!
fun View.linearLayout(@IdRes id: Int) = findView<LinearLayout>(id)!!
fun View.viewGroup(@IdRes id: Int) = findView<ViewGroup>(id)!!
fun View.spinner(@IdRes id: Int) = findView<Spinner>(id)!!
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

fun <T : View> T.enabled(enabled: Boolean) = apply { isEnabled = enabled }

fun <T : View> T.enabled() = apply { isEnabled = true }

fun <T : View> T.disabled() = apply { isEnabled = false }

val <T : View> T.isVisible get() = visibility == VISIBLE

val <T : View> T.isInvisible get() = visibility == INVISIBLE

val <T : View> T.isGone get() = visibility == GONE

fun <T : View> T.visibleIf(condition: Boolean) = apply { if (condition) visible() else invisible() }

fun <T : View> T.invisibleIf(condition: Boolean) =
    apply { if (condition) invisible() else visible() }

fun <T : View> T.visible() = apply { visibility = VISIBLE }

fun <T : View> T.invisible() = apply { visibility = INVISIBLE }

fun <T : View> T.showIf(condition: Boolean?) = apply { if (condition.isTrue) show() else hide() }

fun <T : View> T.hideIf(condition: Boolean?) = apply { if (condition.isTrue) hide() else show() }

fun <T : View> T.show() = apply { visibility = VISIBLE }

fun <T : View> T.hide() = apply { visibility = GONE }

val <T : View> T.superview get() = parent as? View

val <T : View> T.parentView get() = parent as? View

fun <T : View> T.removeFromSuperview() = apply { (parent as? ViewGroup)?.remove(this) }

fun <T : View> View.findViewRecursive(id: Int): T? = findView(id)
    ?: parentView?.findViewRecursive(id)

fun <T : View> T.onClick(onClick: (view: T) -> Unit) =
    apply { setOnClickListener { onClick(this) } }

fun <T : View> T.createBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    Canvas(bitmap).apply {
        background?.draw(this) ?: this.drawColor(Color.WHITE)
        draw(this)
    }
    return bitmap
}

fun <T : Any> View.tagProperty(@IdRes key: Int, onCreate: () -> T): T {
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




