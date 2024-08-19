/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package treebolic.glue;

import android.graphics.Canvas;
import android.graphics.Picture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import treebolic.glue.component.Component;

/**
 * Graphics cache implementation
 *
 * @noinspection WeakerAccess
 */
public class GraphicsCache implements treebolic.glue.iface.GraphicsCache<Graphics>
{
	private static final boolean CACHE = true;

	@Nullable
	private final Picture picture;

	private final Canvas canvas;

	private final int width;

	private final int height;

	/**
	 * Graphics cache
	 *
	 * @param component component
	 * @param graphics  graphics context
	 * @param width     width
	 * @param height    height
	 */
	public GraphicsCache(@SuppressWarnings("unused") final Component component, @NonNull final Graphics graphics, final int width, final int height)
	{
		this.canvas = graphics.canvas;
		if (GraphicsCache.CACHE)
		{
			this.picture = new Picture();
			this.width = width;
			this.height = height;
		}
		else
		{
			this.picture = null;
			this.width = 0;
			this.height = 0;
		}
	}

	@NonNull
	@Override
	public Graphics getGraphics()
	{
		if (GraphicsCache.CACHE && this.picture != null)
		{
			final Canvas canvas = this.picture.beginRecording(this.width, this.height);
			return new Graphics(canvas);
		}
		return new Graphics(this.canvas);
	}

	@Override
	public void put(@NonNull final Graphics graphics)
	{
		if (GraphicsCache.CACHE && this.picture != null)
		{
			this.picture.endRecording();
			graphics.canvas.drawPicture(this.picture);
		}
	}
}
