/*
 *  Android Split Pane Layout.
 *  https://github.com/MobiDevelop/android-split-pane-layout
 *
 *  Copyright (C) 2012 Justin Shapcott
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package treebolic.glue.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ViewGroup;

import org.treebolic.glue.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A layout that splits the available space between two child views.
 * <p/>
 * An optionally movable bar exists between the children which allows the user to redistribute the space allocated to each view.
 *
 * @author com.mobidevelop
 * @author Bernard Bou (adapt)
 */
@SuppressWarnings("ALL")
public class SplitPaneLayout extends ViewGroup
{
	/**
	 * Preference key to splitter relative position
	 */
	static private final String SPLITTER_POSITION_PERCENT = "splitter_position_percent";

	// O R I E N T A T I O N

	/**
	 * Orientation horizontal values
	 */
	@SuppressWarnings("WeakerAccess")
	public static final int ORIENTATION_HORIZONTAL = 0;

	/**
	 * Orientation vertical value
	 */
	@SuppressWarnings("WeakerAccess")
	public static final int ORIENTATION_VERTICAL = 1;

	/**
	 * Splitter color
	 */
	private static final int SPLITTER_COLOR = 0x80FFFFFF;

	/**
	 * Splitter color when dragging
	 */
	@SuppressWarnings("WeakerAccess")
	private static final int SPLITTER_DRAG_COLOR = 0xC0FFFFFF;

	/**
	 * Orientation
	 */
	private int orientation;

	/**
	 * Splitter position (== Integer.MIN_VALUE if splitterPositionPercent is used)
	 */
	private int splitterPosition;

	/**
	 * Splitter relative position (== -1 if splitterPosition is used)
	 */
	private float splitterPositionPercent;

	/**
	 * Splitter size in pixels
	 */
	private int splitterSize = 12;

	/**
	 * Whether splitter is movable
	 */
	private boolean splitterMovable = true;

	/**
	 * Splitter rectangle
	 */
	private final Rect splitterRect = new Rect();

	/**
	 * Whether the splitter is dragging
	 */
	private boolean isDragging = false;

	/**
	 * Splitter drawable
	 */
	@Nullable
	private Drawable splitterDrawable;

	/**
	 * Dragging splitter drawable
	 */
	@Nullable
	private Drawable splitterDraggingDrawable;

	/**
	 * Splitter rectangle
	 */
	private final Rect splitterRectangle = new Rect();

	/**
	 * Last touch X
	 */
	private int lastTouchX;

	/**
	 * Last touch Y
	 */
	private int lastTouchY;

	// C O N S T R U C T O R S

	/**
	 * Basic constructor
	 *
	 * @param context context
	 */
	public SplitPaneLayout(final Context context)
	{
		super(context);

		this.orientation = SplitPaneLayout.ORIENTATION_HORIZONTAL;
		this.splitterPositionPercent = 0.5f;
		this.splitterPosition = Integer.MIN_VALUE;
		this.splitterDrawable = new PaintDrawable(SPLITTER_COLOR);
		this.splitterDraggingDrawable = new PaintDrawable(SPLITTER_DRAG_COLOR);
	}

	/**
	 * Constructor with attributes
	 *
	 * @param context context
	 * @param attrs   attributes
	 */
	public SplitPaneLayout(@NonNull final Context context, final AttributeSet attrs)
	{
		super(context, attrs);

		this.orientation = SplitPaneLayout.ORIENTATION_HORIZONTAL;
		this.splitterPositionPercent = 0.5f;
		this.splitterPosition = Integer.MIN_VALUE;

		extractAttributes(context, attrs);
	}

	/**
	 * Constructor with attributes and style
	 *
	 * @param context  context
	 * @param attrs    attributes
	 * @param defStyle style
	 */
	public SplitPaneLayout(@NonNull final Context context, final AttributeSet attrs, final int defStyle)
	{
		super(context, attrs, defStyle);

		this.orientation = SplitPaneLayout.ORIENTATION_HORIZONTAL;
		this.splitterPositionPercent = 0.5f;
		this.splitterPosition = Integer.MIN_VALUE;

		extractAttributes(context, attrs);
	}

	// A T T R I B U T E S

	private void extractAttributes(@NonNull final Context context, @Nullable final AttributeSet attrs)
	{
		if (attrs != null)
		{
			final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SplitPaneLayout);

			// misc
			this.orientation = array.getInt(R.styleable.SplitPaneLayout_orientation, 0);
			this.splitterSize = array.getDimensionPixelSize(R.styleable.SplitPaneLayout_splitterSize, context.getResources().getDimensionPixelSize(R.dimen.spl_default_splitter_size));
			this.splitterMovable = array.getBoolean(R.styleable.SplitPaneLayout_splitterMovable, true);

			// position
			TypedValue value = array.peekValue(R.styleable.SplitPaneLayout_splitterPosition);
			if (value != null)
			{
				if (value.type == TypedValue.TYPE_DIMENSION)
				{
					this.splitterPosition = array.getDimensionPixelSize(R.styleable.SplitPaneLayout_splitterPosition, Integer.MIN_VALUE);
					this.splitterPositionPercent = -1;
				}
				else if (value.type == TypedValue.TYPE_FRACTION)
				{
					this.splitterPositionPercent = array.getFraction(R.styleable.SplitPaneLayout_splitterPosition, 100, 100, 50) * 0.01f;
					this.splitterPosition = Integer.MIN_VALUE;
				}
			}
			else
			{
				this.splitterPosition = Integer.MIN_VALUE;
				this.splitterPositionPercent = 0.5f;
			}

			// backgrounds
			value = array.peekValue(R.styleable.SplitPaneLayout_splitterBackground);
			if (value != null)
			{
				if (value.type == TypedValue.TYPE_REFERENCE || value.type == TypedValue.TYPE_STRING)
				{
					this.splitterDrawable = array.getDrawable(R.styleable.SplitPaneLayout_splitterBackground);
				}
				else if (value.type == TypedValue.TYPE_INT_COLOR_ARGB8 || value.type == TypedValue.TYPE_INT_COLOR_ARGB4 || value.type == TypedValue.TYPE_INT_COLOR_RGB8 || value.type == TypedValue.TYPE_INT_COLOR_RGB4)
				{
					this.splitterDrawable = new PaintDrawable(array.getColor(R.styleable.SplitPaneLayout_splitterBackground, 0xFF000000));
				}
			}
			value = array.peekValue(R.styleable.SplitPaneLayout_splitterDraggingBackground);
			if (value != null)
			{
				if (value.type == TypedValue.TYPE_REFERENCE || value.type == TypedValue.TYPE_STRING)
				{
					this.splitterDraggingDrawable = array.getDrawable(R.styleable.SplitPaneLayout_splitterDraggingBackground);
				}
				else if (value.type == TypedValue.TYPE_INT_COLOR_ARGB8 || value.type == TypedValue.TYPE_INT_COLOR_ARGB4 || value.type == TypedValue.TYPE_INT_COLOR_RGB8 || value.type == TypedValue.TYPE_INT_COLOR_RGB4)
				{
					this.splitterDraggingDrawable = new PaintDrawable(array.getColor(R.styleable.SplitPaneLayout_splitterDraggingBackground, SPLITTER_DRAG_COLOR));
				}
			}
			else
			{
				this.splitterDraggingDrawable = new PaintDrawable(SPLITTER_DRAG_COLOR);
			}
			array.recycle();
		}
	}

	// O V E R R I D E S

	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		check();

		if (widthSize > 0 && heightSize > 0)
		{
			switch (this.orientation)
			{
				case ORIENTATION_HORIZONTAL:
				{
					// neither specified
					// middle: p=# %=-1
					if (this.splitterPosition == Integer.MIN_VALUE && this.splitterPositionPercent < 0)
					{
						this.splitterPosition = widthSize / 2;
					}
					// absolute specified
					// absolute: p=x %=-1
					else if (this.splitterPosition != Integer.MIN_VALUE && this.splitterPositionPercent < 0)
					{
						this.splitterPositionPercent = (float) this.splitterPosition / (float) widthSize;
					}
					// percent specified
					// percent: p=# %=x
					else if (/* this.splitterPosition == Integer.MIN_VALUE && */this.splitterPositionPercent >= 0)
					{
						this.splitterPosition = (int) (widthSize * this.splitterPositionPercent);
					}
					getChildAt(0).measure(MeasureSpec.makeMeasureSpec(this.splitterPosition - this.splitterSize / 2, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
					getChildAt(1).measure(MeasureSpec.makeMeasureSpec(widthSize - this.splitterSize / 2 - this.splitterPosition, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
					break;
				}
				case ORIENTATION_VERTICAL:
				{
					if (this.splitterPosition == Integer.MIN_VALUE && this.splitterPositionPercent < 0)
					{
						this.splitterPosition = heightSize / 2;
					}
					else if (this.splitterPosition != Integer.MIN_VALUE && this.splitterPositionPercent < 0)
					{
						this.splitterPositionPercent = (float) this.splitterPosition / (float) heightSize;
					}
					else if (/* this.splitterPosition == Integer.MIN_VALUE && */this.splitterPositionPercent >= 0)
					{
						this.splitterPosition = (int) (heightSize * this.splitterPositionPercent);
					}
					getChildAt(0).measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(this.splitterPosition - this.splitterSize / 2, MeasureSpec.EXACTLY));
					getChildAt(1).measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize - this.splitterSize / 2 - this.splitterPosition, MeasureSpec.EXACTLY));
					break;
				}
				default:
					break;
			}
		}
	}

	@Override
	protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b)
	{
		final int w = r - l;
		final int h = b - t;
		switch (this.orientation)
		{
			case ORIENTATION_HORIZONTAL:
			{
				getChildAt(0).layout(0, 0, this.splitterPosition - this.splitterSize / 2, h);
				this.splitterRect.set(this.splitterPosition - this.splitterSize / 2, 0, this.splitterPosition + this.splitterSize / 2, h);
				getChildAt(1).layout(this.splitterPosition + this.splitterSize / 2, 0, r, h);
				break;
			}
			case ORIENTATION_VERTICAL:
			{
				getChildAt(0).layout(0, 0, w, this.splitterPosition - this.splitterSize / 2);
				this.splitterRect.set(0, this.splitterPosition - this.splitterSize / 2, w, this.splitterPosition + this.splitterSize / 2);
				getChildAt(1).layout(0, this.splitterPosition + this.splitterSize / 2, w, h);
				break;
			}
			default:
				break;
		}
	}

	@Override
	protected void dispatchDraw(@NonNull final Canvas canvas)
	{
		super.dispatchDraw(canvas);
		if (this.splitterDrawable != null)
		{
			this.splitterDrawable.setBounds(this.splitterRect);
			this.splitterDrawable.draw(canvas);
		}
		if (this.isDragging)
		{
			this.splitterDraggingDrawable.setBounds(this.splitterRectangle);
			this.splitterDraggingDrawable.draw(canvas);
		}
	}

	// T O U C H L I S T E N E R

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(@NonNull final MotionEvent event)
	{
		if (this.splitterMovable)
		{
			final int x = (int) event.getX();
			final int y = (int) event.getY();

			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
				{
					if (this.splitterRect.contains(x, y))
					{
						performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
						this.isDragging = true;
						this.splitterRectangle.set(this.splitterRect);
						invalidate(this.splitterRectangle);
						this.lastTouchX = x;
						this.lastTouchY = y;
					}
					break;
				}
				case MotionEvent.ACTION_MOVE:
				{
					if (this.isDragging)
					{
						switch (this.orientation)
						{
							case ORIENTATION_HORIZONTAL:
							{
								this.splitterRectangle.offset(x - this.lastTouchX, 0);
								break;
							}
							case ORIENTATION_VERTICAL:
							{
								this.splitterRectangle.offset(0, y - this.lastTouchY);
								break;
							}
							default:
								break;
						}
						this.lastTouchX = x;
						this.lastTouchY = y;
						invalidate();
					}
					break;
				}
				case MotionEvent.ACTION_UP:
				{
					if (this.isDragging)
					{
						this.isDragging = false;
						switch (this.orientation)
						{
							case ORIENTATION_HORIZONTAL:
							{
								this.splitterPosition = x;
								break;
							}
							case ORIENTATION_VERTICAL:
							{
								this.splitterPosition = y;
								break;
							}
							default:
								break;
						}
						this.splitterPositionPercent = -1;
						remeasure();
						requestLayout();
					}
					break;
				}
				default:
					break;
			}
			return true;
		}
		return false;
	}

	// H E L P E R S

	/**
	 * Convenience for calling own measure method.
	 */
	private void remeasure()
	{
		measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
	}

	/**
	 * Checks that we have exactly two children.
	 */
	private void check()
	{
		if (getChildCount() != 2)
		{
			throw new RuntimeException("SplitPaneLayout must have exactly two child views.");
		}
	}

	// G E T / S E T

	/**
	 * Gets the current orientation of the layout.
	 *
	 * @return the orientation of the layout
	 */
	public int getOrientation()
	{
		return this.orientation;
	}

	/**
	 * Sets the orientation of the layout.
	 *
	 * @param orientation the desired orientation of the layout
	 */
	public void setOrientation(final int orientation)
	{
		if (this.orientation != orientation)
		{
			this.orientation = orientation;
			if (getChildCount() == 2)
			{
				remeasure();
			}
		}
	}

	/**
	 * Gets the current drawable used for the splitter.
	 *
	 * @return the drawable used for the splitter
	 */
	@Nullable
	public Drawable getSplitterDrawable()
	{
		return this.splitterDrawable;
	}

	/**
	 * Sets the drawable used for the splitter.
	 *
	 * @param splitterDrawable the desired orientation of the layout
	 */
	public void setSplitterDrawable(final Drawable splitterDrawable)
	{
		this.splitterDrawable = splitterDrawable;
		if (getChildCount() == 2)
		{
			remeasure();
		}
	}

	/**
	 * Gets the current drawable used for the splitter dragging overlay.
	 *
	 * @return the drawable used for the splitter
	 */
	@Nullable
	public Drawable getSplitterDraggingDrawable()
	{
		return this.splitterDraggingDrawable;
	}

	/**
	 * Sets the drawable used for the splitter dragging overlay.
	 *
	 * @param splitterDraggingDrawable the drawable to use while dragging the splitter
	 */
	public void setSplitterDraggingDrawable(final Drawable splitterDraggingDrawable)
	{
		this.splitterDraggingDrawable = splitterDraggingDrawable;
		if (this.isDragging)
		{
			invalidate();
		}
	}

	/**
	 * Gets the current size of the splitter in pixels.
	 *
	 * @return the size of the splitter
	 */
	public int getSplitterSize()
	{
		return this.splitterSize;
	}

	/**
	 * Sets the current size of the splitter in pixels.
	 *
	 * @param splitterSize the desired size of the splitter
	 */
	public void setSplitterSize(final int splitterSize)
	{
		this.splitterSize = splitterSize;
		if (getChildCount() == 2)
		{
			remeasure();
		}
	}

	/**
	 * Gets whether the splitter is movable by the user.
	 *
	 * @return whether the splitter is movable
	 */
	public boolean isSplitterMovable()
	{
		return this.splitterMovable;
	}

	/**
	 * Sets whether the splitter is movable by the user.
	 *
	 * @param splitterMovable whether the splitter is movable
	 */
	public void setSplitterMovable(@SuppressWarnings("SameParameterValue") final boolean splitterMovable)
	{
		this.splitterMovable = splitterMovable;
	}

	/**
	 * Gets the current position of the splitter in pixels.
	 *
	 * @return the position of the splitter
	 */
	public int getSplitterPosition()
	{
		return this.splitterPosition;
	}

	/**
	 * Sets the current position of the splitter in pixels.
	 *
	 * @param position0 the desired position of the splitter
	 */
	public void setSplitterPosition(final int position0)
	{
		int position = position0;
		if (position < 0)
		{
			position = 0;
		}
		this.splitterPosition = position;
		this.splitterPositionPercent = -1;
		if (getChildCount() == 2)
		{
			remeasure();
		}
	}

	/**
	 * Gets the current position of the splitter as a percent.
	 *
	 * @return the position of the splitter
	 */
	@SuppressWarnings("WeakerAccess")
	public float getSplitterPositionPercent()
	{
		return this.splitterPositionPercent;
	}

	/**
	 * Sets the current position of the splitter as a percentage of the layout.
	 *
	 * @param position0 the desired position of the splitter
	 */
	public void setSplitterPositionPercent(final float position0)
	{
		float position = position0;
		if (position < 0F)
		{
			position = 0F;
		}
		if (position > 1F)
		{
			position = 1F;
		}
		this.splitterPosition = Integer.MIN_VALUE;
		this.splitterPositionPercent = position;
		if (getChildCount() == 2)
		{
			remeasure();
		}
	}

	// S A V E   S T A T E

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		restorePref();
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		savePref();
	}

	private void savePref()
	{
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		float value = getSplitterPositionPercent();
		if (value == -1F)
		{
			sharedPrefs.edit().remove(SPLITTER_POSITION_PERCENT).apply();
		}
		else
		{
			sharedPrefs.edit().putFloat(SPLITTER_POSITION_PERCENT, value).apply();
		}
	}

	private void restorePref()
	{
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		float value = sharedPrefs.getFloat(SPLITTER_POSITION_PERCENT, -1F);
		if (value != -1F)
		{
			setSplitterPositionPercent(value);
		}
	}

	@Override
	public Parcelable onSaveInstanceState()
	{
		final Parcelable superState = super.onSaveInstanceState();
		final SavedState savedState = new SavedState(superState);
		savedState.splitterPositionPercent = this.splitterPositionPercent;
		return savedState;
	}

	@Override
	public void onRestoreInstanceState(final Parcelable state)
	{
		if (!(state instanceof SavedState))
		{
			super.onRestoreInstanceState(state);
			return;
		}
		final SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		setSplitterPositionPercent(savedState.splitterPositionPercent);
	}

	/**
	 * Holds important values when we need to save instance state.
	 */
	public static class SavedState extends BaseSavedState
	{
		@SuppressWarnings("hiding")
		public static final Creator<SavedState> CREATOR = new Creator<SavedState>()
		{
			@SuppressWarnings("synthetic-access")
			@Override
			public SavedState createFromParcel(@NonNull final Parcel in)
			{
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(final int size)
			{
				return new SavedState[size];
			}
		};

		/**
		 * Position percent
		 */
		float splitterPositionPercent;

		SavedState(final Parcelable superState)
		{
			super(superState);
		}

		private SavedState(@NonNull final Parcel in)
		{
			super(in);
			this.splitterPositionPercent = in.readFloat();
		}

		@Override
		public void writeToParcel(@NonNull final Parcel out, final int flags)
		{
			super.writeToParcel(out, flags);
			out.writeFloat(this.splitterPositionPercent);
		}
	}
}
