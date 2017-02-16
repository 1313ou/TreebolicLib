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

package treebolic;

import treebolic.control.Controller;
import treebolic.model.Model;
import treebolic.provider.IProvider;

/**
 * Treebolic's main panel. Call either init() to feed the data
 *
 * @author Bernard Bou
 */
public interface IWidget
{

	/**
	 * Init (typically called by embedding applet's init()). Data source and data provider have not yet been determined.
	 */
	public void init();

	/**
	 * Init (typically called by embedding applet's init()). Data source and data provider names have been determined.
	 *
	 * @param thisProviderName
	 *        provider class name (null will default to XML provider)
	 * @param thisSource
	 *        source (anything the provider will make sense of)
	 */
	public void init(String thisProviderName, final String thisSource);

	/**
	 * Init from provider and source
	 *
	 * @param thisProvider
	 *        provider
	 * @param thisSource
	 *        source
	 */
	public void init(final IProvider thisProvider, final String thisSource);

	/**
	 * Init from model
	 *
	 * @param thisModel
	 *        model
	 */
	public void init(final Model thisModel);

	/**
	 * Init from serialized model
	 *
	 * @param thisSerFile
	 *        serialized file
	 */
	public void initSerialized(final String thisSerFile);

	/**
	 * Re-init from same provider and new source (this assumes the provider is loaded)
	 *
	 * @param thisSource
	 *        data source
	 */
	public void reinit(final String thisSource);

	// Version

	/**
	 * Get version (wrapper)
	 */
	public String getVersion();

	// JavaScript

	/**
	 * Focus
	 *
	 * @param thisNodeId
	 *        node id to get focus
	 */
	public void focus(String thisNodeId);

	/**
	 * Link to Url
	 *
	 * @param thisUrlString
	 *        url string
	 * @param thisUrlTarget
	 *        target string
	 */
	public void link(String thisUrlString, String thisUrlTarget);

	// SEARCH PARAMETER VALUES

	public static final String SEARCH = Controller.SearchCommand.SEARCH.name();

	public static final String SEARCHCONTINUE = Controller.SearchCommand.CONTINUE.name();

	public static final String SEARCHRESET = Controller.SearchCommand.RESET.name();

	public static final String SEARCHSCOPELABEL = Controller.MatchScope.LABEL.name();

	public static final String SEARCHSCOPECONTENT = Controller.MatchScope.CONTENT.name();

	public static final String SEARCHSCOPELINK = Controller.MatchScope.LINK.name();

	public static final String SEARCHSCOPEID = Controller.MatchScope.ID.name();

	public static final String SEARCHMODEEQUALS = Controller.MatchMode.EQUALS.name();

	public static final String SEARCHMODESTARTSWITH = Controller.MatchMode.STARTSWITH.name();

	public static final String SEARCHMODEINCLUDES = Controller.MatchMode.INCLUDES.name();

	/**
	 * Match node against string
	 *
	 * @param thisTargetString
	 *        string to search for
	 * @param thisScopeString
	 *        scope ("LABEL", "CONTENT", "LINK", "ID")
	 * @param thisModeString
	 *        mode ("EQUALS", "STARTSWITH", "INCLUDES")
	 * @return node id
	 */
	public String match(String thisTargetString, String thisScopeString, String thisModeString);

	/**
	 * Match node against string
	 *
	 * @param thisCommandString
	 *        command string ("SEARCH", "CONTINUE", "RESET")
	 * @param theseParams
	 *        parameters for search ("CONTINUE", "RESET" don't require any)
	 */
	public void search(String thisCommandString, String... theseParams);
}
