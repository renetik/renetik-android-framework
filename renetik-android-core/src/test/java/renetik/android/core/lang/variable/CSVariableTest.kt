package renetik.android.core.lang.variable

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import renetik.android.core.lang.value.isNull
import renetik.android.core.lang.value.notNull

class CSVariableTest {
    @Test
    fun variableIsNullTest() {
        val property = object : CSVariable<String?> {
            override var value: String? = null
        }
        assertTrue(property.isNull)
        property.value = ""
        assertTrue(property.notNull)
        property.value = null
        assertNotNull(property.isNull)
    }
}