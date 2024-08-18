/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import org.treebolic.glue.R

/**
 * Progress panel
 * API class
 *
 * @param handle context
 *
 * @author Bernard Bou
 */
open class Progress(handle: Any?) : LinearLayout(handle as Context), treebolic.glue.iface.component.Progress {

    /**
     * Handler
     */
    internal class ProgressHandler(private val progress: Progress) : Handler(Looper.getMainLooper()) {

        override fun handleMessage(m: Message) {
            val fail = m.data.getBoolean("fail")
            var str = m.data.getString("text")
            if (fail) {
                str = "\n${progress.statusView.text}\n$str"
                progress.progressBar.isIndeterminate = false
                progress.progressBar.visibility = GONE
                progress.progressIcon.setImageResource(R.drawable.progress_fail)
            } else {
                progress.progressBar.isIndeterminate = true
                progress.progressBar.visibility = VISIBLE
            }
            progress.statusView.text = str
        }
    }

    /**
     * Message handler
     */
    private val handler: Handler

    /**
     * Status
     */
    private val statusView: TextView

    /**
     * Progress bar
     */
    private val progressBar: ProgressBar

    /**
     * Icon
     */
    private val progressIcon: ImageView

    /**
     * Constructor
     */
    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        handler = ProgressHandler(this)

        // inflate
        val inflater = checkNotNull(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val wrappedView = inflater.inflate(R.layout.progress, this) as ViewGroup
        progressIcon = wrappedView.findViewById(R.id.progressIcon)
        statusView = wrappedView.findViewById(R.id.progressStatus)
        progressBar = wrappedView.findViewById(R.id.progressBar)
        statusView.text = ""
        progressBar.max = 100
        progressBar.visibility = INVISIBLE
        progressBar.isIndeterminate = false
    }

    override fun put(str: String, fail: Boolean) {
        val message = handler.obtainMessage()
        val bundle = Bundle()
        bundle.putString("text", str)
        bundle.putBoolean("fail", fail)
        message.data = bundle
        handler.sendMessage(message)
    }
}
