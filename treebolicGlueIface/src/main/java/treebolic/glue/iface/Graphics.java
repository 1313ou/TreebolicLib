/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.glue.iface;

import androidx.annotation.NonNull;

/**
 * Glue interface for Graphics context
 *
 * @param <C> color
 * @param <I> image
 * @author Bernard Bou
 */
public interface Graphics<C, I>
{
	// P O L Y G O N

	/**
	 * Draw background color
	 *
	 * @param color  color
	 * @param left   left
	 * @param top    top
	 * @param width  width
	 * @param height height
	 */
	void drawBackgroundColor(final C color, final int left, final int top, final int width, final int height);

	// L I N E

	/**
	 * Straight line
	 *
	 * @param x  x-from
	 * @param y  y-from
	 * @param x2 x-to
	 * @param y2 y-to
	 */
	void drawLine(final int x, final int y, final int x2, final int y2);

	/**
	 * Arc
	 *
	 * @param x          left of enclosing rectangle
	 * @param y          top of enclosing rectangle
	 * @param w          width of enclosing rectangle
	 * @param h          height of enclosing rectangle
	 * @param startAngle start angle
	 * @param arcAngle   extend angle
	 */
	void drawArc(final float x, final float y, final float w, final float h, final float startAngle, final float arcAngle);

	/**
	 * Polyline
	 *
	 * @param x      array of xs
	 * @param y      array of ys
	 * @param length number of points
	 */
	void drawPolyline(final int[] x, final int[] y, final int length);

	// P O L Y G O N

	/**
	 * Draw polygon
	 *
	 * @param x      array of xs
	 * @param y      array of ys
	 * @param length number of points
	 */
	void drawPolygon(final int[] x, final int[] y, final int length);

	/**
	 * Fill polygon
	 *
	 * @param x      array of xs
	 * @param y      array of ys
	 * @param length number of points
	 */
	void fillPolygon(final int[] x, final int[] y, final int length);

	// O V A L

	/**
	 * Draw oval
	 *
	 * @param x       x-center
	 * @param y       y-center
	 * @param xradius x-radius
	 * @param yradius y-radius
	 */
	void drawOval(final float x, final float y, final float xradius, final float yradius);

	/**
	 * Fill oval
	 *
	 * @param x       x-center
	 * @param y       y-center
	 * @param xradius x-radius
	 * @param yradius y-radius
	 */
	void fillOval(final float x, final float y, final float xradius, final float yradius);

	// R E C T A N G L E

	/**
	 * Fill rectangle
	 *
	 * @param x left
	 * @param y top
	 * @param w width
	 * @param h height
	 */
	void fillRectangle(final int x, final int y, final int w, final int h);

	/**
	 * Draw rectangle
	 *
	 * @param x  left
	 * @param y  top
	 * @param w  width
	 * @param h  height
	 * @param rx x-radius for corners
	 * @param ry y-radius for corners
	 */
	void drawRoundRectangle(int x, int y, int w, int h, int rx, int ry);

	/**
	 * Fill rectangle
	 *
	 * @param x  left
	 * @param y  top
	 * @param w  width
	 * @param h  height
	 * @param rx x-radius for corners
	 * @param ry y-radius for corners
	 */
	void fillRoundRectangle(int x, int y, int w, int h, int rx, int ry);

	/**
	 * Draw string
	 *
	 * @param str string
	 * @param x   x-location
	 * @param y   y-location
	 */
	void drawString(final String str, final int x, final int y);

	// I M A G E

	/**
	 * Draw image
	 *
	 * @param image image
	 * @param x     x-location
	 * @param y     y-location
	 */
	void drawImage(final I image, final int x, final int y);

	/**
	 * Draw image
	 *
	 * @param image image
	 * @param x     x-location
	 * @param y     y-location
	 * @param w     width
	 * @param h     height
	 */
	void drawImage(final I image, final int x, final int y, final int w, final int h);

	// C O L O R

	/**
	 * Set color
	 *
	 * @param color color
	 */
	void setColor(final C color);

	/**
	 * Get color
	 *
	 * @return color
	 */
	@NonNull
	C getColor();

	// F O N T

	/**
	 * Set font
	 *
	 * @param fontface font face
	 * @param style    style
	 */
	void setFont(final String fontface, int style);

	/**
	 * Set text
	 *
	 * @param size size
	 */
	void setTextSize(float size);

	/**
	 * Get string width
	 *
	 * @param string string
	 * @return string width
	 */
	int stringWidth(String string);

	/**
	 * Get font ascent
	 *
	 * @return font ascent
	 */
	int getAscent();

	/**
	 * Get font descent
	 *
	 * @return font descent
	 */
	int getDescent();

	// S T R O K E

	int SOLID = 1;

	int DOT = 2;

	int DASH = 3;

	/**
	 * Set stroke
	 *
	 * @param stroke stroke type
	 * @param width  stroke width
	 */
	void setStroke(int stroke, int width);

	/**
	 * Push stroke
	 */
	void pushStroke();

	/**
	 * Pop stroke
	 */
	void popStroke();

	// T R A N S F O R M

	/**
	 * Save matrix
	 */
	void pushMatrix();

	/**
	 * Restore matrix
	 */
	void popMatrix();

	/**
	 * Translate
	 *
	 * @param x x-translation
	 * @param y y-translation
	 */
	void translate(final float x, final float y);

	/**
	 * Rotate
	 *
	 * @param theta rotation angle
	 * @param x     pivot x (pivot is the point that maps to itself)
	 * @param y     pivot y (pivot is the point that maps to itself)
	 */
	void rotate(float theta, float x, float y);

	/**
	 * Scale
	 *
	 * @param factor scaling factor
	 * @param x      pivot x (pivot is the point that maps to itself)
	 * @param y      pivot y (pivot is the point that maps to itself)
	 */
	void scale(float factor, float x, float y);
}
