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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ViewGroup;

import org.treebolic.glue.R;

/**
 * A layout that splits the available space between two child views.
 * <p/>
 * An optionally movable bar exists between the children which allows the user to redistribute the space allocated to each view.
 *
 * @author com.mobidevelop
 * @author Bernard Bou (adapt)
 */
public class SplitPaneLayout extends ViewGroup
{
	public static final int ORIENTATION_HORIZONTAL = 0;
	public static final int ORIENTATION_VERTICAL = 1;

	private int mOrientation = SplitPaneLayout.ORIENTATION_HORIZONTAL;
	private int mSplitterSize = 12;
	private boolean mSplitterMovable = true;
	private int mSplitterPosition = Integer.MIN_VALUE;
	private float mSplitterPositionPercent = 0.5f;

	private Drawable mSplitterDrawable;
	private Drawable mSplitterDraggingDrawable;

	private final Rect mSplitterRect = new Rect();

	private int lastX;
	private int lastY;
	private final Rect temp = new Rect();
	private boolean isDragging = false;

	// C O N S T R U C T O R S

	public SplitPaneLayout(final Context context)
	{
		super(context);
		this.mSplitterPositionPercent = 0.5f;
		this.mSplitterDrawable = new PaintDrawable(0x88FFFFFF);
		this.mSplitterDraggingDrawable = new PaintDrawable(0x88FFFFFF);
	}

	public SplitPaneLayout(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);
		extractAttributes(context, attrs);
	}

	public SplitPaneLayout(final Context context, final AttributeSet attrs, final int defStyle)
	{
		super(context, attrs, defStyle);
		extractAttributes(context, attrs);
	}

	// A T T R I B U T E S

	private void extractAttributes(final Context context, final AttributeSet attrs)
	{
		if (attrs != null)
		{
			final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SplitPaneLayout);
			this.mOrientation = a.getInt(R.styleable.SplitPaneLayout_orientation, 0);
			this.mSplitterSize = a.getDimensionPixelSize(R.styleable.SplitPaneLayout_splitterSize,
					context.getResources().getDimensionPixelSize(R.dimen.spl_default_splitter_size));
			this.mSplitterMovable = a.getBoolean(R.styleable.SplitPaneLayout_splitterMovable, true);
			TypedValue value = a.peekValue(R.styleable.SplitPaneLayout_splitterPosition);
			if (value != null)
			{
				if (value.type == TypedValue.TYPE_DIMENSION)
				{
					this.mSplitterPosition = a.getDimensionPixelSize(R.styleable.SplitPaneLayout_splitterPosition, Integer.MIN_VALUE);
				}
				else if (value.type == TypedValue.TYPE_FRACTION)
				{
					this.mSplitterPositionPercent = a.getFraction(R.styleable.SplitPaneLayout_splitterPosition, 100, 100, 50) * 0.01f;
				}
			}
			else
			{
				this.mSplitterPosition = Integer.MIN_VALUE;
				this.mSplitterPositionPercent = 0.5f;
			}

			value = a.peekValue(R.styleable.SplitPaneLayout_splitterBackground);
			if (value != null)
			{
				if (value.type == TypedValue.TYPE_REFERENCE || value.type == TypedValue.TYPE_STRING)
				{
					this.mSplitterDrawable = a.getDrawable(R.styleable.SplitPaneLayout_splitterBackground);
				}
				else if (value.type == TypedValue.TYPE_INT_COLOR_ARGB8 || value.type == TypedValue.TYPE_INT_COLOR_ARGB4
						|| value.type == TypedValue.TYPE_INT_COLOR_RGB8 || value.type == TypedValue.TYPE_INT_COLOR_RGB4)
				{
					this.mSplitterDrawable = new PaintDrawable(a.getColor(R.styleable.SplitPaneLayout_splitterBackground, 0xFF000000));
				}
			}
			value = a.peekValue(R.styleable.SplitPaneLayout_splitterDraggingBackground);
			if (value != null)
			{
				if (value.type == TypedValue.TYPE_REFERENCE || value.type == TypedValue.TYPE_STRING)
				{
					this.mSplitterDraggingDrawable = a.getDrawable(R.styleable.SplitPaneLayout_splitterDraggingBackground);
				}
				else if (value.type == TypedValue.TYPE_INT_COLOR_ARGB8 || value.type == TypedValue.TYPE_INT_COLOR_ARGB4
						|| value.type == TypedValue.TYPE_INT_COLOR_RGB8 || value.type == TypedValue.TYPE_INT_COLOR_RGB4)
				{
					this.mSplitterDraggingDrawable = new PaintDrawable(a.getColor(R.styleable.SplitPaneLayout_splitterDraggingBackground, 0x88FFFFFF));
				}
			}
			else
			{
				this.mSplitterDraggingDrawable = new PaintDrawable(0x88FFFFFF);
			}
			a.recycle();
		}
	}

	// O V E R R I D E S

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		check();

		if (widthSize > 0 && heightSize > 0)
		{
			switch (this.mOrientation)
			{
			case ORIENTATION_HORIZONTAL:
			{
				// middle: p=# %=-1
				if (this.mSplitterPosition == Integer.MIN_VALUE && this.mSplitterPositionPercent < 0)
				{
					this.mSplitterPosition = widthSize / 2;
				}
				// absolute: p=x %=-1
				else if (this.mSplitterPosition != Integer.MIN_VALUE && this.mSplitterPositionPercent < 0)
				{
					this.mSplitterPositionPercent = (float) this.mSplitterPosition / (float) widthSize;
				}
				// percent: p=# %=x
				else if (/* this.mSplitterPosition == Integer.MIN_VALUE && */this.mSplitterPositionPercent >= 0)
				{
					this.mSplitterPosition = (int) (widthSize * this.mSplitterPositionPercent);
				}
				getChildAt(0).measure(MeasureSpec.makeMeasureSpec(this.mSplitterPosition - this.mSplitterSize / 2, MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
				getChildAt(1).measure(MeasureSpec.makeMeasureSpec(widthSize - this.mSplitterSize / 2 - this.mSplitterPosition, MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
				break;
			}
			case ORIENTATION_VERTICAL:
			{
				if (this.mSplitterPosition == Integer.MIN_VALUE && this.mSplitterPositionPercent < 0)
				{
					this.mSplitterPosition = heightSize / 2;
				}
				else if (this.mSplitterPosition != Integer.MIN_VALUE && this.mSplitterPositionPercent < 0)
				{
					this.mSplitterPositionPercent = (float) this.mSplitterPosition / (float) heightSize;
				}
				else if (/* this.mSplitterPosition == Integer.MIN_VALUE && */this.mSplitterPositionPercent >= 0)
				{
					this.mSplitterPosition = (int) (heightSize * this.mSplitterPositionPercent);
				}
				getChildAt(0).measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(this.mSplitterPosition - this.mSplitterSize / 2, MeasureSpec.EXACTLY));
				getChildAt(1).measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(heightSize - this.mSplitterSize / 2 - this.mSplitterPosition, MeasureSpec.EXACTLY));
				break;
			}
			default:
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b)
	{
		final int w = r - l;
		final int h = b - t;
		switch (this.mOrientation)
		{
		case ORIENTATION_HORIZONTAL:
		{
			getChildAt(0).layout(0, 0, this.mSplitterPosition - this.mSplitterSize / 2, h);
			this.mSplitterRect.set(this.mSplitterPosition - this.mSplitterSize / 2, 0, this.mSplitterPosition + this.mSplitterSize / 2, h);
			getChildAt(1).layout(this.mSplitterPosition + this.mSplitterSize / 2, 0, r, h);
			break;
		}
		case ORIENTATION_VERTICAL:
		{
			getChildAt(0).layout(0, 0, w, this.mSplitterPosition - this.mSplitterSize / 2);
			this.mSplitterRect.set(0, this.mSplitterPosition - this.mSplitterSize / 2, w, this.mSplitterPosition + this.mSplitterSize / 2);
			getChildAt(1).layout(0, this.mSplitterPosition + this.mSplitterSize / 2, w, h);
			break;
		}
		default:
			break;
		}
	}

	// T O U C H L I S T E N E R

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		if (this.mSplitterMovable)
		{
			final int x = (int) event.getX();
			final int y = (int) event.getY();

			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
			{
				if (this.mSplitterRect.contains(x, y))
				{
					performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
					this.isDragging = true;
					this.temp.set(this.mSplitterRect);
					invalidate(this.temp);
					this.lastX = x;
					this.lastY = y;
				}
				break;
			}
			case MotionEvent.ACTION_MOVE:
			{
				if (this.isDragging)
				{
					switch (this.mOrientation)
					{
					case ORIENTATION_HORIZONTAL:
					{
						this.temp.offset(x - this.lastX, 0);
						break;
					}
					case ORIENTATION_VERTICAL:
					{
						this.temp.offset(0, y - this.lastY);
						break;
					}
					default:
						break;
					}
					this.lastX = x;
					this.lastY = y;
					invalidate();
				}
				break;
			}
			case MotionEvent.ACTION_UP:
			{
				if (this.isDragging)
				{
					this.isDragging = false;
					switch (this.mOrientation)
					{
					case ORIENTATION_HORIZONTAL:
					{
						this.mSplitterPosition = x;
						break;
					}
					case ORIENTATION_VERTICAL:
					{
						this.mSplitterPosition = y;
						break;
					}
					default:
						break;
					}
					this.mSplitterPositionPercent = -1;
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
			throw new RuntimeException("SplitPaneLayout must have exactly two child views."); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.ViewGroup#dispatchDraw(android.graphics.Canvas)
	 */
	@Override
	protected void dispatchDraw(final Canvas canvas)
	{
		super.dispatchDraw(canvas);
		if (this.mSplitterDrawable != null)
		{
			this.mSplitterDrawable.setBounds(this.mSplitterRect);
			this.mSplitterDrawable.draw(canvas);
		}
		if (this.isDragging)
		{
			this.mSplitterDraggingDrawable.setBounds(this.temp);
			this.mSplitterDraggingDrawable.draw(canvas);
		}
	}

	// G E T / S E T

	/**
	 * Gets the current drawable used for the splitter.
	 *
	 * @return the drawable used for the splitter
	 */
	public Drawable getSplitterDrawable()
	{
		return this.mSplitterDrawable;
	}

	/**
	 * Sets the drawable used for the splitter.
	 *
	 * @param splitterDrawable
	 *            the desired orientation of the layout
	 */
	public void setSplitterDrawable(final Drawable splitterDrawable)
	{
		this.mSplitterDrawable = splitterDrawable;
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
	public Drawable getSplitterDraggingDrawable()
	{
		return this.mSplitterDraggingDrawable;
	}

	/**
	 * Sets the drawable used for the splitter dragging overlay.
	 *
	 * @param splitterDraggingDrawable
	 *            the drawable to use while dragging the splitter
	 */
	public void setSplitterDraggingDrawable(final Drawable splitterDraggingDrawable)
	{
		this.mSplitterDraggingDrawable = splitterDraggingDrawable;
		if (this.isDragging)
		{
			invalidate();
		}
	}

	/**
	 * Gets the current orientation of the layout.
	 *
	 * @return the orientation of the layout
	 */
	public int getOrientation()
	{
		return this.mOrientation;
	}

	/**
	 * Sets the orientation of the layout.
	 *
	 * @param orientation
	 *            the desired orientation of the layout
	 */
	public void setOrientation(final int orientation)
	{
		if (this.mOrientation != orientation)
		{
			this.mOrientation = orientation;
			if (getChildCount() == 2)
			{
				remeasure();
			}
		}
	}

	/**
	 * Gets the current size of the splitter in pixels.
	 *
	 * @return the size of the splitter
	 */
	public int getSplitterSize()
	{
		return this.mSplitterSize;
	}

	/**
	 * Sets the current size of the splitter in pixels.
	 *
	 * @param splitterSize
	 *            the desired size of the splitter
	 */
	public void setSplitterSize(final int splitterSize)
	{
		this.mSplitterSize = splitterSize;
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
		return this.mSplitterMovable;
	}

	/**
	 * Sets whether the splitter is movable by the user.
	 *
	 * @param splitterMovable
	 *            whether the splitter is movable
	 */
	public void setSplitterMovable(final boolean splitterMovable)
	{
		this.mSplitterMovable = splitterMovable;
	}

	/**
	 * Gets the current position of the splitter in pixels.
	 *
	 * @return the position of the splitter
	 */
	public int getSplitterPosition()
	{
		return this.mSplitterPosition;
	}

	/**
	 * Sets the current position of the splitter in pixels.
	 *
	 * @param position0
	 *            the desired position of the splitter
	 */
	public void setSplitterPosition(final int position0)
	{
		int position = position0;
		if (position < 0)
		{
			position = 0;
		}
		this.mSplitterPosition = position;
		this.mSplitterPositionPercent = -1;
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
	public float getSplitterPositionPercent()
	{
		return this.mSplitterPositionPercent;
	}

	/**
	 * Sets the current position of the splitter as a percentage of the layout.
	 *
	 * @param position0
	 *            the desired position of the splitter
	 */
	public void setSplitterPositionPercent(final float position0)
	{
		float position = position0;
		if (position < 0)
		{
			position = 0;
		}
		if (position > 1)
		{
			position = 1;
		}
		this.mSplitterPosition = Integer.MIN_VALUE;
		this.mSplitterPositionPercent = position;
		if (getChildCount() == 2)
		{
			remeasure();
		}
	}

	// I N S T A N C E S T A T E

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState()
	{
		final Parcelable superState = super.onSaveInstanceState();
		final SavedState ss = new SavedState(superState);
		ss.mSplitterPositionPercent = this.mSplitterPositionPercent;
		return ss;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(final Parcelable state)
	{
		if (!(state instanceof SavedState))
		{
			super.onRestoreInstanceState(state);
			return;
		}
		final SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		setSplitterPositionPercent(ss.mSplitterPositionPercent);
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
			public SavedState createFromParcel(final Parcel in)
			{
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(final int size)
			{
				return new SavedState[size];
			}
		};
		float mSplitterPositionPercent;

		SavedState(final Parcelable superState)
		{
			super(superState);
		}

		private SavedState(final Parcel in)
		{
			super(in);
			this.mSplitterPositionPercent = in.readFloat();
		}

		@Override
		public void writeToParcel(final Parcel out, final int flags)
		{
			super.writeToParcel(out, flags);
			out.writeFloat(this.mSplitterPositionPercent);
		}
	}
}
