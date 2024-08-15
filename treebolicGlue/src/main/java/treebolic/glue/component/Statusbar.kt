/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.treebolic.glue.R
import treebolic.glue.component.Utils.fetchColors
import treebolic.glue.component.Utils.getDrawable
import treebolic.glue.component.Utils.screenSize
import treebolic.glue.component.Utils.tint
import treebolic.glue.iface.ActionListener
import treebolic.glue.iface.component.Statusbar
import java.util.function.Function

/**
 * Status bar
 * API class
 *
 * @param activity activity
 *
 * @author Bernard Bou
 * @noinspection WeakerAccess
 */
@SuppressLint("CutPasteId")
open class Statusbar(
    handle: Any
) : FrameLayout(handle as AppCompatActivity), Statusbar {

   private val activity: AppCompatActivity = handle as AppCompatActivity

   /**
     * Main status view
     */
    private val statusView: TextView

    /**
     * Web content status view
     */
    private val webContentView: WebView?

    /**
     * Text content status view
     */
    private val textContentView: TextView?

    /**
     * Horizontal / vertical
     */
    private val isHorizontal: Boolean

    /**
     * Style
     */
    private var style: String? = null

    /**
     * Background
     */
    private val background: Int

    /**
     * Foreground
     */
    private val foreground: Int

    /**
     * Icon tint
     */
    private val iconTint: Int

    /**
     * Action listener
     */
    private var actionListener: ActionListener? = null

    // C O N S T R U C T O R

    init {
        // determine orientation
        val size = screenSize(this.activity)
        val isHorizontalScreen = size.x >= size.y
        this.isHorizontal = !isHorizontalScreen

        // colors
        val colors = fetchColors(this.activity, R.attr.treebolic_statusbar_background, R.attr.treebolic_statusbar_foreground, R.attr.treebolic_statusbar_foreground_icon)
        this.background = colors[0]
        this.foreground = colors[1]
        this.iconTint = colors[2]

        // inflate
        val inflater = checkNotNull(activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        var statusView0: TextView
        var webContentView0: WebView? = null
        var textContentView0: TextView? = null
        try {
            val wrappedView = inflater.inflate(if (isHorizontalScreen) R.layout.status_h else R.layout.status_v, this) as ViewGroup
            statusView0 = wrappedView.findViewById(R.id.status)
            webContentView0 = wrappedView.findViewById(R.id.content)
            webContentView0.isFocusable = false
            webContentView0.setBackgroundColor(this.background)
            webContentView0.settings.allowFileAccess = true
            webContentView0.webViewClient = object : WebViewClient(
            ) {
                private var intercept = false

                override fun onPageFinished(view0: WebView, url: String) {
                    this.intercept = true
                }

                @Deprecated("Deprecated in Java")
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
                        actionListener!!.onAction(uri.toString())
                        return true
                    }
                    return false
                }
            }
        } catch (e: InflateException) {
            val wrappedView = inflater.inflate(if (isHorizontalScreen) R.layout.status_h_text else R.layout.status_v_text, this) as ViewGroup
            statusView0 = wrappedView.findViewById(R.id.status)
            textContentView0 = wrappedView.findViewById(R.id.content)
        }
        statusView = statusView0
        webContentView = webContentView0
        textContentView = textContentView0
    }

    override fun init(image: Int) {
        // drawable
        val drawable = getDrawable(image)

        // tint drawable
        tint(drawable!!, this.iconTint)

        // set
        if (this.isHorizontal) {
            statusView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        } else {
            statusView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
        }
    }

    override fun addListener(arg0: ActionListener) {
        // does not fire events
    }

    override fun setListener(actionListener0: ActionListener) {
        actionListener = actionListener0
    }

    override fun setColors(backColor: Int?, foreColor: Int?) {
        //
    }

    override fun setStyle(style0: String) {
        this.style = style0
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun put(image: Int, converter: Function<Array<out String>, String>?, label0: String?, content0: Array<out String>) {
        // icon
        val drawable = getDrawable(image)

        // tint drawable
        tint(drawable!!, this.iconTint)

        // set
        if (this.isHorizontal) {
            statusView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        } else {
            statusView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
        }

        // label
        var label = label0
        if (labelProcessor != null) {
            label = labelProcessor!!.invoke(label, this)
        }
        statusView.text = label

        // content
        if (this.webContentView != null) {
            var content: String? = converter?.apply(content0) ?: content0.joinToString("<br>")
            if (contentProcessor != null) {
                content = contentProcessor!!.invoke(content, this)
            }

            if (content == null) {
                webContentView.loadUrl("about:blank")
            } else {
                val html = StringBuilder()
                html.append("<html><head>")
                html.append("<style type='text/css'>")
                html.append(defaultBaseStyle)
                if (this.style != null && style!!.isNotEmpty()) {
                    html.append(this.style)
                }
                html.append("</style>")
                html.append("</head><body><div class='body'>")
                html.append(content)
                html.append("</div></body></html>")

                // Log.d(TAG, html.toString());
                webContentView.loadDataWithBaseURL(base, html.toString(), "text/html", "UTF-8", null)
            }
        } else {
            checkNotNull(this.textContentView)
            textContentView.text = content0.joinToString("\n")
        }
    }

    override fun put(message: String) {
        statusView.text = message
    }

    /**
     * Get drawable from index
     *
     * @param index index
     * @return drawable
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun getDrawable(index: Int): Drawable? {
        if (drawables[index] == null) {
            var resId = -1
            when (Statusbar.ImageIndices.entries[index]) {
                Statusbar.ImageIndices.INFO -> resId = R.drawable.status_info
                Statusbar.ImageIndices.LINK -> resId = R.drawable.status_link
                Statusbar.ImageIndices.MOUNT -> resId = R.drawable.status_mount
                Statusbar.ImageIndices.SEARCH -> resId = R.drawable.status_search
                else -> {}
            }
            if (resId != -1) {
                drawables[index] = getDrawable(this.activity, resId)
            }
        }
        return drawables[index]
    }

    private val defaultBaseStyle: String
        /**
         * Default style
         *
         * @return default style
         */
        get() = "body {" + String.format("color: #%06X;", 0xFFFFFF and this.foreground) + String.format("background-color: #%06X;", 0xFFFFFF and this.background) + '}'

    companion object {

        private const val TAG = "Statusbar"

        /**
         * Base URL for webview
         */
        var base = "file:///android_asset/"

        /**
         * Drawables
         */
        @OptIn(ExperimentalStdlibApi::class)
        private val drawables = arrayOfNulls<Drawable>(Statusbar.ImageIndices.entries.size)

        // P R O C E S S O R

        /**
         * Label processor
         */
        var labelProcessor: ((String?, View) -> String?)? = null

        /**
         * Content processor
         */
        private var contentProcessor: ((String?, View) -> String?)? = null
    }
}