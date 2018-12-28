package renetik.android.view.extensions

import android.animation.Animator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.webkit.WebView
import android.widget.*
import renetik.android.view.adapter.CSAnimatorAdapter

fun <T : View> View.findView(id: Int): T? = findViewById<T>(id)
fun View.simpleView(id: Int) = findView<View>(id)!!
fun View.editText(id: Int) = findView<EditText>(id)!!
fun View.textView(id: Int) = findView<TextView>(id)!!
fun View.listView(id: Int) = findView<ListView>(id)!!
fun View.radio(id: Int) = findView<RadioButton>(id)!!
fun View.radioGroup(id: Int) = findView<RadioGroup>(id)!!
fun View.datePicker(id: Int) = findView<DatePicker>(id)!!
fun View.frame(id: Int) = findView<FrameLayout>(id)!!
fun View.linearLayout(id: Int) = findView<LinearLayout>(id)!!
fun View.viewGroup(id: Int) = findView<ViewGroup>(id)!!
fun View.spinner(id: Int) = findView<Spinner>(id)!!
fun View.button(id: Int) = findView<Button>(id)!!
fun View.compoundButton(id: Int) = findView<CompoundButton>(id)!!
fun View.timePicker(id: Int) = findView<TimePicker>(id)!!
fun View.webView(id: Int) = findView<WebView>(id)!!
fun View.imageView(id: Int) = findView<ImageView>(id)!!


val <T : View> T.isVisible get() = visibility == VISIBLE

fun <T : View> T.visible(visible: Boolean) = apply { visibility = if (visible) VISIBLE else GONE }

fun <T : View> T.hide() = apply { visibility = GONE }

fun <T : View> T.show() = apply { visibility = VISIBLE }

val <T : View> T.parentView get() = parent as? View

fun <T : View> T.removeFromSuperview() = apply { (parent as? ViewGroup)?.remove(this) }

fun <T : View> View.findViewRecursive(id: Int): T? = findView<T>(id)
        ?: parentView?.findViewRecursive<T>(id)


fun <T : View> T.fade(fadeIn: Boolean) = if (fadeIn) fadeIn() else fadeOut()

fun <T : View> T.fadeIn() = fadeIn(150)

fun <T : View> T.fadeIn(duration: Int): ViewPropertyAnimator? {
    if (isVisible) return null
    show()
    alpha = 0f
    return animate().alpha(1.0f).setDuration(duration.toLong())
            .setInterpolator(AccelerateDecelerateInterpolator()).setListener(null)
}

fun <T : View> T.fadeOut() = fadeOut(300)

fun <T : View> T.fadeOut(duration: Int, onDone: (() -> Unit)? = null): ViewPropertyAnimator? {
    return if (!isVisible || alpha == 0f) null else animate().alpha(0f).setDuration(duration.toLong())
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : CSAnimatorAdapter() {
                override fun onAnimationEnd(animator: Animator?) {
                    hide()
                    onDone?.invoke()
                }
            })
}

fun <T : View> T.onClick(onClick: (view: T) -> Unit) = apply { setOnClickListener { onClick(this) } }

fun <T : View> T.hasSize(onHasSize: (View) -> Unit) {
    val self = this
    if (width == 0 && height == 0)
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                onHasSize(self)
            }
        })
    else onHasSize(this)
}

fun <T : View> T.width(function: (Int) -> Unit) = hasSize { function(width) }

fun <T : View> T.height(function: (Int) -> Unit) = hasSize { function(height) }

fun <T : View> T.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
    val params = layoutParams as? ViewGroup.MarginLayoutParams
    params?.setMargins(left, top, right, bottom)
    layoutParams = params
}

fun <T : View> T.bottomMargin(bottom: Int) {
    val params = layoutParams as? ViewGroup.MarginLayoutParams
    params?.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottom)
    layoutParams = params
}

fun <T : View> T.topMargin(top: Int) {
    val params = layoutParams as? ViewGroup.MarginLayoutParams
    params?.setMargins(params.leftMargin, top, params.rightMargin, params.bottomMargin)
    layoutParams = params
}

fun <T : View> T.createBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    Canvas(bitmap).apply {
        background?.draw(this) ?: this.drawColor(Color.WHITE)
        draw(this)
    }
    return bitmap
}


