package treebolic.glue.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.treebolic.glue.R;

/**
 * Container
 *
 * @author Bernard Bou
 */
public class Container extends LinearLayout implements Component, treebolic.glue.iface.component.Container<Component>
{
	static private final float VIEWSHARE = .80f;

	private final boolean isHorizontal;

	/**
	 * Constructor
	 *
	 * @param context
	 *            context
	 */
	protected Container(final Context context)
	{
		super(context);

		// determine orientation
		final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		final Display display = windowManager.getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		this.isHorizontal = size.x >= size.y;
		setOrientation(this.isHorizontal ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);

		// other
		setGravity(Gravity.CENTER);
		setWeightSum(1F);
	}

	/**
	 * Constructor
	 *
	 * @param handle
	 *            context
	 */
	protected Container(final Object handle)
	{
		this((Context) handle);
	}

	private View view;
	private View toolbar;
	private View statusbar;

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.component.Container#addComponent(treebolic.glue.component.Component, int)
	 */
	@Override
	public void addComponent(final Component component, final int position)
	{
		final View viewToAdd = (View) component;

		switch (position)
		{
		case PANE:
			final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
					1.F);
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

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.component.Container#removeAll()
	 */
	@Override
	public void removeAll()
	{
		removeAllViews();
		this.view = null;
		this.toolbar = null;
		this.statusbar = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.component.Container#validate()
	 */
	@Override
	public void validate()
	{
		LayoutParams params = null;
		if (this.toolbar != null)
		{
			params = new LayoutParams(this.isHorizontal ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT,
					this.isHorizontal ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, 0.F);
			addView(this.toolbar, params);
		}
		ViewGroup layout = this;
		if (this.view != null && this.statusbar != null)
		{
			final SplitPaneLayout splitLayout = new SplitPaneLayout(getContext());
			splitLayout.setOrientation(this.isHorizontal ? 0 : 1);
			splitLayout.setSplitterPositionPercent(Container.VIEWSHARE);
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
			params = new LayoutParams(this.isHorizontal ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT,
					this.isHorizontal ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT, 0.F);
			layout.addView(this.statusbar, params);
		}

		invalidate();
	}

	/**
	 * Get splitter drawable
	 *
	 * @param dragging
	 *            dragging splitter
	 * @return drawable
	 */
	private Drawable getSplitterDrawable(final boolean dragging)
	{
		final Resources res = getResources();
		try
		{
			return Drawable.createFromXml(res, res.getXml(dragging ? this.isHorizontal ? R.drawable.splitter_bg_move_v : R.drawable.splitter_bg_move_h
					: this.isHorizontal ? R.drawable.splitter_bg_v : R.drawable.splitter_bg_h));
		}
		catch (final Exception ex)
		{
			// Log.e(TAG, "Exception loading drawable");
		}
		return null;
	}
}
