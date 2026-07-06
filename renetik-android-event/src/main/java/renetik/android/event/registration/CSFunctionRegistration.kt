package renetik.android.event.registration

import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.Fun

fun CSFunctionRegistration(function: ArgFun<CSRegistration>) =
    CSFunctionRegistration(function, null)

class CSFunctionRegistration(
    function: ArgFun<CSFunctionRegistration>,
    val onCancel: ArgFun<CSFunctionRegistration>? = null)
    : CSRegistrationImpl(isActive = true) {

    val function: Fun = { if (isActive) function(this) }

    override fun onCancel() {
        super.onCancel()
        onCancel?.invoke(this)
    }

    fun invoke() = function()
}