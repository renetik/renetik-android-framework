package renetik.android.controller.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import androidx.annotation.LayoutRes
import renetik.android.content.input
import renetik.android.controller.extensions.view
import renetik.android.extensions.inflate
import renetik.android.framework.CSContext
import renetik.android.framework.event.CSEvent
import renetik.android.framework.event.CSEventRegistrations
import renetik.android.framework.event.CSVisibleEventOwner
import renetik.android.framework.event.event
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.java.extensions.later
import renetik.android.java.extensions.notNull
import renetik.android.view.extensions.inflate
import renetik.android.view.extensions.isShowing

open class CSView<ViewType : View> : CSContext, CSVisibleEventOwner {

    private var parentGroup: ViewGroup? = null
    private var layoutRes: CSLayoutRes? = null
    private var _view: ViewType? = null

    constructor(parent: Context, layoutRes: CSLayoutRes) : super(parent) {
        this.layoutRes = layoutRes
    }

    constructor(parent: ViewGroup, layoutRes: CSLayoutRes) : super(parent.context) {
        this.parentGroup = parent
        this.layoutRes = layoutRes
    }

    constructor(parent: CSView<out ViewGroup>, layoutRes: CSLayoutRes) : super(parent) {
        this.parentGroup = parent.view
        this.layoutRes = layoutRes
    }

    @Suppress("UNCHECKED_CAST")
    constructor(parent: CSView<*>, viewId: Int) : super(parent) {
        this.parentGroup = parent.parentGroup
        setView(parent.view(viewId) as ViewType)
    }

    constructor(parent: CSView<*>, view: ViewType) : super(parent) {
        this.parentGroup = parent.parentGroup
        setView(view)
    }

    constructor(parent: CSView<out ViewType>) : super(parent) {
        this.parentGroup = parent.parentGroup
        setView(parent.view)
    }

    constructor(view: ViewType) : super(view.context) {
        setView(view)
    }

    constructor(parent: Context) : super(parent)

    val view: ViewType
        get() {
            if (_view != null) return _view!!
            setView(layoutRes?.let { inflate(it.id) } ?: let { obtainView()!! })
            return _view!!
        }

    fun setView(view: ViewType) {
//        if (_view.notNull) throw Exception("setView should be called before view was initialized")
        _view = view
        _view!!.tag = this@CSView
        onViewReady()
    }

    fun <ViewType : View> inflate(@LayoutRes layoutId: Int): ViewType =
        parentGroup?.inflate(layoutId) ?: context.inflate(layoutId)

    protected open fun obtainView(): ViewType? = null

    protected open fun onViewReady() = Unit

    val hasParent get() = view.parent.notNull

    fun hideKeyboard() {
        hideKeyboardImpl()
        later { hideKeyboardImpl() }
    }

    open fun hideKeyboardImpl() {
        input.hideSoftInputFromWindow(view.rootView.windowToken, 0)
    }

    fun showKeyboard(view: View, flag: Int) = input.showSoftInput(view, flag)

    fun showKeyboard() = input.toggleSoftInput(SHOW_FORCED, HIDE_IMPLICIT_ONLY);

    override fun onDestroy() {
        super.onDestroy()
        whileShowingEventRegistrations.cancel()
        isVisibleEventRegistrations.cancel()
        view.tag = null
    }

    open fun onAddedToParent() {
        updateVisibilityChanged()
    }

    open fun onRemovedFromParent() {
        updateVisibilityChanged()
    }

    // Visibility
    protected var isShowing = false
    private var onViewShowingCalled = false
    val onViewVisibilityChanged = event<Boolean>()
    private val isVisibleEventRegistrations = CSEventRegistrations()
    private val whileShowingEventRegistrations = CSEventRegistrations()

    protected fun updateVisibilityChanged() {
        if (checkIfIsShowing()) {
            if (!isShowing) onViewVisibilityChanged(true)
        } else if (isShowing) onViewVisibilityChanged(false)
    }

    protected open fun checkIfIsShowing() = view.isShowing()

    private fun onViewVisibilityChanged(showing: Boolean) {
        if (isShowing == showing) return
        isShowing = showing
        onViewVisibilityChanged()
        if (isShowing) {
            isVisibleEventRegistrations.setActive(true)
            onViewShowing()
        } else {
            isVisibleEventRegistrations.setActive(false)
            onViewHiding()
            whileShowingEventRegistrations.cancel()
        }
    }

    protected open fun onViewVisibilityChanged() {
        onViewVisibilityChanged.fire(isShowing)
    }

    protected open fun onViewShowing() {
        if (!onViewShowingCalled) {
            onViewShowingFirstTime()
            onViewShowingCalled = true
        } else onViewShowingAgain()
    }

    protected open fun onViewShowingFirstTime() {}

    protected open fun onViewShowingAgain() {}

    protected open fun onViewHiding() {
        if (!onViewShowingCalled) {
            onViewHidingFirstTime()
            onViewShowingCalled = true
        } else onViewHidingAgain()
    }

    protected open fun onViewHidingFirstTime() {}

    protected open fun onViewHidingAgain() {}

    fun ifVisible(registration: CSEvent.CSEventRegistration?) =
        registration?.let { isVisibleEventRegistrations.add(it) }

    override fun whileShowing(registration: CSEvent.CSEventRegistration) =
        registration.let { whileShowingEventRegistrations.add(it) }
}
