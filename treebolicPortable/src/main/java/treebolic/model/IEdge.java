/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
package treebolic.model;

import java.io.Serializable;

import treebolic.glue.Color;
import treebolic.glue.Image;

/**
 * @formatter:off
 * Edge interface Style bits (32): 00dd-dddd-tttt-tttt-ffff-ffff-aaaa-aaaa with a=arc t=to-terminator f=from-terminator 
 * byte 1 = arc 
 * 		wwww dDlh with wwww=width d=dot D=dash l=line h=hidden
 * byte 2 = from-terminator
 * 		SSSS 000f with S=shape, f=fill
 * byte 3 = to terminator 
 * 		SSSS 000f with S=shape, f=fill
 * Shapes 
 * 	0001 = 1 = triangle
 *  0010 = 2 = circle 
 *  0011 = 3 = diamond 
 *  0100 = 4 = arrow
 *  0101 = 5 = hook
 * @formatter:on
 * @author Bernard Bou
 */
public interface IEdge extends Serializable
{
	/**
	 * Define bits
	 */
	static public final int HIDDENDEF = 0x01000000;

	static public final int LINEDEF = 0x02000000;

	static public final int STROKEDEF = 0x04000000;

	static public final int STROKEWIDTHDEF = 0x08000000;

	static public final int FROMDEF = 0x10000000;

	static public final int TODEF = 0x20000000;

	// S T R O K E

	/**
	 * Hidden bit
	 */
	static public final byte HIDDEN = 0x1;

	/**
	 * Straight Line bit
	 */
	static public final byte LINE = 0x2;

	// stroke ---- dDs-

	/**
	 * Stroke style bits
	 */
	static public final int STROKEMASK = 0xc;

	/**
	 * Solid style bit
	 */
	static public final byte SOLID = 0x0;

	/**
	 * Dash style bit
	 */
	static public final byte DASH = 0x4;

	/**
	 * Dotted style bit
	 */
	static public final byte DOT = 0x8;

	// stroke width wwww ----

	/**
	 * Stroke width bits
	 */
	static public final int STROKEWIDTHMASK = 0xf0;

	/**
	 * From-terminator shift
	 */
	static public final int STROKEWIDTHSHIFT = 4;

	// T E R M I N A T O R S

	// xxxx ---- mask
	// 0001 ---- triangle
	// 0010 ---- circle
	// 0011 ---- diamond
	// 0100 ---- arrow
	// 0101 ---- hook

	/**
	 * Terminator mask
	 */
	static public final int SHAPEMASK = 0xf0;

	/**
	 * Triangle terminator style
	 */
	static public final int TRIANGLE = 0x10;

	/**
	 * Circle terminator style
	 */
	static public final int CIRCLE = 0x20;

	/**
	 * Diamond terminator style
	 */
	static public final int DIAMOND = 0x30;

	/**
	 * Arrow terminator style
	 */
	static public final int ARROW = 0x40;

	/**
	 * Arrow terminator style
	 */
	static public final int HOOK = 0x50;

	/**
	 * Fill terminator flag
	 */
	static public final int FILL = 0x01;

	// F R O M T E R M I N A T O R
	// 0000 0000 0000 0000 ffff ffff <<<< <<<<

	/**
	 * From-terminator mask
	 */
	static public final int FROMMASK = 0x0000ff00;

	/**
	 * From-terminator shift
	 */
	static public final int FROMSHIFT = 8;

	/**
	 * Solid from-terminator bit
	 */
	static public final int FROMFILL = IEdge.FILL << IEdge.FROMSHIFT;

	/**
	 * Triangle from-terminator bit
	 */
	static public final int FROMTRIANGLE = IEdge.TRIANGLE << IEdge.FROMSHIFT;

	/**
	 * Circle from-terminator bit
	 */
	static public final int FROMCIRCLE = IEdge.CIRCLE << IEdge.FROMSHIFT;

	/**
	 * Diamond from-terminator bit
	 */
	static public final int FROMDIAMOND = IEdge.DIAMOND << IEdge.FROMSHIFT;

	/**
	 * Arrow from-terminator bit
	 */
	static public final int FROMARROW = IEdge.ARROW << IEdge.FROMSHIFT;

	/**
	 * Hook from-terminator bit
	 */
	static public final int FROMHOOK = IEdge.HOOK << IEdge.FROMSHIFT;

	// T O T E R M I N A T O R
	// 0000 0000 ttttt tttt <<<< <<<< <<<< <<<<

	/**
	 * To-terminator mask
	 */
	static public final int TOMASK = 0x00ff0000;

	/**
	 * To-terminator shift
	 */
	static public final int TOSHIFT = 16;

	/**
	 * Solid to-terminator bit
	 */
	static public final int TOFILL = IEdge.FILL << IEdge.TOSHIFT;

	/**
	 * Triangle to-terminator bit
	 */
	static public final int TOTRIANGLE = IEdge.TRIANGLE << IEdge.TOSHIFT;

	/**
	 * Circle to-terminator bit
	 */
	static public final int TOCIRCLE = IEdge.CIRCLE << IEdge.TOSHIFT;

	/**
	 * Diamond to-terminator bit
	 */
	static public final int TODIAMOND = IEdge.DIAMOND << IEdge.TOSHIFT;

	/**
	 * Arrow to-terminator bit
	 */
	static public final int TOARROW = IEdge.ARROW << IEdge.TOSHIFT;

	/**
	 * Arrow to-terminator bit
	 */
	static public final int TOHOOK = IEdge.HOOK << IEdge.TOSHIFT;

	// M E T H O D S

	/**
	 * Get from-node
	 *
	 * @return from-node
	 */
	public INode getFrom();

	/**
	 * Get to-node
	 *
	 * @return to-node
	 */
	public INode getTo();

	/**
	 * Get edge label
	 *
	 * @return edge label
	 */
	public String getLabel();

	/**
	 * Get edge color
	 *
	 * @return edge color
	 */
	public Color getColor();

	/**
	 * Set edge style
	 *
	 * @return edge style
	 */
	public Integer getStyle();

	/**
	 * Get edge image filename
	 *
	 * @return path to edge image filename
	 */
	public String getImageFile();

	/**
	 * Get edge image index
	 *
	 * @return edge image index
	 */
	public int getImageIndex();

	/**
	 * Get edge image
	 *
	 * @return edge image
	 */
	public Image getImage();

	/**
	 * Set image file
	 *
	 * @param thisImage
	 *        edge image
	 */
	public void setImage(Image thisImage);
}
