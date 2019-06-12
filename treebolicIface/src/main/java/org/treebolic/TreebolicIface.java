/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package org.treebolic;

public interface TreebolicIface
{
	boolean USE_MODEL_REFERENCES = false;

	// intent extras

	String ARG_SOURCE = "org.treebolic.SOURCE";

	String ARG_PLUGINPKG = "org.treebolic.PLUGINPKG";

	String ARG_PROVIDER = "org.treebolic.PROVIDER";

	String ARG_SERVICE = "org.treebolic.CLIENT";

	String ARG_BASE = "org.treebolic.BASE";

	String ARG_IMAGEBASE = "org.treebolic.IMAGEBASE";

	String ARG_MODEL_REFERENCE = "org.treebolic.MODEL_REFERENCE";

	String ARG_MODEL = "org.treebolic.MODEL";

	String ARG_SERIALIZED = "org.treebolic.SERIALIZED";

	String ARG_SERIALIZED_MODEL_URI = "org.treebolic.SERIALIZED_MODEL_URI";

	String ARG_SETTINGS = "org.treebolic.SETTINGS";

	String ARG_STYLE = "org.treebolic.STYLE";

	String ARG_URLSCHEME = "org.treebolic.URLSCHEME";

	String ARG_MORE = "org.treebolic.MORE";

	String ARG_PARENTACTIVITY = "org.treebolic.PARENT";

	// action

	String ACTION_MAKEMODEL = "org.treebolic.action.MAKE_MODEL";

	// activities

	String PKG_TREEBOLIC = "org.treebolic";

	String ACTIVITY_TREEBOLIC = "org.treebolic.TreebolicActivity";

	String ACTIVITY_PLUGIN = "org.treebolic.TreebolicPluginActivity";

	String ACTIVITY_MODEL = "org.treebolic.TreebolicModelActivity";

	String ACTIVITY_CLIENT = "org.treebolic.TreebolicClientActivity";

	// preference keys

	String PREF_SOURCE = "pref_source";

	String PREF_BASE = "pref_base";

	String PREF_IMAGEBASE = "pref_imagebase";

	String PREF_SETTINGS = "pref_settings";
}
