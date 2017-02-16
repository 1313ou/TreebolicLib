package treebolic.glue.component;

import android.annotation.TargetApi;
import android.app.Activity;
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
public class Toolbar extends FrameLayout implements treebolic.glue.iface.component.Toolbar<ActionListener>
{
	/**
	 * Image index enum
	 */
	static public enum ImageIndices
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
	static public String[] tooltips = { "Reset", // //$NON-NLS-1$
			"Radial", "North", "South", "East", "West", // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"Expand", "Shrink", "Widen", "Narrow", // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"Zoom In", "Zoom Out", "Zoom reset", // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"Scale Up", "Scale Down", "Scale reset", // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"Arc/Line", "Tooltip toggle", "Tooltip displays content", "Hovering triggers focus", }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	/**
	 * Activity
	 */
	private final Activity activity;

	/**
	 * Panel of buttons
	 */
	private final LinearLayout panel;

	/**
	 * Lay out parameters
	 */
	private final LinearLayout.LayoutParams layoutParams;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param activity0
	 *            activity
	 */
	@TargetApi(Build.VERSION_CODES.M)
	@SuppressWarnings("deprecation")
	protected Toolbar(final Activity activity0)
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

		// background
		final Resources resources = getResources();
		final int color = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? resources.getColor(R.color.background, null) : resources.getColor(R.color.background);
		this.panel.setBackgroundColor(color);

		// scroll
		final FrameLayout scroll = isHorizontal ? new ScrollView(activity0) : new HorizontalScrollView(activity0);
		scroll.addView(this.panel);
		scroll.setBackgroundColor(color);

		// top
		this.addView(scroll);
		setBackgroundColor(color);

		// layout parameters for later addition
		this.layoutParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		this.layoutParams.setMargins(isHorizontal ? 5 : 0, isHorizontal ? 0 : 5, 5, 5);
	}

	/**
	 * Constructor from handle
	 *
	 * @param handle
	 *            activity
	 */
	protected Toolbar(final Object handle)
	{
		this((Activity) handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.glue.iface.component.Toolbar#addButton(int, java.lang.String, treebolic.glue.ActionListener)
	 */
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void addButton(final int thisIconIndex, final String thisToolTip, final ActionListener thisListener)
	{
		final ImageButton btn = new ImageButton(getContext());
		btn.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		// drawable
		final Drawable bitmapDrawable = getDrawable(thisIconIndex);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.glue.iface.component.Toolbar#addToggle(int, int, java.lang.String, boolean, treebolic.glue.ActionListener)
	 */
	@Override
	public void addToggle(final int thisIconIndex, final int thisSelectedIconIndex, final String thisToolTip, final boolean thisState,
			final ActionListener thisListener)
	{
		//
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see treebolic.glue.iface.component.Toolbar#addSeparator()
	 */
	@Override
	public void addSeparator()
	{
		//
	}

	/**
	 * Get drawable from index
	 *
	 * @param index
	 *            index
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
