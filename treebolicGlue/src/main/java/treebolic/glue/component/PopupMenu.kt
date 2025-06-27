/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import org.treebolic.glue.R
import treebolic.glue.ActionListener
import treebolic.glue.component.QuickAction.ActionItem
import treebolic.glue.component.Utils.getDrawable
import treebolic.glue.iface.component.PopupMenu
import treebolic.glue.iface.component.PopupMenu.LabelIndices

/**
 * Popup context menu
 * API class
 *
 * @property
 * @property anchor anchor view
 *
 * @author Bernard Bou
 */
open class PopupMenu(
    val context: Context,
    private val anchor: View
) : PopupMenu<Component?, ActionListener?> {

    /**
     * Quick action component
     */
    private val quickAction: QuickAction

    /**
     * Constructor
     */
    init {
        // labels: info,focus,linkto,mount,unmount,cancel
        labels = context.resources.getStringArray(R.array.popup_labels)
        assert(labels!!.size == LabelIndices.LABEL_COUNT.ordinal)

        // create quickaction
        quickAction = QuickAction(context)

        // set listener for on dismiss event
        // this listener will be called only if quickaction dialog was dismissed by clicking the area outside the dialog.
        quickAction.setOnDismissListener(object : QuickAction.OnDismissListener {
            override fun onDismiss() {
            }
        })
    }

    /**
     * Constructor
     * API
     *
     * @param handle anchor view
     */
    constructor(handle: Any) : this((handle as View).context, handle)

    override fun addItem(labelIdx: Int, label2: String?, resource: Int, listener: ActionListener?) {
        // just click outside dialog
        if (resource == PopupMenu.ImageIndices.IMAGE_CANCEL.ordinal) {
            return
        }
        var label: String = if (labelIdx == -1) "" else labels!![labelIdx]
        if (label2 != null)
            label += " $label2"
        val item = ActionItem(label, getDrawable(resource)!!, false, listener)
        quickAction.addActionItem(item)
    }

    override fun addItem(labelIdx: Int, resource: Int, listener: ActionListener?) {
        val label = labels!![labelIdx]
        addItem(-1, label, resource, listener)
    }

    override fun popup(component: Component?, x: Int, y: Int) {
        quickAction.show(anchor, x.toFloat(), y.toFloat())
    }

    /**
     * Get drawable from index
     *
     * @param index index
     * @return drawable
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun getDrawable(index: Int): Drawable? {
        if (drawables[index] == null) {
            var resId = -1
            when (PopupMenu.ImageIndices.entries[index]) {
                PopupMenu.ImageIndices.IMAGE_CANCEL -> resId = R.drawable.menu_cancel
                PopupMenu.ImageIndices.IMAGE_GOTO -> resId = R.drawable.menu_goto
                PopupMenu.ImageIndices.IMAGE_SEARCH -> resId = R.drawable.menu_search
                PopupMenu.ImageIndices.IMAGE_FOCUS -> resId = R.drawable.menu_focus
                PopupMenu.ImageIndices.IMAGE_LINK -> resId = R.drawable.menu_link
                PopupMenu.ImageIndices.IMAGE_MOUNT -> resId = R.drawable.menu_mount
                PopupMenu.ImageIndices.IMAGE_INFO -> resId = R.drawable.menu_info
                else -> {}
            }
            if (resId != -1) {
                drawables[index] = getDrawable(context, resId)
            }
        }
        return drawables[index]
    }

    companion object {

        /**
         * Labels
         */
        var labels: Array<String>? = null // arrayOf({  "Cancel", "Info", "Focus", "Link", "Mount", "UnMount", "Goto", "Search")

        /**
         * Drawables, lazy cache
         */
        val drawables: Array<Drawable?> = arrayOfNulls(PopupMenu.ImageIndices.IMAGE_COUNT.ordinal)
    }
}
