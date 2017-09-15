package treebolic.glue.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.treebolic.glue.R;

import treebolic.glue.ActionListener;

/**
 * Tool bar
 *
 * @author Bernard Bou
 */
@SuppressLint("ViewConstructor")
public class Toolbar extends FrameLayout implements treebolic.glue.iface.component.Toolbar<ActionListener>
{
	/**
	 * (Ordered) toolbar
	 *
	 * @return list of buttons
	 */
	static public Button[] toolbar()
	{
		return new Button[]{Button.HOME, //
				Button.ZOOMIN, Button.ZOOMOUT, Button.ZOOMONE, //
				Button.SCALEUP, Button.SCALEDOWN, Button.SCALEONE, //
				Button.RADIAL, Button.SOUTH, Button.NORTH, Button.EAST, Button.WEST, //
				Button.EXPAND, Button.SHRINK, Button.EXPANSIONRESET, //
				Button.WIDEN, Button.NARROW, Button.SWEEPRESET //
		};
	}

	/**
	 * Buttons
	 */
	private enum ButtonImplementation
	{
		HOME, //
		RADIAL, NORTH, SOUTH, EAST, WEST, //
		EXPAND, SHRINK, EXPANSIONRESET, //
		WIDEN, NARROW, SWEEPRESET, //
		ZOOMIN, ZOOMOUT, ZOOMONE, //
		SCALEUP, SCALEDOWN, SCALEONE //
		;

		public int getIconIndex()
		{
			return ordinal();
		}
	}

	/**
	 * Drawables
	 */
	static private final int[] drawableIds = new int[]{R.drawable.toolbar_home, //
			R.drawable.toolbar_radial, R.drawable.toolbar_north, R.drawable.toolbar_south, R.drawable.toolbar_east, R.drawable.toolbar_west, //
			R.drawable.toolbar_expand, R.drawable.toolbar_shrink, R.drawable.toolbar_expand_reset,
			R.drawable.toolbar_widen, R.drawable.toolbar_narrow, R.drawable.toolbar_widen_reset, //
			R.drawable.toolbar_zoomin, R.drawable.toolbar_zoomout, R.drawable.toolbar_zoomone, //
			R.drawable.toolbar_scaleup, R.drawable.toolbar_scaledown, R.drawable.toolbar_scaleone};

	private final Drawable[] drawables;

	/**
	 * Panel of buttons
	 */
	private final LinearLayout panel;

	/**
	 * Lay out parameters
	 */
	private final LinearLayout.LayoutParams layoutParams;

	/**
	 * Tint
	 */
	private final int iconTint;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param activity activity
	 */
	@TargetApi(Build.VERSION_CODES.M)
	@SuppressWarnings("deprecation")
	protected Toolbar(final AppCompatActivity activity)
	{
		super(activity);

		// orientation
		final WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		final Display display = windowManager.getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		final boolean isHorizontal = size.x >= size.y;

		// panel
		this.panel = new LinearLayout(activity);
		this.panel.setOrientation(isHorizontal ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);

		// focus
		this.panel.setFocusable(false);

		// gravity
		this.panel.setGravity(Gravity.CENTER);

		// colors
		final int[] colors = Utils.fetchColors(activity, R.attr.treebolic_toolbar_background, R.attr.treebolic_toolbar_icon_color);
		final int background = colors[0];
		this.iconTint = colors[1];

		// drawables
		final Resources resources = activity.getResources();
		this.drawables = new Drawable[drawableIds.length];
		for (int i = 0; i < Toolbar.drawableIds.length; i++)
		{
			this.drawables[i] = Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP ? //
					resources.getDrawable(drawableIds[i], null) : resources.getDrawable(drawableIds[i]);
		}

		// background
		this.panel.setBackgroundColor(background);

		// scroll
		final FrameLayout scroll = isHorizontal ? new ScrollView(activity) : new HorizontalScrollView(activity);
		scroll.addView(this.panel);
		scroll.setBackgroundColor(background);

		// top
		this.addView(scroll);
		setBackgroundColor(background);

		// layout parameters for later addition
		this.layoutParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		this.layoutParams.setMargins(isHorizontal ? 5 : 0, isHorizontal ? 0 : 5, 5, 5);
	}

	/**
	 * Constructor from handle
	 *
	 * @param handle activity
	 */
	protected Toolbar(final Object handle)
	{
		//noinspection ConstantConditions
		this((AppCompatActivity) handle);
	}

	// A D D  B U T T O N

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void addButton(final Button button, final ActionListener thisListener)
	{
		// interface button to implementation
		final String name = button.name();
		final ButtonImplementation impl = ButtonImplementation.valueOf(name);

		// new button
		final ImageButton imageButton = new ImageButton(getContext());
		imageButton.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		// drawable
		final int thisIconIndex = impl.getIconIndex();
		final Drawable bitmapDrawable = this.drawables[thisIconIndex];

		// tint drawable
		Utils.tint(bitmapDrawable, this.iconTint);

		// button drawable
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			imageButton.setBackground(bitmapDrawable);
		}
		else
		{
			imageButton.setBackgroundDrawable(bitmapDrawable);
		}

		// listener
		imageButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				thisListener.onAction();
			}
		});

		// add
		this.panel.addView(imageButton, this.layoutParams);
	}
}
