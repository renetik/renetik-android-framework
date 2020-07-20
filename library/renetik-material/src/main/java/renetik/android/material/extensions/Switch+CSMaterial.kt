package renetik.android.material.extensions

import android.view.View
import com.google.android.material.switchmaterial.SwitchMaterial
import renetik.android.base.CSView
import renetik.android.view.extensions.findView

fun View.materialSwitch(id: Int) = findView<SwitchMaterial>(id)!!

fun CSView<*>.materialSwitch(id: Int) = view.materialSwitch(id)
