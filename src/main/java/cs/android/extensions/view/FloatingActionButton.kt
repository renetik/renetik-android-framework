package cs.android.extensions.view

import androidx.core.graphics.drawable.DrawableCompat.setTint
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.iconTint(color: Int): FloatingActionButton {
    setTint(drawable, color)
    return this
}