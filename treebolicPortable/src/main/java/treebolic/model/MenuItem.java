/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
package treebolic.model;

import java.io.Serializable;

import treebolic.control.Controller.MatchMode;
import treebolic.control.Controller.MatchScope;

/**
 * Menu item
 *
 * @author Bernard Bou
 */
public class MenuItem implements Serializable
{
	private static final long serialVersionUID = 6176932077061226910L;

	/**
	 * Action types
	 */
	public enum Action
	{
		CANCEL, INFO, FOCUS, LINK, MOUNT, GOTO, SEARCH
	}

	/**
	 * Label
	 */
	public String theLabel;

	/**
	 * Action
	 */
	public Action theAction;

	/**
	 * Url link
	 */
	public String theLink;

	/**
	 * Url target frame
	 */
	public String theTarget;

	/**
	 * Match target
	 */
	public String theMatchTarget;

	/**
	 * Match scope
	 */
	public MatchScope theMatchScope;

	/**
	 * Match mode
	 */
	public MatchMode theMatchMode;
}
