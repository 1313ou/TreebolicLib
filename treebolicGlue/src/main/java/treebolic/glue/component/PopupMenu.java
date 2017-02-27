/**
 * Title: Hyperbolic engine
 * Description: Treebolic Engine
 * Version: provider
 * Copyright: (c) 2001-2008
 * Terms of use:see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author: Bernard Bou
 * Update: Mon Mar 10 00:00:00 CEST 2008
 */
package treebolic.glue.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import org.treebolic.glue.R;

import treebolic.glue.ActionListener;
import treebolic.glue.component.QuickAction.ActionItem;

/**
 * Popup context menu
 *
 * @author Bernard Bou
 */
public class PopupMenu implements treebolic.glue.iface.component.PopupMenu<Component, ActionListener>
{
	/**
	 * Image index enum
	 */
	public enum ImageIndices
	{
		IMAGE_CANCEL, IMAGE_INFO, IMAGE_FOCUS, IMAGE_LINK, IMAGE_MOUNT, IMAGE_GOTO, IMAGE_SEARCH, COUNT
	}

	/**
	 * Drawables
	 */
	static final Drawable[] drawables = new Drawable[ImageIndices.COUNT.ordinal()];

	/**
	 * Labels
	 */
	static public String[] labels = null; //{  "Cancel", "Info", "Focus", "Link", "Mount", "UnMount", "Goto", "Search" };

	/**
	 * Context
	 */
	final Context context;

	/**
	 * Anchor view
	 */
	final View anchor;

	/**
	 * Quick action component
	 */
	final QuickAction quickAction;

	/**
	 * Constructor
	 */
	protected PopupMenu(final Context context0, final View anchor0)
	{
		this.context = context0;
		this.anchor = anchor0;

		// labels: info,focus,linkto,mount,unmount,cancel
		labels = this.context.getResources().getStringArray(R.array.popup_labels);

		// create quickaction
		this.quickAction = new QuickAction(context0, QuickAction.VERTICAL);

		// set listener for on dismiss event
		// this listener will be called only if quickaction dialog was dismissed by clicking the area outside the dialog.
		this.quickAction.setOnDismissListener(new QuickAction.OnDismissListener()
		{
			@Override
			public void onDismiss()
			{
				//
			}
		});
	}

	/**
	 * Constructor
	 *
	 * @param handle
	 *            anchor view
	 */
	protected PopupMenu(final Object handle)
	{
		this(((View) handle).getContext(), (View) handle);
	}

	@Override
	public void addItem(final String label, final int resource, final ActionListener listener)
	{
		// just click outside dialog
		if (resource == ImageIndices.IMAGE_CANCEL.ordinal())
			return;

		final ActionItem item = new ActionItem(label, getDrawable(resource), false, listener);
		this.quickAction.addActionItem(item);
	}

	@Override
	public void popup(final Component component, final int x, final int y)
	{
		this.quickAction.show(this.anchor, x, y);
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
		if (PopupMenu.drawables[index] == null)
		{
			int res = -1;
			switch (ImageIndices.values()[index])
			{
			case IMAGE_CANCEL:
				res = R.drawable.menu_cancel;
				break;
			case IMAGE_GOTO:
				res = R.drawable.menu_goto;
				break;
			case IMAGE_SEARCH:
				res = R.drawable.menu_search;
				break;
			case IMAGE_FOCUS:
				res = R.drawable.menu_focus;
				break;
			case IMAGE_LINK:
				res = R.drawable.menu_link;
				break;
			case IMAGE_MOUNT:
				res = R.drawable.menu_mount;
				break;
			case IMAGE_INFO:
				res = R.drawable.menu_info;
				break;
			default:
				break;
			}
			if (res != -1)
			{
				PopupMenu.drawables[index] = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP //
						? this.context.getResources().getDrawable(res, null) : this.context.getResources().getDrawable(res);
			}
		}
		return PopupMenu.drawables[index];
	}
}
