package treebolic.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import treebolic.glue.Color;
import treebolic.model.MenuItem.Action;
import treebolic.model.Types.MatchMode;
import treebolic.model.Types.MatchScope;

/**
 * Attribute utilities
 *
 * @author Bernard Bou
 */
public class Utils
{
	// S T Y L E

	@SuppressWarnings("WeakerAccess")
	static public final String NONE = "";

	/**
	 * Style component pointer
	 */
	public enum StyleComponent
	{STROKE, STROKEWIDTH, FROMTERMINATOR, TOTERMINATOR, LINE, HIDDEN}

	// 0000 dddd ttttt tttt ffff ffff 0000 sssh

	/**
	 * Parse edge style
	 *
	 * @param stroke         stroke
	 * @param fromTerminator from-terminator
	 * @param toTerminator   to-terminator
	 * @param lineFlag       whether edge is rendered as line ('true' or 'false')
	 * @param hiddenFlag     whether edge is hidden ('true' or 'false')
	 * @return style
	 */
	@SuppressWarnings("boxing")
	static public Integer parseStyle(@Nullable final String stroke, @Nullable final String fromTerminator, @Nullable final String toTerminator, @Nullable final String lineFlag, @Nullable final String hiddenFlag)
	{
		boolean isDefined = false;
		int style = 0;
		if (hiddenFlag != null && !hiddenFlag.isEmpty())
		{
			style |= (Boolean.valueOf(hiddenFlag) ? IEdge.HIDDEN : 0) | IEdge.HIDDENDEF;
			isDefined = true;
		}
		if (lineFlag != null && !lineFlag.isEmpty())
		{
			style |= (Boolean.valueOf(lineFlag) ? IEdge.LINE : 0) | IEdge.LINEDEF;
			isDefined = true;
		}
		if (stroke != null && !stroke.isEmpty())
		{
			style |= Utils.stringToStroke(stroke) | IEdge.STROKEDEF;
			style |= Utils.stringToStrokeWidth(stroke) | IEdge.STROKEWIDTHDEF;
			isDefined = true;
		}
		if (fromTerminator != null && !fromTerminator.isEmpty())
		{
			style |= Utils.stringToShape(fromTerminator) << IEdge.FROMSHIFT;
			style |= Utils.stringToFill(fromTerminator) << IEdge.FROMSHIFT;
			style |= IEdge.FROMDEF;
			isDefined = true;
		}
		if (toTerminator != null && !toTerminator.isEmpty())
		{
			style |= Utils.stringToShape(toTerminator) << IEdge.TOSHIFT;
			style |= Utils.stringToFill(toTerminator) << IEdge.TOSHIFT;
			style |= IEdge.TODEF;
			isDefined = true;
		}
		return !isDefined ? null : style;
	}

	/**
	 * Modify style
	 *
	 * @param style0    current style
	 * @param value     string value
	 * @param component style component to modify
	 * @return modified style
	 */
	@Nullable
	@SuppressWarnings("boxing")
	static public Integer modifyStyle(@Nullable final Integer style0, @Nullable final Object value, @NonNull final StyleComponent component)
	{
		// System.out.println(">modifyStyle" + " oldvalue=" + (style0 == null ? "null" : Integer.toHexString(style0)) + " value=" + value + " " + component.name());

		if (style0 == null && value == null)
		{
			// System.out.println("<modifyStyle null");
			return null;
		}

		// initial value
		Integer style = style0;
		if (style == null)
		{
			style = 0;
		}

		// change relevant bits
		switch (component)
		{
			case HIDDEN:
			{
				// clear
				style &= ~(IEdge.HIDDEN | IEdge.HIDDENDEF);

				// set
				final Boolean booleanValue = (Boolean) value;
				if (booleanValue != null)
				{
					if (booleanValue)
					{
						style |= IEdge.HIDDEN;
					}
					style |= IEdge.HIDDENDEF;
				}
				break;
			}

			case LINE:
			{
				// clear
				style &= ~(IEdge.LINE | IEdge.LINEDEF);

				// set
				final Boolean booleanValue = (Boolean) value;
				if (booleanValue != null)
				{
					if (booleanValue)
					{
						style |= IEdge.LINE;
					}
					style |= IEdge.LINEDEF;
				}
				break;
			}

			case STROKE:
			{
				// clear
				style &= ~(IEdge.STROKEMASK | IEdge.STROKEDEF);

				// set
				final String stringValue = (String) value;
				if (stringValue != null && !stringValue.isEmpty())
				{
					style |= Utils.stringToStroke(stringValue);
					style |= IEdge.STROKEDEF;
				}
				break;
			}

			case STROKEWIDTH:
			{
				// clear
				style &= ~(IEdge.STROKEWIDTHMASK | IEdge.STROKEWIDTHDEF);

				// set
				final Integer intValue = (Integer) value;
				if (intValue != null)
				{
					style |= intValue << IEdge.STROKEWIDTHSHIFT;
					style |= IEdge.STROKEWIDTHDEF;
				}
				break;
			}

			case FROMTERMINATOR:
			{
				// clear
				style &= ~(IEdge.FROMMASK | IEdge.FROMDEF);

				// set
				final String stringValue = (String) value;
				if (stringValue != null && !stringValue.isEmpty())
				{
					style |= Utils.stringToShape(stringValue) << IEdge.FROMSHIFT;
					style |= Utils.stringToFill(stringValue) << IEdge.FROMSHIFT;
					style |= IEdge.FROMDEF;
				}
				break;
			}

			case TOTERMINATOR:
			{
				// clear
				style &= ~(IEdge.TOMASK | IEdge.TODEF);

				// set
				final String stringValue = (String) value;
				if (stringValue != null && !stringValue.isEmpty())
				{
					style |= Utils.stringToShape(stringValue) << IEdge.TOSHIFT;
					style |= Utils.stringToFill(stringValue) << IEdge.TOSHIFT;
					style |= IEdge.TODEF;
				}
				break;
			}
			default:
			{
				// System.out.println("<modifyStyle null");
				return null;
			}
		}
		// System.out.println("<modifyStyle " + Integer.toHexString(style));
		return style;
	}

	/**
	 * Stringify edge style component
	 *
	 * @param style     style
	 * @param component part of style to stringify
	 * @return string
	 */
	@Nullable
	@SuppressWarnings({"boxing", "WeakerAccess"})
	static public String toString(@Nullable final Integer style, @NonNull final StyleComponent component)
	{
		if (style == null)
		{
			return null;
		}

		switch (component)
		{
			case HIDDEN:
			{
				if ((style & IEdge.HIDDENDEF) == 0)
				{
					return null;
				}
				return (style & IEdge.HIDDEN) != 0 ? "true" : "false";
			}
			case LINE:
			{
				if ((style & IEdge.LINEDEF) == 0)
				{
					return null;
				}
				return (style & IEdge.LINE) != 0 ? "true" : "false";
			}
			case STROKE:
			{
				if ((style & IEdge.STROKEDEF) == 0)
				{
					return null;
				}
				return Utils.strokeToString(style);
			}
			case STROKEWIDTH:
			{
				if ((style & IEdge.STROKEWIDTHDEF) == 0)
				{
					return null;
				}
				return Utils.strokeWidthToString(style);
			}
			case FROMTERMINATOR:
			{
				if ((style & IEdge.FROMDEF) == 0)
				{
					return null;
				}
				int n = style & IEdge.FROMMASK;
				n >>= IEdge.FROMSHIFT;
				return Utils.shapeToString(n) + Utils.fillToString(n);
			}
			case TOTERMINATOR:
			{
				if ((style & IEdge.TODEF) == 0)
				{
					return null;
				}
				int n = style & IEdge.TOMASK;
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
	 * @param style     style
	 * @param component part of style to stringify
	 * @return string
	 */
	@Nullable
	static public Integer toInteger(@Nullable final Integer style, @NonNull final StyleComponent component)
	{
		if (style == null)
		{
			return null;
		}

		if (component == StyleComponent.STROKEWIDTH)
		{
			if ((style & IEdge.STROKEWIDTHDEF) == 0)
			{
				return null;
			}
			return Utils.strokeWidthToInteger(style);
		}
		return null;
	}

	/**
	 * Stringify edge style component
	 *
	 * @param style style
	 * @return hidden, stroke, fromterminator, toterminator strings
	 */
	static public String[] toStrings(final Integer style)
	{
		return new String[]{ //
				Utils.toString(style, StyleComponent.HIDDEN), //
				Utils.toString(style, StyleComponent.LINE), //
				Utils.toString(style, StyleComponent.STROKE), //
				Utils.toString(style, StyleComponent.STROKEWIDTH), //
				Utils.toString(style, StyleComponent.FROMTERMINATOR), //
				Utils.toString(style, StyleComponent.TOTERMINATOR),};
	}

	// hidden

	/**
	 * Convert to boolean (true,null)
	 *
	 * @param style     style
	 * @param component part of style to convert
	 * @return true or null
	 */
	@Nullable
	static public Boolean toTrueBoolean(@Nullable final Integer style, @NonNull final StyleComponent component)
	{
		if (style == null)
		{
			return null;
		}

		switch (component)
		{
			case HIDDEN:
			{
				return (style & IEdge.HIDDEN) != 0 ? Boolean.TRUE : null;
			}
			case LINE:
			{
				return (style & IEdge.LINE) != 0 ? Boolean.TRUE : null;
			}
			default:
				return null;
		}
	}

	// stroke

	/**
	 * Convert string to stroke code
	 *
	 * @param str stroke string
	 * @return stroke code style
	 */
	@SuppressWarnings("WeakerAccess")
	public static int stringToStroke(@Nullable final String str)
	{
		int style = 0;
		if (str != null)
		{
			if (str.startsWith("solid"))
			{
				style |= IEdge.SOLID;
			}
			else if (str.startsWith("dash"))
			{
				style |= IEdge.DASH;
			}
			else if (str.startsWith("dot"))
			{
				style |= IEdge.DOT;
			}
		}
		return style;
	}

	/**
	 * Convert string to stroke code
	 *
	 * @param str stroke string
	 * @return stroke code style
	 */
	@SuppressWarnings("WeakerAccess")
	public static int stringToStrokeWidth(@Nullable final String str)
	{
		int style = 0;
		if (str != null)
		{
			int i = str.lastIndexOf(' ');
			if (i != -1 && i < str.length())
			{
				try
				{
					int width = Integer.parseInt(str.substring(i + 1));
					style |= (width << IEdge.STROKEWIDTHSHIFT) & IEdge.STROKEWIDTHMASK;
				}
				catch (NumberFormatException ignored)
				{
					//
				}
			}
		}
		return style;
	}

	/**
	 * Convert stroke code to string
	 *
	 * @param style stroke code style
	 * @return stroke string
	 */
	@NonNull
	@SuppressWarnings({"WeakerAccess"})
	public static String strokeToString(@Nullable final Integer style)
	{
		if (style == null)
		{
			return NONE;
		}
		final int n = style & IEdge.STROKEMASK;
		switch (n)
		{
			case IEdge.SOLID:
				return "solid";
			case IEdge.DASH:
				return "dash";
			case IEdge.DOT:
				return "dot";
			default:
				break;
		}
		return NONE;
	}

	/**
	 * Convert stroke width to string
	 *
	 * @param style stroke code style
	 * @return stroke width string
	 */
	@SuppressWarnings({"WeakerAccess"})
	public static String strokeWidthToString(@Nullable final Integer style)
	{
		if (style == null)
		{
			return NONE;
		}
		final int w = (style & IEdge.STROKEWIDTHMASK) >> IEdge.STROKEWIDTHSHIFT;
		if (w == 0)
		{
			return NONE;
		}
		return Integer.toString(w);
	}

	/**
	 * Convert stroke width to int
	 *
	 * @param style stroke code style
	 * @return stroke witch
	 */
	@Nullable
	@SuppressWarnings({"boxing", "WeakerAccess"})
	public static Integer strokeWidthToInteger(@Nullable final Integer style)
	{
		if (style == null)
		{
			return null;
		}
		final int w = (style & IEdge.STROKEWIDTHMASK) >> IEdge.STROKEWIDTHSHIFT;
		if (w == 0)
		{
			return null;
		}
		return w;
	}

	// fill

	/**
	 * Convert string to fill code style
	 *
	 * @param str fill string
	 * @return fill code style
	 */
	@SuppressWarnings("WeakerAccess")
	public static int stringToFill(@NonNull final CharSequence str)
	{
		if (str.length() > 1 && str.charAt(1) == 'f')
		{
			return IEdge.FILL;
		}
		return 0;
	}

	/**
	 * Convert string to fill code style
	 *
	 * @param style fill code style
	 * @return fill string
	 */
	@NonNull
	@SuppressWarnings({"WeakerAccess"})
	public static String fillToString(@Nullable final Integer style)
	{
		if (style == null)
		{
			return NONE;
		}
		return (style & IEdge.FILL) != 0 ? "f" : NONE;
	}

	// shape

	/**
	 * Convert string to shape code style
	 *
	 * @param str shape string
	 * @return shape code style
	 */
	@SuppressWarnings("WeakerAccess")
	public static int stringToShape(@NonNull final CharSequence str)
	{
		switch (str.charAt(0))
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
	 * @param style shape code style
	 * @return shape string
	 */
	@NonNull
	@SuppressWarnings({"WeakerAccess"})
	public static String shapeToString(@Nullable final Integer style)
	{
		if (style == null)
		{
			return NONE;
		}
		final int n = style & IEdge.SHAPEMASK;
		switch (n)
		{
			case IEdge.ARROW:
				return "a";
			case IEdge.HOOK:
				return "h";
			case IEdge.CIRCLE:
				return "c";
			case IEdge.DIAMOND:
				return "d";
			case IEdge.TRIANGLE:
				return "t";
			case 0:
				return "z";
			// return NONE;
			default:
				return "?";
		}
	}

	// M E N U I T E M

	/**
	 * Parse strings and set menuitem fields accordingly
	 *
	 * @param menuItem     menuitem
	 * @param actionString action string
	 * @param scopeString  scope string
	 * @param modeString   mode string
	 */
	static public void parseMenuItem(@NonNull final MenuItem menuItem, final String actionString, final String scopeString, final String modeString)
	{
		menuItem.action = Utils.stringToAction(actionString);
		menuItem.matchScope = Utils.stringToScope(scopeString);
		menuItem.matchMode = Utils.stringToMode(modeString);
	}

	/**
	 * Parse menuitem action
	 *
	 * @param actionString action string
	 * @return action
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	static public Action stringToAction(@Nullable final String actionString)
	{
		return actionString == null || actionString.isEmpty() ? null : Action.valueOf(actionString.toUpperCase(Locale.ROOT));
	}

	/**
	 * Parse menuitem scope
	 *
	 * @param scopeString scope string
	 * @return scope
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	static public MatchScope stringToScope(@Nullable final String scopeString)
	{
		return scopeString == null || scopeString.isEmpty() ? null : MatchScope.valueOf(scopeString.toUpperCase(Locale.ROOT));
	}

	/**
	 * Parse menuitem mode
	 *
	 * @param modeString mode string
	 * @return mode
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	static public MatchMode stringToMode(@Nullable final String modeString)
	{
		return modeString == null || modeString.isEmpty() ? null : MatchMode.valueOf(modeString.toUpperCase(Locale.ROOT));
	}

	/**
	 * Stringify action
	 *
	 * @param action action
	 * @return action string
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	static public String toString(@Nullable final Action action)
	{
		return action == null ? null : action.name();
	}

	/**
	 * Stringify scope
	 *
	 * @param scope scope
	 * @return scope string
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	static public String toString(@Nullable final MatchScope scope)
	{
		return scope == null ? null : scope.name();
	}

	/**
	 * Stringify mode
	 *
	 * @param mode mode
	 * @return mode string
	 */
	@Nullable
	@SuppressWarnings("WeakerAccess")
	static public String toString(@Nullable final MatchMode mode)
	{
		return mode == null ? null : mode.name();
	}

	/**
	 * Parse menuitem action, matchmode, matchscope fields to strings
	 *
	 * @param menuItem menuitem
	 * @return action, matchscope, matchmode strings
	 */
	static public String[] toStrings(@NonNull final MenuItem menuItem)
	{
		final String action = Utils.toString(menuItem.action);
		final String scope = Utils.toString(menuItem.matchScope);
		final String mode = Utils.toString(menuItem.matchMode);
		return new String[]{action, scope, mode};
	}

	// C O L O R

	/**
	 * Convert color to hexadecimal string
	 *
	 * @param color color
	 * @return prefixless hexadecimal representation of color
	 */
	@NonNull
	static public String colorToString(@SuppressWarnings("TypeMayBeWeakened") @Nullable final Color color)
	{
		return color == null ? NONE : Integer.toHexString(color.getRGB()).substring(2);
	}

	/**
	 * Convert hexadecimal string to color
	 *
	 * @param str0 prefixless hexadecimal representation of color
	 * @return color
	 */
	static public Color stringToColor(final String str0)
	{
		String str = str0;
		if (str == null || str.isEmpty())
		{
			return null;
		}
		if (str.startsWith("#"))
		{
			str = str.substring(1);
		}
		try
		{
			final Color color = new Color();
			color.parse(str);
			return color;
		}
		catch (@NonNull final Exception ignored)
		{
			// do nothing
		}
		return null;
	}

	/**
	 * Scaler to string
	 *
	 * @param scalerString scaler string representation
	 * @return scaler
	 */
	public static float[] stringToFloats(@NonNull final String scalerString)
	{
		final String[] scalerItem = scalerString.split("[\\s,;]+");
		final float[] scaler = new float[scalerItem.length];
		for (int i = 0; i < scalerItem.length; i++)
		{
			try
			{
				final float f = Float.parseFloat(scalerItem[i]);
				scaler[i] = f;
			}
			catch (@NonNull final NumberFormatException ignored)
			{
				return null;
			}
		}
		return scaler;
	}

	/**
	 * Scaler to string
	 *
	 * @param scaler scaler
	 * @return string representation
	 */
	public static String floatsToString(@NonNull final float[] scaler)
	{
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (final float f : scaler)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(',');
			}
			sb.append(f);
		}
		return sb.toString();
	}

	/**
	 * Load properties from URL
	 *
	 * @param url url of property file to load from
	 * @throws IOException io exception
	 */
	@NonNull
	static public Properties load(@NonNull final URL url) throws IOException
	{
		final Properties properties = new Properties();
		properties.load(url.openStream());
		return properties;
	}
}
