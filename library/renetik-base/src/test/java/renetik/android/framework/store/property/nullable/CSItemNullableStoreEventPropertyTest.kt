package renetik.android.framework.store.property.nullable

import org.junit.Assert.*
import org.junit.Test
import renetik.android.framework.store.property.nullable.TestEnum.*
import renetik.android.framework.json.store.CSStringJsonStore

private enum class TestEnum {
    First, Second, Third
}

class CSItemNullableStoreEventPropertyTest {
    private val store = CSStringJsonStore("{}")
    private var _value: TestEnum? = null
    private val property = CSListItemNullableStoreEventProperty(store, "key",
        TestEnum.values().toList(), First) {
        _value = it
    }

    @Test
    fun firstLoad() {
        assertEquals(First, property.value)
        assertEquals(null, _value)
        assertTrue(store.has(property.key))
    }

    @Test
    fun nulling() {
        property.value = Second
        assertEquals(Second, property.value)
        assertEquals(Second, _value)
        assertTrue(store.has(property.key))
        assertEquals("""{"key":"Second"}""", store.jsonString)

        property.value = null
        assertEquals(null, property.value)
        assertEquals(null, _value)
        assertFalse(store.has(property.key))
        assertEquals("{}", store.jsonString)
    }

    @Test
    fun reload() {
        assertEquals(First, property.value)
        property.save(store, Third)
        assertEquals("""{"key":"Third"}""", store.jsonString)
        assertEquals(First, property.value)

        property.reload()
        assertEquals(Third, property.value)
        assertEquals(Third, _value)
    }

    @Test
    fun thisIsProblematicBehaviour() {
        property.value = Third
        assertEquals("""{"key":"Third"}""", store.jsonString)
        assertEquals(Third, property.value)

        property.value = null
        assertEquals(null, property.value)
        assertEquals("""{}""", store.jsonString)

        property.reload()
        // TODO: After Reload value is changed but store is same
        // TODO: Maybe best is to have initial as default value for nullable properties
        // TODO: What breaks concept of initial value and is different from value property

        assertEquals(First, property.value)
        assertEquals("""{"key":"First"}""", store.jsonString)
    }
}