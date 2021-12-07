package renetik.android.material.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import androidx.annotation.FontRes
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.internal.CheckableImageButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_CUSTOM
import renetik.android.R
import renetik.android.framework.event.*
import renetik.android.framework.event.property.CSEventProperty
import renetik.kotlin.notNull
import renetik.kotlin.privateField
import renetik.android.view.gone
import renetik.android.view.propertyWithTag
import renetik.android.widget.*

val <T : TextInputLayout> T.startIconView: CheckableImageButton?
    get() = privateField("startIconView")

fun <T : TextInputLayout> T.onCheck(listener: () -> Unit): T = apply {
    propertyWithTag(R.id.ViewEventOnCheckTag) { event<Unit>() }.listen(listener)
}

@Suppress("UNCHECKED_CAST")
fun <T : TextInputLayout> T.fireCheck(): T = apply {
    (getTag(R.id.ViewEventOnCheckTag) as? CSEvent<Unit>)?.fire()
}

@SuppressLint("RestrictedApi")
fun <T : TextInputLayout> T.startIconCheckable(checkable: Boolean = true) = apply {
    isStartIconCheckable = checkable
    if (checkable) setStartIconOnClickListener { isChecked = !isChecked }
    else setStartIconOnClickListener(null)
}

var <T : TextInputLayout> T.isChecked
    @SuppressLint("RestrictedApi") get() =
        startIconView?.isChecked ?: false
    set(value) {
        @SuppressLint("RestrictedApi")
        startIconView?.isChecked = value
        fireCheck()
    }

val TextInputLayout.autoCompleteView get() = (editText as AutoCompleteTextView)

fun <T : TextInputLayout> T.dropdown(context: Context, @LayoutRes itemLayout: Int,
                                     items: List<Any>, filter: Boolean = false,
                                     onItemChange: ((Int) -> Unit)? = null) = apply {
    val adapter = if (filter) ArrayAdapter(context, itemLayout, items) else
        NotFilteringArrayAdapter(context, itemLayout, items)
    autoCompleteView.setAdapter(adapter)
    autoCompleteView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
        onItemChange?.invoke(position)
    }
}

class NotFilteringArrayAdapter<T>(context: Context, @LayoutRes resource: Int, val objects: List<T>)
    : ArrayAdapter<T>(context, resource, objects) {

    val notFilteringFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?) = FilterResults().apply {
            values = objects
            count = objects.size
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) =
            notifyDataSetChanged()
    }

    override fun getFilter() = notFilteringFilter
}

val <T : TextInputLayout> T.eventClear
    get() = propertyWithTag(R.id.ViewEventOnClearTag) { event<Unit>() }

fun <T : TextInputLayout> T.onClear(listener: () -> Unit): T = apply {
    eventClear.listen(listener)
}

@SuppressLint("PrivateResource")
fun <T : TextInputLayout> T.withClear(): TextInputLayout = apply {
    var endIconDrawableToRestore: Drawable? = null
    var isEndIconVisibleToRestore: Boolean? = null
    var endIconModeToRestore: Int? = null
    fun restoreState() {
        endIconDrawable = endIconDrawableToRestore
        endIconDrawableToRestore = null
        isEndIconVisible = isEndIconVisibleToRestore!!
        isEndIconVisibleToRestore = null
        endIconMode = endIconModeToRestore!!
        endIconModeToRestore = null
        // Imposible to restore click listener if was set and
        // there was visual glitch if set to null
        setEndIconOnClickListener { editText!!.performClick() }
    }

    fun saveStateToRestore() {
        endIconDrawableToRestore = endIconDrawable
        isEndIconVisibleToRestore = isEndIconVisible
        endIconModeToRestore = endIconMode
    }

    fun showClearIcon() {
        setEndIconDrawable(R.drawable.abc_ic_clear_material)
        isEndIconVisible = true
        endIconMode = END_ICON_CUSTOM
        setEndIconOnClickListener {
//            restoreState()
            title = ""
            eventClear.fire()
        }
    }

    fun isClearVisible() = isEndIconVisibleToRestore != null
    fun updateClearIcon() {
        if (title.isNotEmpty()) {
            if (!isClearVisible()) {
                saveStateToRestore()
                showClearIcon()
            }
        } else if (isClearVisible()) restoreState()
    }
    updateClearIcon()
    editText!!.doAfterTextChanged { updateClearIcon() }
}

fun <T : TextInputLayout> T.filters(vararg filters: InputFilter) = apply {
    editText!!.filters = filters
}

fun TextInputLayout.error(hint: String? = null) {
    isErrorEnabled = true
    if (hint == null) {
        error(" ")
        if (childCount == 2) getChildAt(1).gone()
    } else error = hint
}

fun <T : TextInputLayout> T.errorClear() = apply {
    error = null
    isErrorEnabled = false
}

val TextInputLayout.isError get() = error != null && isErrorEnabled

fun TextInputLayout.onChangeClearError() = apply {
    onTextChange { if (isError) errorClear() }
}

fun TextInputLayout.typeface(@FontRes font: Int) = apply {
    typeface = ResourcesCompat.getFont(context, font)
}

fun TextInputLayout.textTypeFace(@FontRes font: Int) = apply {
    editText!!.typeface(font)
}

fun TextInputLayout.text() = editText!!.text()
fun TextInputLayout.text(value: String) = editText!!.text(value)

var <T : TextInputLayout> T.title: String
    get() = editText!!.title
    set(value) {
        editText!!.title = value
    }

fun <T : TextInputLayout> T.title(string: String) = apply { title = string }

fun <T : TextInputLayout> T.onTextChange(onChange: (view: T) -> Unit) =
    apply { editText!!.onTextChange { onChange(this) } }

fun <T : TextInputLayout> T.onFocusChange(onChange: (view: T) -> Unit) =
    apply { editText!!.onFocusChange { onChange(this) } }

fun <T : Any> TextInputLayout.valueProperty(
    parent: CSVisibleEventOwner, property: CSEventProperty<T?>
) = apply {
    onTextChange { if (property.value.notNull) errorClear() }
    onClear { property.value = null }
    editText!!.text(parent, property)
}

//@JvmName("propertyString")
//fun TextInputLayout.textProperty(
//    parent: CSVisibleEventOwner, property: CSEventProperty<String?>
//) = apply {
//    onTextChange { if (property.value.isSet) errorClear() }
//    onClear { property.value = null }
//    editText!!.textProperty(parent, property)
//}