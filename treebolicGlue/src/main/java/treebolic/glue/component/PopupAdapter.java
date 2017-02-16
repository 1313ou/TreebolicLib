package treebolic.glue.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Custom popup window.
 *
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 */
public class PopupAdapter
{
	/**
	 * Context
	 */
	protected Context context;

	/**
	 * Popup window
	 */
	protected PopupWindow window;

	/**
	 * Wrapped view
	 */
	protected View view;

	/**
	 * Window manager
	 */
	protected WindowManager windowManager;

	/**
	 * Constructor.
	 *
	 * @param context0
	 *            context
	 */
	public PopupAdapter(final Context context0)
	{
		this.context = context0;
		this.window = new PopupWindow(context0);

		// dismiss when touched outside
		this.window.setTouchInterceptor(new OnTouchListener()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
			 */
			@Override
			public boolean onTouch(final View view0, final MotionEvent event)
			{
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
			}
		});

		// get windows manager
		this.windowManager = (WindowManager) context0.getSystemService(Context.WINDOW_SERVICE);
	}

	/**
	 * On pre show
	 */
	@TargetApi(Build.VERSION_CODES.M)
	@SuppressWarnings("deprecation")
	protected void preShow()
	{
		if (this.view == null)
			throw new IllegalStateException("setContentView was not called with a view to display."); //$NON-NLS-1$

		// hook
		onShow();

		// color
		final Resources resources = this.context.getResources();
		int color = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? resources.getColor(android.R.color.transparent, null) : resources.getColor(android.R.color.transparent);
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
	 * @param root
	 *            Root view
	 */
	public void setContentView(final View root)
	{
		this.view = root;
		this.window.setContentView(root);
	}

	/**
	 * Set content view.
	 *
	 * @param layoutResID
	 *            Resource id
	 */
	public void setContentView(final int layoutResID)
	{
		final LayoutInflater inflator = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(inflator.inflate(layoutResID, null));
	}

	/**
	 * Set listener on window dismissed.
	 *
	 * @param listener
	 */
	public void setOnDismissListener(final PopupWindow.OnDismissListener listener)
	{
		this.window.setOnDismissListener(listener);
	}

	/**
	 * Dismiss the popup window.
	 */
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
	protected void onShow()
	{
		//
	}
}