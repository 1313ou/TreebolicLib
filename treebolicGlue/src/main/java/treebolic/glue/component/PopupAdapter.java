/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package treebolic.glue.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

/**
 * Custom popup window.
 *
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 */
@SuppressWarnings({"EmptyMethod", "WeakerAccess"})
public class PopupAdapter
{
	/**
	 * Context
	 */
	@NonNull
	@SuppressWarnings("WeakerAccess")
	protected final Context context;

	/**
	 * Popup window
	 */
	@NonNull
	@SuppressWarnings("WeakerAccess")
	protected final PopupWindow window;

	/**
	 * Wrapped view
	 */
	@SuppressWarnings("WeakerAccess")
	protected View view;

	/**
	 * Constructor.
	 *
	 * @param context context
	 */
	public PopupAdapter(@NonNull final Context context)
	{
		this.context = context;
		this.window = new PopupWindow(context);

		// dismiss when touched outside
		this.window.setTouchInterceptor((view0, event) -> {
			switch (event.getAction())
			{
				case MotionEvent.ACTION_OUTSIDE:
					PopupAdapter.this.window.dismiss();
					return true;
				case MotionEvent.ACTION_UP:
					view0.performClick();
					return false;
				default:
					break;
			}
			return false;
		});
	}

	/**
	 * On pre-show
	 */
	@SuppressWarnings({"WeakerAccess"})
	protected void preShow()
	{
		if (this.view == null)
		{
			throw new IllegalStateException("setContentView was not called with a view to display.");
		}

		// hook
		onShow();

		// color
		int color = Utils.getColor(this.context, android.R.color.transparent);
		this.window.setBackgroundDrawable(new ColorDrawable(color));

		// setup window
		this.window.setWidth(LayoutParams.WRAP_CONTENT);
		this.window.setHeight(LayoutParams.WRAP_CONTENT);
		this.window.setTouchable(true);
		this.window.setOutsideTouchable(true);
		this.window.setFocusable(true);
		this.window.setContentView(this.view);
	}

	/**
	 * Set content view.
	 *
	 * @param root Root view
	 */
	@SuppressWarnings("WeakerAccess")
	public void setContentView(final View root)
	{
		this.view = root;
		this.window.setContentView(root);
	}

	/**
	 * Set content view.
	 *
	 * @param layoutResID Resource id
	 */
	public void setContentView(final int layoutResID)
	{
		final LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		assert inflater != null;
		setContentView(inflater.inflate(layoutResID, null));
	}

	/**
	 * Set listener on window dismissed.
	 *
	 * @param listener listener
	 */
	@SuppressWarnings("WeakerAccess")
	public void setOnDismissListener(final PopupWindow.OnDismissListener listener)
	{
		this.window.setOnDismissListener(listener);
	}

	/**
	 * Dismiss the popup window.
	 */
	@SuppressWarnings("WeakerAccess")
	public void dismiss()
	{
		this.window.dismiss();
	}

	/**
	 * On dismiss
	 */
	protected void onDismiss()
	{
		//
	}

	/**
	 * On show
	 */
	@SuppressWarnings("WeakerAccess")
	protected void onShow()
	{
		//
	}
}