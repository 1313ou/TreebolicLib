/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

package treebolic.glue;

/**
 * Color treebolic glue
 *
 * @author Bernard Bou
 * @noinspection WeakerAccess
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
