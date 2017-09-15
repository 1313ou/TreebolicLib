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

/**
 * Utilities
 *
 * @author Bernard Bou
 */
public class Utils
{
	static public int[] fetchColors(final Context context, int... attrs)
	{
		final TypedValue typedValue = new TypedValue();
		final TypedArray array = context.obtainStyledAttributes(typedValue.data, attrs);
		final int colors[] = new int[attrs.length];
		for (int i = 0; i < attrs.length; i++)
		{
			colors[i] = array.getColor(i, 0);
		}
		array.recycle();
		return colors;
	}

	static public Integer[] fetchColorsNullable(final Context context, int... attrs)
	{
		final TypedValue typedValue = new TypedValue();
		final TypedArray array = context.obtainStyledAttributes(typedValue.data, attrs);
		final Integer colors[] = new Integer[attrs.length];
		for (int i = 0; i < attrs.length; i++)
		{
			colors[i] = array.hasValue(i) ? array.getColor(i, 0) : null;
		}
		array.recycle();
		return colors;
	}

	static public int fetchColor(final Context context, int attr)
	{
		final TypedValue typedValue = new TypedValue();
		final Resources.Theme theme = context.getTheme();
		theme.resolveAttribute(attr, typedValue, true);
		return typedValue.data;
	}

	static public Integer fetchColorNullable(final Context context, int attr)
	{
		final TypedValue typedValue = new TypedValue();
		final Resources.Theme theme = context.getTheme();
		theme.resolveAttribute(attr, typedValue, true);
		return typedValue.type == TypedValue.TYPE_NULL ? null : typedValue.data;
	}

	static public int getColor(final Context context, int resId)
	{
		final Resources resources = context.getResources();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			return resources.getColor(resId, null);
		}
		else
		{
			//noinspection deprecation
			return resources.getColor(resId);
		}
	}

	static public void tint(final Drawable drawable, int iconTint)
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

	static public int screenWidth(final Context context)
	{
		final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		final Display display = wm.getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		// int height = size.y;
		return size.x;
	}
}