package treebolic.glue.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
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
	 * Image index enum
	 */
	public enum ImageIndices
	{
		HOME, NORTH, SOUTH, EAST, WEST, RADIAL, EXPAND, SHRINK, WIDEN, NARROW, ZOOMIN, ZOOMOUT, ZOOMONE, SCALEUP, SCALEDOWN, SCALEONE, //
		ARC, NO_ARC, NODETOOLTIP, NO_NODETOOLTIP, NODETOOLTIPCONTENT, NO_NODETOOLTIPCONTENT, HOVERFOCUS, NO_HOVERFOCUS
	}

	/**
	 * Drawables
	 */
	static private final Drawable[] drawables = new Drawable[24];

	/**
	 * Toolbar tooltips (unused, but do not erase)
	 */
	static public final String[] tooltips = {"Reset", //
			"Radial", "North", "South", "East", "West", //
			"Expand", "Shrink", "Widen", "Narrow", //
			"Zoom In", "Zoom Out", "Zoom reset", //
			"Scale Up", "Scale Down", "Scale reset", //
			"Arc/Line", "Tooltip toggle", "Tooltip displays content", "Hovering triggers focus",};

	/**
	 * Activity
	 */
	private final AppCompatActivity activity;

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
	 * @param activity0 activity
	 */
	@TargetApi(Build.VERSION_CODES.M)
	@SuppressWarnings("deprecation")
	protected Toolbar(final AppCompatActivity activity0)
	{
		super(activity0);
		this.activity = activity0;

		// orientation
		final WindowManager windowManager = (WindowManager) this.activity.getSystemService(Context.WINDOW_SERVICE);
		final Display display = windowManager.getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		final boolean isHorizontal = size.x >= size.y;

		// panel
		this.panel = new LinearLayout(activity0);
		this.panel.setOrientation(isHorizontal ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);

		// focus
		this.panel.setFocusable(false);

		// gravity
		this.panel.setGravity(Gravity.CENTER);

		// colors
		int[] colors = Utils.fetchColors(this.activity, R.attr.treebolic_toolbar_background, R.attr.treebolic_toolbar_icon_color);
		int background = colors[0];
		this.iconTint = colors[1];

		// background
		this.panel.setBackgroundColor(background);

		// scroll
		final FrameLayout scroll = isHorizontal ? new ScrollView(activity0) : new HorizontalScrollView(activity0);
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
		this((AppCompatActivity) handle);
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void addButton(final int thisIconIndex, final String thisToolTip, final ActionListener thisListener)
	{
		final ImageButton btn = new ImageButton(getContext());
		btn.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		// drawable
		final Drawable bitmapDrawable = getDrawable(thisIconIndex);

		// iconTint drawable
		DrawableCompat.setTint(bitmapDrawable, this.iconTint);

		// add
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			btn.setBackground(bitmapDrawable);
		}
		else
		{
			btn.setBackgroundDrawable(bitmapDrawable);
		}

		// listener
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				thisListener.onAction();
			}
		});

		// add
		this.panel.addView(btn, this.layoutParams);
	}

	@Override
	public void addToggle(final int thisIconIndex, final int thisSelectedIconIndex, final String thisToolTip, final boolean thisState, final ActionListener thisListener)
	{
		//
	}

	@Override
	public void addSeparator()
	{
		//
	}

	/**
	 * Get drawable from index
	 *
	 * @param index index
	 * @return drawable
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@SuppressWarnings("deprecation")
	private Drawable getDrawable(final int index)
	{
		if (Toolbar.drawables[index] == null)
		{
			int res = -1;
			switch (ImageIndices.values()[index])
			{
				case HOME:
					res = R.drawable.toolbar_home;
					break;
				case NORTH:
					res = R.drawable.toolbar_north;
					break;
				case SOUTH:
					res = R.drawable.toolbar_south;
					break;
				case EAST:
					res = R.drawable.toolbar_east;
					break;
				case WEST:
					res = R.drawable.toolbar_west;
					break;
				case RADIAL:
					res = R.drawable.toolbar_radial;
					break;
				case EXPAND:
					res = R.drawable.toolbar_expand;
					break;
				case SHRINK:
					res = R.drawable.toolbar_shrink;
					break;
				case WIDEN:
					res = R.drawable.toolbar_widen;
					break;
				case NARROW:
					res = R.drawable.toolbar_narrow;
					break;
				case ZOOMIN:
					res = R.drawable.toolbar_zoomin;
					break;
				case ZOOMOUT:
					res = R.drawable.toolbar_zoomout;
					break;
				case ZOOMONE:
					res = R.drawable.toolbar_zoomone;
					break;

				case SCALEDOWN:
					res = R.drawable.toolbar_scaledown;
					break;
				case SCALEUP:
					res = R.drawable.toolbar_scaleup;
					break;
				case SCALEONE:
					res = R.drawable.toolbar_scaleone;
					break;

				// case ARC:
				// res = R.drawable.toolbar_arc;
				// break;
				// case NO_ARC:
				// res = R.drawable.toolbar_no_arc;
				// break;
				// case HOVERFOCUS:
				// res = R.drawable.toolbar_hoverfocus;
				// break;
				// case NO_HOVERFOCUS:
				// res = R.drawable.toolbar_no_hoverfocus;
				// break;
				// case NODETOOLTIP:
				// res = R.drawable.toolbar_nodetooltip;
				// break;
				// case NO_NODETOOLTIP:
				// res = R.drawable.toolbar_no_nodetooltip;
				// break;
				// case NODETOOLTIPCONTENT:
				// res = R.drawable.toolbar_tooltipcontent;
				// break;
				// case NO_NODETOOLTIPCONTENT:
				// res = R.drawable.toolbar_no_tooltipcontent;
				// break;

				default:
					break;
			}
			if (res != -1)
			{
				Toolbar.drawables[index] = Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP //
						? this.activity.getResources().getDrawable(res, null) : this.activity.getResources().getDrawable(res);
			}
		}
		return Toolbar.drawables[index];
	}
}
