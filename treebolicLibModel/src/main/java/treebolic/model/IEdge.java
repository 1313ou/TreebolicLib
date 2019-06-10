package treebolic.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

import treebolic.glue.Color;
import treebolic.glue.Image;

// @formatter:off
/**
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
 * @author Bernard Bou
 */
// @formatter:on
public interface IEdge extends Serializable
{
	/**
	 * Define bits
	 */
	int HIDDENDEF = 0x01000000;

	int LINEDEF = 0x02000000;

	int STROKEDEF = 0x04000000;

	int STROKEWIDTHDEF = 0x08000000;

	int FROMDEF = 0x10000000;

	int TODEF = 0x20000000;

	// S T R O K E

	/**
	 * Hidden bit
	 */
	byte HIDDEN = 0x1;

	/**
	 * Straight Line bit
	 */
	byte LINE = 0x2;

	// stroke ---- dDs-

	/**
	 * Stroke style bits
	 */
	int STROKEMASK = 0xc;

	/**
	 * Solid style bit
	 */
	byte SOLID = 0x0;

	/**
	 * Dash style bit
	 */
	byte DASH = 0x4;

	/**
	 * Dotted style bit
	 */
	byte DOT = 0x8;

	// stroke width wwww ----

	/**
	 * Stroke width bits
	 */
	int STROKEWIDTHMASK = 0xf0;

	/**
	 * From-terminator shift
	 */
	int STROKEWIDTHSHIFT = 4;

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
	int SHAPEMASK = 0xf0;

	/**
	 * Triangle terminator style
	 */
	int TRIANGLE = 0x10;

	/**
	 * Circle terminator style
	 */
	int CIRCLE = 0x20;

	/**
	 * Diamond terminator style
	 */
	int DIAMOND = 0x30;

	/**
	 * Arrow terminator style
	 */
	int ARROW = 0x40;

	/**
	 * Arrow terminator style
	 */
	int HOOK = 0x50;

	/**
	 * Fill terminator flag
	 */
	int FILL = 0x01;

	// F R O M T E R M I N A T O R
	// 0000 0000 0000 0000 ffff ffff <<<< <<<<

	/**
	 * From-terminator mask
	 */
	int FROMMASK = 0x0000ff00;

	/**
	 * From-terminator shift
	 */
	int FROMSHIFT = 8;

	/**
	 * Solid from-terminator bit
	 */
	int FROMFILL = IEdge.FILL << IEdge.FROMSHIFT;

	/**
	 * Triangle from-terminator bit
	 */
	int FROMTRIANGLE = IEdge.TRIANGLE << IEdge.FROMSHIFT;

	/**
	 * Circle from-terminator bit
	 */
	int FROMCIRCLE = IEdge.CIRCLE << IEdge.FROMSHIFT;

	/**
	 * Diamond from-terminator bit
	 */
	int FROMDIAMOND = IEdge.DIAMOND << IEdge.FROMSHIFT;

	/**
	 * Arrow from-terminator bit
	 */
	int FROMARROW = IEdge.ARROW << IEdge.FROMSHIFT;

	/**
	 * Hook from-terminator bit
	 */
	int FROMHOOK = IEdge.HOOK << IEdge.FROMSHIFT;

	// T O T E R M I N A T O R
	// 0000 0000 ttttt tttt <<<< <<<< <<<< <<<<

	/**
	 * To-terminator mask
	 */
	int TOMASK = 0x00ff0000;

	/**
	 * To-terminator shift
	 */
	int TOSHIFT = 16;

	/**
	 * Solid to-terminator bit
	 */
	int TOFILL = IEdge.FILL << IEdge.TOSHIFT;

	/**
	 * Triangle to-terminator bit
	 */
	int TOTRIANGLE = IEdge.TRIANGLE << IEdge.TOSHIFT;

	/**
	 * Circle to-terminator bit
	 */
	int TOCIRCLE = IEdge.CIRCLE << IEdge.TOSHIFT;

	/**
	 * Diamond to-terminator bit
	 */
	int TODIAMOND = IEdge.DIAMOND << IEdge.TOSHIFT;

	/**
	 * Arrow to-terminator bit
	 */
	int TOARROW = IEdge.ARROW << IEdge.TOSHIFT;

	/**
	 * Arrow to-terminator bit
	 */
	int TOHOOK = IEdge.HOOK << IEdge.TOSHIFT;

	// M E T H O D S

	/**
	 * Get from-node
	 *
	 * @return from-node
	 */
	INode getFrom();

	/**
	 * Get to-node
	 *
	 * @return to-node
	 */
	INode getTo();

	/**
	 * Get edge label
	 *
	 * @return edge label
	 */
	@Nullable
	String getLabel();

	/**
	 * Get edge color
	 *
	 * @return edge color
	 */
	@Nullable
	Color getColor();

	/**
	 * Set edge style
	 *
	 * @return edge style
	 */
	@Nullable
	Integer getStyle();

	/**
	 * Get edge image filename
	 *
	 * @return path to edge image filename
	 */
	@Nullable
	String getImageFile();

	/**
	 * Get edge image index
	 *
	 * @return edge image index
	 */
	int getImageIndex();

	/**
	 * Get edge image
	 *
	 * @return edge image
	 */
	@Nullable
	Image getImage();

	/**
	 * Set image file
	 *
	 * @param image edge image
	 */
	void setImage(Image image);
}
