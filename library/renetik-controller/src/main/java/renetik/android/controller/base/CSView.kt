package renetik.android.controller.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import androidx.annotation.LayoutRes
import renetik.android.base.CSContextController
import renetik.android.base.CSLayoutRes
import renetik.android.extensions.inflate
import renetik.android.extensions.service
import renetik.android.java.extensions.later
import renetik.android.java.extensions.notNull
import renetik.android.view.extensions.inflate

open class CSView<ViewType : View> : CSContextController {

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

    constructor(parent: CSView<*>, view: ViewType) : super(parent) {
        this.parentGroup = parent.parentGroup
        setView(view)
    }

    constructor(parent: CSView<out ViewType>) : super(parent) {
        this.parentGroup = parent.parentGroup
        setView(parent.view)
    }

    constructor(parent: Context) : super(parent)

    val view: ViewType
        get() {
            if (_view != null) return _view!!
            setView(layoutRes?.let { inflate<ViewType>(it.id) } ?: let { obtainView()!! })
            return _view!!
        }

    fun setView(view: ViewType) {
        if (_view.notNull) throw Exception("setView should be called before view was initialized")
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
        service<InputMethodManager>(Context.INPUT_METHOD_SERVICE)
            .hideSoftInputFromWindow(view.rootView.windowToken, 0)
    }

    fun showKeyboard(view: View, flag: Int) =
        service<InputMethodManager>(Context.INPUT_METHOD_SERVICE).showSoftInput(view, flag)

    fun showKeyboard() =
        service<InputMethodManager>(Context.INPUT_METHOD_SERVICE)
            .toggleSoftInput(SHOW_FORCED, HIDE_IMPLICIT_ONLY);

    override fun onDestroy() {
        super.onDestroy()
        view.tag = null
    }
}
