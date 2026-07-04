package renetik.android.core.lang.variable

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import renetik.android.core.lang.variable.CSVariable.Companion.variable
import renetik.android.core.lang.variable.CSWeakAtomic.Companion.weakAtomic
import renetik.android.core.lang.variable.CSSynchronizedProperty.Companion.synchronized

class CSVariableExtensionsTest {

    @Test
    fun variableImplApplyAndDelegateSetValueInvokeOnChange() {
        val changes = mutableListOf<Int>()
        val variable = CSVariableImpl(1) { changes += it }
        val holder = VariableHolder(variable)

        variable.value = 1
        variable.value = 2
        assertEquals(emptyList<Int>(), changes)

        variable.apply()
        holder.value = 2
        holder.value = 3
        holder.value = 3

        assertEquals(listOf(2, 3), changes)
    }

    @Test
    fun computedVariableReadsAndWritesBackingState() {
        var backing = 10
        val computed = variable(from = { backing * 2 }, to = { backing = it / 2 })

        assertEquals(20, computed.value)
        computed.value = 50
        assertEquals(25, backing)
        assertEquals(50, computed.value)
    }

    @Test
    fun variableExtensionsAssignClearAndApplyArithmetic() {
        val nullable = variable<String?>("value")
        val nullableFloat = variable<Float?>(2f)
        val float = variable(8f)

        nullable assign "changed"
        assertEquals("changed", nullable.value)
        nullable.clear()
        assertNull(nullable.value)

        nullableFloat += 3f
        nullableFloat -= 1f
        assertEquals(4f, nullableFloat.value)
        nullableFloat.clear()
        nullableFloat += 3f
        assertNull(nullableFloat.value)

        float *= 2f
        float /= 4f
        assertEquals(4f, float.value)
    }

    @Test
    fun listValuesVariableNavigatesEnumValues() {
        val mode = ModeVariable()

        assertFalse(mode.isLast)
        assertEquals(Mode.Second, mode.next)
        mode.next()
        assertEquals(Mode.Second, mode.value)
        mode.value(2)
        assertTrue(mode.isLast)
        assertEquals(Mode.Second, mode.previous)
        mode.previous()
        assertEquals(Mode.Second, mode.value)
    }

    @Test
    fun weakAtomicCompareSetGetAndClear() {
        val first = Any()
        val second = Any()
        val third = Any()
        val atomic = weakAtomic(first)

        assertSame(first, atomic.value)
        assertFalse(atomic.compareAndSet(second, third))
        assertTrue(atomic.compareAndSet(first, second))
        assertSame(second, atomic.value)
        assertSame(second, atomic.getAndSet(third))
        assertSame(third, atomic.clear())
        assertNull(atomic.value)
    }

    @Test
    fun synchronizedPropertyStoresValueAndCallsDidSet() {
        val holder = SynchronizedHolder()

        holder.value = "one"
        holder.value = "two"

        assertEquals("two", holder.value)
        assertEquals(listOf("one", "two"), holder.changes)
    }

    private enum class Mode { First, Second, Third }

    private class ModeVariable : CSListValuesVariable<Mode> {
        override val values = Mode.entries
        override var value = Mode.First
    }

    private class SynchronizedHolder {
        val changes = mutableListOf<String>()
        var value by synchronized("initial") { changes += it }
    }

    private class VariableHolder(variable: CSVariable<Int>) {
        var value by variable
    }
}
