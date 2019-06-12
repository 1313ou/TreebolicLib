/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
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
	{SEARCH, CONTINUE, RESET}

	/**
	 * Match scope
	 */
	public enum MatchScope
	{LABEL, CONTENT, LINK, ID}

	/**
	 * Match mode
	 */
	public enum MatchMode
	{EQUALS, STARTSWITH, INCLUDES}
}
