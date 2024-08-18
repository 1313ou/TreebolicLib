/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import org.treebolic.glue.R
import treebolic.glue.component.Utils.fetchColors
import treebolic.glue.component.Utils.getDrawables
import treebolic.glue.component.Utils.screenSize
import treebolic.glue.component.Utils.tint
import treebolic.glue.iface.ActionListener
import treebolic.glue.iface.component.Toolbar

/**
 * Toolbar
 * API class
 *
 * @param handle activity
 *
 * @author Bernard Bou
 */
open class Toolbar (handle: Any?) : FrameLayout(handle as Context), Toolbar {

    private val context = handle as Context

    /**
     * Buttons
     */
    private enum class ButtonImplementation {

        HOME,  
        RADIAL, NORTH, SOUTH, EAST, WEST,  
        EXPAND, SHRINK, EXPANSIONRESET,  
        EXPANSIONSWEEPRESET,  
        WIDEN, NARROW, SWEEPRESET,  
        ZOOMIN, ZOOMOUT, ZOOMONE,  
        SCALEUP, SCALEDOWN, SCALEONE; 

        val index: Int
            get() = ordinal
    }

    private val drawables: Array<Drawable?>

    /**
     * Panel of buttons
     */
    private val panel: LinearLayout

    /**
     * Lay out parameters
     */
    private val layoutParams: LinearLayout.LayoutParams

    /**
     * Tint
     */
    private val iconTint: Int

    // C O N S T R U C T O R

     init {
        // orientation
        val size = screenSize(context)
        val isHorizontal = size.x >= size.y

        // panel
        panel = LinearLayout(context)
        panel.orientation = if (isHorizontal) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL

        // focus
        panel.isFocusable = false

        // gravity
        panel.gravity = Gravity.CENTER

        // colors
        val colors = fetchColors(context, R.attr.treebolic_toolbar_background, R.attr.treebolic_toolbar_foreground_icon)
        val background = colors[0]
        iconTint = colors[1]

        // drawables
        drawables = getDrawables(context, *drawableIds)

        // background
        panel.setBackgroundColor(background)

        // scroll
        val scroll = if (isHorizontal) ScrollView(context) else HorizontalScrollView(context)
        scroll.addView(panel)
        scroll.setBackgroundColor(background)

        // top
        addView(scroll)
        setBackgroundColor(background)

        // layout parameters for later addition
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(if (isHorizontal) 5 else 0, if (isHorizontal) 0 else 5, 5, 5)
    }

    // A D D  B U T T O N

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun addButton(button: Toolbar.Button, listener: ActionListener) {
        // interface button to implementation
        val name = button.name
        val impl = ButtonImplementation.valueOf(name)

        // new button
        val imageButton = ImageButton(context)
        imageButton.layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val index: Int = impl.index

        // drawable
        val bitmapDrawable = drawables[index]

        // tint drawable
        tint(bitmapDrawable!!, iconTint)

        // button drawable
        imageButton.background = bitmapDrawable

        // description
        imageButton.contentDescription = context.getString(descIds[index])

        // listener
        imageButton.setOnClickListener { params: View? -> listener.onAction(params) }

        // add
        panel.addView(imageButton, layoutParams)
    }

    override fun getButtons(): Array<Toolbar.Button> {
        return Companion.buttons
    }

    companion object {

        /**
         * Content descriptors
         */
        private val descIds = intArrayOf(
            R.string.desc_toolbar_home,  
            R.string.desc_toolbar_radial, R.string.desc_toolbar_north, R.string.desc_toolbar_south, R.string.desc_toolbar_east, R.string.desc_toolbar_west,  
            R.string.desc_toolbar_expand, R.string.desc_toolbar_shrink, R.string.desc_toolbar_expand_reset, R.string.desc_toolbar_expand_widen_reset,  
            R.string.desc_toolbar_widen, R.string.desc_toolbar_narrow, R.string.desc_toolbar_widen_reset,  
            R.string.desc_toolbar_zoomin, R.string.desc_toolbar_zoomout, R.string.desc_toolbar_zoomone,  
            R.string.desc_toolbar_scaleup, R.string.desc_toolbar_scaledown, R.string.desc_toolbar_scaleone
        )

        /**
         * Drawables
         */
        private val drawableIds = intArrayOf(
            R.drawable.toolbar_home,  
            R.drawable.toolbar_radial, R.drawable.toolbar_north, R.drawable.toolbar_south, R.drawable.toolbar_east, R.drawable.toolbar_west,  
            R.drawable.toolbar_expand, R.drawable.toolbar_shrink, R.drawable.toolbar_expand_reset, R.drawable.toolbar_expand_widen_reset,  
            R.drawable.toolbar_widen, R.drawable.toolbar_narrow, R.drawable.toolbar_widen_reset,  
            R.drawable.toolbar_zoomin, R.drawable.toolbar_zoomout, R.drawable.toolbar_zoomone,  
            R.drawable.toolbar_scaleup, R.drawable.toolbar_scaledown, R.drawable.toolbar_scaleone
        )

        /**
         * Toolbar's ordered list of buttons
         */
        private val buttons = arrayOf(
            
            Toolbar.Button.HOME,  
            Toolbar.Button.ZOOMIN, Toolbar.Button.ZOOMOUT, Toolbar.Button.ZOOMONE,  
            Toolbar.Button.SCALEUP, Toolbar.Button.SCALEDOWN, Toolbar.Button.SCALEONE,  
            Toolbar.Button.RADIAL, Toolbar.Button.SOUTH, Toolbar.Button.NORTH, Toolbar.Button.EAST, Toolbar.Button.WEST,  
            Toolbar.Button.EXPAND, Toolbar.Button.SHRINK, Toolbar.Button.EXPANSIONRESET,  
            Toolbar.Button.WIDEN, Toolbar.Button.NARROW, Toolbar.Button.SWEEPRESET,  
            Toolbar.Button.EXPANSIONSWEEPRESET,  
        )
    }
}
