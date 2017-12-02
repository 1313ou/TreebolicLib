package treebolic.glue.component;

import android.annotation.TargetApi;
import android.content.Context;
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
@SuppressWarnings({"EmptyMethod", "WeakerAccess"})
public class PopupAdapter
{
	/**
	 * Context
	 */
	@SuppressWarnings("WeakerAccess")
	protected final Context context;

	/**
	 * Popup window
	 */
	@SuppressWarnings("WeakerAccess")
	protected final PopupWindow window;

	/**
	 * Wrapped view
	 */
	@SuppressWarnings("WeakerAccess")
	protected View view;

	/**
	 * Window manager
	 */
	@SuppressWarnings("WeakerAccess")
	protected final WindowManager windowManager;

	/**
	 * Constructor.
	 *
	 * @param context0 context
	 */
	public PopupAdapter(final Context context0)
	{
		this.context = context0;
		this.window = new PopupWindow(context0);

		// dismiss when touched outside
		this.window.setTouchInterceptor(new OnTouchListener()
		{
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
	@SuppressWarnings({"deprecation", "WeakerAccess"})
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
		final LayoutInflater inflator = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		assert inflator != null;
		setContentView(inflator.inflate(layoutResID, null));
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