package renetik.android.material.extensions

import android.view.View
import com.google.android.material.switchmaterial.SwitchMaterial
import renetik.android.ui.extensions.view.findView

fun View.materialSwitch(id: Int) = findView<SwitchMaterial>(id)!!


