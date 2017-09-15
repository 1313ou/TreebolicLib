package treebolic.model;

import java.io.Serializable;

import treebolic.model.Types.MatchMode;
import treebolic.model.Types.MatchScope;

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
