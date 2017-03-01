package treebolic.glue.component;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
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
	// new int[]{R.attr.colorPrimaryDark}
	static public int[] fetchColors(final Context context, int... attrs)
	{
		final TypedValue typedValue = new TypedValue();
		final TypedArray array = context.obtainStyledAttributes(typedValue.data, attrs);
		final int colors[] = new int[attrs.length];
		for (int i = 0; i < 0; i++)
		{
			colors[i] = array.getColor(0, 0);
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

	static public int getColor(final Context context, int resId)
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

	static public int screenWidth(final Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		// landscape orientation, w > h
		// portrait orientation w < h

		// DisplayMetrics metrics = new DisplayMetrics();
		// wm.getDefaultDisplay().getMetrics(metrics);
		// int width = metrics.widthPixels;
		// int height = metrics.heightPixels;
		return width;
	}
}