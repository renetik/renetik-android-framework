package renetik.android.material.extensions

import android.annotation.SuppressLint
import android.content.Context
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
import renetik.android.java.event.event
import renetik.android.java.event.fire
import renetik.android.java.event.listen
import renetik.android.java.extensions.privateField
import renetik.android.view.extensions.*

@SuppressLint("RestrictedApi")
fun <T : TextInputLayout> T.startIconCheckable(onCheckChanged: (TextInputLayout) -> Unit) = apply {
    isStartIconCheckable = true
    this.editText?.isEnabled = isChecked
    isEndIconVisible = isChecked
    setStartIconOnClickListener {
        startIconView.toggle()
        this.editText?.isEnabled = isChecked
        isEndIconVisible = isChecked
        onCheckChanged(this)
    }
}

val TextInputLayout.autoCompleteView get() = (editText as AutoCompleteTextView)

val <T : TextInputLayout> T.startIconView: CheckableImageButton
    get() = privateField("startIconView")

var <T : TextInputLayout> T.isChecked
    @SuppressLint("RestrictedApi") get() = startIconView.isChecked
    set(value) {
        @SuppressLint("RestrictedApi")
        startIconView.isChecked = value
    }

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
    get() = propertyWithTag(R.id.EditTextEventOnClearTagKey) { event<Unit>() }

fun <T : TextInputLayout> T.onClear(listener: () -> Unit): T = apply {
    eventClear.listen(listener)
}

@SuppressLint("PrivateResource")
fun <T : TextInputLayout> T.withClear(): TextInputLayout = apply {
    fun updateClearIcon() {
        val endIconDrawableToRestore = endIconDrawable
        val isEndIconVisibleToRestore = isEndIconVisible
        val endIconModeToRestore = endIconMode
        fun restoreIcon() {
            endIconDrawable = endIconDrawableToRestore
            isEndIconVisible = isEndIconVisibleToRestore
            endIconMode = endIconModeToRestore
            // Imposible to restore click listener for now
//            setEndIconOnClickListener { editText!!.performClick() } //TODO: remove if works
            setEndIconOnClickListener(null)
        }
        if (title.isNotEmpty()) {
            setEndIconDrawable(R.drawable.abc_ic_clear_material)
            isEndIconVisible = true
            endIconMode = END_ICON_CUSTOM
            setEndIconOnClickListener {
                restoreIcon()
                title = ""
                eventClear.fire()
            }
        } else restoreIcon()
    }
    updateClearIcon()
    editText!!.doAfterTextChanged { updateClearIcon() }
}

fun <T : TextInputLayout> T.filters(vararg filters: InputFilter) = apply {
    editText!!.filters = filters
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