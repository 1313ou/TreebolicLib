/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import org.treebolic.glue.R
import treebolic.glue.component.Utils.fetchColors
import treebolic.glue.iface.ActionListener
import treebolic.glue.iface.component.Dialog
import java.util.function.Function

/**
 * Dialog
 * API class
 *
 * @author Bernard Bou
 * @noinspection WeakerAccess
 */
open class Dialog : AppCompatDialogFragment(), Dialog {

    /**
     * Header
     */
    private var header: CharSequence? = null

    /**
     * Content
     */
    private var content: Array<out String>? = null

    /**
     * Converter
     */
    private var converter: Function<Array<out String>, String>? = null

    /**
     * Style
     */
    private var style: String? = null

    /**
     * Activity
     */
    private var activity: AppCompatActivity? = null

    /**
     * Action listener
     */
    private var actionListener: ActionListener? = null

    /**
     * Background color
     */
    private var background = 0

    /**
     * Foreground color
     */
    private var foreground = 0

    override fun setHandle(handle: Any) {
        this.activity = handle as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            header = savedInstanceState.getString(STATE_HEADER)
            content = savedInstanceState.getStringArray(STATE_CONTENT)
        }

        // colors
        val colors = fetchColors(requireActivity(), R.attr.treebolic_dialog_background, R.attr.treebolic_dialog_foreground, R.attr.treebolic_dialog_foreground_enhanced, R.attr.treebolic_dialog_foreground_icon)
        background = colors[0]
        foreground = colors[1]
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putCharSequence(STATE_HEADER, header)
        outState.putCharSequenceArray(STATE_CONTENT, content)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AppCompatDialog {
        // use the Builder class for convenient dialog construction
        val activity: Activity = requireActivity()
        val builder = AlertDialog.Builder(activity)

        // get the layout inflater
        val inflater = activity.layoutInflater

        // inflate layout for the dialog
        val frameLayout = activity.findViewById<FrameLayout>(android.R.id.custom)
        var view: View
        try {
            view = inflater.inflate(R.layout.dialog_layout, frameLayout, false)

            // header
            val headerView = view.findViewById<TextView>(R.id.header)
            headerView.text = header

            // content
            val webView = view.findViewById<WebView>(R.id.content)
            val html = StringBuilder()
            html.append("<html><head>")
            html.append("<style type='text/css'>")
            html.append(defaultBaseStyle)
            if (style != null && style!!.isNotEmpty()) {
                html.append(style)
            }
            html.append("</style>")
            html.append("</head><body><div class='body'>")
            if (converter != null) {
                html.append(converter!!.apply(content!!))
            } else {
                html.append(content?.joinToString("<br>"))
            }
            html.append("</div></body></html>")

            // client
            webView.webViewClient = makeWebViewClient()

            // load
            webView.loadDataWithBaseURL(base, html.toString(), "text/html", "UTF-8", null)
        } catch (e: InflateException) {
            view = inflater.inflate(R.layout.dialog_layout_text, frameLayout, false)

            // header
            val headerView = view.findViewById<TextView>(R.id.header_text)
            headerView.text = header

            // content
            val textView = view.findViewById<TextView>(R.id.content_text)
            textView.text = content?.joinToString("\n")
        }

        // set the layout for the dialog
        builder.setView(view)

        // create the dialog object and return it
        val dialog: AppCompatDialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    private fun makeWebViewClient(): WebViewClient {
        return object : WebViewClient(
        ) {
            private var intercept = false

            override fun onPageFinished(view0: WebView, url: String) {
                intercept = true
            }

            @Deprecated(message = "Deprecated in Java")
            override fun shouldOverrideUrlLoading(view0: WebView, url: String?): Boolean {
                if (intercept && url != null) {
                    Log.d(TAG, "url:$url")
                    actionListener!!.onAction(url)
                    return true
                }
                return false
            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                if (intercept && uri != null) {
                    Log.d(TAG, "url:$uri")
                    actionListener!!.onAction(uri)
                    return true
                }
                return false
            }
        }
    }

    override fun set(header0: CharSequence, vararg content0: String) {
        header = header0
        content = content0
    }

    override fun setConverter(converter0: Function<Array<out String>, String>?) {
        converter = converter0
    }

    override fun setStyle(style0: String) {
        style = style0
    }

    /**
     * Show
     */
    override fun display() {
        show(requireActivity().supportFragmentManager, "info")
    }

    private val defaultBaseStyle: String
        /**
         * Default style
         *
         * @return default style
         */
        get() = "body {" + String.format("color: #%06X;", 0xFFFFFF and foreground) + String.format("background-color: #%06X;", 0xFFFFFF and background) + '}'

    override fun setListener(actionListener0: ActionListener) {
        actionListener = actionListener0
    }

    companion object {

        private const val TAG = "Dialog"

        /**
         * Save key for header
         */
        private const val STATE_HEADER = "org.treebolic.web.header"

        /**
         * Save key for content
         */
        private const val STATE_CONTENT = "org.treebolic.web.content"

        /**
         * Base URL for webview
         */
        var base = "file:///android_asset/"
    }
}
