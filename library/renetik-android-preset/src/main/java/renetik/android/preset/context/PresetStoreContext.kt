package renetik.android.preset.context

import renetik.android.core.kotlin.primitives.joinToString
import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.CSHasId
import renetik.android.event.change.invoke
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.CSModel
import renetik.android.json.obj.CSJsonObjectInterface
import renetik.android.preset.CSPreset
import renetik.android.preset.Preset
import renetik.android.preset.extensions.nullDoubleProperty
import renetik.android.preset.extensions.nullFloatProperty
import renetik.android.preset.extensions.nullIntProperty
import renetik.android.preset.extensions.nullListItemProperty
import renetik.android.preset.extensions.nullStringProperty
import renetik.android.preset.extensions.property
import renetik.android.preset.property.CSPresetProperty
import renetik.android.preset.property.nullable.CSDoubleNullablePresetProperty
import renetik.android.preset.property.nullable.CSFloatNullablePresetProperty
import renetik.android.preset.property.nullable.CSIntNullablePresetProperty
import renetik.android.preset.property.nullable.CSListItemNullablePresetProperty
import renetik.android.preset.property.nullable.CSStringNullablePresetProperty
import renetik.android.preset.property.value.CSBooleanValuePresetProperty
import renetik.android.preset.property.value.CSFloatValuePresetProperty
import renetik.android.preset.property.value.CSHasIdListValuePresetProperty
import renetik.android.preset.property.value.CSIntListValuePresetProperty
import renetik.android.preset.property.value.CSIntValuePresetProperty
import renetik.android.preset.property.value.CSListItemValuePresetProperty
import renetik.android.preset.property.value.CSStringValuePresetProperty
import renetik.android.store.context.CSStoreContext
import renetik.android.store.extensions.operation

class PresetStoreContext(
    parent: CSHasDestruct,
    override val id: String,
    val preset: Preset,
    val presetId: String? = null,
    override val key: String? = presetId
) : CSModel(parent), CSStoreContext {

    companion object {
        fun PresetStoreContext(
            parent: CSHasDestruct, hasId: CSHasId, preset: Preset, key: String? = null,
        ) = PresetStoreContext(
            parent, id = key?.let { "${hasId.id} $it" } ?: hasId.id,
            preset, presetId = key, key
        )
    }

    override val data: CSJsonObjectInterface = preset.store

    private val childContexts = mutableListOf<CSStoreContext>()

    private fun <T : CSStoreContext> T.init(parent: PresetStoreContext) = apply {
        parent.childContexts += this
        eventDestruct { if (!parent.isDestructed) parent.childContexts -= this }
    }

    override fun context(parent: CSHasDestruct, key: String?): PresetStoreContext =
        PresetStoreContext(parent,
            id = (id to key).joinToString(" "), preset,
            presetId = (presetId to key).joinToString(" "), key
        ).init(this)


    fun context(parent: CSHasDestruct, id: String, presetId: String): PresetStoreContext =
        PresetStoreContext(parent,
            id = "${this.id} $id", preset,
            presetId = (this.presetId to presetId).joinToString(" "), key
        ).init(this)

    override fun appContext(parent: CSHasDestruct, key: String?) =
        AppStoreContext(parent, this, key).init(this)

    override fun memoryContext(parent: CSHasDestruct, key: String?) =
        RuntimeStoreContext(parent, this, key).init(this)

    override fun onChange(function: (Unit) -> Unit) = preset.onChange(function)

    private val properties = mutableMapOf<String, CSPresetProperty<*>>()
    private fun <T : CSPresetProperty<*>> T.init(key: String) = apply {
        properties[key] = this
    }

    private val presets = mutableListOf<CSPreset<*, *>>()

    fun add(preset: CSPreset<*, *>) {
        presets += preset
        preset.eventDestruct { if (!isDestructed) presets -= preset }
    }

    override fun clear(): Unit = preset.store.operation {
        properties.values.forEach { it.clear() }
        childContexts.toList().onEach { it.clear() }
        presets.toList().onEach { it.clear() }
    }

    private val String.newKey: String
        get() = presetId?.let { "$it $this" } ?: this

    override fun property(
        key: String, default: String, onChange: ArgFun<String>?,
    ): CSStringValuePresetProperty = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun property(
        key: String, default: Boolean, onChange: ArgFun<Boolean>?,
    ): CSBooleanValuePresetProperty = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun property(
        key: String, default: Float, onChange: ArgFun<Float>?,
    ): CSFloatValuePresetProperty = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun property(
        key: String, default: Int, onChange: ArgFun<Int>?
    ): CSIntValuePresetProperty = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun property(
        key: String, default: () -> Int, onChange: ArgFun<Int>?
    ): CSIntValuePresetProperty = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun <T> property(
        key: String, values: () -> Collection<T>,
        default: () -> T, onChange: ArgFun<T>?
    ): CSListItemValuePresetProperty<T> = preset.property(
        this, key.newKey, values, default, onChange
    ).init(key)

    override fun nullIntProperty(
        key: String, default: Int?, onChange: ((value: Int?) -> Unit)?
    ): CSIntNullablePresetProperty = preset.nullIntProperty(
        this, key.newKey, default, onChange
    ).init(key)

    override fun nullFloatProperty(
        key: String, default: Float?, onChange: ((value: Float?) -> Unit)?
    ): CSFloatNullablePresetProperty = preset.nullFloatProperty(
        this, key.newKey, default, onChange
    ).init(key)

    override fun nullDoubleProperty(
        key: String, default: Double?, onChange: ((value: Double?) -> Unit)?
    ): CSDoubleNullablePresetProperty = preset.nullDoubleProperty(
        this, key.newKey, default, onChange
    ).init(key)

    override fun nullStringProperty(
        key: String, default: String?, onChange: ((value: String?) -> Unit)?
    ): CSStringNullablePresetProperty = preset.nullStringProperty(
        this, key.newKey, default, onChange
    ).init(key)

    override fun <T> nullListItemProperty(
        key: String, values: List<T>, default: T?, onChange: ((value: T?) -> Unit)?
    ): CSListItemNullablePresetProperty<T?> = preset.nullListItemProperty(
        this, key.newKey, values, default, onChange
    ).init(key)

    override fun property(
        key: String, default: List<Int>, onChange: ArgFun<List<Int>>?
    ): CSIntListValuePresetProperty = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun <T : CSHasId> property(
        key: String, values: List<T>,
        default: List<T>, onChange: ArgFun<List<T>>?
    ): CSHasIdListValuePresetProperty<T> = preset.property(
        this, key.newKey, values, default, onChange
    ).init(key)
}