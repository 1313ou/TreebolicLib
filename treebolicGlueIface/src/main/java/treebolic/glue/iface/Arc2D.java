/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.glue.iface;

import treebolic.annotations.NonNull;

/**
 * Glue interface for Arc2D
 *
 * @param <P> platform point type
 * @author Bernard Bou
 */
public interface Arc2D<P>
{
	/**
	 * Get x
	 *
	 * @return x
	 */
	double getX();

	/**
	 * Get y
	 *
	 * @return y
	 */
	double getY();

	/**
	 * Get width of the framing rectangle
	 *
	 * @return width
	 */
	double getWidth();

	/**
	 * Height of the framing rectangle
	 *
	 * @return height
	 */
	double getHeight();

	/**
	 * Set frame from center and one corner
	 *
	 * @param x  x-center
	 * @param y  y-center
	 * @param x1 x-corner
	 * @param y1 y-corner
	 */
	void setFrameFromCenter(final double x, final double y, final double x1, final double y1);

	/**
	 * Adjust angles from ends (while preserving enclosing rectangle)
	 *
	 * @param from from end
	 * @param to   to end
	 */
	void setAngles(final P from, final P to);

	/**
	 * Get x-center
	 *
	 * @return x-center
	 */
	double getCenterX();

	/**
	 * Get y-center
	 *
	 * @return y-center
	 */
	double getCenterY();

	/**
	 * Get start point
	 *
	 * @return start point
	 */
	@NonNull
	P getStartPoint();

	/**
	 * Get end point
	 *
	 * @return end point
	 */
	@NonNull
	P getEndPoint();

	/**
	 * Get angular extent of the arc in degrees.
	 *
	 * @return angle extent of the arc in degrees
	 */
	double getAngleExtent();

	/**
	 * Set angular extent of the arc in degrees.
	 *
	 * @param extent angle extent of the arc in degrees
	 */
	void setAngleExtent(final double extent);

	/**
	 * The starting angle of the arc in degrees.
	 *
	 * @return starting angle of the arc in degrees.
	 */
	double getAngleStart();

	/**
	 * Set starting angle of the arc in degrees.
	 *
	 * @param start starting angle of the arc in degrees.
	 */
	void setAngleStart(final double start);

	/**
	 * Get direction
	 *
	 * @return counterclockwise flag
	 */
	boolean getCounterclockwise();

	/**
	 * Set direction
	 *
	 * @param flag counterclockwise flag
	 */
	void setCounterclockwise(boolean flag);

	/**
	 * Whether arc contains angle
	 *
	 * @param angle angle
	 * @return true if arc contains angle
	 */
	boolean containsAngle(final double angle);
}
