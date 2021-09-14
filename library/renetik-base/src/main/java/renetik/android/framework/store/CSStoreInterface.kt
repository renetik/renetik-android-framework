package renetik.android.framework.store

import renetik.android.framework.event.property.CSPropertyStoreInterface
import renetik.android.framework.json.CSJsonMapInterface
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.lang.CSId
import renetik.android.framework.store.property.late.CSBooleanLateStoreEventProperty
import renetik.android.framework.store.property.late.CSStringLateStoreEventProperty
import renetik.android.framework.store.property.late.CSValuesItemLateStoreEventProperty
import renetik.android.framework.store.property.nullable.CSBooleanNullableStoreEventProperty
import renetik.android.framework.store.property.nullable.CSIntNullableStoreEventProperty
import renetik.android.framework.store.property.nullable.CSListItemNullableStoreEventProperty
import renetik.android.framework.store.property.preset.*
import renetik.android.primitives.asDouble
import renetik.android.primitives.asFloat
import renetik.android.primitives.asInt
import renetik.android.primitives.asLong
import kotlin.reflect.KClass

interface CSStoreInterface : CSPropertyStoreInterface,
    Iterable<Map.Entry<String, Any?>>, CSJsonMapInterface {

    val data: Map<String, Any?>

    override fun asStringMap(): Map<String, *> = data

    override fun iterator(): Iterator<Map.Entry<String, Any?>> = data.iterator()

    fun has(key: String): Boolean = data.containsKey(key)

    fun save(key: String, value: String?)
    fun get(key: String): String? = data[key]?.toString()

    fun save(key: String, value: Map<String, *>?)
    fun getMap(key: String): Map<String, *>?

    fun save(key: String, value: Array<*>?)
    fun getArray(key: String): Array<*>?

    fun save(key: String, value: List<*>?)
    fun getList(key: String): List<*>?

    fun <T : CSJsonObject> getJsonList(key: String, type: KClass<T>): List<T>?

    fun <T : CSJsonObject> save(key: String, jsonObject: T?)
    fun <T : CSJsonObject> getJsonObject(key: String, type: KClass<T>): T?

    fun load(store: CSStoreInterface)
    fun clear()
    fun clear(key: String)

    fun reload(store: CSStoreInterface) {
        clear()
        load(store)
    }

    fun save(key: String, value: Int?) = save(key, value?.toString())
    fun save(key: String, value: Boolean?) = save(key, value?.toString())
    fun save(key: String, value: Float?) = save(key, value?.toString())
    fun save(key: String, value: Double?) = save(key, value?.toString())
    fun save(key: String, value: Long?) = save(key, value?.toString())

    fun getBoolean(key: String, default: Boolean) = get(key)?.toBoolean() ?: default
    fun getBoolean(key: String, default: Boolean? = null) = get(key)?.toBoolean() ?: default
    fun getDouble(key: String, default: Double) = get(key)?.asDouble() ?: default
    fun getDouble(key: String, default: Double? = null) = get(key)?.asDouble() ?: default
    fun getLong(key: String, default: Long) = get(key)?.asLong() ?: default
    fun getLong(key: String, default: Long?) = get(key)?.asLong() ?: default
    fun getFloat(key: String, default: Float) = get(key)?.asFloat() ?: default
    fun getFloat(key: String, default: Float? = null) = get(key)?.asFloat() ?: default
    fun getInt(key: String, default: Int) = get(key)?.asInt() ?: default
    fun getInt(key: String, default: Int? = null) = get(key)?.asInt() ?: default
    fun getString(key: String, default: String) = get(key) ?: default
    fun getString(key: String) = get(key)

    override fun property(key: String, value: String, onChange: ((value: String) -> Unit)?) =
        CSStringValueStoreEventProperty(this, key, value, onChange)

    override fun property(key: String, value: Boolean, onChange: ((value: Boolean) -> Unit)?) =
        CSBooleanValueStoreEventProperty(this, key, value, onChange)

    override fun property(key: String, value: Int, onChange: ((value: Int) -> Unit)?) =
        CSIntValueStoreEventProperty(this, key, value, onChange)

    override fun property(key: String, value: Double, onChange: ((value: Double) -> Unit)?) =
        CSDoubleValueStoreEventProperty(this, key, value, onChange)

    override fun property(key: String, value: Float, onChange: ((value: Float) -> Unit)?) =
        CSFloatValueStoreEventProperty(this, key, value, onChange)

    override fun <T> property(
        key: String, values: List<T>, value: T, onChange: ((value: T) -> Unit)?) =
        CSListItemValueStoreEventProperty(this, key, values, value, onChange)

    override fun <T : CSId> property(
        key: String, values: Iterable<T>, value: List<T>, onChange: ((value: List<T>) -> Unit)?) =
        CSListStoreEventProperty(this, key, values, value, onChange)

    fun lateStringProperty(key: String, onChange: ((value: String) -> Unit)? = null) =
        CSStringLateStoreEventProperty(this, key, onChange)

    override fun lateBooleanProperty(key: String, onChange: ((value: Boolean) -> Unit)?) =
        CSBooleanLateStoreEventProperty(this, key, onChange)

    fun <T> lateItemProperty(key: String, values: List<T>,
                             onChange: ((value: T) -> Unit)? = null) =
        CSValuesItemLateStoreEventProperty(this, key, values, onChange)

    override fun propertyNullBool(key: String, default: Boolean?,
                                  onChange: ((value: Boolean?) -> Unit)?) =
        CSBooleanNullableStoreEventProperty(this, key, default, onChange)

    override fun propertyNullInt(key: String, default: Int?, onChange: ((value: Int?) -> Unit)?) =
        CSIntNullableStoreEventProperty(this, key, default, onChange)

    override fun <T> propertyNullListItem(
        key: String, values: List<T>, default: T?, onChange: ((value: T?) -> Unit)?) =
        CSListItemNullableStoreEventProperty(this, key, values, default, onChange)
}

