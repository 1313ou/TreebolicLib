package treebolic.glue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Color treebolic.glue
 *
 * @author Bernard Bou
 */
public class Color implements treebolic.glue.iface.Color<Color>, Serializable
{
	private static final long serialVersionUID = 5704334480899935769L;

	public static final Color WHITE = new Color(android.graphics.Color.WHITE);

	public static final Color BLACK = new Color(android.graphics.Color.BLACK);

	public static final Color RED = new Color(android.graphics.Color.RED);

	public static final Color GREEN = new Color(android.graphics.Color.GREEN);

	public static final Color BLUE = new Color(android.graphics.Color.BLUE);

	public static final Color ORANGE = new Color(0xFFA500);

	public static final Color YELLOW = new Color(android.graphics.Color.YELLOW);

	public static final Color PINK = new Color(0xFFC0C0);

	public static final Color CYAN = new Color(android.graphics.Color.CYAN);

	public static final Color MAGENTA = new Color(android.graphics.Color.MAGENTA);

	public static final Color GRAY = new Color(android.graphics.Color.GRAY);

	public static final Color LIGHT_GRAY = new Color(android.graphics.Color.LTGRAY);

	public static final Color DARK_GRAY = new Color(android.graphics.Color.DKGRAY);

	/**
	 * Color value
	 */
	transient private int color;

	/**
	 * Whether color is null
	 */
	transient private boolean isNull;

	/**
	 * Constructor (null color)
	 */
	public Color()
	{
		this.color = 0;
		this.isNull = true;
	}

	/**
	 * Constructor
	 *
	 * @param r red
	 * @param g green
	 * @param b blue
	 */
	@SuppressWarnings("WeakerAccess")
	public Color(final int r, final int g, final int b)
	{
		this.color = android.graphics.Color.rgb(r, g, b);
		this.isNull = false;
	}

	/**
	 * Constructor
	 *
	 * @param rgb color
	 */
	public Color(final int rgb)
	{
		this.color = rgb;
		this.isNull = false;
	}

	@Override
	public void set(final int r, final int g, final int b)
	{
		this.color = r << 16 | g << 8 | b;
		this.isNull = false;
	}

	@Override
	public void set(final int rgb)
	{
		this.color = rgb;
		this.isNull = false;
	}

	/**
	 * Returns the red component in the range 0-255 in the default sRGB space.
	 *
	 * @return the red component.
	 */
	@SuppressWarnings("WeakerAccess")
	public int getRed()
	{
		return this.color >> 16 & 0xFF;
	}

	/**
	 * Returns the green component in the range 0-255 in the default sRGB space.
	 *
	 * @return the green component.
	 */
	@SuppressWarnings("WeakerAccess")
	public int getGreen()
	{
		return this.color >> 8 & 0xFF;
	}

	/**
	 * Returns the blue component in the range 0-255 in the default sRGB space.
	 *
	 * @return the blue component.
	 */
	@SuppressWarnings("WeakerAccess")
	public int getBlue()
	{
		return this.color & 0xFF;
	}

	/**
	 * Brighter/darker factor
	 */
	private static final float FACTOR = 0.85F;

	@Override
	public Color makeBrighter()
	{
		int r = getRed();
		int g = getGreen();
		int b = getBlue();

		final int i = (int) (1.0 / (1.0 - Color.FACTOR));
		if (r == 0 && g == 0 && b == 0)
		{
			return new Color(i, i, i);
		}
		if (r > 0 && r < i)
		{
			r = i;
		}
		if (g > 0 && g < i)
		{
			g = i;
		}
		if (b > 0 && b < i)
		{
			b = i;
		}

		return new Color(Math.min((int) (r / Color.FACTOR), 255), Math.min((int) (g / Color.FACTOR), 255), Math.min((int) (b / Color.FACTOR), 255));
	}

	@Override
	public Color makeDarker()
	{
		final int r = getRed();
		final int g = getGreen();
		final int b = getBlue();
		return new Color(Math.max((int) (r * Color.FACTOR), 0), Math.max((int) (g * Color.FACTOR), 0), Math.max((int) (b * Color.FACTOR), 0));
	}

	@Override
	public int getRGB()
	{
		return this.color;
	}

	public int getOpaqueRGB()
	{
		return 0xFF000000 | this.color;
	}

	@Override
	public void parse(final String string)
	{
		this.isNull = true;
		try
		{
			this.color = android.graphics.Color.parseColor("#" + string);
			this.isNull = false;
		}
		catch (final IllegalArgumentException e)
		{
			try
			{
				this.color = Integer.parseInt(string);
				this.isNull = false;
			}
			catch (final NumberFormatException e2)
			{
				try
				{
					this.color = Integer.parseInt(string, 16);
					this.isNull = false;
				}
				catch (final NumberFormatException e3)
				{
					throw new IllegalArgumentException("color:" + string);
				}
			}
		}
	}

	@Override
	public boolean isNull()
	{
		return this.isNull;
	}

	// O V E R R I D E S E R I A L I Z A T I O N

	/**
	 * Write object to serialization stream
	 *
	 * @param out serialization stream
	 * @throws IOException io exception
	 */
	@SuppressWarnings("boxing")
	private void writeObject(final ObjectOutputStream out) throws IOException
	{
		if (this.isNull)
		{
			out.writeObject(null);
		}
		else
		{
			out.writeObject(this.color);
		}
	}

	/**
	 * Read object from serialization stream
	 *
	 * @param in serialization stream
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	@SuppressWarnings("boxing")
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		final Integer value = (Integer) in.readObject();
		if (value == null)
		{
			this.color = 0;
			this.isNull = true;
		}
		else
		{
			this.color = value;
			this.isNull = false;
		}
	}
}
