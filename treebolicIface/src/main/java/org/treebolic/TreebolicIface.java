/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package org.treebolic;

/**
 * Constants used in Android
 * TODO remove from here
 */
public interface TreebolicIface
{
	/**
	 * Whether to use model references
	 */
	boolean USE_MODEL_REFERENCES = false;

	// intent extras

	/**
	 * Make model
	 */
	String ARG_SOURCE = "org.treebolic.SOURCE";

	/**
	 * Plugin package
	 */
	String ARG_PLUGINPKG = "org.treebolic.PLUGINPKG";

	/**
	 * Provider
	 */
	String ARG_PROVIDER = "org.treebolic.PROVIDER";

	/**
	 * Service
	 */
	String ARG_SERVICE = "org.treebolic.CLIENT"; // TODO

	/**
	 * Base
	 */
	String ARG_BASE = "org.treebolic.BASE";

	/**
	 * Image base
	 */
	String ARG_IMAGEBASE = "org.treebolic.IMAGEBASE";

	/**
	 * Model reference
	 */
	String ARG_MODEL_REFERENCE = "org.treebolic.MODEL_REFERENCE";

	/**
	 * Model
	 */
	String ARG_MODEL = "org.treebolic.MODEL";

	/**
	 * Serialized
	 */
	String ARG_SERIALIZED = "org.treebolic.SERIALIZED";

	/**
	 * Serialized model URI
	 */
	String ARG_SERIALIZED_MODEL_URI = "org.treebolic.SERIALIZED_MODEL_URI";

	/**
	 * Settings
	 */
	String ARG_SETTINGS = "org.treebolic.SETTINGS";

	/**
	 * Style
	 */
	String ARG_STYLE = "org.treebolic.STYLE";

	/**
	 * URL scheme
	 */
	String ARG_URLSCHEME = "org.treebolic.URLSCHEME";

	/**
	 * More
	 */
	String ARG_MORE = "org.treebolic.MORE";

	/**
	 * Parent Activity
	 */
	String ARG_PARENTACTIVITY = "org.treebolic.PARENT";

	// action

	/**
	 * Make model
	 */
	String ACTION_MAKEMODEL = "org.treebolic.action.MAKE_MODEL";

	// activities

	/**
	 * Treebolic package
	 */
	String PKG_TREEBOLIC = "org.treebolic";

	/**
	 * Treebolic activity
	 */
	String ACTIVITY_TREEBOLIC = "org.treebolic.TreebolicActivity";

	/**
	 * Treebolic plugin
	 */
	String ACTIVITY_PLUGIN = "org.treebolic.TreebolicPluginActivity";

	/**
	 * Treebolic model
	 */
	String ACTIVITY_MODEL = "org.treebolic.TreebolicModelActivity";

	/**
	 * Treebolic client
	 */
	String ACTIVITY_CLIENT = "org.treebolic.TreebolicClientActivity";

	// preference keys

	/**
	 * Source preference key
	 */
	String PREF_SOURCE = "pref_source";

	/**
	 * Base preference key
	 */
	String PREF_BASE = "pref_base";

	/**
	 * Image base preference key
	 */
	String PREF_IMAGEBASE = "pref_imagebase";

	/**
	 * Settings preference key
	 */
	String PREF_SETTINGS = "pref_settings";
}
