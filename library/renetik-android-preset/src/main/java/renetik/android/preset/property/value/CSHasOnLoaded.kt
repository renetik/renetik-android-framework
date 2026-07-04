package renetik.android.preset.property.value

interface CSHasOnLoaded<T> {
    var onValueLoaded: ((T) -> T)?

    companion object {
        fun <V, T : CSHasOnLoaded<V>> T.onLoaded(
            onLoaded: (V) -> V) = apply {
            onValueLoaded = onLoaded
        }
    }
}