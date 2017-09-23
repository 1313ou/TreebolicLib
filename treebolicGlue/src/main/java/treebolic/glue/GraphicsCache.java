package treebolic.glue;

import android.graphics.Canvas;
import android.graphics.Picture;

import treebolic.glue.component.Component;

public class GraphicsCache implements treebolic.glue.iface.GraphicsCache<Graphics>
{
	private static final boolean CACHE = true;

	private final Picture picture;

	private final Canvas canvas;

	private final int width;

	private final int height;

	public GraphicsCache(@SuppressWarnings({"unused", "UnusedParameters"}) final Component component, final Graphics thatGraphics, final int width0, final int height0)
	{
		this.canvas = thatGraphics.canvas;
		if (GraphicsCache.CACHE)
		{
			this.picture = new Picture();
			this.width = width0;
			this.height = height0;
		}
		else
		{
			this.picture = null;
			this.width = 0;
			this.height = 0;
		}
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public Graphics getGraphics()
	{
		if (GraphicsCache.CACHE)
		{
			final Canvas thisCanvas = this.picture.beginRecording(this.width, this.height);
			return new Graphics(thisCanvas);
		}
		return new Graphics(this.canvas);
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void put(final Graphics thisGraphics)
	{
		if (GraphicsCache.CACHE)
		{
			this.picture.endRecording();
			thisGraphics.canvas.drawPicture(this.picture);
		}
	}
}
