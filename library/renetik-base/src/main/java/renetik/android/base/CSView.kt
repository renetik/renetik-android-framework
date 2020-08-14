package renetik.android.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import renetik.android.extensions.inflate
import renetik.android.extensions.service
import renetik.android.java.extensions.later
import renetik.android.java.extensions.notNull
import renetik.android.view.extensions.inflate

open class CSView<ViewType : View>(context: Context) : CSContextController(context) {

    private var parentGroup: ViewGroup? = null
    private var layoutId: CSLayoutId? = null
    private var _view: ViewType? = null

    constructor(parent: Context, layoutId: CSLayoutId? = null) : this(parent) {
        this.layoutId = layoutId
    }

    constructor(parent: ViewGroup, layoutId: CSLayoutId? = null) : this(parent.context, layoutId) {
        this.parentGroup = parent
    }

    constructor(parent: CSView<out ViewGroup>, layoutId: CSLayoutId? = null) :
            this(parent.view, layoutId)

    val view: ViewType
        get() {
            if (_view != null) return _view!!
            setView(layoutId?.let { inflate<ViewType>(it.id) } ?: let { obtainView()!! })
            return _view!!
        }

    fun setView(view: ViewType) {
        if (_view.notNull) throw Exception("setView should be called before view was initialized")
        _view = view
        _view!!.tag = this@CSView
        onViewReady()
    }

    fun <ViewType : View> inflate(layoutId: Int): ViewType =
        parentGroup?.inflate(layoutId) ?: context.inflate(layoutId)

    protected open fun obtainView(): ViewType? = null

    protected open fun onViewReady() = Unit

    val hasParent get() = view.parent.notNull

    open fun hideKeyboard() = later {
        service<InputMethodManager>(Context.INPUT_METHOD_SERVICE)
            .hideSoftInputFromWindow(view.rootView.windowToken, 0)
    }

    fun showKeyboard(view: View, flag: Int) =
        service<InputMethodManager>(Context.INPUT_METHOD_SERVICE).showSoftInput(view, flag)

    override fun onDestroy() {
        super.onDestroy()
        view.tag = null
    }
}
