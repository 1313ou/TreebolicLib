/**
 * Title : Treebolic
 * Description : Treebolic mutable
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Mon Mar 10 00:00:00 CEST 2008
 */
package treebolic.model;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import treebolic.control.Controller.MatchMode;
import treebolic.control.Controller.MatchScope;
import treebolic.glue.Color;
import treebolic.model.MenuItem.Action;

/**
 * Attribute utilities
 *
 * @author Bernard Bou
 */
public class Utils
{
	// S T Y L E

	static public final String NONE = ""; //$NON-NLS-1$

	/**
	 * Style component pointer
	 */
	static public enum StyleComponent
	{
		STROKE, STROKEWIDTH, FROMTERMINATOR, TOTERMINATOR, LINE, HIDDEN
	}

	// 0000 dddd ttttt tttt ffff ffff 0000 sssh

	/**
	 * Parse edge style
	 *
	 * @param thisStroke
	 *        stroke
	 * @param thisFromTerminator
	 *        from-terminator
	 * @param thisToTerminator
	 *        to-terminator
	 * @param thisLineFlag
	 *        whether edge is rendered as line ('true' or 'false')
	 * @param thisHiddenFlag
	 *        whether edge is hidden ('true' or 'false')
	 * @return style
	 */
	@SuppressWarnings("boxing")
	static public Integer parseStyle(final String thisStroke, final String thisFromTerminator, final String thisToTerminator, final String thisLineFlag, final String thisHiddenFlag)
	{
		boolean isDefined = false;
		int thisStyle = 0;
		if (thisHiddenFlag != null && !thisHiddenFlag.isEmpty())
		{
			thisStyle |= (Boolean.valueOf(thisHiddenFlag).booleanValue() ? IEdge.HIDDEN : 0) | IEdge.HIDDENDEF;
			isDefined = true;
		}
		if (thisLineFlag != null && !thisLineFlag.isEmpty())
		{
			thisStyle |= (Boolean.valueOf(thisLineFlag).booleanValue() ? IEdge.LINE : 0) | IEdge.LINEDEF;
			isDefined = true;
		}
		if (thisStroke != null && !thisStroke.isEmpty())
		{
			thisStyle |= Utils.stringToStroke(thisStroke) | IEdge.STROKEDEF;
			thisStyle |= Utils.stringToStrokeWidth(thisStroke) | IEdge.STROKEWIDTHDEF;
			isDefined = true;
		}
		if (thisFromTerminator != null && !thisFromTerminator.isEmpty())
		{
			thisStyle |= Utils.stringToShape(thisFromTerminator) << IEdge.FROMSHIFT;
			thisStyle |= Utils.stringToFill(thisFromTerminator) << IEdge.FROMSHIFT;
			thisStyle |= IEdge.FROMDEF;
			isDefined = true;
		}
		if (thisToTerminator != null && !thisToTerminator.isEmpty())
		{
			thisStyle |= Utils.stringToShape(thisToTerminator) << IEdge.TOSHIFT;
			thisStyle |= Utils.stringToFill(thisToTerminator) << IEdge.TOSHIFT;
			thisStyle |= IEdge.TODEF;
			isDefined = true;
		}
		return !isDefined ? null : thisStyle;
	}

	/**
	 * Modify style
	 *
	 * @param thatStyle
	 *        current style
	 * @param thisValue
	 *        string value
	 * @param thisComponent
	 *        style component to modify
	 * @return modified style
	 */
	@SuppressWarnings("boxing")
	static public Integer modifyStyle(final Integer thatStyle, final Object thisValue, final StyleComponent thisComponent)
	{
		// System.out.println(">modifyStyle" + " oldvalue=" + (thatStyle == null ? "null" : Integer.toHexString(thatStyle)) + " value=" + thisValue + " "
		// + thisComponent.name());

		if (thatStyle == null && thisValue == null)
		{
			// System.out.println("<modifyStyle null");
			return null;
		}

		// initial value
		Integer thisStyle = thatStyle;
		if (thisStyle == null)
		{
			thisStyle = 0;
		}

		// change relevant bits
		switch (thisComponent)
		{
		case HIDDEN:
		{
			// clear
			thisStyle &= ~(IEdge.HIDDEN | IEdge.HIDDENDEF);

			// set
			final Boolean thisBooleanValue = (Boolean) thisValue;
			if (thisBooleanValue != null)
			{
				if (thisBooleanValue)
				{
					thisStyle |= IEdge.HIDDEN;
				}
				thisStyle |= IEdge.HIDDENDEF;
			}
			break;
		}

		case LINE:
		{
			// clear
			thisStyle &= ~(IEdge.LINE | IEdge.LINEDEF);

			// set
			final Boolean thisBooleanValue = (Boolean) thisValue;
			if (thisBooleanValue != null)
			{
				if (thisBooleanValue)
				{
					thisStyle |= IEdge.LINE;
				}
				thisStyle |= IEdge.LINEDEF;
			}
			break;
		}

		case STROKE:
		{
			// clear
			thisStyle &= ~(IEdge.STROKEMASK | IEdge.STROKEDEF);

			// set
			final String thisStringValue = (String) thisValue;
			if (thisStringValue != null && !thisStringValue.isEmpty())
			{
				thisStyle |= Utils.stringToStroke(thisStringValue);
				thisStyle |= IEdge.STROKEDEF;
			}
			break;
		}

		case STROKEWIDTH:
		{
			// clear
			thisStyle &= ~(IEdge.STROKEWIDTHMASK | IEdge.STROKEWIDTHDEF);

			// set
			final Integer thisIntValue = (Integer) thisValue;
			if (thisIntValue != null)
			{
				thisStyle |= thisIntValue << IEdge.STROKEWIDTHSHIFT;
				thisStyle |= IEdge.STROKEWIDTHDEF;
			}
			break;
		}

		case FROMTERMINATOR:
		{
			// clear
			thisStyle &= ~(IEdge.FROMMASK | IEdge.FROMDEF);

			// set
			final String thisStringValue = (String) thisValue;
			if (thisStringValue != null && !thisStringValue.isEmpty())
			{
				thisStyle |= Utils.stringToShape(thisStringValue) << IEdge.FROMSHIFT;
				thisStyle |= Utils.stringToFill(thisStringValue) << IEdge.FROMSHIFT;
				thisStyle |= IEdge.FROMDEF;
			}
			break;
		}

		case TOTERMINATOR:
		{
			// clear
			thisStyle &= ~(IEdge.TOMASK | IEdge.TODEF);

			// set
			final String thisStringValue = (String) thisValue;
			if (thisStringValue != null && !thisStringValue.isEmpty())
			{
				thisStyle |= Utils.stringToShape(thisStringValue) << IEdge.TOSHIFT;
				thisStyle |= Utils.stringToFill(thisStringValue) << IEdge.TOSHIFT;
				thisStyle |= IEdge.TODEF;
			}
			break;
		}
		default:
		{
			// System.out.println("<modifyStyle null");
			return null;
		}
		}
		// System.out.println("<modifyStyle " + Integer.toHexString(thisStyle));
		return thisStyle;
	}

	/**
	 * Stringify edge style component
	 *
	 * @param thisStyle
	 *        style
	 * @param thisComponent
	 *        part of style to stringify
	 * @return string
	 */
	@SuppressWarnings("boxing")
	static public String toString(final Integer thisStyle, final StyleComponent thisComponent)
	{
		if (thisStyle == null)
			return null;

		switch (thisComponent)
		{
		case HIDDEN:
		{
			if ((thisStyle & IEdge.HIDDENDEF) == 0)
				return null;
			return (thisStyle & IEdge.HIDDEN) != 0 ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		case LINE:
		{
			if ((thisStyle & IEdge.LINEDEF) == 0)
				return null;
			return (thisStyle & IEdge.LINE) != 0 ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		case STROKE:
		{
			if ((thisStyle & IEdge.STROKEDEF) == 0)
				return null;
			return Utils.strokeToString(thisStyle);
		}
		case STROKEWIDTH:
		{
			if ((thisStyle & IEdge.STROKEWIDTHDEF) == 0)
				return null;
			return Utils.strokeWidthToString(thisStyle);
		}
		case FROMTERMINATOR:
		{
			if ((thisStyle & IEdge.FROMDEF) == 0)
				return null;
			int n = thisStyle & IEdge.FROMMASK;
			n >>= IEdge.FROMSHIFT;
			return Utils.shapeToString(n) + Utils.fillToString(n);
		}
		case TOTERMINATOR:
		{
			if ((thisStyle & IEdge.TODEF) == 0)
				return null;
			int n = thisStyle & IEdge.TOMASK;
			n >>= IEdge.TOSHIFT;
			return Utils.shapeToString(n) + Utils.fillToString(n);
		}

		default:
			return null;
		}
	}

	/**
	 * Integer value from edge style component
	 *
	 * @param thisStyle
	 *        style
	 * @param thisComponent
	 *        part of style to stringify
	 * @return string
	 */
	@SuppressWarnings("boxing")
	static public Integer toInteger(final Integer thisStyle, final StyleComponent thisComponent)
	{
		if (thisStyle == null)
			return null;

		switch (thisComponent)
		{
		case STROKEWIDTH:
		{
			if ((thisStyle & IEdge.STROKEWIDTHDEF) == 0)
				return null;
			return Utils.strokeWidthToInteger(thisStyle);
		}
		default:
			return null;
		}
	}

	/**
	 * Stringify edge style component
	 *
	 * @param thisStyle
	 *        style
	 * @return hidden, stroke, fromterminator, toterminator strings
	 */
	static public String[] toStrings(final Integer thisStyle)
	{
		return new String[] { //
				Utils.toString(thisStyle, StyleComponent.HIDDEN), //
				Utils.toString(thisStyle, StyleComponent.LINE), //
				Utils.toString(thisStyle, StyleComponent.STROKE), //
				Utils.toString(thisStyle, StyleComponent.STROKEWIDTH), //
				Utils.toString(thisStyle, StyleComponent.FROMTERMINATOR), //
				Utils.toString(thisStyle, StyleComponent.TOTERMINATOR), };
	}

	// hidden

	/**
	 * Convert to boolean (true,null)
	 *
	 * @param thisStyle
	 *        style
	 * @param thisComponent
	 *        part of style to convert
	 * @return true or null
	 */
	@SuppressWarnings("boxing")
	static public Boolean toTrueBoolean(final Integer thisStyle, final StyleComponent thisComponent)
	{
		if (thisStyle == null)
			return null;

		switch (thisComponent)
		{
		case HIDDEN:
		{
			return (thisStyle & IEdge.HIDDEN) != 0 ? Boolean.TRUE : null;
		}
		case LINE:
		{
			return (thisStyle & IEdge.LINE) != 0 ? Boolean.TRUE : null;
		}
		default:
			return null;
		}
	}

	// stroke

	/**
	 * Convert string to stroke code
	 *
	 * @param thisString
	 *        stroke string
	 * @return stroke code style
	 */
	public static int stringToStroke(final String thisString)
	{
		int thisStyle = 0;
		if (thisString != null)
		{
			if (thisString.startsWith("solid")) //$NON-NLS-1$
			{
				thisStyle |= IEdge.SOLID;
			}
			else if (thisString.startsWith("dash")) //$NON-NLS-1$
			{
				thisStyle |= IEdge.DASH;
			}
			else if (thisString.startsWith("dot")) //$NON-NLS-1$
			{
				thisStyle |= IEdge.DOT;
			}
		}
		return thisStyle;
	}

	/**
	 * Convert string to stroke code
	 *
	 * @param thisString
	 *        stroke string
	 * @return stroke code style
	 */
	public static int stringToStrokeWidth(final String thisString)
	{
		int thisStyle = 0;
		if (thisString != null)
		{
			int i = thisString.lastIndexOf(' ');
			if (i != -1 && i < thisString.length())
			{
				try
				{
					int thisWidth = Integer.parseInt(thisString.substring(i + 1));
					thisStyle |= (thisWidth << IEdge.STROKEWIDTHSHIFT) & IEdge.STROKEWIDTHMASK;
				}
				catch (NumberFormatException e)
				{
					//
				}
			}
		}
		return thisStyle;
	}

	/**
	 * Convert stroke code to string
	 *
	 * @param thisStyle
	 *        stroke code style
	 * @return stroke string
	 */
	@SuppressWarnings("boxing")
	public static String strokeToString(final Integer thisStyle)
	{
		if (thisStyle == null)
			return NONE;
		final int n = thisStyle & IEdge.STROKEMASK;
		switch (n)
		{
		case IEdge.SOLID:
			return "solid"; //$NON-NLS-1$
		case IEdge.DASH:
			return "dash"; //$NON-NLS-1$
		case IEdge.DOT:
			return "dot"; //$NON-NLS-1$
		default:
			break;
		}
		return NONE;
	}

	/**
	 * Convert stroke width to string
	 *
	 * @param thisStyle
	 *        stroke code style
	 * @return stroke width string
	 */
	@SuppressWarnings("boxing")
	public static String strokeWidthToString(final Integer thisStyle)
	{
		if (thisStyle == null)
			return NONE;
		final int w = (thisStyle & IEdge.STROKEWIDTHMASK) >> IEdge.STROKEWIDTHSHIFT;
		if (w == 0)
			return NONE;
		return Integer.toString(w);
	}

	/**
	 * Convert stroke width to int
	 *
	 * @param thisStyle
	 *        stroke code style
	 * @return stroke witch
	 */
	@SuppressWarnings("boxing")
	public static Integer strokeWidthToInteger(final Integer thisStyle)
	{
		if (thisStyle == null)
			return null;
		final int w = (thisStyle & IEdge.STROKEWIDTHMASK) >> IEdge.STROKEWIDTHSHIFT;
		if (w == 0)
			return null;
		return w;
	}

	// fill

	/**
	 * Convert string to fill code style
	 *
	 * @param thisString
	 *        fill string
	 * @return fill code style
	 */
	public static int stringToFill(final String thisString)
	{
		if (thisString.length() > 1 && thisString.charAt(1) == 'f')
			return IEdge.FILL;
		return 0;
	}

	/**
	 * Convert string to fill code style
	 *
	 * @param thisStyle
	 *        fill code style
	 * @return fill string
	 */
	@SuppressWarnings("boxing")
	public static String fillToString(final Integer thisStyle)
	{
		if (thisStyle == null)
			return NONE;
		return (thisStyle & IEdge.FILL) != 0 ? "f" : NONE; //$NON-NLS-1$
	}

	// shape

	/**
	 * Convert string to shape code style
	 *
	 * @param thisString
	 *        shape string
	 * @return shape code style
	 */
	public static int stringToShape(final String thisString)
	{
		switch (thisString.charAt(0))
		{
		case 'a':
			return IEdge.ARROW;
		case 'h':
			return IEdge.HOOK;
		case 'c':
			return IEdge.CIRCLE;
		case 'd':
			return IEdge.DIAMOND;
		case 't':
			return IEdge.TRIANGLE;
		case 'z':
		default:
			return 0;
		}
	}

	/**
	 * Convert shape code style to string
	 *
	 * @param thisStyle
	 *        shape code style
	 * @return shape string
	 */
	@SuppressWarnings("boxing")
	public static String shapeToString(final Integer thisStyle)
	{
		if (thisStyle == null)
			return NONE;
		final int n = thisStyle & IEdge.SHAPEMASK;
		switch (n)
		{
		case IEdge.ARROW:
			return "a"; //$NON-NLS-1$
		case IEdge.HOOK:
			return "h"; //$NON-NLS-1$
		case IEdge.CIRCLE:
			return "c"; //$NON-NLS-1$
		case IEdge.DIAMOND:
			return "d"; //$NON-NLS-1$
		case IEdge.TRIANGLE:
			return "t"; //$NON-NLS-1$
		case 0:
			return "z"; //$NON-NLS-1$
		// return NONE;
		default:
			return "?"; //$NON-NLS-1$
		}
	}

	// M E N U I T E M

	/**
	 * Parse strings and set menuitem fields accordingly
	 *
	 * @param thisMenuItem
	 *        menuitem
	 * @param thisActionString
	 *        action string
	 * @param thisScopeString
	 *        scope string
	 * @param thisModeString
	 *        mode string
	 */
	static public void parseMenuItem(final MenuItem thisMenuItem, final String thisActionString, final String thisScopeString, final String thisModeString)
	{
		thisMenuItem.theAction = Utils.stringToAction(thisActionString);
		thisMenuItem.theMatchScope = Utils.stringToScope(thisScopeString);
		thisMenuItem.theMatchMode = Utils.stringToMode(thisModeString);
	}

	/**
	 * Parse menuitem action
	 *
	 * @param thisActionString
	 *        action string
	 * @return action
	 */
	static public Action stringToAction(final String thisActionString)
	{
		return thisActionString == null || thisActionString.isEmpty() ? null : Action.valueOf(thisActionString.toUpperCase());
	}

	/**
	 * Parse menuitem scope
	 *
	 * @param thisScopeString
	 *        scope string
	 * @return scope
	 */
	static public MatchScope stringToScope(final String thisScopeString)
	{
		return thisScopeString == null || thisScopeString.isEmpty() ? null : MatchScope.valueOf(thisScopeString.toUpperCase());
	}

	/**
	 * Parse menuitem mode
	 *
	 * @param thisModeString
	 *        mode string
	 * @return mode
	 */
	static public MatchMode stringToMode(final String thisModeString)
	{
		return thisModeString == null || thisModeString.isEmpty() ? null : MatchMode.valueOf(thisModeString.toUpperCase());
	}

	/**
	 * Stringify action
	 *
	 * @param thisAction
	 *        action
	 * @return action string
	 */
	static public String toString(final Action thisAction)
	{
		return thisAction == null ? null : thisAction.name();
	}

	/**
	 * Stringify scope
	 *
	 * @param thisScope
	 *        scope
	 * @return scope string
	 */
	static public String toString(final MatchScope thisScope)
	{
		return thisScope == null ? null : thisScope.name();
	}

	/**
	 * Stringify mode
	 *
	 * @param thisMode
	 *        mode
	 * @return mode string
	 */
	static public String toString(final MatchMode thisMode)
	{
		return thisMode == null ? null : thisMode.name();
	}

	/**
	 * Parse menuitem action, matchmode, matchscope fields to strings
	 *
	 * @param thisMenuItem
	 *        menuitem
	 * @return action, matchscope, matchmode strings
	 */
	static public String[] toStrings(final MenuItem thisMenuItem)
	{
		final String thisAction = Utils.toString(thisMenuItem.theAction);
		final String thisScope = Utils.toString(thisMenuItem.theMatchScope);
		final String thisMode = Utils.toString(thisMenuItem.theMatchMode);
		return new String[] { thisAction, thisScope, thisMode };
	}

	// C O L O R

	/**
	 * Convert color to hexadecimal string
	 *
	 * @param thisColor
	 *        color
	 * @return prefixless hexadecimal representation of color
	 */
	static public String colorToString(final Color thisColor)
	{
		return thisColor == null ? NONE : Integer.toHexString(thisColor.getRGB()).substring(2);
	}

	/**
	 * Convert hexadecimal string to color
	 *
	 * @param thatString
	 *        prefixless hexadecimal representation of color
	 * @return color
	 */
	static public Color stringToColor(final String thatString)
	{
		String thisString = thatString;
		if (thisString == null || thisString.isEmpty())
			return null;
		if (thisString.startsWith("#")) //$NON-NLS-1$
		{
			thisString = thisString.substring(1);
		}
		try
		{
			final Color thisColor = new Color();
			thisColor.parse(thisString);
			return thisColor;
		}
		catch (final Exception e)
		{
			// do nothing
		}
		return null;
	}

	/**
	 * Scaler to string
	 *
	 * @param thisScalerString
	 *        scaler string representation
	 * @return scaler
	 */
	public static float[] stringToFloats(final String thisScalerString)
	{
		final String[] thisScalerItem = thisScalerString.split("[\\s,;]+"); //$NON-NLS-1$
		final float[] thisScaler = new float[thisScalerItem.length];
		for (int i = 0; i < thisScalerItem.length; i++)
		{
			try
			{
				final float f = Float.parseFloat(thisScalerItem[i]);
				thisScaler[i] = f;
			}
			catch (final NumberFormatException e)
			{
				return null;
			}
		}
		return thisScaler;
	}

	/**
	 * Scaler to string
	 *
	 * @param thisScaler
	 *        scaler
	 * @return string representation
	 */
	public static String floatsToString(final float[] thisScaler)
	{
		final StringBuffer thisBuffer = new StringBuffer();
		boolean first = true;
		for (final float f : thisScaler)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				thisBuffer.append(',');
			}
			thisBuffer.append(Float.toString(f));
		}
		return thisBuffer.toString();
	}

	/**
	 * Load properties from URL
	 *
	 * @param thisUrl
	 *        url of property file to load from
	 * @throws IOException
	 */
	static public Properties load(final URL thisUrl) throws IOException
	{
		final Properties theseProperties = new Properties();
		theseProperties.load(thisUrl.openStream());
		return theseProperties;
	}
}
