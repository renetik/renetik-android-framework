package renetik.android.framework.event

fun CSVisibility.whileShowingTrue(function: (Boolean) -> Unit) {
    if (isVisible) function(true)
    eventViewVisibilityChanged.listen { visible ->
        if (visible) function(true) else function(false)
    }
}