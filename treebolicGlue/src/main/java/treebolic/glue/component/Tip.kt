/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.app.Activity
import android.os.Bundle
import android.view.InflateException
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import org.treebolic.glue.R

/**
 * Tip dialog
 *
 * @author Bernard Bou
 * @noinspection WeakerAccess, WeakerAccess
 */
class Tip : AppCompatDialogFragment() {

    private var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            text = savedInstanceState.getString(STATE_TEXT)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_TEXT, text)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AppCompatDialog {
        // use the Builder class for convenient dialog construction
        val activity: Activity = requireActivity()
        val builder = AlertDialog.Builder(activity)

        // get the layout inflater
        val inflater = activity.layoutInflater

        val frameLayout = activity.findViewById<FrameLayout>(android.R.id.custom)

        // get the layout inflater
        var view: View
        try {
            // try layout with a web view
            // inflate layout for the dialog
            view = inflater.inflate(R.layout.tip_layout, frameLayout, false)

            // data
            val webView = view.findViewById<WebView>(R.id.text)
            webView.loadData((if (text == null) "" else text)!!, "text/html; charset=UTF-8", "utf-8")
        } catch (e: InflateException) {
            // fall back on layout with text view
            // inflate layout for the dialog
            view = inflater.inflate(R.layout.tip_layout_text, frameLayout, false)

            // data
            val textView = view.findViewById<TextView>(R.id.text_text)
            textView.text = text
        }

        // attach view to the dialog
        builder.setView(view)

        // create the dialog and return it
        val dialog: AppCompatDialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    /**
     * Set text
     *
     * @param text0 text
     */
    fun setText(text0: String?) {
        text = text0
    }

    companion object {

        /**
         * Text name (used when saving instance)
         */
        private const val STATE_TEXT = "org.treebolic.tip"

        /**
         * Convenience method to display tip
         *
         * @param manager fragment manager
         * @param text    text to display
         */
        fun tip(manager: FragmentManager, text: String?) {
            val tip = Tip()
            tip.setText(text)
            tip.show(manager, STATE_TEXT)
        }
    }
}
