package treebolic.glue;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.TypedValue;

import org.treebolic.glue.R;

/**
 * Graphics content and toolkit
 *
 * @author Bernard Bou
 */
public class Graphics implements treebolic.glue.iface.Graphics<Color, Image>
{
	public static final int PLAIN = 0;

	@SuppressWarnings("WeakerAccess")
	public static final int BOLD = 1;

	/**
	 * Font factor to convert pt (point) to pixel units used by Paint.setTextSize
	 */
	@SuppressWarnings("WeakerAccess")
	static public float PT2PX = 160 * (1.0f / 72);

	/**
	 * Font factor may be changed by application depending on screen size
	 */
	@SuppressWarnings("WeakerAccess")
	static public float fontFactor = 1F;

	/**
	 * Canvas
	 */
	final public Canvas canvas;

	/**
	 * Paint
	 */
	private final Paint paint;

	/**
	 * Saved stroke
	 */
	private float strokeWidth;
	private PathEffect strokeEffect;

	/**
	 * Dot effect
	 */
	static private final PathEffect dotEffect = new DashPathEffect(new float[] { 5, 2 }, 0); // on-interval off-interval

	/**
	 * Dash effect
	 */
	static private final PathEffect dashEffect = new DashPathEffect(new float[] { 2, 2 }, 0); // on-interval off-interval

	// S T A T I C I N I T

	static private boolean initDone = false;

	/**
	 * Init
	 *
	 * @param context
	 *            context application context
	 */
	static public void init(final Context context)
	{
		if (!Graphics.initDone)
		{
			final Resources resources = context.getApplicationContext().getResources();

			// font factor for resolution
			Graphics.PT2PX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, 1, resources.getDisplayMetrics());

			// font factor for screen size
			final TypedValue outValue = new TypedValue();
			resources.getValue(R.dimen.font_factor, outValue, true);
			Graphics.fontFactor = outValue.getFloat();

			Graphics.initDone = true;
		}
	}

	// C O N S T R U C T

	/**
	 * Construct
	 *
	 * @param canvas0
	 *            canvas
	 */
	public Graphics(final Canvas canvas0)
	{
		super();
		this.canvas = canvas0;
		// if (theScale != 1F)
		// this.canvas.scale(theScale, theScale, this.canvas.getWidth() / 2F, this.canvas.getHeight() / 2F);
		// if (Graphics.scale != 1F)
		// this.theCanvas.scale(Graphics.scale, Graphics.scale, this.theCanvas.getWidth() / 2F, this.theCanvas.getHeight() / 2F);

		this.paint = new Paint();
		this.paint.setAntiAlias(true);
		this.paint.setHinting(Paint.HINTING_ON); // font

		this.paint.setStrokeWidth(2);
		this.paint.setStrokeCap(Cap.BUTT);
		this.paint.setStrokeJoin(Join.BEVEL);
		this.paint.setStrokeMiter(1);
		this.paint.setColor(android.graphics.Color.WHITE);
	}

	// B A C K G R O U N D


	@Override
	public void drawBackgroundColor(final Color color, final int left, final int top, final int width, final int height)
	{
		this.canvas.drawColor(color.getOpaqueRGB());

		// alternatively:
		// setColor(color);
		// fillRectangle(left, top, width, height);
	}

	// D R A W


	@Override
	public void drawLine(final int x1, final int y1, final int x2, final int y2)
	{
		this.paint.setStyle(Style.STROKE);
		this.canvas.drawLine(x1, y1, x2, y2, this.paint);
	}


	@Override
	public void drawArc(final float x, final float y, final float w, final float h, final float start, final float extent)
	{
		final RectF oval = Rectangle2D.makeRect(x, y, w, h);
		this.paint.setStyle(Style.STROKE);
		this.canvas.drawArc(oval, -start, -extent, false, this.paint); // android: angles start at 3:00 and rotate clockwise
	}


	@Override
	public void drawPolyline(final int[] x, final int[] y, final int length)
	{
		final Path path = new Path();
		path.reset(); // only needed when reusing this path for a new build
		int i = 0;
		path.moveTo(x[i], y[i]); // used for first point
		for (++i; i < length; i++)
		{
			path.lineTo(x[i], y[i]);
		}
		this.paint.setStyle(Style.STROKE);
		this.canvas.drawPath(path, this.paint);
	}


	@Override
	public void drawPolygon(final int[] x, final int[] y, final int length)
	{
		final Path path = new Path();
		path.reset(); // only needed when reusing this path for a new build
		path.moveTo(x[0], y[0]); // used for first point
		for (int i = 1; i < length; i++)
		{
			path.lineTo(x[i], y[i]);
		}
		path.lineTo(x[0], y[0]);
		this.paint.setStyle(Style.STROKE);
		this.canvas.drawPath(path, this.paint);
	}


	@Override
	public void fillPolygon(final int[] x, final int[] y, final int length)
	{
		final Path path = new Path();
		path.reset(); // only needed when reusing this path for a new build
		path.moveTo(x[0], y[0]); // used for first point
		for (int i = 1; i < length; i++)
		{
			path.lineTo(x[i], y[i]);
		}
		path.lineTo(x[0], y[0]);
		this.paint.setStyle(Style.FILL_AND_STROKE);
		this.canvas.drawPath(path, this.paint);
	}


	@Override
	public void fillRectangle(final int left, final int top, final int width, final int height)
	{
		final RectF rect = Rectangle2D.makeRect(left, top, width, height);
		this.paint.setStyle(Style.FILL_AND_STROKE);
		this.canvas.drawRect(rect, this.paint);
	}


	@Override
	public void drawRoundRectangle(final int left, final int top, final int width, final int height, final int rx, final int ry)
	{
		final RectF rect = Rectangle2D.makeRect(left, top, width, height);
		this.paint.setStyle(Style.STROKE);
		this.canvas.drawRoundRect(rect, rx, ry, this.paint);
	}


	@Override
	public void fillRoundRectangle(final int left, final int top, final int width, final int height, final int rx, final int ry)
	{
		final RectF rect = Rectangle2D.makeRect(left, top, width, height);
		this.paint.setStyle(Style.FILL_AND_STROKE);
		this.canvas.drawRoundRect(rect, rx, ry, this.paint);
	}


	@Override
	public void drawOval(final float left, final float top, final float width, final float height)
	{
		final RectF rect = Rectangle2D.makeRect(left, top, width, height);
		this.paint.setStyle(Style.STROKE);
		this.canvas.drawOval(rect, this.paint);
	}


	@Override
	public void fillOval(final float left, final float top, final float width, final float height)
	{
		final RectF rect = Rectangle2D.makeRect(left, top, width, height);
		this.paint.setStyle(Style.FILL_AND_STROKE);
		this.canvas.drawOval(rect, this.paint);
	}


	@Override
	public void drawString(final String str, final int x, final int y)
	{
		this.paint.setStyle(Style.FILL);
		this.paint.setTextAlign(Align.LEFT);
		this.canvas.drawText(str, x, y, this.paint);
	}


	@Override
	public void drawImage(final Image image, final int x, final int y)
	{
		if (image.bitmap != null)
		{
			this.canvas.drawBitmap(image.bitmap, x, y, this.paint);
		}
	}


	@Override
	public void drawImage(final Image image, final int x, final int y, final int w, final int h)
	{
		if (image.bitmap != null)
		{
			final Rect r = new Rect(x, y, x + w, y + h);
			this.canvas.drawBitmap(image.bitmap, null, r, this.paint);
		}
	}

	// P A I N T

	@Override
	public void setColor(final Color color)
	{
		if (!color.isNull())
		{
			this.paint.setColor(color.getRGB());
			this.paint.setAlpha(255);
		}
	}

	@Override
	public Color getColor()
	{
		return new Color(this.paint.getColor());
	}

	@Override
	public void setFont(final String face0, final int style0)
	{
		final Typeface thisTypeface = Typeface.create(face0, style0 == Graphics.BOLD ? Typeface.BOLD : Typeface.NORMAL);
		this.paint.setTypeface(thisTypeface);
	}

	@Override
	public void setTextSize(final float size)
	{
		this.paint.setTextSize(size * Graphics.fontFactor * Graphics.PT2PX);
	}

	@Override
	public int stringWidth(final String string)
	{
		final Rect bounds = new Rect();
		this.paint.getTextBounds(string, 0, string.length(), bounds);
		return (int) this.paint.measureText(string);
	}

	@Override
	public int getDescent()
	{
		return (int) this.paint.descent();
	}

	@Override
	public int getAscent()
	{
		// ascent is returned negative by android
		return -(int) this.paint.ascent();
	}

	@Override
	public void setStroke(final int stroke, int width)
	{
		this.paint.setStyle(Style.STROKE);
		switch (stroke)
		{
		case treebolic.glue.iface.Graphics.SOLID:
			this.paint.setPathEffect(null);
			break;
		case treebolic.glue.iface.Graphics.DOT:
			this.paint.setPathEffect(Graphics.dotEffect);
			break;
		case treebolic.glue.iface.Graphics.DASH:
			this.paint.setPathEffect(Graphics.dashEffect);
			break;
		default:
			break;
		}
		if (width <= 0)
		{
			this.paint.setStrokeWidth(0);
		}
		else
		{
			this.paint.setStrokeWidth(width);
		}
	}

	@Override
	public void pushStroke()
	{
		this.strokeEffect = this.paint.getPathEffect();
		this.strokeWidth = this.paint.getStrokeWidth();
	}

	@Override
	public void popStroke()
	{
		this.paint.setPathEffect(this.strokeEffect);
		this.paint.setStrokeWidth(this.strokeWidth);
	}

	// T R A N S F O R M S

	@Override
	public void translate(final float dx, final float dy)
	{
		this.canvas.translate(dx, dy);
	}

	@Override
	public void rotate(final float theta, final float px, final float py)
	{
		this.canvas.translate(px, py);
		this.canvas.rotate((float) Math.toDegrees(theta));
	}

	@Override
	public void scale(final float factor, final float px, final float py)
	{
		this.canvas.scale(factor, factor, px, py);
	}

	@Override
	public void pushMatrix()
	{
		this.canvas.save(Canvas.MATRIX_SAVE_FLAG);
	}

	@Override
	public void popMatrix()
	{
		this.canvas.restore();
	}
}
