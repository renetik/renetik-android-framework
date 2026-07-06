package renetik.android.ui.protocol

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.core.lang.variable.assign
import renetik.android.event.change.CSHasChangeValue
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.testing.CSAssert.assert
import renetik.android.testing.TestApplication

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class CSVisibilityProtocolTest {

    private val visibility = property(false)

    private val visible = object : CSVisibility {
        override val isVisibility: CSHasChangeValue<Boolean> get() = visibility
    }

    @Test
    fun onShowingAndOnHiding() {
        var showing = 0
        var hiding = 0
        visible.onShowing { showing += 1 }
        visible.onHiding { hiding += 1 }

        visibility assign true
        assert(expected = 1, actual = showing)
        assert(expected = 0, actual = hiding)

        visibility assign false
        assert(expected = 1, actual = showing)
        assert(expected = 1, actual = hiding)
    }

    @Test
    fun onShowingFirstTimeFiresOnce() {
        var count = 0
        visible.onShowingFirstTime { count += 1 }
        visibility assign true
        visibility assign false
        visibility assign true
        assert(expected = 1, actual = count)
    }

    @Test
    fun onShowingRegistrationCancel() {
        var showing = 0
        val registration = visible.onShowing { showing += 1 }
        visibility assign true
        registration.cancel()
        visibility assign false
        visibility assign true
        assert(expected = 1, actual = showing)
    }
}
