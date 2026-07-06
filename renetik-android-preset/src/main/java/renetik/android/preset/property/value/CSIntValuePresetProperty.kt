package renetik.android.preset.property.value

import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSIntValuePresetProperty(
    parent: CSHasDestruct, preset: CSPreset<*, *>, key: String,
    private val getDefault: () -> Int, onChange: ((value: Int) -> Unit)?
) : CSValuePresetProperty<Int>(parent, preset, key, onChange),
    CSHasOnLoaded<Int> {

    constructor(
        parent: CSHasDestruct, preset: CSPreset<*, *>, key: String,
        default: Int, onChange: ((value: Int) -> Unit)?
    ) : this(parent, preset, key, { default }, onChange)

    override val default: Int get() = getDefault()

    override var onValueLoaded: ((Int) -> Int)? = null

    override fun get(store: CSStore): Int? {
        val loaded = store.getInt(key)
        return loaded?.let { onValueLoaded?.invoke(it) ?: it }
    }

    override fun set(store: CSStore, value: Int) =
        store.set(key, value)
}