package treebolic.glue.component;

import android.support.v7.app.AppCompatActivity;
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

import treebolic.glue.EventListener;
import treebolic.glue.Graphics;

/**
 * Surface treebolic.glue to serve as base for view
 *
 * @author Bernard Bou
 */
public abstract class Surface extends SurfaceView implements SurfaceHolder.Callback, Component, treebolic.glue.iface.component.Surface<Graphics, EventListener>
{
	/**
	 * Log tag
	 */
	static private final boolean LOG = false;
	static private final String TAG = "Treebolic Surface"; //$NON-NLS-1$

	/**
	 * Margin of error when finding node
	 */
	public static final double FINDERRORMARGINFACTOR = 2.5F;

	/**
	 * The thread that actually draws the animation
	 */
	private TreebolicThread thread;

	/**
	 * Touch, gesture, hover event listener
	 */
	private EventListener listener;

	/**
	 * Gesture detector
	 */
	private final GestureDetector gestureDetector;

	/**
	 * Scale detector
	 */
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

	/**
	 * Scale detector that keeps track of active pointers
	 */
	static private class XScaleGestureDetector extends ScaleGestureDetector
	{
		private final SparseArray<PointF> activePointers;

		public XScaleGestureDetector(final Context context, final OnScaleGestureListener listener0)
		{
			super(context, listener0);
			this.activePointers = new SparseArray<>();
		}

		public void reset()
		{
			this.activePointers.clear();
		}

		@Override
		public boolean onTouchEvent(final MotionEvent event)
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

				final PointF f = new PointF();
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
	 *
	 * @param thisHandle
	 *            handle
	 */
	public Surface(final Object thisHandle)
	{
		this((AppCompatActivity) thisHandle);
	}

	/**
	 * Constructor
	 *
	 * @param activity
	 *            activity
	 */
	public Surface(final AppCompatActivity activity)
	{
		super(activity);

		// for debugging
		this.setId(R.id.treebolicId);

		// graphics init
		Graphics.init(activity.getApplicationContext());

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
			public boolean onDown(final MotionEvent event)
			{
				// returning false would result in the sequel of events not being dispatched to detector
				return true;
			}

			@Override
			public void onLongPress(final MotionEvent event)
			{
				// if(LOG) Log.d(Surface.TAG, "long press");
				// Surface.this.listener.onMenu((int) event.getX(), (int) event.getY());
			}

			@SuppressWarnings("synthetic-access")
			@Override
			public boolean onDoubleTap(final MotionEvent event)
			{
				// if(LOG) Log.d(Surface.TAG, "double tap");
				// Surface.this.listener.onLink((int) event.getX(), (int) event.getY());
				Surface.this.listener.onMenu((int) event.getX(), (int) event.getY());
				return false;
			}

			// @Override
			// public boolean onDoubleTapEvent(final MotionEvent event)
			// {
			// if(LOG) Log.d(Surface.TAG, "double tap event");
			// return false;
			// }

			@Override
			public boolean onSingleTapConfirmed(final MotionEvent event)
			{
				// if(LOG) Log.d(Surface.TAG, "single tap confirmed");
				// Surface.this.listener.onFocus((int) event.getX(), (int) event.getY());
				return false;
			}

			// @Override
			// public boolean onSingleTapUp(MotionEvent event)
			// {
			// if(LOG) Log.d(LOG_TAG, "single tap up");
			// return false;
			// }

			@Override
			public boolean onFling(final MotionEvent event1, final MotionEvent event2, final float velocityX, final float velocityY)
			{
				// if(LOG) Log.d(Surface.TAG, "fling");
				return false;
			}

			@Override
			public boolean onScroll(final MotionEvent event1, final MotionEvent event2, final float distanceX, final float distanceY)
			{
				// if(LOG) Log.d(Surface.TAG, "scroll");
				return false;
			}

			@SuppressWarnings("synthetic-access")
			@Override
			public void onShowPress(final MotionEvent event)
			{
				// if(LOG) Log.d(Surface.TAG, "show press");
				Surface.this.listener.onSelect((int) event.getX(), (int) event.getY());
			}
		});
		this.gestureDetector.setIsLongpressEnabled(true);

		// scale detector
		this.scaleDetector = new XScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener()
		{
			@SuppressWarnings("synthetic-access")
			@Override
			public boolean onScale(final ScaleGestureDetector detector)
			{
				// scaleFactor change since previous event
				Surface.this.scaleFactor *= detector.getScaleFactor();
				if (LOG)
					Log.d(Surface.TAG, "scaleFactor " + Surface.this.scaleFactor); //$NON-NLS-1$
				return true;
			}

			@SuppressWarnings("synthetic-access")
			@Override
			public boolean onScaleBegin(final ScaleGestureDetector detector)
			{
				Surface.this.isScaling = true;
				Surface.this.scaleFactor = 1F;
				if (LOG)
					Log.d(Surface.TAG, "scale begin"); //$NON-NLS-1$
				return true;
			}

			@SuppressWarnings("synthetic-access")
			@Override
			public void onScaleEnd(final ScaleGestureDetector detector)
			{
				// accumulated scale
				final float scale = -Surface.this.scaleFactor;

				// zoom or scale
				final XScaleGestureDetector xdetector = (XScaleGestureDetector) detector;
				PointF left = null;
				PointF right = null;
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
				boolean zoom = false;
				//noinspection ConstantConditions
				if (left != null && right != null && left.y < right.y)
				{
					zoom = true;
				}

				// fire event
				if (zoom)
				{
					Surface.this.listener.onZoom(scale, 0, 0);
					if (LOG)
						Log.d(Surface.TAG, "zoom: " + scale); //$NON-NLS-1$
				}
				else
				{
					Surface.this.listener.onScale(0, scale, scale);
					if (LOG)
						Log.d(Surface.TAG, "scale: " + scale); //$NON-NLS-1$
				}

				// reset
				Surface.this.scaleFactor = 1F;
				xdetector.reset();

				// wait for delay until dragging is allowed
				final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
				exec.schedule(new Runnable()
				{
					@Override
					public void run()
					{
						Surface.this.isScaling = false;
					}
				}, 500, TimeUnit.MILLISECONDS);
			}
		});

		// features
		setFocusable(true); // make sure we get key events
	}

	// T H R E A D

	/**
	 * Fetches the animation thread corresponding to this LunarView.
	 *
	 * @return the animation thread
	 */
	public TreebolicThread getThread()
	{
		return this.thread;
	}

	/**
	 * Run thread: recreate if terminated and start
	 */
	public void runThread()
	{
		if (this.thread == null || this.thread.getState() == Thread.State.TERMINATED)
		{
			if (LOG)
				Log.d(Surface.TAG, "thread created"); //$NON-NLS-1$
			this.thread = new TreebolicThread(this, getHolder());
		}

		// do not terminate
		this.thread.setTerminate(false);

		// start if not
		if (this.thread.getState() == Thread.State.NEW)
		{
			if (LOG)
				Log.d(Surface.TAG, "thread started"); //$NON-NLS-1$
			this.thread.start();
		}
	}

	// S U R F A C E C A L L B A C K S

	/*
	 * Callback invoked when the Surface has been created and is ready to be used.
	 */
	@Override
	public void surfaceCreated(final SurfaceHolder holder0)
	{
		if (LOG)
			Log.d(Surface.TAG, "surface created"); //$NON-NLS-1$

		// start the thread here so that we don't busy-wait in run() waiting for the surface to be created
		runThread();
	}

	/*
	 * Callback invoked when the surface dimensions change.
	 */
	@Override
	public void surfaceChanged(final SurfaceHolder holder0, final int format, final int width, final int height)
	{
		if (LOG)
			Log.d(Surface.TAG, "surface changed"); //$NON-NLS-1$

		this.thread.unpause();
	}

	/*
	 * Callback invoked when the Surface has been destroyed and must no longer be touched. WARNING: after this method returns, the Surface/Canvas must never be
	 * touched again!
	 */
	@Override
	public void surfaceDestroyed(final SurfaceHolder holder0)
	{
		if (LOG)
			Log.d(Surface.TAG, "surface destroyed"); //$NON-NLS-1$

		// tell thread to shut down & wait for it to finish
		this.thread.waitForTermination();
		this.thread = null;
	}

	// R E P A I N T

	@Override
	public void repaint()
	{
		if (LOG)
			Log.d(Surface.TAG, "surface repainting"); //$NON-NLS-1$
		runThread();
		this.thread.unpause();
		if (LOG)
			Log.d(Surface.TAG, "surface repainted"); //$NON-NLS-1$
	}

	// O T H E R

	// @Override
	// public int getWidth();

	// @Override
	// public int getHeight();

	@Override
	public void setCursor(final int cursor)
	{
		// pointless
	}

	@Override
	public void setToolTipText(final String text)
	{
		if (text != null)
		{
			final AppCompatActivity host = (AppCompatActivity) getContext();
			Tip.tip(host, text);
		}
	}

	// T O U C H A N D H O V E R

	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		// scaleFactor detection hook
		this.scaleDetector.onTouchEvent(event);

		// gesture detection hook
		this.gestureDetector.onTouchEvent(event);

		// standard handling
		if (event != null)
		{
			if (this.listener != null)
			{
				if (!Surface.this.isScaling)
				{
					final int action = event.getActionMasked();
					switch (action)
					{
					case MotionEvent.ACTION_DOWN:
						// if(LOG) Log.d(Surface.TAG, "touch down");
						this.listener.onDown((int) event.getX(), (int) event.getY(), false);
						break;

					case MotionEvent.ACTION_MOVE:
						// if(LOG) Log.d(Surface.TAG, "touch move");
						this.listener.onDragged((int) event.getX(), (int) event.getY());
						break;

					case MotionEvent.ACTION_UP:
						// if(LOG) Log.d(Surface.TAG, "touch up");
						this.listener.onUp((int) event.getX(), (int) event.getY());
						break;

					case MotionEvent.ACTION_CANCEL:
						// if(LOG) Log.d(Surface.TAG, "touch cancel");
						this.listener.onUp((int) event.getX(), (int) event.getY());
						break;

					default:
						break;
					}
					// common to handled cases above
					return true;
				}
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void addEventListener(final EventListener listener0)
	{
		this.listener = listener0;
	}

	@Override
	public void setFireHover(final boolean flag)
	{
		this.fireHover = flag;
	}

	@Override
	public boolean onHoverEvent(final MotionEvent event)
	{
		if (this.fireHover)
		{
			this.listener.onHover((int) event.getX(), (int) event.getY());
		}
		return super.onHoverEvent(event);
	}
}
