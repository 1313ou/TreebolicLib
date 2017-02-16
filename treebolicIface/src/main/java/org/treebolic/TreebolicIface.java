package org.treebolic;

interface TreebolicIface
{
	boolean USE_MODEL_REFERENCES = false;

	// intent extras

	String ARG_SOURCE = "org.treebolic.SOURCE"; //$NON-NLS-1$

	String ARG_PLUGINPKG = "org.treebolic.PLUGINPKG"; //$NON-NLS-1$

	String ARG_PROVIDER = "org.treebolic.PROVIDER"; //$NON-NLS-1$

	String ARG_BASE = "org.treebolic.BASE"; //$NON-NLS-1$

	String ARG_IMAGEBASE = "org.treebolic.IMAGEBASE"; //$NON-NLS-1$

	String ARG_MODEL_REFERENCE = "org.treebolic.MODEL_REFERENCE"; //$NON-NLS-1$

	String ARG_MODEL = "org.treebolic.MODEL"; //$NON-NLS-1$

	String ARG_SERIALIZED = "org.treebolic.SERIALIZED"; //$NON-NLS-1$

	String ARG_SERIALIZED_MODEL_URI = "org.treebolic.SERIALIZED_MODEL_URI"; //$NON-NLS-1$

	String ARG_SETTINGS = "org.treebolic.SETTINGS"; //$NON-NLS-1$

	String ARG_STYLE = "org.treebolic.STYLE"; //$NON-NLS-1$

	String ARG_URLSCHEME = "org.treebolic.URLSCHEME"; //$NON-NLS-1$

	String ARG_MORE = "org.treebolic.MORE"; //$NON-NLS-1$

	String ARG_PARENTACTIVITY = "org.treebolic.PARENT"; //$NON-NLS-1$

	// action

	String ACTION_MAKEMODEL = "org.treebolic.action.MAKE_MODEL"; //$NON-NLS-1$

	// activities

	String PKG_TREEBOLIC = "org.treebolic"; //$NON-NLS-1$

	String ACTIVITY_TREEBOLIC = "org.treebolic.TreebolicActivity"; //$NON-NLS-1$

	String ACTIVITY_PLUGIN = "org.treebolic.TreebolicPluginActivity"; //$NON-NLS-1$

	String ACTIVITY_MODEL = "org.treebolic.TreebolicModelActivity"; //$NON-NLS-1$

	String ACTIVITY_CLIENT = "org.treebolic.TreebolicClientActivity"; //$NON-NLS-1$

	// preference keys

	String PREF_SOURCE = "pref_source"; //$NON-NLS-1$

	String PREF_BASE = "pref_base"; //$NON-NLS-1$

	String PREF_IMAGEBASE = "pref_imagebase"; //$NON-NLS-1$

	String PREF_SETTINGS = "pref_settings"; //$NON-NLS-1$
}
