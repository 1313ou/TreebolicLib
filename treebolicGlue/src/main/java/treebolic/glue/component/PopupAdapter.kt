/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import treebolic.glue.component.Utils.getColor

/**
 * Custom popup window.
 *
 * @param context context
 *
 * @author Lorensius W. L. T <lorenz></lorenz>@londatiga.net>
 */
open class PopupAdapter(
    @JvmField protected val context: Context
) {

    /**
     * Popup window
     */
    @JvmField
    protected val window: PopupWindow = PopupWindow(context)

    /**
     * Wrapped view
     */
    protected lateinit var view: View

    /**
     * Constructor
     */
    init {
        // dismiss when touched outside
        window.setTouchInterceptor { view0: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_OUTSIDE -> {
                    window.dismiss()
                    return@setTouchInterceptor true
                }

                MotionEvent.ACTION_UP -> {
                    view0.performClick()
                    return@setTouchInterceptor false
                }

                else -> {}
            }
            false
        }
    }

    /**
     * On pre-show
     */
    protected fun preShow() {

        // hook
        onShow()

        // color
        val color = getColor(context, android.R.color.transparent)
        window.setBackgroundDrawable(ColorDrawable(color))

        // setup window
        window.width = ViewGroup.LayoutParams.WRAP_CONTENT
        window.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.isTouchable = true
        window.isOutsideTouchable = true
        window.isFocusable = true
        window.contentView = view
    }

    /**
     * Set content view.
     *
     * @param root Root view
     */
    fun setContentView(root: View) {
        view = root
        window.contentView = root
    }

    /**
     * Set content view.
     *
     * @param layoutResID Resource id
     */
    fun setContentView(layoutResID: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setContentView(inflater.inflate(layoutResID, null))
    }

    /**
     * Set listener on window dismissed.
     *
     * @param listener listener
     */
    fun setOnDismissListener(listener: PopupWindow.OnDismissListener?) {
        window.setOnDismissListener(listener)
    }

    /**
     * Dismiss the popup window.
     */
    fun dismiss() {
        window.dismiss()
    }

    /**
     * On dismiss
     */
    protected open fun onDismiss() {
    }

    /**
     * On show
     */
    @Suppress("EmptyMethod")
    private fun onShow() {
    }
}