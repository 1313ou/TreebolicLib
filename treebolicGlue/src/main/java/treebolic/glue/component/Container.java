/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.component;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.treebolic.glue.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Container
 * API class
 *
 * @author Bernard Bou
 */
public class Container extends LinearLayout implements Component, treebolic.glue.iface.component.Container<Component>
{
	static private float splitterPositionPercent = .80f;

	private final boolean isHorizontal;

	/**
	 * View
	 */
	@Nullable
	private View view;

	/**
	 * Toolbar
	 */
	@Nullable
	private View toolbar;

	/**
	 * Statusbar
	 */
	@Nullable
	private View statusbar;

	/**
	 * Constructor
	 *
	 * @param context context
	 */
	@SuppressWarnings("WeakerAccess")
	protected Container(@NonNull final Context context)
	{
		super(context);

		// determine orientation
		@NonNull final Point size = Utils.screenSize(context);
		this.isHorizontal = size.x >= size.y;
		setOrientation(this.isHorizontal ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);

		// other
		setGravity(Gravity.CENTER);
		setWeightSum(1F);
	}

	/**
	 * Constructor
	 * API
	 *
	 * @param handle context
	 */
	public Container(final Object handle)
	{
		this((Context) handle);
	}

	@SuppressWarnings("WeakerAccess")
	@Override
	public void addComponent(final Component component, final int position)
	{
		final View viewToAdd = (View) component;

		switch (position)
		{
			case PANE:
				@NonNull final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.F);
				addView(viewToAdd, params);
				break;

			// delayed add (until validation)
			case VIEW:
				this.view = viewToAdd;
				break;

			// delayed add (until validation)
			case TOOLBAR:
				this.toolbar = viewToAdd;
				break;

			// delayed add (until validation)
			case STATUSBAR:
				this.statusbar = viewToAdd;
				break;

			default:
				break;
		}
	}

	@SuppressWarnings("WeakerAccess")
	@Override
	public void removeAll()
	{
		removeAllViews();
		this.view = null;
		this.toolbar = null;
		this.statusbar = null;
	}

	@SuppressWarnings("WeakerAccess")
	@Override
	public void validate()
	{
		@Nullable @SuppressWarnings("UnusedAssignment") LayoutParams params = null;
		if (this.toolbar != null)
		{
			params = new LayoutParams(this.isHorizontal ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT, this.isHorizontal ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, 0.F);
			addView(this.toolbar, params);
		}
		@NonNull ViewGroup layout = this;
		if (this.view != null && this.statusbar != null)
		{
			@NonNull final SplitPaneLayout splitLayout = new SplitPaneLayout(getContext());
			splitLayout.setOrientation(this.isHorizontal ? 0 : 1);
			splitLayout.setSplitterPositionPercent(Container.splitterPositionPercent);
			splitLayout.setSplitterMovable(true);
			splitLayout.setSplitterDrawable(getSplitterDrawable(false));
			splitLayout.setSplitterDraggingDrawable(getSplitterDrawable(true));

			this.addView(splitLayout);
			layout = splitLayout;
		}
		if (this.view != null)
		{
			params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.F);
			layout.addView(this.view, params);
		}
		if (this.statusbar != null)
		{
			params = new LayoutParams(this.isHorizontal ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT, this.isHorizontal ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, 0.F);
			layout.addView(this.statusbar, params);
		}

		invalidate();
	}

	/**
	 * Get splitter position fraction (first panel/whole)
	 *
	 * @return splitterPositionPercent splitter position fraction [0,1]
	 */
	public static float getSplitterPositionPercent()
	{
		return Container.splitterPositionPercent;
	}

	/**
	 * Set splitter position fraction (first panel/whole)
	 *
	 * @param splitterPositionPercent splitter position fraction [0,1]
	 */
	public static void setSplitterPositionPercent(float splitterPositionPercent)
	{
		Container.splitterPositionPercent = splitterPositionPercent;
	}

	/**
	 * Get splitter drawable
	 *
	 * @param dragging dragging splitter
	 * @return drawable
	 */
	@NonNull
	private Drawable getSplitterDrawable(final boolean dragging)
	{
		@NonNull final int[] colors = Utils.fetchColors(getContext(), R.attr.treebolic_splitbar_color, R.attr.treebolic_splitbar_drag_color);
		return new ColorDrawable(colors[dragging ? 1 : 0]);
	}

	/*
	private Drawable getSplitterDrawable0(final boolean dragging)
	{
		final Resources res = getResources();
		try
		{
			return Drawable.createFromXml(res, res.getXml(dragging ? //
					this.isHorizontal ? //
							R.xml.splitter_bg_move_v : //
							R.xml.splitter_bg_move_h //
					: this.isHorizontal ? //
					R.xml.splitter_bg_v : //
					R.xml.splitter_bg_h));
		}
		catch (final Exception ex)
		{
			Log.e("TAG", "Exception loading drawable");
		}
		return null;
	}
	*/
}
