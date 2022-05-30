package renetik.android.framework.preset.property.nullable

import junit.framework.Assert.assertEquals
import org.junit.Test
import renetik.android.framework.base.CSBase
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.*
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStore

class CSIntNullablePresetEventPropertyTest {

    private val store = CSJsonObject()
    private val parent = CSPresetTestParentClass(store)

    @Test
    fun test() {
        assertEquals(1, parent.property1.value)
        parent.property1.value = 2
        assertEquals(2, parent.property1.value)
        parent.property1.value = null
        assertEquals(null, parent.property1.value)
        parent.parentPreset.reload()
        assertEquals(1, parent.property1.value)
    }

    @Test
    fun test2() {
        parent.property1.value = null
        parent.property2.value = 3
        assertEquals(null, parent.property1.value)
        assertEquals(3, parent.property2.value)
    }
}

class CSPresetTestPresetItem(override val id: String) : CSPresetItem {
    override val store = CSJsonObject()
    override fun delete() = Unit
    override fun save(properties: Iterable<CSPresetKeyData>) =
        properties.forEach { it.saveTo(store) }
}

class CSPresetTestPresetItemList : CSPresetItemList<CSPresetTestPresetItem> {
    override val defaultList = mutableListOf<CSPresetTestPresetItem>()
    override val userList = mutableListOf<CSPresetTestPresetItem>()
    override fun add(item: CSPresetTestPresetItem) {
        defaultList.add(item)
    }

    override fun remove(item: CSPresetTestPresetItem) {
        defaultList.remove(item)
    }

    override fun createPresetItem(title: String, isDefault: Boolean, id: String) =
        CSPresetTestPresetItem(title)

    override fun reload() = Unit
}

class CSPresetTestParentClass(val store: CSStore) : CSBase() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.add(CSPresetTestPresetItem("Clear Parent"))
    }

    val id = "parentClass"
    val parentPreset = CSPreset(this, store, "$id parent", presetList)
    val property1 = parentPreset.propertyNullInt(this, "property1", 1)
    val property2 = parentPreset.propertyNullInt(this, "property2", 2)
}