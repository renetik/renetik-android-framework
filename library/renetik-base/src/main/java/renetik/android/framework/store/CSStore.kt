package renetik.android.framework.store

import renetik.android.core.CSApplication.Companion.app
import renetik.android.core.kotlin.primitives.asDouble
import renetik.android.core.kotlin.primitives.asFloat
import renetik.android.core.kotlin.primitives.asInt
import renetik.android.core.kotlin.primitives.asLong
import renetik.android.core.lang.CSHasId
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.event.CSEvent
import renetik.android.json.CSJsonObject
import renetik.android.framework.store.property.CSPropertyStore
import renetik.android.framework.store.json.CSStoreJsonObject
import renetik.android.json.CSJsonObjectInterface
import renetik.android.framework.store.json.CSFileJsonStore
import renetik.android.framework.store.property.late.*
import renetik.android.framework.store.property.nullable.CSBooleanNullableStoreEventProperty
import renetik.android.framework.store.property.nullable.CSIntNullableStoreEventProperty
import renetik.android.framework.store.property.nullable.CSListItemNullableStoreEventProperty
import renetik.android.framework.store.property.value.*
import java.io.Closeable
import kotlin.reflect.KClass

interface CSStore : CSPropertyStore,
    Iterable<Map.Entry<String, Any?>>, renetik.android.json.CSJsonObjectInterface {

    companion object {
        var store = CSFileJsonStore(app, "app", isJsonPretty = true)
    }

    val eventChanged: CSEvent<CSStore>

    val data: Map<String, Any?>

    override fun asStringMap(): Map<String, *> = data

    override fun iterator(): Iterator<Map.Entry<String, Any?>> = data.iterator()

    fun has(key: String): Boolean = data.containsKey(key)

    fun bulkSave(): Closeable = Closeable { logWarn("Bulk save not implemented") }
    fun set(key: String, value: String?)
    fun get(key: String): String? = data[key]?.toString()

    fun set(key: String, value: Map<String, *>?)
    fun getMap(key: String): Map<String, *>?

    fun set(key: String, value: Array<*>?)
    fun getArray(key: String): Array<*>?

    fun set(key: String, value: List<*>?)
    fun getList(key: String): List<*>?

    fun <T : renetik.android.json.CSJsonObject> getJsonObjectList(key: String, type: KClass<T>): List<T>?

    fun <T : renetik.android.json.CSJsonObject> set(key: String, value: T?)
    fun <T : renetik.android.json.CSJsonObject> getJsonObject(key: String, type: KClass<T>): T?

    fun load(store: CSStore)
    fun clear()
    fun clear(key: String)

    fun reload(store: CSStore) = bulkSave().use {
        clear()
        load(store)
    }

    fun set(key: String, value: Boolean?) = set(key, value?.toString())
    fun set(key: String, value: Int?) = set(key, value?.toString())
    fun set(key: String, value: Long?) = set(key, value?.toString())
    fun set(key: String, value: Double?) = set(key, value?.toString())
    fun set(key: String, value: Float?) = set(key, value?.toString())

    fun getString(key: String, default: String): String = get(key) ?: default
    fun getString(key: String): String? = get(key)
    fun getBoolean(key: String, default: Boolean): Boolean = get(key)?.toBoolean() ?: default
    fun getBoolean(key: String, default: Boolean? = null): Boolean? =
        get(key)?.toBoolean() ?: default

    fun getInt(key: String, default: Int): Int = get(key)?.asInt() ?: default
    fun getInt(key: String, default: Int? = null): Int? = get(key)?.asInt() ?: default
    fun getLong(key: String, default: Long): Long = get(key)?.asLong() ?: default
    fun getLong(key: String, default: Long? = null): Long? = get(key)?.asLong() ?: default
    fun getFloat(key: String, default: Float): Float = get(key)?.asFloat() ?: default
    fun getFloat(key: String, default: Float? = null): Float? = get(key)?.asFloat() ?: default
    fun getDouble(key: String, default: Double): Double = get(key)?.asDouble() ?: default
    fun getDouble(key: String, default: Double? = null): Double? = get(key)?.asDouble() ?: default

    override fun property(key: String, value: String, onChange: ((value: String) -> Unit)?) =
        CSStringValueStoreEventProperty(this, key, value, listenStoreChanged = false, onChange)

    override fun property(key: String, value: Boolean, onChange: ((value: Boolean) -> Unit)?) =
        CSBooleanValueStoreEventProperty(this, key, value, onChange)

    override fun property(key: String, value: Int, onChange: ((value: Int) -> Unit)?) =
        CSIntValueStoreEventProperty(this, key, value, onChange = onChange)

    override fun property(key: String, value: Double, onChange: ((value: Double) -> Unit)?) =
        CSDoubleValueStoreEventProperty(this, key, value, onChange)

    override fun property(key: String, value: Float, onChange: ((value: Float) -> Unit)?) =
        CSFloatValueStoreEventProperty(this, key, value, onChange)

    override fun property(key: String, value: Long, onChange: ((value: Long) -> Unit)?) =
        CSLongValueStoreEventProperty(this, key, value, onChange)

    override fun <T> property(
        key: String, values: List<T>, value: T, onChange: ((value: T) -> Unit)?) =
        CSListItemValueStoreEventProperty(this, key, values, value, false, onChange)

    override fun <T> property(
        key: String, values: List<T>, getDefault: () -> T, onChange: ((value: T) -> Unit)?) =
        CSListItemValueStoreEventProperty(this, key, { values }, getDefault, false, onChange)

    override fun <T> property(
        key: String, getValues: () -> List<T>,
        defaultIndex: Int, onChange: ((value: T) -> Unit)?
    ) = CSListItemValueStoreEventProperty(this, key, getValues,
        { getValues()[defaultIndex] }, listenStoreChanged = false, onChange)

    override fun <T : CSHasId> property(
        key: String, values: Iterable<T>, value: List<T>, onChange: ((value: List<T>) -> Unit)?) =
        CSListValueStoreEventProperty(this, key, values, value, onChange)

    fun <T : CSStoreJsonObject> CSStore.lateProperty(
        key: String, listType: KClass<T>, onApply: ((value: List<T>) -> Unit)? = null
    ) = CSJsonListLateStoreEventProperty(this, key, listType, onApply)

    fun lateStringProperty(key: String, onChange: ((value: String) -> Unit)? = null) =
        CSStringLateStoreEventProperty(this, key, onChange)

    fun lateIntProperty(key: String, onChange: ((value: Int) -> Unit)? = null) =
        CSIntLateStoreEventProperty(this, key, onChange)

    override fun lateBoolProperty(key: String, onChange: ((value: Boolean) -> Unit)?) =
        CSBooleanLateStoreEventProperty(this, key, onChange)

    fun <T> lateItemProperty(key: String, values: List<T>,
                             onChange: ((value: T) -> Unit)? = null) =
        CSValuesItemLateStoreEventProperty(this, key, values, onChange)

    override fun nullBoolProperty(key: String, default: Boolean?,
                                  onChange: ((value: Boolean?) -> Unit)?) =
        CSBooleanNullableStoreEventProperty(this, key, default, onChange)

    override fun propertyNullInt(key: String, default: Int?, onChange: ((value: Int?) -> Unit)?) =
        CSIntNullableStoreEventProperty(this, key, default, onChange)

    override fun <T> propertyNullListItem(
        key: String, values: List<T>, default: T?, onChange: ((value: T?) -> Unit)?) =
        CSListItemNullableStoreEventProperty(this, key, values, default, onChange)
}

