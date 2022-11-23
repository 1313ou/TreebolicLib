/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
import treebolic.model.Model;
import treebolic.model.Types.MatchMode;
import treebolic.model.Types.MatchScope;
import treebolic.model.Types.SearchCommand;
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
	 * @param providerName provider class name (null will default to XML provider)
	 * @param source       source (anything the provider will make sense of)
	 */
	void init(String providerName, final String source);

	/**
	 * Init from provider and source
	 *
	 * @param provider provider
	 * @param source   source
	 */
	void init(final IProvider provider, final String source);

	/**
	 * Init from model
	 *
	 * @param model model
	 */
	void init(final Model model);

	/**
	 * Init from serialized model
	 *
	 * @param serFile serialized file
	 */
	void initSerialized(final String serFile);

	/**
	 * Re-init from same provider and new source (this assumes the provider is loaded)
	 *
	 * @param source data source
	 */
	void reinit(final String source);

	// Version

	/**
	 * Get version (wrapper)
	 *
	 * @return version
	 */
	@NonNull
	String getVersion();

	// JavaScript

	/**
	 * Focus
	 *
	 * @param nodeId node id to get focus
	 */
	void focus(@SuppressWarnings("SameParameterValue") String nodeId);

	/**
	 * Link to Url
	 *
	 * @param urlString url string
	 * @param urlTarget target string
	 */
	void link(String urlString, String urlTarget);

	// SEARCH PARAMETER VALUES

	/**
	 * Search
	 */
	String SEARCH = SearchCommand.SEARCH.name();

	/**
	 * Continue search
	 */
	String SEARCHCONTINUE = SearchCommand.CONTINUE.name();

	/**
	 * Reset search
	 */
	String SEARCHRESET = SearchCommand.RESET.name();

	/**
	 * Search labels
	 */
	String SEARCHSCOPELABEL = MatchScope.LABEL.name();

	/**
	 * Search contents
	 */
	String SEARCHSCOPECONTENT = MatchScope.CONTENT.name();

	/**
	 * Search links
	 */
	String SEARCHSCOPELINK = MatchScope.LINK.name();

	/**
	 * Search links
	 */
	String SEARCHSCOPEID = MatchScope.ID.name();

	/**
	 * Match if equals parameter
	 */
	String SEARCHMODEEQUALS = MatchMode.EQUALS.name();

	/**
	 * Match if starts with parameter
	 */
	String SEARCHMODESTARTSWITH = MatchMode.STARTSWITH.name();

	/**
	 * Match if includes parameter
	 */
	String SEARCHMODEINCLUDES = MatchMode.INCLUDES.name();

	/**
	 * Match node against string
	 *
	 * @param targetString string to search for
	 * @param scopeString  scope ("LABEL", "CONTENT", "LINK", "ID")
	 * @param modeString   mode ("EQUALS", "STARTSWITH", "INCLUDES")
	 * @return node id
	 */
	@Nullable
	String match(String targetString, String scopeString, String modeString);

	/**
	 * Match node against string
	 *
	 * @param commandString command string ("SEARCH", "CONTINUE", "RESET")
	 * @param params        parameters for search ("CONTINUE", "RESET" don't require any)
	 */
	void search(String commandString, String... params);
}
