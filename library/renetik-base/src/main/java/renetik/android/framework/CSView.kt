package renetik.android.framework

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import androidx.annotation.LayoutRes
import renetik.android.content.inflate
import renetik.android.content.input
import renetik.android.framework.event.*
import renetik.android.framework.event.CSEvent.CSEventRegistration
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.java.extensions.later
import renetik.android.java.extensions.notNull
import renetik.android.java.extensions.unexpected
import renetik.android.logging.CSLog.logError
import renetik.android.view.extensions.inflate
import renetik.android.view.extensions.isShowing

open class CSView<ViewType : View> : CSContext,
    CSVisibleEventOwner, CSHasParent, CSEventOwner {

    private val layout: CSLayoutRes?
    private val viewId: Int?

    private var parent: CSView<*>? = null

    private var _group: ViewGroup? = null
    private val group: ViewGroup? by lazy { _group ?: parent?.view as? ViewGroup ?: parent?.group }

    private var _view: ViewType? = null

    constructor(parent: Context, layout: CSLayoutRes) : super(parent) {
        this.layout = layout
        this.viewId = null
    }

    constructor(group: ViewGroup, layout: CSLayoutRes) : super(group.context) {
        this._group = group
        this.layout = layout
        this.viewId = null
    }

    constructor(parent: CSView<*>, layout: CSLayoutRes) : super(parent) {
        this.parent = parent
        this.layout = layout
        this.viewId = null
    }

    constructor(parent: CSView<*>, viewId: Int) : super(parent) {
        this.parent = parent
        this.layout = null
        this.viewId = viewId
    }

    constructor(parent: CSView<*>) : super(parent) {
        this.parent = parent
        this.layout = null
        this.viewId = null
    }

    @Suppress("UNCHECKED_CAST")
    val view: ViewType
        get() {
            when {
                isDestroyed -> let { logError(this); throw unexpected }
                _view != null -> return _view!!
                layout != null -> setView(inflate(layout.id))
                viewId != null -> setView(parent!!.view(viewId) as ViewType)
                else -> (parent?.view as? ViewType)?.let {
                    _view = it
                    onViewReady()
                } ?: setView(obtainView()!!)
            }
            return _view!!
        }


    private fun setView(view: ViewType) {
        _view = view
        _view!!.tag = this@CSView
        onViewReady()
    }

    fun <ViewType : View> inflate(@LayoutRes layoutId: Int): ViewType =
        group?.inflate(layoutId) ?: context.inflate(layoutId)

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
        if (isDestroyed) let { logError(this); throw unexpected }
        eventRegistrations.cancel()
        whileShowingEventRegistrations.cancel()
        isVisibleEventRegistrations.cancel()
        if (layout != null) {
            if (_view == null) let { logError(this); throw unexpected }
            // TODO: Should I for some reason destroy children manually ?
            // (_view as? ViewGroup)?.children?.forEach { (it.tag as? CSView<*>)?.onDestroy() }
            _view!!.tag = null
        }
        _view = null
        super.onDestroy()
    }

    override fun onAddedToParent() = updateVisibilityChanged()

    override fun onRemovedFromParent() = updateVisibilityChanged()

    private val eventRegistrations = CSEventRegistrations()
    override fun register(registration: CSEventRegistration) =
        registration.also { eventRegistrations.add(it) }

    fun cancel(registration: CSEventRegistration) =
        eventRegistrations.cancel(registration)

    protected var isShowing = false
    private var onViewShowingCalled = false
    val onViewVisibilityChanged = event<Boolean>()
    private val isVisibleEventRegistrations = CSEventRegistrations()
    private val whileShowingEventRegistrations = CSEventRegistrations()

    fun updateVisibilityChanged() {
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

    fun ifVisible(registration: CSEventRegistration?) =
        registration?.let { isVisibleEventRegistrations.add(it) }

    override fun whileShowing(registration: CSEventRegistration) =
        registration.let { whileShowingEventRegistrations.add(it) }
}
