/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import org.treebolic.glue.R
import treebolic.glue.ActionListener
import treebolic.glue.component.Utils.screenSize
import treebolic.glue.component.Utils.screenWidth

/**
 * QuickAction dialog, shows action list as icon and text like the one in Gallery3D app. Currently, supports vertical and horizontal layout.
 *
 * @author Lorensius W. L. T <lorenz></lorenz>@londatiga.net> Contributors: - Kevin Peck <kevinwpeck></kevinwpeck>@gmail.com>
 */
class QuickAction @JvmOverloads constructor(
    context: Context,
    private val orientation: Int = VERTICAL
) : PopupAdapter(context), PopupWindow.OnDismissListener {

    /**
     * Action item
     *
     * @param title    title
     * @param icon     icon to use
     * @param sticky   whether item is sticky (disable QuickAction dialog being dismissed after an item is clicked)
     * @param listener listener
     */
    class ActionItem(
        /**
         * Title
         */
        val title: String,
        /**
         * Icon
         */
        val icon: Drawable,
        /**
         * Whether item is sticky (disable QuickAction dialog being dismissed after an item is clicked)
         */
        val sticky: Boolean,
        /**
         * Action listener
         */
        val listener: ActionListener?
    )

    /**
     * Layout inflater
     */
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /**
     *
     */
    private var tracks: ViewGroup? = null

    /**
     * Scroller
     */
    private var scroller: ScrollView? = null

    /**
     * Anchor arrow up
     */
    private var arrowUp: ImageView? = null

    /**
     * Anchor arrow down
     */
    private var arrowDown: ImageView? = null

    /**
     * Action items
     */
    private val actionItems: MutableList<ActionItem> = ArrayList()

    /**
     * Dismiss listener
     */
    private var dismissListener: OnDismissListener? = null

    /**
     * Action done flag
     */
    private var didAction = false

    /**
     * Child position
     */
    private var childPos = 0

    /**
     * Insert position
     */
    private var insertPos = 0

    /**
     * Animation style
     */
    private var animationStyle: Int

    /**
     * Popup width
     */
    private var popupWidth = 0

    /**
     * Constructor allowing orientation override
     *
     * @param context      context
     * @param orientation layout orientation, can be vertical or horizontal
     */
    /**
     * Constructor for default vertical layout
     *
     * @param context context
     */
    init {
        animationStyle = ANIM_AUTO
        setRootViewId(if (orientation == HORIZONTAL) R.layout.popup_horizontal else R.layout.popup_vertical)
    }

    /**
     * Get action item at an index
     *
     * @param index index of item (position from callback)
     * @return action item at the position
     */
    private fun getActionItem(index: Int): ActionItem {
        return actionItems[index]
    }

    /**
     * Set root view
     *
     * @param id Layout resource id
     */
    private fun setRootViewId(id: Int) {
        view = inflater.inflate(id, null)
        tracks = view.findViewById(R.id.tracks)
        arrowDown = view.findViewById(R.id.arrow_down)
        arrowUp = view.findViewById(R.id.arrow_up)
        scroller = view.findViewById(R.id.scroller)

        // This was previously defined on show() method, moved here to prevent force close that occurred
        // when tapping fast on a view to show quickaction dialog. Thanks to zammbi (github.com/zammbi)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setContentView(view)
    }

    /**
     * Set animation style
     *
     * @param animStyle animation style, default is set to ANIM_AUTO
     */
    fun setAnimStyle(animStyle: Int) {
        animationStyle = animStyle
    }

    /**
     * Add action item
     *
     * @param action [ActionItem]
     */
    fun addActionItem(action: ActionItem) {
        actionItems.add(action)

        val title = action.title
        val icon = action.icon
        val itemView = inflater.inflate(if (orientation == HORIZONTAL) R.layout.popup_horizontal_item else R.layout.popup_vertical_item, null)

        val img = itemView.findViewById<ImageView>(R.id.iv_icon)
        val text = itemView.findViewById<TextView>(R.id.tv_title)

        img.setImageDrawable(icon)
        text.text = title

        val pos = childPos

        itemView.setOnClickListener { v: View? ->
            action.listener?.onAction(action)
            if (!getActionItem(pos).sticky) {
                this@QuickAction.didAction = true
                dismiss()
            }
        }

        itemView.isFocusable = true
        itemView.isClickable = true

        if (orientation == HORIZONTAL && childPos != 0) {
            val separator = inflater.inflate(R.layout.quickaction_horiz_separator, tracks)

            val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            separator.layoutParams = params
            separator.setPadding(5, 0, 5, 0)

            tracks!!.addView(separator, insertPos)

            insertPos++
        }

        tracks!!.addView(itemView, insertPos)

        childPos++
        insertPos++
    }

    /**
     * Show quickaction popup. Popup is automatically positioned, on top or bottom of anchor view.
     *
     * @param anchor anchor view (the popup will be anchored to)
     */
    fun show(anchor: View) {
        preShow()
        didAction = false

        // anchor screen rect
        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        val anchorRect = Rect(location[0], location[1], location[0] + anchor.width, location[1] + anchor.height)

        // wrapped view dimensions
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val rootHeight = view.measuredHeight
        if (popupWidth == 0) {
            popupWidth = view.measuredWidth
        }

        // screen size
        val size = screenSize(context)
        val screenWidth = size.x
        val screenHeight = size.y

        // X coord of popup (top left)
        var xPos: Int
        if (anchorRect.left + popupWidth > screenWidth) {
            xPos = anchorRect.left - (popupWidth - anchor.width)
            if (xPos < 0) {
                xPos = 0
            }
        } else {
            xPos = if (anchor.width > popupWidth) {
                anchorRect.centerX() - popupWidth / 2
            } else {
                anchorRect.left
            }
        }
        val arrowPos = anchorRect.centerX() - xPos

        // Y coord of popup (top left)
        val yPos: Int
        val dyTop = anchorRect.top
        val dyBottom = screenHeight - anchorRect.bottom
        val onTop = dyTop > dyBottom
        if (onTop) {
            if (rootHeight > dyTop) {
                yPos = 15
                val l = scroller!!.layoutParams
                l.height = dyTop - anchor.height
            } else {
                yPos = anchorRect.top - rootHeight
            }
        } else {
            yPos = anchorRect.bottom
            if (rootHeight > dyBottom) {
                val l = scroller!!.layoutParams
                l.height = dyBottom
            }
        }

        // show
        showArrow(if (onTop) R.id.arrow_down else R.id.arrow_up, arrowPos)
        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop)
        window.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos)
    }

    /**
     * Show at x,y of anchor view
     *
     * @param anchor anchor view
     * @param x0     x location
     * @param y0     y location
     */
    fun show(anchor: View, x0: Float, y0: Float) {
        preShow()
        didAction = false

        // anchor screen rect
        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        val anchorRect = Rect(location[0], location[1], location[0] + anchor.width, location[1] + anchor.height)

        // screen size
        val screenWidth = screenWidth(context)

        // wrapped view dimensions
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val popupHeight = view.measuredHeight
        if (popupWidth == 0) {
            popupWidth = view.measuredWidth
        }

        // X
        val w2 = popupWidth / 2
        var arrowPos = w2
        val x = location[0] + x0.toInt() - w2
        var dx = x - location[0]
        if (dx < 0) {
            arrowPos = w2 + dx
        }
        dx = x - (screenWidth - popupWidth)
        if (dx > 0) {
            arrowPos = popupWidth - (w2 - dx)
        }

        // Y
        val above = y0 > anchor.height / 2f
        var y = y0.toInt() + location[1]
        if (above) {
            y -= popupHeight
        }

        // show
        showArrow(if (above) R.id.arrow_down else R.id.arrow_up, arrowPos)
        setAnimationStyle(screenWidth, anchorRect.centerX(), above)
        window.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y)
    }

    /**
     * Set animation style
     *
     * @param screenWidth screen width
     * @param requestedX  distance from left edge
     * @param onTop       flag to indicate where the popup should be displayed. Set TRUE if displayed on top of anchor view and vice versa
     */
    private fun setAnimationStyle(screenWidth: Int, requestedX: Int, onTop: Boolean) {
        val arrowPos = requestedX - arrowUp!!.measuredWidth / 2

        when (animationStyle) {
            ANIM_GROW_FROM_LEFT -> window.animationStyle = if (onTop) R.style.Animations_PopUpMenu_Left else R.style.Animations_PopDownMenu_Left
            ANIM_GROW_FROM_RIGHT -> window.animationStyle = if (onTop) R.style.Animations_PopUpMenu_Right else R.style.Animations_PopDownMenu_Right
            ANIM_GROW_FROM_CENTER -> window.animationStyle = if (onTop) R.style.Animations_PopUpMenu_Center else R.style.Animations_PopDownMenu_Center
            ANIM_REFLECT -> window.animationStyle = if (onTop) R.style.Animations_PopUpMenu_Reflect else R.style.Animations_PopDownMenu_Reflect
            ANIM_AUTO -> if (arrowPos <= screenWidth / 4) {
                window.animationStyle = if (onTop) R.style.Animations_PopUpMenu_Left else R.style.Animations_PopDownMenu_Left
            } else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4)) {
                window.animationStyle = if (onTop) R.style.Animations_PopUpMenu_Center else R.style.Animations_PopDownMenu_Center
            } else {
                window.animationStyle = if (onTop) R.style.Animations_PopUpMenu_Right else R.style.Animations_PopDownMenu_Right
            }

            else -> {}
        }
    }

    /**
     * Show arrow
     *
     * @param whichArrow arrow type resource id
     * @param requestedX distance from left screen
     */
    private fun showArrow(whichArrow: Int, requestedX: Int) {
        val showArrow: View? = if (whichArrow == R.id.arrow_up) arrowUp else arrowDown
        val hideArrow: View? = if (whichArrow == R.id.arrow_up) arrowDown else arrowUp

        // x adjust
        val arrowWidth = arrowUp!!.measuredWidth
        val arrowWidth2 = arrowWidth / 2
        val param = showArrow!!.layoutParams as MarginLayoutParams
        param.leftMargin = requestedX - arrowWidth2
        if (param.leftMargin < 0) {
            param.leftMargin = 0
        }
        if (param.leftMargin > popupWidth - arrowWidth) {
            param.leftMargin = popupWidth - arrowWidth
        }

        // show
        showArrow.visibility = View.VISIBLE

        // hide
        hideArrow!!.visibility = View.INVISIBLE
    }

    /**
     * Set listener for window dismissed. This listener will only be fired if the quickaction dialog is dismissed by clicking outside the dialog or clicking on
     * sticky item.
     *
     * @param listener dismiss listener
     */
    fun setOnDismissListener(listener: OnDismissListener?) {
        setOnDismissListener(this)
        dismissListener = listener
    }

    override fun onDismiss() {
        if (!didAction && dismissListener != null) {
            dismissListener!!.onDismiss()
        }
    }

    /**
     * Listener for item click
     */
    internal interface OnActionItemClickListener {

        fun onItemClick(source: QuickAction?, pos: Int, actionId: Int)
    }

    /**
     * Listener for window dismiss
     */
    interface OnDismissListener {

        @Suppress("EmptyMethod")
        fun onDismiss()
    }

    companion object {

        /**
         * Horizontal
         */
        const val HORIZONTAL: Int = 0

        /**
         * Vertical
         */
        const val VERTICAL: Int = 1

        /**
         * Animation grows from left (west)
         */
        const val ANIM_GROW_FROM_LEFT: Int = 1

        /**
         * Animation grows from right (east)
         */
        const val ANIM_GROW_FROM_RIGHT: Int = 2

        /**
         * Animation grows from center
         */
        const val ANIM_GROW_FROM_CENTER: Int = 3

        /**
         * Animation reflects
         */
        const val ANIM_REFLECT: Int = 4

        /**
         * Animation auto
         */
        const val ANIM_AUTO: Int = 5
    }
}