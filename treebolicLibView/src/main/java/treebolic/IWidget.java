package treebolic;

import treebolic.model.Types.MatchMode;
import treebolic.model.Types.MatchScope;
import treebolic.model.Types.SearchCommand;
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
	 * @param thisProviderName provider class name (null will default to XML provider)
	 * @param thisSource       source (anything the provider will make sense of)
	 */
	void init(String thisProviderName, final String thisSource);

	/**
	 * Init from provider and source
	 *
	 * @param thisProvider provider
	 * @param thisSource   source
	 */
	void init(final IProvider thisProvider, final String thisSource);

	/**
	 * Init from model
	 *
	 * @param thisModel model
	 */
	void init(final Model thisModel);

	/**
	 * Init from serialized model
	 *
	 * @param thisSerFile serialized file
	 */
	void initSerialized(final String thisSerFile);

	/**
	 * Re-init from same provider and new source (this assumes the provider is loaded)
	 *
	 * @param thisSource data source
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
	 * @param thisNodeId node id to get focus
	 */
	void focus(String thisNodeId);

	/**
	 * Link to Url
	 *
	 * @param thisUrlString url string
	 * @param thisUrlTarget target string
	 */
	void link(String thisUrlString, String thisUrlTarget);

	// SEARCH PARAMETER VALUES

	String SEARCH = SearchCommand.SEARCH.name();

	String SEARCHCONTINUE = SearchCommand.CONTINUE.name();

	String SEARCHRESET = SearchCommand.RESET.name();

	String SEARCHSCOPELABEL = MatchScope.LABEL.name();

	String SEARCHSCOPECONTENT = MatchScope.CONTENT.name();

	String SEARCHSCOPELINK = MatchScope.LINK.name();

	String SEARCHSCOPEID = MatchScope.ID.name();

	String SEARCHMODEEQUALS = MatchMode.EQUALS.name();

	String SEARCHMODESTARTSWITH = MatchMode.STARTSWITH.name();

	String SEARCHMODEINCLUDES = MatchMode.INCLUDES.name();

	/**
	 * Match node against string
	 *
	 * @param thisTargetString string to search for
	 * @param thisScopeString  scope ("LABEL", "CONTENT", "LINK", "ID")
	 * @param thisModeString   mode ("EQUALS", "STARTSWITH", "INCLUDES")
	 * @return node id
	 */
	String match(String thisTargetString, String thisScopeString, String thisModeString);

	/**
	 * Match node against string
	 *
	 * @param thisCommandString command string ("SEARCH", "CONTINUE", "RESET")
	 * @param theseParams       parameters for search ("CONTINUE", "RESET" don't require any)
	 */
	void search(String thisCommandString, String... theseParams);
}
