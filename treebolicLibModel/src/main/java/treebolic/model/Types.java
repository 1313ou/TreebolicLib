/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model;

/**
 * Types
 *
 * @author Bernard Bou
 */
public class Types
{
	/**
	 * Search Commands
	 */
	public enum SearchCommand
	{
		/**
		 * Start search
		 */
		SEARCH,
		/**
		 * Continue search
		 */
		CONTINUE,
		/**
		 * Reset search
		 */
		RESET
	}

	/**
	 * Match scope
	 */
	public enum MatchScope
	{
		/**
		 * Match labels
		 */
		LABEL,
		/**
		 * Match contents
		 */
		CONTENT,
		/**
		 * Match links
		 */
		LINK,
		/**
		 * Match IDs
		 */
		ID
	}

	/**
	 * Match mode
	 */
	public enum MatchMode
	{
		/**
		 * Equals matching
		 */
		EQUALS,
		/**
		 * Starts-with matching
		 */
		STARTSWITH,
		/**
		 * Include matching
		 */
		INCLUDES
	}
}
