package renetik.android.material.extensions

import android.view.View
import com.google.android.material.switchmaterial.SwitchMaterial
import renetik.android.extensions.findView

fun View.materialSwitch(id: Int) = findView<SwitchMaterial>(id)!!


