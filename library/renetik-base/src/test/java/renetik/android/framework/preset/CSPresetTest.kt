package renetik.android.framework.preset

import junit.framework.Assert.*
import org.junit.Test
import renetik.android.framework.CSModelBase
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.lang.property.isTrue
import renetik.android.framework.preset.property.CSPresetStoreEventProperty
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.collections.second

class CSPresetTest {

    private val store = CSJsonObject()
    private val parent = CSPresetTestParentClass(store)

    @Test
    fun test() {
        assertEquals("parent preset item 1", parent.parentPreset.current.value.id)
//        assertTrue("parent initial", parent.parentPreset.store.value.data.isEmpty())
        assertEquals("parent initial", parent.property.value)
        assertFalse(parent.parentPreset.isModified.isTrue)
        assertEquals("preset item 1", parent.child.childPreset.current.value.id)
        assertEquals("initial", parent.child.property.value)
        assertFalse(parent.child.childPreset.isModified.isTrue)
    }

    @Test
    fun test2() {
        parent.property.value = "new value"
        assertEquals("new value", parent.property.value)
        assertTrue(parent.parentPreset.isModified.isTrue)
        assertFalse(parent.child.childPreset.isModified.isTrue)

        parent.parentPreset.saveAsCurrent()
        parent.parentPreset.current.value = parent.parentPreset.list.items.second()
        assertEquals("parent initial", parent.property.value)
        assertFalse(parent.parentPreset.isModified.isTrue)
        assertFalse(parent.child.childPreset.isModified.isTrue)

        parent.parentPreset.current.value = parent.parentPreset.list.items.first()
        assertEquals("new value", parent.property.value)
        assertFalse(parent.parentPreset.isModified.isTrue)
        assertFalse(parent.child.childPreset.isModified.isTrue)
    }

    @Test
    fun test3() {
        parent.child.property.value = "new value"
        assertEquals("new value", parent.child.property.value)
        assertTrue(parent.child.childPreset.isModified.isTrue)

        parent.child.childPreset.saveAsCurrent()
        parent.child.childPreset.current.value = parent.child.childPreset.list.items.second()
        assertEquals("initial", parent.child.property.value)
        assertFalse(parent.child.childPreset.isModified.isTrue)

        parent.child.childPreset.current.value = parent.child.childPreset.list.items.first()
        assertEquals("new value", parent.child.property.value)
        assertFalse(parent.child.childPreset.isModified.isTrue)
    }

    @Test
    fun test4() {
        parent.child.property.value = "new value"
        assertEquals("new value", parent.child.property.value)
        assertFalse(parent.parentPreset.isModified.isTrue)
        assertTrue(parent.child.childPreset.isModified.isTrue)

        parent.parentPreset.saveAsCurrent()
        parent.parentPreset.current.value = parent.parentPreset.list.items.second()
        assertEquals("initial", parent.child.property.value)
        assertFalse(parent.parentPreset.isModified.isTrue)

        parent.parentPreset.current.value = parent.parentPreset.list.items.first()
        assertEquals("new value", parent.child.property.value)
    }
}

class CSPresetTestPresetItem(override val id: String) : CSPresetItem {
    override val store = CSJsonObject()
    override fun delete() = Unit
    override fun save(properties: Iterable<CSPresetStoreEventProperty<*>>) =
        properties.forEach { it.save(store) }
}

class CSPresetTestPresetItemList : CSPresetItemList<CSPresetTestPresetItem> {
    override val path: String = "path"
    override val items = mutableListOf<CSPresetTestPresetItem>()
    override fun put(preset: CSPresetTestPresetItem) {
        items.add(preset)
    }

    override fun remove(preset: CSPresetTestPresetItem) {
        items.remove(preset)
    }
}

class CSPresetTestParentClass(val store: CSStoreInterface) : CSModelBase() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.put(CSPresetTestPresetItem("parent preset item 1"))
        presetList.put(CSPresetTestPresetItem("parent preset item 2"))
        presetList.put(CSPresetTestPresetItem("parent preset item 3"))
    }

    val id = "parentClass"
    val parentPreset = CSPreset(this, store, "$id parent", presetList)
    val child = CSPresetTestChildClass(this, parentPreset)
    val property = parentPreset.property(this, "property", "parent initial")
}

class CSPresetTestChildClass(parent: CSPresetTestParentClass, preset: CSPreset<*, *>) :
    CSModelBase() {

    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.put(CSPresetTestPresetItem("preset item 1"))
        presetList.put(CSPresetTestPresetItem("preset item 2"))
        presetList.put(CSPresetTestPresetItem("preset item 3"))
    }

    val id = "${parent.id} childClass"
    val childPreset = CSPreset(this, preset, "$id child", presetList)
    val property = childPreset.property(this, "property", "initial")
}