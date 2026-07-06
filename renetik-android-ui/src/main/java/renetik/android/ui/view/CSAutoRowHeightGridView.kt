package renetik.android.ui.view

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.View
import android.widget.GridView
import android.widget.ListAdapter

class CSAutoRowHeightGridView : GridView {
    private var previousFirstVisible = 0
    private var _numColumns = 2

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) :
            super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun setNumColumns(numColumns: Int) {
        this._numColumns = numColumns
        super.setNumColumns(numColumns)
        setSelection(previousFirstVisible)
    }

    override fun onLayout(changed: Boolean, leftPos: Int, topPos: Int,
                          rightPos: Int, bottomPos: Int) {
        super.onLayout(changed, leftPos, topPos, rightPos, bottomPos)
        setHeights()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        numColumns = _numColumns
    }

    override fun onScrollChanged(newHorizontal: Int, newVertical: Int,
                                 oldHorizontal: Int, oldVertical: Int) {
        val firstVisible = firstVisiblePosition
        if (previousFirstVisible != firstVisible) {
            // Update position, and update heights
            previousFirstVisible = firstVisible
            setHeights()
        }
        super.onScrollChanged(newHorizontal, newVertical, oldHorizontal, oldVertical)
    }

    private fun setHeights() {
        val adapter: ListAdapter? = adapter
        if (adapter != null) {
            var childIndex = 0
            while (childIndex < childCount) {
                // Determine the maximum height for this row
                var maxHeight = 0
                for (j in childIndex until childIndex + _numColumns) {
                    val view: View? = getChildAt(j)
                    if (view != null && view.height > maxHeight) {
                        maxHeight = view.height
                    }
                }
                // Set max height for each element in this row
                if (maxHeight > 0)
                    for (j in childIndex until childIndex + _numColumns) {
                        val view: View? = getChildAt(j)
                        if (view != null && view.height != maxHeight)
                            view.minimumHeight = maxHeight
                    }
                childIndex += _numColumns
            }
        }
    }
}