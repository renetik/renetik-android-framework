package renetik.android.view

import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import com.airbnb.paris.extensions.style
import renetik.android.R
import renetik.android.application
import renetik.android.extensions.view.add
import renetik.android.extensions.view.remove
import renetik.android.java.collections.CSList
import renetik.android.java.collections.list
import renetik.android.lang.CSLang.*
import renetik.android.viewbase.CSActivity
import renetik.android.viewbase.CSViewController
import renetik.android.viewbase.menu.CSOnMenuItem

open class CSNavigationController(activity: CSActivity)
    : CSViewController<FrameLayout>(activity) {

    open var controllers: CSList<CSViewController<*>> = list()

    override fun createView() = FrameLayout(this).apply { style(R.style.CSNavigationContainer) }

    fun <T : View> push(controller: CSViewController<T>): CSViewController<T> {
        if (controllers.hasItems) controllers.last()?.showingInContainer(NO)
        controllers.put(controller)
        controller.view.startAnimation(loadAnimation(this, R.anim.abc_slide_in_top))
        view.add(controller)
        controller.showingInContainer(YES)
        controller.initialize(state)
        updateBackButton()
        updateTitleButton()
        invalidateOptionsMenu()
        hideKeyboard()
        return controller
    }

    fun pop() {
        val controller = controllers.removeLast()
        controller.view.startAnimation(loadAnimation(this, R.anim.abc_slide_out_top))
        controller.showingInContainer(NO)
        view.remove(controller)
        controller.onDeinitialize()
        controllers.last()?.showingInContainer(YES)
        updateBackButton()
        updateTitleButton()
        hideKeyboard()
    }

    fun <T : View> pushAsLast(controller: CSViewController<T>): CSViewController<T> {
        val lastController = controllers.removeLast()
        lastController.view.startAnimation(loadAnimation(this, R.anim.abc_fade_out))
        lastController.showingInContainer(NO)
        view.remove(lastController)
        lastController.onDeinitialize()

        controllers.put(controller)
        controller.view.startAnimation(loadAnimation(this, R.anim.abc_fade_in))
        view.add(controller)
        controller.showingInContainer(YES)
        controller.initialize(state)
        updateBackButton()
        updateTitleButton()
        invalidateOptionsMenu()
        hideKeyboard()
        return controller
    }

    private fun updateTitleButton() {
        val title = (controllers.last() as? CSNavigationItem)?.navigationTitle()
        if (set(title)) actionBar?.title = title
        else actionBar?.title = application.name
    }

    private fun updateBackButton() {
        val isBackButtonVisible =
                (controllers.last() as? CSNavigationItem)?.isNavigationBackButtonVisible() ?: true
        if (controllers.count() > 1 && isBackButtonVisible) showBackButton()
        else hideBackButton()
    }

    private fun showBackButton() = actionBar?.setDisplayHomeAsUpEnabled(YES)

    private fun hideBackButton() = actionBar?.setDisplayHomeAsUpEnabled(NO)

    override fun onGoBack(): Boolean {
        if (controllers.count() > 1) {
            pop()
            return NO
        }
        return YES
    }

    override fun onOptionsItemSelected(onItem: CSOnMenuItem) {
        super.onOptionsItemSelected(onItem)
        if (onItem.consume(android.R.id.home)) goBack()
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
