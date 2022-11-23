/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model;

import java.io.Serializable;

import treebolic.annotations.Nullable;
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
		/**
		 * Cancel
		 */
		CANCEL,
		/**
		 * Info on node
		 */
		INFO,
		/**
		 * Focus node
		 */
		FOCUS,
		/**
		 * Follow link
		 */
		LINK,
		/**
		 * Mount
		 */
		MOUNT,
		/**
		 * Go to
		 */
		GOTO,
		/**
		 * Search
		 */
		SEARCH
	}

	/**
	 * Label
	 */
	@Nullable
	public String label;

	/**
	 * Action
	 */
	@Nullable
	public Action action;

	/**
	 * Url link
	 */
	@Nullable
	public String link;

	/**
	 * Url target frame
	 */
	@Nullable
	public String target;

	/**
	 * Match target
	 */
	@Nullable
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
