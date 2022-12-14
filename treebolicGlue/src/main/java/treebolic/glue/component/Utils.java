/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.component;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Utilities
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class Utils
{
	/**
	 * Fetch colors resources
	 *
	 * @param context context
	 * @param attrs   attributes
	 * @return array of int resources, with 0 value if not found
	 */
	@NonNull
	static public int[] fetchColors(@NonNull final Context context, @NonNull int... attrs)
	{
		final TypedValue typedValue = new TypedValue();
		try (final TypedArray array = context.obtainStyledAttributes(typedValue.data, attrs))
		{
			final int[] colors = new int[attrs.length];
			for (int i = 0; i < attrs.length; i++)
			{
				colors[i] = array.getColor(i, 0);
			}
			return colors;
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
		try (final TypedArray array = context.obtainStyledAttributes(styleId, colorAttrIds))
		{
			final int[] colors = new int[colorAttrIds.length];
			for (int i = 0; i < colorAttrIds.length; i++)
			{
				colors[i] = array.getColor(i, 0);
			}
			return colors;
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
	@NonNull
	static public Integer[] fetchColorsNullable(@NonNull final Context context, @NonNull @SuppressWarnings("SameParameterValue") int... attrs)
	{
		final TypedValue typedValue = new TypedValue();
		try (final TypedArray array = context.obtainStyledAttributes(typedValue.data, attrs))
		{
			final Integer[] colors = new Integer[attrs.length];
			for (int i = 0; i < attrs.length; i++)
			{
				colors[i] = array.hasValue(i) ? array.getColor(i, 0) : null;
			}
			return colors;
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
	static public int getColor(@NonNull final Context context, @ColorRes @SuppressWarnings("SameParameterValue") int resId)
	{
		return ContextCompat.getColor(context, resId);
	}

	/**
	 * Get drawable
	 *
	 * @param context context
	 * @param resId   drawable id
	 * @return drawable
	 */
	static public Drawable getDrawable(@NonNull final Context context, @DrawableRes int resId)
	{
		return ResourcesCompat.getDrawable(context.getResources(), resId, context.getTheme());
	}

	/**
	 * Get drawables
	 *
	 * @param context context
	 * @param resIds  drawable ids
	 * @return drawables
	 */
	@NonNull
	static public Drawable[] getDrawables(@NonNull final Context context, @NonNull @SuppressWarnings("SameParameterValue") int... resIds)
	{
		final Resources resources = context.getResources();
		final Resources.Theme theme = context.getTheme();
		Drawable[] drawables = new Drawable[resIds.length];
		for (int i = 0; i < resIds.length; i++)
		{
			drawables[i] = ResourcesCompat.getDrawable(resources, resIds[i], theme);
		}
		return drawables;
	}

	/**
	 * Tint drawable
	 *
	 * @param drawable drawable
	 * @param tint     tint
	 */
	static public void tint(@NonNull final Drawable drawable, @ColorInt int tint)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			drawable.setTint(tint);
		}
		else
		{
			DrawableCompat.setTint(DrawableCompat.wrap(drawable), tint);
		}
	}

	/**
	 * Screen width
	 *
	 * @param context context
	 * @return screen width
	 */
	static public int screenWidth(@NonNull final Context context)
	{
		final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		assert wm != null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
		{
			final Rect bounds = wm.getCurrentWindowMetrics().getBounds();
			return bounds.width();
		}
		else
		{
			final Display display = wm.getDefaultDisplay();
			final Point size = new Point();
			display.getSize(size);
			// int height = size.y;
			return size.x;
		}
	}

	/**
	 * Screen size
	 *
	 * @param context context
	 * @return a point whose x represents width and y represents height
	 */
	static public Point screenSize(@NonNull final Context context)
	{
		final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		assert wm != null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
		{
			final Rect bounds = wm.getCurrentWindowMetrics().getBounds();
			return new Point(bounds.width(), bounds.height());
		}
		else
		{
			final Display display = wm.getDefaultDisplay();
			final Point size = new Point();
			display.getSize(size);
			return size;
		}
	}

	/**
	 * Join character sequences
	 *
	 * @param delim delimiter
	 * @param strs  input character sequences
	 * @return string output
	 */
	public static String join(@NonNull final CharSequence delim, @Nullable final CharSequence[] strs)
	{
		if (strs == null)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (CharSequence str : strs)
		{
			if (str == null || str.length() == 0)
			{
				continue;
			}
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(delim);
			}
			sb.append(str);
		}
		return sb.toString();
	}
}