package treebolic.glue.component;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.treebolic.glue.R;

import java.util.ArrayList;
import java.util.List;

import treebolic.glue.ActionListener;

/**
 * QuickAction dialog, shows action list as icon and text like the one in Gallery3D app. Currently supports vertical and horizontal layout.
 *
 * @author Lorensius W. L. T <lorenz@londatiga.net> Contributors: - Kevin Peck <kevinwpeck@gmail.com>
 */
public class QuickAction extends PopupAdapter implements OnDismissListener
{
	/**
	 * Action item
	 */
	static public class ActionItem
	{
		/**
		 * Icon
		 */
		public final Drawable icon;

		/**
		 * Title
		 */
		public final String title;

		/**
		 * Whether item is sticky (disable QuickAction dialog being dismissed after an item is clicked)
		 */
		public final boolean sticky;

		/**
		 * Action listener
		 */
		public final ActionListener listener;

		/**
		 * Constructor
		 *
		 * @param title0
		 *            title
		 * @param icon0
		 *            icon to use
		 * @param sticky0
		 *            whether item is sticky (disable QuickAction dialog being dismissed after an item is clicked)
		 * @param listener0
		 *            listener
		 */
		public ActionItem(final String title0, final Drawable icon0, final boolean sticky0, final ActionListener listener0)
		{
			this.title = title0;
			this.icon = icon0;
			this.sticky = sticky0;
			this.listener = listener0;
		}
	}

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;

	/**
	 * Layout inflater
	 */
	private final LayoutInflater inflater;

	/**
	 *
	 */
	private ViewGroup tracks;

	/**
	 * Scroller
	 */
	private ScrollView scroller;

	/**
	 * Anchor arrow up
	 */
	private ImageView arrowUp;

	/**
	 * Anchor arrow down
	 */
	private ImageView arrowDown;

	/**
	 * Action items
	 */
	private final List<ActionItem> actionItems = new ArrayList<>();

	/**
	 * Dismiss listener
	 */
	private OnDismissListener dismissListener;

	/**
	 * Action done flag
	 */
	private boolean didAction;

	/**
	 * Child position
	 */
	private int childPos;

	/**
	 * Insert position
	 */
	private int insertPos;

	/**
	 * Animation style
	 */
	private int animationStyle;

	/**
	 * Orientation
	 */
	private final int orientation;

	/**
	 * Popup width
	 */
	private int popupWidth = 0;

	/**
	 * Constructor for default vertical layout
	 *
	 * @param context0
	 *            context
	 */
	public QuickAction(final Context context0)
	{
		this(context0, QuickAction.VERTICAL);
	}

	/**
	 * Constructor allowing orientation override
	 *
	 * @param context0
	 *            context
	 * @param orientation0
	 *            layout orientation, can be vertical or horizontal
	 */
	public QuickAction(final Context context0, final int orientation0)
	{
		super(context0);

		this.orientation = orientation0;
		this.inflater = (LayoutInflater) context0.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.animationStyle = QuickAction.ANIM_AUTO;
		this.childPos = 0;
		setRootViewId(this.orientation == QuickAction.HORIZONTAL ? R.layout.popup_horizontal : R.layout.popup_vertical);
	}

	/**
	 * Get action item at an index
	 *
	 * @param index
	 *            index of item (position from callback)
	 * @return action item at the position
	 */
	public ActionItem getActionItem(final int index)
	{
		return this.actionItems.get(index);
	}

	/**
	 * Set root view
	 *
	 * @param id
	 *            Layout resource id
	 */
	public void setRootViewId(final int id)
	{
		this.view = this.inflater.inflate(id, null);
		this.tracks = (ViewGroup) this.view.findViewById(R.id.tracks);
		this.arrowDown = (ImageView) this.view.findViewById(R.id.arrow_down);
		this.arrowUp = (ImageView) this.view.findViewById(R.id.arrow_up);
		this.scroller = (ScrollView) this.view.findViewById(R.id.scroller);

		// This was previously defined on show() method, moved here to prevent force close that occurred
		// when tapping fast on a view to show quickaction dialog. Thanks to zammbi (github.com/zammbi)
		this.view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		setContentView(this.view);
	}

	/**
	 * Set animation style
	 *
	 * @param animStyle
	 *            animation style, default is set to ANIM_AUTO
	 */
	public void setAnimStyle(final int animStyle)
	{
		this.animationStyle = animStyle;
	}

	/**
	 * Add action item
	 *
	 * @param action
	 *            {@link ActionItem}
	 */
	public void addActionItem(final ActionItem action)
	{
		this.actionItems.add(action);

		final String title = action.title;
		final Drawable icon = action.icon;
		final View itemView = this.inflater.inflate(this.orientation == QuickAction.HORIZONTAL ? R.layout.popup_horizontal_item : R.layout.popup_vertical_item,
				null);

		final ImageView img = (ImageView) itemView.findViewById(R.id.iv_icon);
		final TextView text = (TextView) itemView.findViewById(R.id.tv_title);

		if (icon != null)
		{
			img.setImageDrawable(icon);
		}
		else
		{
			img.setVisibility(View.GONE);
		}
		if (title != null)
		{
			text.setText(title);
		}
		else
		{
			text.setVisibility(View.GONE);
		}

		final int pos = this.childPos;

		itemView.setOnClickListener(new OnClickListener()
		{
			@SuppressWarnings("synthetic-access")
			@Override
			public void onClick(final View v)
			{
				if (action.listener != null)
				{
					action.listener.onAction(action);
				}

				if (!getActionItem(pos).sticky)
				{
					QuickAction.this.didAction = true;
					dismiss();
				}
			}
		});

		itemView.setFocusable(true);
		itemView.setClickable(true);

		if (this.orientation == QuickAction.HORIZONTAL && this.childPos != 0)
		{
			final View separator = this.inflater.inflate(R.layout.quickaction_horiz_separator, this.tracks);

			final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			separator.setLayoutParams(params);
			separator.setPadding(5, 0, 5, 0);

			this.tracks.addView(separator, this.insertPos);

			this.insertPos++;
		}

		this.tracks.addView(itemView, this.insertPos);

		this.childPos++;
		this.insertPos++;
	}

	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or bottom of anchor view.
	 */
	public void show(final View anchor)
	{
		preShow();
		this.didAction = false;

		// anchor screen rect
		final int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		final Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

		// wrapped view dimensions
		this.view.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final int rootHeight = this.view.getMeasuredHeight();
		if (this.popupWidth == 0)
		{
			this.popupWidth = this.view.getMeasuredWidth();
		}

		// screen size
		final Point size = new Point();
		this.windowManager.getDefaultDisplay().getSize(size);
		final int screenWidth = size.x;
		final int screenHeight = size.y;

		// X coord of popup (top left)
		int xPos, arrowPos;
		if (anchorRect.left + this.popupWidth > screenWidth)
		{
			xPos = anchorRect.left - (this.popupWidth - anchor.getWidth());
			xPos = xPos < 0 ? 0 : xPos;
			arrowPos = anchorRect.centerX() - xPos;
		}
		else
		{
			if (anchor.getWidth() > this.popupWidth)
			{
				xPos = anchorRect.centerX() - this.popupWidth / 2;
			}
			else
			{
				xPos = anchorRect.left;
			}
			arrowPos = anchorRect.centerX() - xPos;
		}

		// Y coord of popup (top left)
		int yPos;
		final int dyTop = anchorRect.top;
		final int dyBottom = screenHeight - anchorRect.bottom;
		final boolean onTop = dyTop > dyBottom;
		if (onTop)
		{
			if (rootHeight > dyTop)
			{
				yPos = 15;
				final LayoutParams l = this.scroller.getLayoutParams();
				l.height = dyTop - anchor.getHeight();
			}
			else
			{
				yPos = anchorRect.top - rootHeight;
			}
		}
		else
		{
			yPos = anchorRect.bottom;
			if (rootHeight > dyBottom)
			{
				final LayoutParams l = this.scroller.getLayoutParams();
				l.height = dyBottom;
			}
		}

		// show
		showArrow(onTop ? R.id.arrow_down : R.id.arrow_up, arrowPos);
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		this.window.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	/**
	 * Show at x,y of anchor view
	 *
	 * @param anchor
	 *            anchor view
	 * @param x0
	 *            x location
	 * @param y0
	 *            y location
	 */
	public void show(final View anchor, final float x0, final float y0)
	{
		preShow();
		this.didAction = false;

		// anchor screen rect
		final int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		final Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

		// screen size
		final Point size = new Point();
		this.windowManager.getDefaultDisplay().getSize(size);
		final int screenWidth = size.x;

		// wrapped view dimensions
		this.view.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final int popupHeight = this.view.getMeasuredHeight();
		if (this.popupWidth == 0)
		{
			this.popupWidth = this.view.getMeasuredWidth();
		}

		// X
		final int w2 = this.popupWidth / 2;
		int arrowPos = w2;
		final int x = location[0] + (int) x0 - w2;
		int dx = x - location[0];
		if (dx < 0)
		{
			arrowPos = w2 + dx;
		}
		dx = x - (screenWidth - this.popupWidth);
		if (dx > 0)
		{
			arrowPos = this.popupWidth - (w2 - dx);
		}

		// Y
		final boolean above = y0 > anchor.getHeight() / 2;
		int y = (int) y0 + location[1];
		if (above)
		{
			y -= popupHeight;
		}

		// show
		showArrow(above ? R.id.arrow_down : R.id.arrow_up, arrowPos);
		setAnimationStyle(screenWidth, anchorRect.centerX(), above);
		this.window.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);
	}

	/**
	 * Set animation style
	 *
	 * @param screenWidth
	 *            screen width
	 * @param requestedX
	 *            distance from left edge
	 * @param onTop
	 *            flag to indicate where the popup should be displayed. Set TRUE if displayed on top of anchor view and vice versa
	 */
	private void setAnimationStyle(final int screenWidth, final int requestedX, final boolean onTop)
	{
		final int arrowPos = requestedX - this.arrowUp.getMeasuredWidth() / 2;

		switch (this.animationStyle)
		{
		case ANIM_GROW_FROM_LEFT:
			this.window.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			break;

		case ANIM_GROW_FROM_RIGHT:
			this.window.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			break;

		case ANIM_GROW_FROM_CENTER:
			this.window.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
			break;

		case ANIM_REFLECT:
			this.window.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
			break;

		case ANIM_AUTO:
			if (arrowPos <= screenWidth / 4)
			{
				this.window.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			}
			else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4))
			{
				this.window.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
			}
			else
			{
				this.window.setAnimationStyle(onTop ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Show arrow
	 *
	 * @param whichArrow
	 *            arrow type resource id
	 * @param requestedX
	 *            distance from left screen
	 */
	private void showArrow(final int whichArrow, final int requestedX)
	{
		final View showArrow = whichArrow == R.id.arrow_up ? this.arrowUp : this.arrowDown;
		final View hideArrow = whichArrow == R.id.arrow_up ? this.arrowDown : this.arrowUp;

		// x adjust
		final int arrowWidth = this.arrowUp.getMeasuredWidth();
		final int arrowWidth2 = arrowWidth / 2;
		final ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow.getLayoutParams();
		param.leftMargin = requestedX - arrowWidth2;
		if (param.leftMargin < 0)
		{
			param.leftMargin = 0;
		}
		if (param.leftMargin > this.popupWidth - arrowWidth)
		{
			param.leftMargin = this.popupWidth - arrowWidth;
		}

		// show
		showArrow.setVisibility(View.VISIBLE);

		// hide
		hideArrow.setVisibility(View.INVISIBLE);
	}

	/**
	 * Set listener for window dismissed. This listener will only be fired if the quickaction dialog is dismissed by clicking outside the dialog or clicking on
	 * sticky item.
	 */
	public void setOnDismissListener(final OnDismissListener listener)
	{
		setOnDismissListener(this);
		this.dismissListener = listener;
	}

	@Override
	public void onDismiss()
	{
		if (!this.didAction && this.dismissListener != null)
		{
			this.dismissListener.onDismiss();
		}
	}

	/**
	 * Listener for item click
	 */
	interface OnActionItemClickListener
	{
		void onItemClick(QuickAction source, int pos, int actionId);
	}

	/**
	 * Listener for window dismiss
	 */
	@SuppressWarnings("EmptyMethod")
	interface OnDismissListener
	{
		void onDismiss();
	}
}