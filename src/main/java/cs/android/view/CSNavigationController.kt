package cs.android.view

import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import cs.android.R
import cs.android.viewbase.CSViewController
import cs.android.viewbase.menu.CSOnMenuItem
import cs.java.collections.CSList
import cs.java.lang.CSLang
import cs.java.lang.CSLang.*

open class CSNavigationController(activity: AppCompatActivity) :
        CSViewController<FrameLayout>(activity, null) {

    open var controllers: CSList<CSViewController<*>> = list()

    init {
        view = FrameLayout(ContextThemeWrapper(this.context(), R.style.Container))
    }

    fun <T : View> push(controller: CSViewController<T>): CSViewController<T> {
        if (controllers.hasItems) controllers.last()?.setShowingInContainer(NO)
        controllers.put(controller)
        controller.view.startAnimation(loadAnimation(context(), R.anim.abc_slide_in_top))
        add(controller)
        controller.setShowingInContainer(YES)
        controller.initialize(state)
        updateBackButton()
        updateTitleButton()
        invalidateOptionsMenu()
        hideKeyboard()
        return controller
    }

    fun pop() {
        val controller = controllers.removeLast()
        controller.view.startAnimation(loadAnimation(context(), R.anim.abc_slide_out_top))
        controller.setShowingInContainer(NO)
        controller.onDeinitialize(state)
        removeView(controller)
        controller.onDestroy()
        controllers.last()?.setShowingInContainer(YES)
        updateBackButton()
        updateTitleButton()
        hideKeyboard()
    }

    private fun updateTitleButton() {
        val title = (controllers.last() as? CSNavigationItem)?.navigationTitle()
        if (set(title)) actionBar.title = title
        else actionBar.title = model().applicationName()
    }

    private fun updateBackButton() {
        val isBackButtonVisible =
                (controllers.last() as? CSNavigationItem)?.isNavigationBackButtonVisible() ?: true
        if (controllers.count() > 1 && isBackButtonVisible) showBackButton()
        else hideBackButton()
    }

    private fun showBackButton() = actionBar.setDisplayHomeAsUpEnabled(YES)

    private fun hideBackButton() = actionBar.setDisplayHomeAsUpEnabled(NO)

    override fun onGoBack(): Boolean {
        if (controllers.count() > 1) {
            pop()
            return NO
        }
        return YES
    }

    override fun onOptionsItemSelected(event: CSOnMenuItem) {
        super.onOptionsItemSelected(event)
        if (event.consume(android.R.id.home)) goBack()
    }
}

interface CSNavigationItem {
    fun isNavigationBackButtonVisible(): Boolean {
        return YES
    }

    fun navigationTitle(): String? {
        return null
    }
}
