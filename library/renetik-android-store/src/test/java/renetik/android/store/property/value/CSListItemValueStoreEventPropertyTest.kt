package renetik.android.store.property.value

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.store.json.CSStringJsonStore

private enum class TestEnum {
    First, Second, Third
}

@RunWith(RobolectricTestRunner::class)
class CSListItemValueStoreEventPropertyTest {

    private val store = CSStringJsonStore("{}")
    private var _value: TestEnum? = null
    private val property = CSListItemValueStoreEventProperty(store, "key",
        TestEnum.values().toList(), TestEnum.First) {
        _value = it
    }

    @Test
    fun firstLoad() {
        Assert.assertEquals(TestEnum.First, property.value)
        Assert.assertEquals(null, _value)
    }

    @Test
    fun value() {
        property.value = TestEnum.Second
        Assert.assertEquals(TestEnum.Second, property.value)
        Assert.assertEquals(TestEnum.Second, _value)
        Assert.assertTrue(store.has(property.key))
        Assert.assertEquals("""{"key":"Second"}""", store.jsonString)
    }

//    @Test
//    fun reload() {
//        property.save(store, TestEnum.Third)
//        Assert.assertEquals("""{"key":"Third"}""", store.jsonString)
//        Assert.assertEquals(TestEnum.First, property.value)
//
//        property.reload()
//        Assert.assertEquals(TestEnum.Third, property.value)
//        Assert.assertEquals(TestEnum.Third, _value)
//    }
}