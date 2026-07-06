package renetik.android.material

import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
@Config(application = MaterialTestApplication::class)
class EditTextFormTest {

    private fun editTextInLayout(): Pair<TextInputLayout, EditText> {
        val layout = TextInputLayout(context)
        val editText = TextInputEditText(layout.context)
        layout.addView(editText)
        return layout to editText
    }

    @Test
    fun inputLayoutResolvesEnclosingTextInputLayout() {
        val (layout, editText) = editTextInLayout()
        assertSame(layout, editText.inputLayout)
    }

    @Test
    fun inputLayoutIsNullWithoutTextInputLayout() {
        assertNull(EditText(context).inputLayout)
    }

    @Test
    fun withStartIconSetsIconOnLayout() {
        val (layout, editText) = editTextInLayout()
        var clicked = 0
        editText.withStartIcon(androidx.appcompat.R.drawable.abc_ic_clear_material) {
            clicked += 1
        }
        assertNotNull(layout.startIconDrawable)
    }

    @Test
    fun withStartIconClearTogglesWithText() {
        val (layout, editText) = editTextInLayout()
        editText.withStartIconClear { }
        assertNull(layout.startIconDrawable)
        editText.setText("something")
        assertNotNull(layout.startIconDrawable)
        editText.setText("")
        assertNull(layout.startIconDrawable)
    }
}
