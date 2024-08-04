/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue

import treebolic.glue.iface.ActionListener

/**
 * ActionListener
 *
 * @author Bernard Bou
 */
abstract class ActionListener : ActionListener {

    abstract override fun onAction(vararg params: Any): Boolean
}
