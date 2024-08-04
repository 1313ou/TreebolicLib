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
class Container(handle: Any?) : LinearLayout(handle as Context?), Component, Container<Component?> {

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
        this.isHorizontal = size.x >= size.y
        orientation = if (this.isHorizontal) HORIZONTAL else VERTICAL

        // other
        gravity = Gravity.CENTER
        weightSum = 1f
    }

    override fun addComponent(component: Component?, position: Int) {
        val viewToAdd = component as View?

        when (position) {
            Container.PANE -> {
                val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
                addView(viewToAdd, params)
            }

            Container.VIEW -> this.view = viewToAdd
            Container.TOOLBAR -> this.toolbar = viewToAdd
            Container.STATUSBAR -> this.statusbar = viewToAdd
            else -> {}
        }
    }

    override fun removeAll() {
        removeAllViews()
        this.view = null
        this.toolbar = null
        this.statusbar = null
    }

    override fun validate() {
        var params: LayoutParams?
        if (this.toolbar != null) {
            params = LayoutParams(if (this.isHorizontal) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT, if (this.isHorizontal) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT, 0f)
            addView(this.toolbar, params)
        }
        var layout: ViewGroup = this
        if (this.view != null && this.statusbar != null) {
            val splitLayout = SplitPaneLayout(context)
            splitLayout.orientation = if (this.isHorizontal) 0 else 1
            splitLayout.splitterPositionPercent = splitterPositionPercent
            splitLayout.isSplitterMovable = true
            splitLayout.splitterDrawable = getSplitterDrawable(false)
            splitLayout.splitterDraggingDrawable = getSplitterDrawable(true)

            this.addView(splitLayout)
            layout = splitLayout
        }
        if (this.view != null) {
            params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
            layout.addView(this.view, params)
        }
        if (this.statusbar != null) {
            params = LayoutParams(if (this.isHorizontal) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT, if (this.isHorizontal) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT, 0f)
            layout.addView(this.statusbar, params)
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
