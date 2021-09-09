package renetik.android.material.extensions

import android.view.View
import com.google.android.material.switchmaterial.SwitchMaterial
import renetik.android.view.findView

fun View.materialSwitch(id: Int) = findView<SwitchMaterial>(id)!!


