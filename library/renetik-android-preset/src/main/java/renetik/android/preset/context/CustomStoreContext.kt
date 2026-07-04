package renetik.android.preset.context

import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.CSHasId
import renetik.android.event.change.invoke
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.CSModel
import renetik.android.event.lifecycle.parent
import renetik.android.event.registration.CSRegistration
import renetik.android.json.obj.CSJsonObjectInterface
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore
import renetik.android.store.context.CSStoreContext
import renetik.android.store.extensions.nullDoubleProperty
import renetik.android.store.extensions.nullFloatProperty
import renetik.android.store.extensions.nullIntProperty
import renetik.android.store.extensions.nullListItemProperty
import renetik.android.store.extensions.nullStringProperty
import renetik.android.store.extensions.operation
import renetik.android.store.extensions.property
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.property.listenLoad
import renetik.android.store.property.value.CSHasIdListValueStoreProperty
import renetik.android.store.property.value.CSIntListValueStoreProperty

class CustomStoreContext(
    parent: CSHasDestruct? = null,
    val store: CSStore,
    private val hasId: CSHasId? = null,
    override val key: String? = null,
) : CSModel(parent), CSStoreContext {
    override val data: CSJsonObjectInterface = store
    override val id = hasId?.id?.let { id -> key?.let { "$id $it" } ?: id } ?: key ?: ""
    private val childContexts = mutableListOf<CSStoreContext>()

    private fun <T : CSStoreContext> T.init() = apply {
        childContexts += this; eventDestruct { childContexts -= this }
    }

    override fun context(parent: CSHasDestruct, key: String?) =
        (key?.let { CustomStoreContext(parent, store, this, key) }
            ?: CustomStoreContext(parent, store, hasId, this.key)).init()

    override fun appContext(parent: CSHasDestruct, key: String?) =
        AppStoreContext(parent, this, key).init()

    override fun memoryContext(parent: CSHasDestruct, key: String?) =
        RuntimeStoreContext(parent, this, key).init()

    override fun onChange(function: (Unit) -> Unit): CSRegistration =
        store.eventLoaded.listen { function(Unit) }


    private val properties = mutableListOf<CSStoreProperty<*>>()
    private fun <T : CSStoreProperty<*>> T.init() = apply {
        properties += this; eventDestruct { properties -= this }
    }

    private val presets = mutableListOf<CSPreset<*, *>>()

    fun add(preset: CSPreset<*, *>) {
        presets += preset; preset.eventDestruct { presets -= preset }
    }

    override fun clear() = store.operation {
        properties.toList().forEach { it.clear() }
        childContexts.toList().onEach { it.clear() }
        presets.toList().onEach { it.clear() }
    }

    private val String.newKey
        get() = if (id.isNotBlank()) "$id $this" else this

    override fun property(
        key: String, default: String, onChange: ArgFun<String>?,
    ) = store.property(this, key.newKey, default, onChange).init()

    override fun property(
        key: String, default: Boolean, onChange: ArgFun<Boolean>?,
    ) = store.property(this, key.newKey, default, onChange).init()

    override fun property(
        key: String, default: Float, onChange: ArgFun<Float>?,
    ) = store.property(this, key.newKey, default, onChange).init()

    override fun property(
        key: String, default: Int, onChange: ArgFun<Int>?
    ) = store.property(this, key.newKey, default, onChange).init()

    override fun property(
        key: String, default: () -> Int, onChange: ArgFun<Int>?
    ) = store.property(this, key.newKey, default, onChange).init()

    override fun <T> property(
        key: String, values: () -> Collection<T>, default: () -> T, onChange: ArgFun<T>?
    ) = store.property(this, key.newKey, values, default, onChange).init()

    override fun nullIntProperty(
        key: String, default: Int?, onChange: ((value: Int?) -> Unit)?
    ) = store.nullIntProperty(this, key.newKey, default, onChange).init()

    override fun nullFloatProperty(
        key: String, default: Float?, onChange: ((value: Float?) -> Unit)?
    ) = store.nullFloatProperty(this, key.newKey, default, onChange).init()

    override fun nullDoubleProperty(
        key: String, default: Double?, onChange: ((value: Double?) -> Unit)?
    ) = store.nullDoubleProperty(this, key.newKey, default, onChange).init()

    override fun nullStringProperty(
        key: String, default: String?, onChange: ((value: String?) -> Unit)?
    ) = store.nullStringProperty(this, key.newKey, default, onChange).init()

    override fun <T> nullListItemProperty(
        key: String, values: List<T>, default: T?, onChange: ((value: T?) -> Unit)?
    ) = store.nullListItemProperty(key.newKey, values, default, onChange)
        .parent(this).listenLoad().init()

    override fun property(
        key: String, default: List<Int>, onChange: ArgFun<List<Int>>?
    ) = CSIntListValueStoreProperty(store, key.newKey, default, onChange)
        .parent(this).listenLoad().init()

    override fun <T : CSHasId> property(
        key: String, values: List<T>,
        default: List<T>, onChange: ArgFun<List<T>>?
    ) = CSHasIdListValueStoreProperty(store, key.newKey, default, onChange = onChange)
        .parent(this).listenLoad().init()
}