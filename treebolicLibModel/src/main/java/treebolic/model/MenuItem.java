/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.model;

import java.io.Serializable;

import androidx.annotation.Nullable;
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
	public String label;

	/**
	 * Action
	 */
	@Nullable
	public Action action;

	/**
	 * Url link
	 */
	public String link;

	/**
	 * Url target frame
	 */
	public String target;

	/**
	 * Match target
	 */
	public String matchTarget;

	/**
	 * Match scope
	 */
	@Nullable
	public MatchScope matchScope;

	/**
	 * Match mode
	 */
	@Nullable
	public MatchMode matchMode;
}
