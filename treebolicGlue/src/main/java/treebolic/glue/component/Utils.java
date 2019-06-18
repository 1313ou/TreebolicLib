/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.component;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Utilities
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class Utils
{
	@NonNull
	static public int[] fetchColors(@NonNull final Context context, @NonNull int... attrs)
	{
		final TypedValue typedValue = new TypedValue();
		final TypedArray array = context.obtainStyledAttributes(typedValue.data, attrs);
		final int[] colors = new int[attrs.length];
		for (int i = 0; i < attrs.length; i++)
		{
			colors[i] = array.getColor(i, 0);
		}
		array.recycle();
		return colors;
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
	static public int[] fetchColorsFromStyle(@NonNull final Context context,  @NonNull int styleId,  @NonNull int... colorAttrIds)
	{
		final TypedArray array = context.obtainStyledAttributes(styleId, colorAttrIds);
		final int[] colors = new int[colorAttrIds.length];
		for (int i = 0; i < colorAttrIds.length; i++)
		{
			colors[i] = array.getColor(i, 0);
		}
		array.recycle();
		return colors;
	}
	*/

	@NonNull
	static public Integer[] fetchColorsNullable(@NonNull final Context context, @NonNull @SuppressWarnings("SameParameterValue") int... attrs)
	{
		final TypedValue typedValue = new TypedValue();
		final TypedArray array = context.obtainStyledAttributes(typedValue.data, attrs);
		final Integer[] colors = new Integer[attrs.length];
		for (int i = 0; i < attrs.length; i++)
		{
			colors[i] = array.hasValue(i) ? array.getColor(i, 0) : null;
		}
		array.recycle();
		return colors;
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
	@SuppressWarnings("deprecation")
	static public int getColor(@NonNull final Context context, @ColorRes @SuppressWarnings("SameParameterValue") int resId)
	{
		final Resources resources = context.getResources();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			return resources.getColor(resId, null);
		}
		else
		{
			return resources.getColor(resId);
		}
	}

	/**
	 * Get drawable
	 *
	 * @param context context
	 * @param resId   drawable id
	 * @return drawable
	 */
	@SuppressWarnings("deprecation")
	static public Drawable getDrawable(@NonNull final Context context, @DrawableRes int resId)
	{
		final Resources resources = context.getResources();
		Drawable drawable;
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
		{
			final Resources.Theme theme = context.getTheme();
			drawable = resources.getDrawable(resId, theme);
		}
		else
		{
			drawable = resources.getDrawable(resId);
		}
		return drawable;
	}

	/**
	 * Get drawables
	 *
	 * @param context context
	 * @param resIds  drawable ids
	 * @return drawables
	 */
	@SuppressWarnings("deprecation")
	@NonNull
	static public Drawable[] getDrawables(@NonNull final Context context, @NonNull @SuppressWarnings("SameParameterValue") int... resIds)
	{
		final Resources resources = context.getResources();
		Drawable[] drawables = new Drawable[resIds.length];
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
		{
			final Resources.Theme theme = context.getTheme();
			for (int i = 0; i < resIds.length; i++)
			{
				drawables[i] = resources.getDrawable(resIds[i], theme);
			}
		}
		else
		{
			for (int i = 0; i < resIds.length; i++)
			{
				drawables[i] = resources.getDrawable(resIds[i]);
			}
		}
		return drawables;
	}

	static public void tint(@NonNull final Drawable drawable, @ColorInt int iconTint)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			drawable.setTint(iconTint);
		}
		else
		{
			DrawableCompat.setTint(DrawableCompat.wrap(drawable), iconTint);
		}
	}

	static public int screenWidth(@NonNull final Context context)
	{
		final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		assert wm != null;
		final Display display = wm.getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		// int height = size.y;
		return size.x;
	}
}