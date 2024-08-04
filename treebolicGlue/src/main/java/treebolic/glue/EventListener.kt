/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue

import treebolic.glue.iface.EventListener

/**
 * Event listener
 *
 * @author Bernard Bou
 */
abstract class EventListener : EventListener {

    abstract override fun onDown(x: Int, y: Int, rotate: Boolean): Boolean

    abstract override fun onUp(x: Int, y: Int): Boolean

    abstract override fun onDragged(x: Int, y: Int): Boolean

    abstract override fun onHover(x: Int, y: Int): Boolean

    abstract override fun onSelect(x: Int, y: Int): Boolean

    abstract override fun onFocus(x: Int, y: Int): Boolean

    abstract override fun onLink(x: Int, y: Int): Boolean

    abstract override fun onMenu(x: Int, y: Int): Boolean

    abstract override fun onMount(x: Int, y: Int): Boolean

    abstract override fun onScale(mapScale: Float, fontScale: Float, imageScale: Float)
}
