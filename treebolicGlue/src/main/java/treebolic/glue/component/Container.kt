/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import org.treebolic.glue.R
import treebolic.glue.iface.component.Container

/**
 * Container
 * API class
 *
 * @param handle context
 *
 * @author Bernard Bou
 */
open class Container(handle: Any?) : LinearLayout(handle as Context?), Component, Container<Component?> {

    private val isHorizontal: Boolean

    /**
     * View
     */
    private var view: View? = null

    /**
     * Toolbar
     */
    private var toolbar: View? = null

    /**
     * Statusbar
     */
    private var statusbar: View? = null

    /**
     * Constructor
     */
    init {
        // determine orientation
        val size = Utils.screenSize(context)
        isHorizontal = size.x >= size.y
        orientation = if (isHorizontal) HORIZONTAL else VERTICAL

        // other
        gravity = Gravity.CENTER
        weightSum = 1f
    }

    override fun addComponent(component: Component?, position: Int) {
        val viewToAdd = component as View?

        when (position) {
            Container.PANE -> {
                val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f)
                addView(viewToAdd, params)
            }

            Container.VIEW -> view = viewToAdd
            Container.TOOLBAR -> toolbar = viewToAdd
            Container.STATUSBAR -> statusbar = viewToAdd
            else -> {}
        }
    }

    override fun removeAll() {
        removeAllViews()
        view = null
        toolbar = null
        statusbar = null
    }

    override fun validate() {
        var params: LayoutParams?
        if (toolbar != null) {
            params = LayoutParams(if (isHorizontal) LayoutParams.WRAP_CONTENT else LayoutParams.MATCH_PARENT, if (isHorizontal) LayoutParams.MATCH_PARENT else LayoutParams.WRAP_CONTENT, 0f)
            addView(toolbar, params)
        }
        var layout: ViewGroup = this
        if (view != null && statusbar != null) {
            val splitLayout = SplitPaneLayout(context)
            splitLayout.orientation = if (isHorizontal) 0 else 1
            splitLayout.isSplitterMovable = true
            splitLayout.splitterDrawable = getSplitterDrawable(false)
            splitLayout.splitterDraggingDrawable = getSplitterDrawable(true)
            splitLayout.positionSplitterPercent(splitterPositionPercent)

            addView(splitLayout)
            layout = splitLayout
        }
        if (view != null) {
            params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f)
            layout.addView(view, params)
        }
        if (statusbar != null) {
            params = LayoutParams(if (isHorizontal) LayoutParams.WRAP_CONTENT else LayoutParams.MATCH_PARENT, if (isHorizontal) LayoutParams.MATCH_PARENT else LayoutParams.WRAP_CONTENT, 0f)
            layout.addView(statusbar, params)
        }

        invalidate()
    }

    /**
     * Get splitter drawable
     *
     * @param dragging dragging splitter
     * @return drawable
     */
    private fun getSplitterDrawable(dragging: Boolean): Drawable {
        val colors = Utils.fetchColors(context, R.attr.treebolic_splitbar_color, R.attr.treebolic_splitbar_drag_color)
        return ColorDrawable(colors[if (dragging) 1 else 0])
    }

    companion object {

        /**
         * Splitter position fraction (first panel/whole)
         */
        var splitterPositionPercent: Float = .80f
    }
}
