package treebolic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
