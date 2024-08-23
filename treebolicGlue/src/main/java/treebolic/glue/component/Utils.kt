/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.webkit.WebResourceResponse
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.use
import androidx.core.graphics.drawable.DrawableCompat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.jar.JarFile

/**
 * Utilities
 *
 * @author Bernard Bou
 */
object Utils {

    /**
     * Handle Jar file path URL
     *
     * @param url url
     * @return WebResourceResponse or null
     */
    fun handleJarFilePath(url: String): WebResourceResponse? {
        if (url.startsWith("jar:file://")) {

            // 1. Intercept the JAR URL
            val (jarFilePath, imagePathWithinJar) = extractJarFilePathAndImagePathWithinJar(url)

            // 2. Extract the Image from the JAR (Implementation not shown, requires JAR file handling)
            val imageBytes = extractImageFromJar(jarFilePath, imagePathWithinJar)
            if (imageBytes != null) {

                // 3. Convert Image to Data URI
                //val base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                //val dataUri = "data:image/png;base64,$base64String" // Assuming PNG format

                // Return a WebResourceResponse with the data URI
                return WebResourceResponse("image/png", "base64", ByteArrayInputStream(imageBytes))
            }
        }
        return null
    }

    /**
     * Extract JAR file path and image path within JAR
     *
     * @param url url
     * @return pair of strings
     */
    private fun extractJarFilePathAndImagePathWithinJar(url: String): Pair<String, String> {
        val fields = url.split('!')
        val jarFilePath = fields[0].substring(11) // strip 'jar:file://'
        val imagePathWithinJar = fields[1].substring(1) // strip '/'
        return jarFilePath to imagePathWithinJar
    }

    /**
     * Extract the image from the JAR as a byte array
     */
    private fun extractImageFromJar(jarFilePath: String, imagePathWithinJar: String): ByteArray? {
        try {
            JarFile(jarFilePath).use { jarFile ->
                val entry = jarFile.getJarEntry(imagePathWithinJar)
                entry?.let {
                    jarFile.getInputStream(entry).use { inputStream ->
                        ByteArrayOutputStream().use { byteArrayOutputStream ->
                            inputStream.copyTo(byteArrayOutputStream)
                            return byteArrayOutputStream.toByteArray()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("WebView", "Error extracting image from JAR: ${e.message}")
        }
        return null
    }

    /**
     * Fetch colors resources
     *
     * @param context context
     * @param attrs   attributes
     * @return array of int resources, with 0 value if not found
     */
    @JvmStatic
    fun fetchColors(context: Context, vararg attrs: Int): IntArray {
        val typedValue = TypedValue()
        context.obtainStyledAttributes(typedValue.data, attrs).use {
            val colors = IntArray(attrs.size)
            for (i in attrs.indices) {
                colors[i] = it.getColor(i, 0)
            }
            return colors
        }
    }

    /**
     * Fetch colors resources
     *
     * @param context context
     * @param attrs   attributes
     * @return array of Integer resources, with null value if not found
     */
    fun fetchColorsNullable(context: Context, vararg attrs: Int): Array<Int?> {
        val typedValue = TypedValue()
        context.obtainStyledAttributes(typedValue.data, attrs).use {
            val colors = arrayOfNulls<Int>(attrs.size)
            for (i in attrs.indices) {
                colors[i] = if (it.hasValue(i)) it.getColor(i, 0) else null
            }
            return colors
        }
    }

    /**
     * Get color
     *
     * @param context context
     * @param resId   resource id
     * @return color int
     */
    @JvmStatic
    fun getColor(context: Context, @ColorRes resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    /**
     * Get drawable
     *
     * @param context context
     * @param resId   drawable id
     * @return drawable
     */
    @JvmStatic
    fun getDrawable(context: Context, @DrawableRes resId: Int): Drawable? {
        return ResourcesCompat.getDrawable(context.resources, resId, context.theme)
    }

    /**
     * Get drawables
     *
     * @param context context
     * @param resIds  drawable ids
     * @return drawables
     */
    @JvmStatic
    fun getDrawables(context: Context, vararg resIds: Int): Array<Drawable?> {
        val resources = context.resources
        val theme = context.theme
        val drawables = arrayOfNulls<Drawable>(resIds.size)
        for (i in resIds.indices) {
            drawables[i] = ResourcesCompat.getDrawable(resources, resIds[i], theme)
        }
        return drawables
    }

    /**
     * Tint drawable
     *
     * @param drawable drawable
     * @param tint     tint
     */
    @JvmStatic
    fun tint(drawable: Drawable, @ColorInt tint: Int) {
        DrawableCompat.setTint(DrawableCompat.wrap(drawable), tint)
    }

    /**
     * Screen width
     *
     * @param context context
     * @return screen width
     */
    @JvmStatic
    @Suppress("deprecation")
    fun screenWidth(context: Context): Int {
        val wm = checkNotNull(context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = wm.currentWindowMetrics.bounds
            return bounds.width()
        } else {
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x
        }
    }

    /**
     * Screen size
     *
     * @param context context
     * @return a point whose x represents width and y represents height
     */
    @JvmStatic
    @Suppress("DEPRECATION")
    fun screenSize(context: Context): Point {
        val wm = checkNotNull(context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = wm.currentWindowMetrics.bounds
            return Point(bounds.width(), bounds.height())
        } else {
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size
        }
    }
}