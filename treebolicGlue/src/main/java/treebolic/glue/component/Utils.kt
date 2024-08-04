/*
 * Copyright (c) 2019-2023. Bernard Bou
 */
package treebolic.glue.component

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Utilities
 *
 * @author Bernard Bou
 */
object Utils {

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
        // try (final TypedArray array = context.obtainStyledAttributes(typedValue.data, attrs))
        var array: TypedArray? = null
        try {
            array = context.obtainStyledAttributes(typedValue.data, attrs)
            val colors = IntArray(attrs.size)
            for (i in attrs.indices) {
                colors[i] = array.getColor(i, 0)
            }
            return colors
        } finally {
            array?.recycle()
        }
    }

    /*
	 * Get color from theme
	 *
	 * @param context      context
	 * @param styleId      style id (ex: R.style.MyTheme)
	 * @param colorAttrIds attr ids (ex: R.attr.editTextColor)
	 * @return colors
	 */
    /*
	@SuppressWarnings("WeakerAccess")
	@NonNull
	static public int[] fetchColorsFromStyle(@NonNull final Context context, @NonNull int styleId, @NonNull int... colorAttrIds)
	{
		// try (final TypedArray array = context.obtainStyledAttributes(styleId, colorAttrIds))
		TypedArray array = null;
		try
		{
			array = context.obtainStyledAttributes(styleId, colorAttrIds);
			final int[] colors = new int[colorAttrIds.length];
			for (int i = 0; i < colorAttrIds.length; i++)
			{
				colors[i] = array.getColor(i, 0);
			}
			return colors;
		}
		finally
		{
			if (array != null)
			{
				array.recycle();
			}
		}
	}
	*/
    /**
     * Fetch colors resources
     *
     * @param context context
     * @param attrs   attributes
     * @return array of Integer resources, with null value if not found
     */
    fun fetchColorsNullable(context: Context, vararg attrs: Int): Array<Int?> {
        val typedValue = TypedValue()
        // try (final TypedArray array = context.obtainStyledAttributes(typedValue.data, attrs))
        var array: TypedArray? = null
        try {
            array = context.obtainStyledAttributes(typedValue.data, attrs)
            val colors = arrayOfNulls<Int>(attrs.size)
            for (i in attrs.indices) {
                colors[i] = if (array.hasValue(i)) array.getColor(i, 0) else null
            }
            return colors
        } finally {
            array?.recycle()
        }
    }

    /*
	static public int fetchColor(final Context context, int attr)
	{
		final TypedValue typedValue = new TypedValue();
		final Resources.Theme theme = context.getTheme();
		theme.resolveAttribute(attr, typedValue, true);
		return typedValue.data;
	}
	*/
    /*
	static public Integer fetchColorNullable(final Context context, int attr)
	{
		final TypedValue typedValue = new TypedValue();
		final Resources.Theme theme = context.getTheme();
		theme.resolveAttribute(attr, typedValue, true);
		return typedValue.type == TypedValue.TYPE_NULL ? null : typedValue.data;
	}
	*/
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
            // int height = size.y;
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