/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package org.treebolic

/**
 * Constants used in Android
 */
interface TreebolicIface {

    companion object {

        /**
         * Whether to use model references
         */
        const val USE_MODEL_REFERENCES: Boolean = false

        // intent extras

        /**
         * Make model
         */
        const val ARG_SOURCE: String = "org.treebolic.SOURCE"

        /**
         * Plugin package
         */
        const val ARG_PLUGINPKG: String = "org.treebolic.PLUGINPKG"

        /**
         * Provider
         */
        const val ARG_PROVIDER: String = "org.treebolic.PROVIDER"

        /**
         * Service
         */
        const val ARG_SERVICE: String = "org.treebolic.SERVICE"

        /**
         * Base
         */
        const val ARG_BASE: String = "org.treebolic.BASE"

        /**
         * Image base
         */
        const val ARG_IMAGEBASE: String = "org.treebolic.IMAGEBASE"

        /**
         * Model reference
         */
        const val ARG_MODEL_REFERENCE: String = "org.treebolic.MODEL_REFERENCE"

        /**
         * Model
         */
        const val ARG_MODEL: String = "org.treebolic.MODEL"

        /**
         * Serialized
         */
        const val ARG_SERIALIZED: String = "org.treebolic.SERIALIZED"

        /**
         * Serialized model URI
         */
        const val ARG_SERIALIZED_MODEL_URI: String = "org.treebolic.SERIALIZED_MODEL_URI"

        /**
         * Settings
         */
        const val ARG_SETTINGS: String = "org.treebolic.SETTINGS"

        /**
         * Style
         */
        const val ARG_STYLE: String = "org.treebolic.STYLE"

        /**
         * URL scheme
         */
        const val ARG_URLSCHEME: String = "org.treebolic.URLSCHEME"

        /**
         * More
         */
        const val ARG_MORE: String = "org.treebolic.MORE"

        /**
         * Parent Activity
         */
        const val ARG_PARENTACTIVITY: String = "org.treebolic.PARENT"

        // action
        /**
         * Make model
         */
        const val ACTION_MAKEMODEL: String = "org.treebolic.action.MAKE_MODEL"

        // activities

        /**
         * Treebolic package
         */
        const val PKG_TREEBOLIC: String = "org.treebolic"

        /**
         * Treebolic activity
         */
        const val ACTIVITY_TREEBOLIC: String = "org.treebolic.TreebolicActivity"

        /**
         * Treebolic plugin
         */
        const val ACTIVITY_PLUGIN: String = "org.treebolic.TreebolicPluginActivity"

        /**
         * Treebolic model
         */
        const val ACTIVITY_MODEL: String = "org.treebolic.TreebolicModelActivity"

        /**
         * Treebolic client
         */
        const val ACTIVITY_CLIENT: String = "org.treebolic.TreebolicClientActivity"

        // preference keys

        /**
         * Source preference key
         */
        const val PREF_SOURCE: String = "pref_source"

        /**
         * Base preference key
         */
        const val PREF_BASE: String = "pref_base"

        /**
         * Image base preference key
         */
        const val PREF_IMAGEBASE: String = "pref_imagebase"

        /**
         * Settings preference key
         */
        const val PREF_SETTINGS: String = "pref_settings"
    }
}
