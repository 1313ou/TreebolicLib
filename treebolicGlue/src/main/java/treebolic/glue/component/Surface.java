/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package treebolic.glue.component;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.treebolic.glue.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import treebolic.glue.EventListener;
import treebolic.glue.Graphics;

/**
 * Surface treebolic glue to serve as base for view
 * API class
 *
 * @author Bernard Bou
 */
public abstract class Surface extends SurfaceView implements SurfaceHolder.Callback, Component, treebolic.glue.iface.component.Surface<Graphics, EventListener>
{
	static private final boolean LOG = false;
	static private final String TAG = "TreebolicSurface";

	/**
	 * Margin of error when finding node
	 *
	 * @noinspection WeakerAccess
	 */
	public static final float FIND_DISTANCE_EPSILON_FACTOR = 2.5F;

	/**
	 * The thread that actually draws the animation
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private TreebolicThread thread;

	/**
	 * Touch, gesture, hover event listener
	 */
	@SuppressWarnings("InstanceVariableOfConcreteClass")
	@Nullable
	private EventListener listener;

	/**
	 * Gesture detector
	 */
	@NonNull
	private final GestureDetector gestureDetector;

	/**
	 * Scale detector
	 */
	@NonNull
	private final ScaleGestureDetector scaleDetector;

	/**
	 * Whether in the middle of scaling op
	 */
	private boolean isScaling = false;

	/**
	 * Accumulated scale factor
	 */
	private float scaleFactor = 1F;

	/**
	 * Whether to fire hover events
	 */
	private boolean fireHover;

	/*
	 * Activity
	 */
	// private final AppCompatActivity activity;

	/**
	 * Scale detector that keeps track of active pointers
	 */
	static private class XScaleGestureDetector extends ScaleGestureDetector
	{
		@NonNull
		private final SparseArray<PointF> activePointers;

		@SuppressWarnings("WeakerAccess")
		public XScaleGestureDetector(final Context context, final OnScaleGestureListener listener0)
		{
			super(context, listener0);
			this.activePointers = new SparseArray<>();
		}

		@SuppressWarnings("WeakerAccess")
		public void reset()
		{
			this.activePointers.clear();
		}

		@Override
		public boolean onTouchEvent(@NonNull final MotionEvent event)
		{
			// record pointers

			// get pointer index from the event object
			final int pointerIndex = event.getActionIndex();

			// get pointer ID
			final int pointerId = event.getPointerId(pointerIndex);

			// get masked (not specific to a pointer) action
			final int maskedAction = event.getActionMasked();

			switch (maskedAction)
			{

				// new pointer : add it to the list of pointers
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
				{

					@NonNull final PointF f = new PointF();
					f.x = event.getX(pointerIndex);
					f.y = event.getY(pointerIndex);
					this.activePointers.put(pointerId, f);
					break;
				}

				// a pointer was moved
				case MotionEvent.ACTION_MOVE:
				{
					for (int size = event.getPointerCount(), i = 0; i < size; i++)
					{
						final PointF point = this.activePointers.get(event.getPointerId(i));
						if (point != null)
						{
							point.x = event.getX(i);
							point.y = event.getY(i);
						}
					}
					break;
				}
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_CANCEL:
				{
					// this.activePointers.remove(pointerId);
					break;
				}
				default:
					break;
			}

			// super
			return super.onTouchEvent(event);
		}
	}

	// C O N S T R U C T O R

	/**
	 * Constructor
	 * API
	 *
	 * @param handle handle
	 */
	@SuppressWarnings("WeakerAccess")
	public Surface(final Object handle)
	{
		this((AppCompatActivity) handle);
	}

	/**
	 * Constructor
	 *
	 * @param activity activity
	 */
	@SuppressWarnings("WeakerAccess")
	public Surface(@NonNull final AppCompatActivity activity)
	{
		super(activity);

		// this.activity = activity;

		// for debugging
		this.setId(R.id.treebolicId);

		// content description
		this.setContentDescription(activity.getString(R.string.desc_surface));

		// graphics init
		Graphics.init(activity);

		// register our interest in hearing about changes to our surface
		final SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		// create thread only (thread is started in surfaceCreated())
		this.thread = new TreebolicThread(this, holder);

		// listener
		this.listener = null;

		// gesture detector
		this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onDown(@NonNull final MotionEvent event)
			{
				// returning false would result in the sequel of events not being dispatched to detector
				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(@NonNull final MotionEvent event)
			{
				// if(LOG) Log.d(TAG, "single tap confirmed");
				// Surface.this.listener.onFocus((int) event.getX(), (int) event.getY());
				return false;
			}

			@Override
			public void onLongPress(@NonNull final MotionEvent event)
			{
				// if(LOG) Log.d(TAG, "long press");
				// Surface.this.listener.onMenu((int) event.getX(), (int) event.getY());
			}

			@Override
			public boolean onDoubleTap(@NonNull final MotionEvent event)
			{
				// if(LOG) Log.d(TAG, "double tap");
				// Surface.this.listener.onLink((int) event.getX(), (int) event.getY());
				assert Surface.this.listener != null;
				Surface.this.listener.onMenu((int) event.getX(), (int) event.getY());
				return false;
			}

			// @Override
			// public boolean onDoubleTapEvent(final MotionEvent event)
			// {
			// if(LOG) Log.d(TAG, "double tap event");
			// return false;
			// }

			// @Override
			// public boolean onSingleTapUp(MotionEvent event)
			// {
			// if(LOG) Log.d(LOG_TAG, "single tap up");
			// return false;
			// }

			@Override
			public boolean onFling(@Nullable MotionEvent event1, @NonNull final MotionEvent event2, final float velocityX, final float velocityY)
			{
				// if(LOG) Log.d(TAG, "fling");
				return false;
			}

			@Override
			public boolean onScroll(@Nullable final MotionEvent event1, @NonNull final MotionEvent event2, final float distanceX, final float distanceY)
			{
				// if(LOG) Log.d(TAG, "scroll");
				return false;
			}

			@Override
			public void onShowPress(@NonNull final MotionEvent event)
			{
				// if(LOG) Log.d(TAG, "show press");
				assert Surface.this.listener != null;
				Surface.this.listener.onSelect((int) event.getX(), (int) event.getY());
			}
		});
		this.gestureDetector.setIsLongpressEnabled(true);

		// scale detector
		this.scaleDetector = new XScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener()
		{
			@Override
			public boolean onScale(@NonNull final ScaleGestureDetector detector)
			{
				// scaleFactor change since previous event
				Surface.this.scaleFactor *= detector.getScaleFactor();
				if (LOG)
				{
					Log.d(TAG, "scaleFactor " + Surface.this.scaleFactor);
				}
				return true;
			}

			@Override
			public boolean onScaleBegin(@NonNull final ScaleGestureDetector detector)
			{
				Surface.this.isScaling = true;
				Surface.this.scaleFactor = 1F;
				if (LOG)
				{
					Log.d(TAG, "scale begin");
				}
				return true;
			}

			@Override
			public void onScaleEnd(@NonNull final ScaleGestureDetector detector)
			{
				// accumulated scale
				final float scale = -Surface.this.scaleFactor;

				// zoom or scale
				final XScaleGestureDetector xdetector = (XScaleGestureDetector) detector;
				@Nullable PointF left = null;
				@Nullable PointF right = null;
				for (int i = 0; i < xdetector.activePointers.size(); i++)
				{
					final int key = xdetector.activePointers.keyAt(i);

					// get the object by the key
					final PointF p = xdetector.activePointers.get(key);
					if (left == null || p.x < left.x)
					{
						left = p;
					}
					if (right == null || p.x > right.x)
					{
						right = p;
					}
				}
				boolean zoom = left != null && /* right != null && */ left.y < right.y;

				// fire event
				assert Surface.this.listener != null;
				if (zoom)
				{
					Surface.this.listener.onZoom(scale, 0, 0);
					if (LOG)
					{
						Log.d(TAG, "zoom: " + scale);
					}
				}
				else
				{
					Surface.this.listener.onScale(0, scale, scale);
					if (LOG)
					{
						Log.d(TAG, "scale: " + scale);
					}
				}

				// reset
				Surface.this.scaleFactor = 1F;
				xdetector.reset();

				// wait for delay until dragging is allowed
				final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
				exec.schedule((Runnable) () -> Surface.this.isScaling = false, 500, TimeUnit.MILLISECONDS);
			}
		});

		// features
		setFocusable(true); // make sure we get key events
	}

	// T H R E A D

	/**
	 * Fetches the animation thread corresponding to this View.
	 *
	 * @return the animation thread
	 */
	@Nullable
	public TreebolicThread getThread()
	{
		return this.thread;
	}

	/**
	 * Run thread: recreate if terminated and start
	 */
	private void runThread()
	{
		if (this.thread == null || this.thread.getState() == Thread.State.TERMINATED)
		{
			if (LOG)
			{
				Log.d(TAG, "thread created");
			}
			this.thread = new TreebolicThread(this, getHolder());
		}

		// do not terminate
		this.thread.setTerminate(false);

		// start if not
		if (this.thread.getState() == Thread.State.NEW)
		{
			if (LOG)
			{
				Log.d(TAG, "thread started");
			}
			this.thread.start();
		}
	}

	// S U R F A C E C A L L B A C K S

	/*
	 * Callback invoked when the Surface has been created and is ready to be used.
	 */
	@Override
	public void surfaceCreated(@NonNull final SurfaceHolder holder0)
	{
		if (LOG)
		{
			Log.d(TAG, "surface created");
		}

		// start the thread here so that we don't busy-wait in run() waiting for the surface to be created
		runThread();
	}

	/*
	 * Callback invoked when the surface dimensions change.
	 */
	@Override
	public void surfaceChanged(@NonNull final SurfaceHolder holder0, final int format, final int width, final int height)
	{
		if (LOG)
		{
			Log.d(TAG, "surface changed");
		}

		assert this.thread != null;
		this.thread.unpause();
	}

	/*
	 * Callback invoked when the Surface has been destroyed and must no longer be touched. WARNING: after this method returns, the Surface/Canvas must never be
	 * touched again!
	 */
	@Override
	public void surfaceDestroyed(@NonNull final SurfaceHolder holder0)
	{
		if (LOG)
		{
			Log.d(TAG, "surface destroyed");
		}

		// tell thread to shut down & wait for it to finish
		assert this.thread != null;
		this.thread.waitForTermination();
		this.thread = null;
	}

	// R E P A I N T

	@Override
	public void repaint()
	{
		if (LOG)
		{
			Log.d(TAG, "surface repainting");
		}
		runThread();
		assert this.thread != null;
		this.thread.unpause();
		if (LOG)
		{
			Log.d(TAG, "surface repainted");
		}
	}

	// O T H E R

	// @Override
	// public int getWidth();

	// @Override
	// public int getHeight();

	@SuppressWarnings({"EmptyMethod", "WeakerAccess"})
	@Override
	public void setCursor(final int cursor)
	{
		// pointless
	}

	@SuppressWarnings("EmptyMethod")
	@Override
	public void setToolTipText(@Nullable final String text)
	{
		// if (text != null) {
		// final AppCompatActivity activity = (AppCompatActivity) this.activity;
		// final FragmentManager manager = activity.getSupportFragmentManager();
		// Tip.tip(manager, text);
		//}
	}

	// T O U C H A N D H O V E R

	@Override
	public boolean onTouchEvent(@NonNull final MotionEvent event)
	{
		// scaleFactor detection hook
		this.scaleDetector.onTouchEvent(event);

		// gesture detection hook
		this.gestureDetector.onTouchEvent(event);

		// standard handling
		if (this.listener != null)
		{
			if (!Surface.this.isScaling)
			{
				final int action = event.getActionMasked();
				switch (action)
				{
					case MotionEvent.ACTION_DOWN:
						// if(LOG) Log.d(TAG, "touch down");
						this.listener.onDown((int) event.getX(), (int) event.getY(), false);
						break;

					case MotionEvent.ACTION_MOVE:
						// if(LOG) Log.d(TAG, "touch move");
						this.listener.onDragged((int) event.getX(), (int) event.getY());
						break;

					case MotionEvent.ACTION_UP:

					case MotionEvent.ACTION_CANCEL:
						// if(LOG) Log.d(TAG, "touch cancel")
						// if(LOG) Log.d(TAG, "touch up")
						this.listener.onUp((int) event.getX(), (int) event.getY());
						break;

					default:
						break;
				}
				// common to handled cases above
				return true;
			}
		}
		return super.onTouchEvent(event);
	}

	@SuppressWarnings("WeakerAccess")
	@Override
	public void addEventListener(final EventListener listener0)
	{
		this.listener = listener0;
	}

	@SuppressWarnings("WeakerAccess")
	@Override
	public void setFireHover(final boolean flag)
	{
		this.fireHover = flag;
	}

	@Override
	public boolean onHoverEvent(@NonNull final MotionEvent event)
	{
		if (this.fireHover)
		{
			assert this.listener != null;
			this.listener.onHover((int) event.getX(), (int) event.getY());
		}
		return super.onHoverEvent(event);
	}

	// F I N D I N G   P R E C I S I O N

	@Override
	public float getFinderDistanceEpsilonFactor()
	{
		return FIND_DISTANCE_EPSILON_FACTOR;
	}
}
