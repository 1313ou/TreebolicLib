/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue;

/**
 * Color treebolic glue
 *
 * @author Bernard Bou
 */
public class Color
{
	/**
	 * Make opaque, ensure that color is opaque
	 *
	 * @param color color
	 * @return opaque color
	 */
	public static int makeOpaque(int color)
	{
		return 0xFF000000 | color;
	}
}
