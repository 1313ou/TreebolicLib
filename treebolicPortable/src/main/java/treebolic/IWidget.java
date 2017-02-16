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
	void init();

	/**
	 * Init (typically called by embedding applet's init()). Data source and data provider names have been determined.
	 *
	 * @param thisProviderName
	 *        provider class name (null will default to XML provider)
	 * @param thisSource
	 *        source (anything the provider will make sense of)
	 */
	void init(String thisProviderName, final String thisSource);

	/**
	 * Init from provider and source
	 *
	 * @param thisProvider
	 *        provider
	 * @param thisSource
	 *        source
	 */
	void init(final IProvider thisProvider, final String thisSource);

	/**
	 * Init from model
	 *
	 * @param thisModel
	 *        model
	 */
	void init(final Model thisModel);

	/**
	 * Init from serialized model
	 *
	 * @param thisSerFile
	 *        serialized file
	 */
	void initSerialized(final String thisSerFile);

	/**
	 * Re-init from same provider and new source (this assumes the provider is loaded)
	 *
	 * @param thisSource
	 *        data source
	 */
	void reinit(final String thisSource);

	// Version

	/**
	 * Get version (wrapper)
	 */
	String getVersion();

	// JavaScript

	/**
	 * Focus
	 *
	 * @param thisNodeId
	 *        node id to get focus
	 */
	void focus(String thisNodeId);

	/**
	 * Link to Url
	 *
	 * @param thisUrlString
	 *        url string
	 * @param thisUrlTarget
	 *        target string
	 */
	void link(String thisUrlString, String thisUrlTarget);

	// SEARCH PARAMETER VALUES

	String SEARCH = Controller.SearchCommand.SEARCH.name();

	String SEARCHCONTINUE = Controller.SearchCommand.CONTINUE.name();

	String SEARCHRESET = Controller.SearchCommand.RESET.name();

	String SEARCHSCOPELABEL = Controller.MatchScope.LABEL.name();

	String SEARCHSCOPECONTENT = Controller.MatchScope.CONTENT.name();

	String SEARCHSCOPELINK = Controller.MatchScope.LINK.name();

	String SEARCHSCOPEID = Controller.MatchScope.ID.name();

	String SEARCHMODEEQUALS = Controller.MatchMode.EQUALS.name();

	String SEARCHMODESTARTSWITH = Controller.MatchMode.STARTSWITH.name();

	String SEARCHMODEINCLUDES = Controller.MatchMode.INCLUDES.name();

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
	String match(String thisTargetString, String thisScopeString, String thisModeString);

	/**
	 * Match node against string
	 *
	 * @param thisCommandString
	 *        command string ("SEARCH", "CONTINUE", "RESET")
	 * @param theseParams
	 *        parameters for search ("CONTINUE", "RESET" don't require any)
	 */
	void search(String thisCommandString, String... theseParams);
}
