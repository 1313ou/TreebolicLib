/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package treebolic.glue;

/**
 * Point
 *
 * @author Bernard Bou
 */
public class Point extends android.graphics.Point implements treebolic.glue.iface.Point
{
	/**
	 * Constructor
	 *
	 * @param x0 x
	 * @param y0 y
	 */
	public Point(final int x0, final int y0)
	{
		super(x0, y0);
	}

	@Override
	public int x()
	{
		return x;
	}

	@Override
	public int y()
	{
		return y;
	}
}
