package renetik.android.framework.preset

import junit.framework.Assert.*
import org.junit.Test
import renetik.android.framework.CSModelBase
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.lang.property.isFalse
import renetik.android.framework.lang.property.isTrue
import renetik.android.framework.preset.property.CSPresetEventProperty
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.collections.second
import renetik.kotlin.collections.third

class CSPresetTest {

    private val store = CSJsonObject()
    private val parent = CSPresetTestParentClass(store)

    @Test
    fun test() {
        assertEquals("Clear Parent", parent.parentPreset.item.value.id)
        assertEquals("parent initial", parent.property.value)
        assertFalse(parent.parentPreset.isModified.isTrue)
        assertEquals("Clear Child", parent.child.childPreset.item.value.id)
        assertEquals("child initial", parent.child.property.value)
        assertFalse(parent.child.childPreset.isModified.isTrue)

        assertTrue(parent.parentPreset.store.value.has("parentClass childClass child preset store"))
        assertEquals(CSJsonObject(""" {"property":"child initial"} """),
            parent.child.childPreset.store.value)
    }

    @Test
    fun test2() {
        parent.property.value = "parent.property.value 1"
        assertEquals("parent.property.value 1", parent.property.value)
        assertTrue(parent.parentPreset.isModified.isTrue)
        assertTrue(parent.child.childPreset.isModified.isFalse)

        parent.parentPreset.saveAsNew(CSPresetTestPresetItem("Parent Item 1"))
        assertTrue(parent.parentPreset.isModified.isFalse)
        assertTrue(parent.child.childPreset.isModified.isFalse)

        parent.property.value = "parent.property.value 2"
        assertTrue(parent.parentPreset.isModified.isTrue)
        assertTrue(parent.child.childPreset.isModified.isFalse)

        parent.parentPreset.item.value = parent.parentPreset.list.items.first()
        assertEquals("parent initial", parent.property.value)
        assertTrue(parent.parentPreset.isModified.isFalse)
        assertTrue(parent.child.childPreset.isModified.isFalse)
    }

    @Test
    fun test3() {
        parent.child.property.value = "new value"
        assertEquals("new value", parent.child.property.value)
        assertTrue(parent.child.childPreset.isModified.isTrue)
        assertTrue(parent.parentPreset.isModified.isFalse)

        parent.child.childPreset.saveAsNew(CSPresetTestPresetItem("Parent Item 1"))
        assertEquals("new value", parent.child.property.value)
        assertTrue(parent.child.childPreset.isModified.isFalse)
        assertTrue(parent.parentPreset.isModified.isTrue)

        parent.child.property.value = "new value"
        parent.child.childPreset.saveAsCurrent()
        assertEquals("new value", parent.child.property.value)
        parent.parentPreset.reload()

        assertEquals("child initial", parent.child.property.value)
        assertTrue(parent.parentPreset.isModified.isFalse)
        assertTrue(parent.child.childPreset.isModified.isFalse)
    }

    @Test
    fun test4() {
        parent.child.property.value = "new value"
        assertEquals("new value", parent.child.property.value)
        assertFalse(parent.parentPreset.isModified.isTrue)
        assertTrue(parent.child.childPreset.isModified.isTrue)

        parent.parentPreset.saveAsNew(CSPresetTestPresetItem("Parent Item 1"))
        assertTrue(parent.parentPreset.isModified.isFalse)
        assertTrue(parent.child.childPreset.isModified.isTrue)
        assertEquals("new value", parent.child.property.value)

        parent.parentPreset.item.value = parent.parentPreset.list.items.first()
        assertEquals("child initial", parent.child.property.value)
    }

    @Test
    fun test5() {
        parent.child.property.value = "Parent Item 2 child.property.value"
        parent.parentPreset.saveAsNew(CSPresetTestPresetItem("Parent Item 2"))

        parent.child.property.value = "Parent Item 3 child.property.value"
        parent.parentPreset.saveAsNew(CSPresetTestPresetItem("Parent Item 3"))
        assertTrue(parent.child.childPreset.isModified.isTrue)
        assertTrue(parent.parentPreset.isModified.isFalse)

        parent.parentPreset.item.value = parent.parentPreset.list.items.first()
        assertEquals("child initial", parent.child.property.value)

        parent.parentPreset.item.value = parent.parentPreset.list.items.third()
        assertEquals("Parent Item 3 child.property.value", parent.child.property.value)
        assertTrue(parent.parentPreset.isModified.isFalse)

        parent.parentPreset.item.value = parent.parentPreset.list.items.second()
        assertEquals("Parent Item 2 child.property.value", parent.child.property.value)
    }
}

class CSPresetTestPresetItem(override val id: String) : CSPresetItem {
    override val store = CSJsonObject()
    override fun delete() = Unit
    override fun save(properties: Iterable<CSPresetEventProperty<*>>) =
        properties.forEach { it.save(store) }
}

private class CSPresetTestPresetItemList : CSPresetItemList<CSPresetTestPresetItem> {
    override val path: String = "path"
    override val items = mutableListOf<CSPresetTestPresetItem>()
    override fun put(item: CSPresetTestPresetItem) {
        items.add(item)
    }

    override fun remove(item: CSPresetTestPresetItem) {
        items.remove(item)
    }
}

private class CSPresetTestParentClass(val store: CSStoreInterface) : CSModelBase() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.put(CSPresetTestPresetItem("Clear Parent"))
    }

    val id = "parentClass"
    val parentPreset = CSPreset(this, store, "$id parent", presetList)
    val child = CSPresetTestChildClass(this, parentPreset)
    val property = parentPreset.property(this, "property", "parent initial")
}

private class CSPresetTestChildClass(parent: CSPresetTestParentClass, preset: CSPreset<*, *>) :
    CSModelBase() {

    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.put(CSPresetTestPresetItem("Clear Child"))
    }

    val id = "${parent.id} childClass"
    val childPreset = CSPreset(this, preset, "$id child", presetList)
    val property = childPreset.property(this, "property", "child initial")
}