package renetik.android.core.android.util

import android.util.Patterns.EMAIL_ADDRESS

fun String?.isValidEmail(): Boolean =
    !isNullOrEmpty() && EMAIL_ADDRESS.matcher(this).matches()
