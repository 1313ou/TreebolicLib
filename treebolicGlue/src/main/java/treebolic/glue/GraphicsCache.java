/**
 *
 */
package treebolic.glue;

import android.graphics.Canvas;
import android.graphics.Picture;

import treebolic.glue.component.Component;

public class GraphicsCache implements treebolic.glue.iface.GraphicsCache<Graphics>
{
	private static boolean cache = true;

	private final Picture picture;

	private final Canvas canvas;

	private int width;

	private int height;

	public GraphicsCache(@SuppressWarnings("unused") final Component component, final Graphics thatGraphics, final int width0, final int height0)
	{
		this.canvas = thatGraphics.canvas;
		if (GraphicsCache.cache)
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
	public Graphics getGraphics()
	{
		if (GraphicsCache.cache)
		{
			final Canvas thisCanvas = this.picture.beginRecording(this.width, this.height);
			return new Graphics(thisCanvas);
		}
		return new Graphics(this.canvas);
	}

	@Override
	public void put(final Graphics thisGraphics)
	{
		if (GraphicsCache.cache)
		{
			this.picture.endRecording();
			thisGraphics.canvas.drawPicture(this.picture);
		}
	}
}
