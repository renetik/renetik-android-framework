package renetik.android.core.android.content

import android.content.Context.MODE_PRIVATE
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
class SharedPreferencesExtensionsTest {

    @Before
    fun clearPreferences() {
        context.getSharedPreferences("source", MODE_PRIVATE).edit().clear().commit()
        context.getSharedPreferences("destination", MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun loadCopiesAllSupportedValueTypesAndKeepsExistingKeys() {
        val source = context.getSharedPreferences("source", MODE_PRIVATE)
        val destination = context.getSharedPreferences("destination", MODE_PRIVATE)
        destination.edit().putString("existing", "keep").commit()
        source.edit()
            .putString("string", "value")
            .putStringSet("set", setOf("a", "b"))
            .putInt("int", 7)
            .putLong("long", 8L)
            .putFloat("float", 1.5f)
            .putBoolean("boolean", true)
            .commit()

        destination.load(source)

        assertEquals(7, destination.size)
        assertEquals("keep", destination.getString("existing"))
        assertEquals("value", destination.getString("string"))
        assertEquals(setOf("a", "b"), destination.getStringSet("set", emptySet()))
        assertEquals(7, destination.getInt("int", 0))
        assertEquals(8L, destination.getLong("long", 0L))
        assertEquals(1.5f, destination.getFloat("float", 0f))
        assertTrue(destination.getBoolean("boolean", false))
    }

    @Test
    fun reloadClearsDestinationBeforeLoading() {
        val source = context.getSharedPreferences("source", MODE_PRIVATE)
        val destination = context.getSharedPreferences("destination", MODE_PRIVATE)
        source.edit().putString("source", "value").commit()
        destination.edit().putString("old", "remove").commit()

        destination.reload(source)

        assertEquals(1, destination.size)
        assertFalse(destination.contains("old"))
        assertEquals("value", destination.getString("source"))
    }

    @Test
    fun copyToLoadsDestination() {
        val source = context.getSharedPreferences("source", MODE_PRIVATE)
        val destination = context.getSharedPreferences("destination", MODE_PRIVATE)
        source.edit().putBoolean("copied", true).commit()

        source.copyTo(destination)

        assertTrue(destination.getBoolean("copied", false))
    }
}
