package renetik.android.ui.view

import android.view.View
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.core.lang.variable.assign
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.testing.TestApplication
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ViewVisibilityTest {

    private val view = View(context)

    @Test
    fun visibleGoneInvisible() {
        view.visible()
        assertTrue(view.isVisible)
        view.gone()
        assertTrue(view.isGone)
        assertFalse(view.isVisible)
        view.invisible()
        assertTrue(view.isInvisible)
        view.visible(false)
        assertTrue(view.isInvisible)
    }

    @Test
    fun showIfAndGoneIfValues() {
        view.showIf(true)
        assertTrue(view.isVisible)
        view.showIf(false)
        assertTrue(view.isGone)
        view.gone(false)
        assertTrue(view.isVisible)
        view.gone()
        assertTrue(view.isGone)
    }

    @Test
    fun shownIfPropertyTracksChanges() {
        val isShown = property(false)
        val registration = view.shownIf(isShown)
        assertTrue(view.isGone)
        isShown assign true
        assertTrue(view.isVisible)
        isShown assign false
        assertTrue(view.isGone)
        registration.cancel()
        isShown assign true
        assertTrue(view.isGone)
    }

    @Test
    fun goneIfPropertyTracksChanges() {
        val isHidden = property(true)
        view.goneIf(isHidden)
        assertTrue(view.isGone)
        isHidden assign false
        assertTrue(view.isVisible)
    }
}
