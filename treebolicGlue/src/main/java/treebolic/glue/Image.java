package treebolic.glue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * Image
 *
 * @author Bernard Bou
 */
public class Image implements treebolic.glue.iface.Image, Serializable
{
	private static final long serialVersionUID = -6088374866767037559L;

	/**
	 * Bitmap (not serialized)
	 */
	transient public Bitmap bitmap;

	/**
	 * Constructor
	 *
	 * @param bitmap0
	 *            android bitmap
	 */
	public Image(final Bitmap bitmap0)
	{
		this.bitmap = bitmap0;
	}

	/**
	 * Bitmap factory options
	 */
	static Options options = new Options();
	static
	{
		// defaults:
		// inDither = false;
		// inScaled = true;
		// inPremultiplied = true;

		// do not prescale
		// scaling will occur on drawing and is location-dependent
		Image.options.inScaled = true;

		// options.inScaled = false;
		// options.inPreferQualityOverSpeed = true;
		// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	}

	/**
	 * Make image from resource
	 *
	 * @param resource
	 *            resource URL
	 */
	static public Image make(final URL resource) throws IOException
	{
		InputStream inputStream = null;
		try
		{
			inputStream = resource.openStream();
			// final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			final Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, Image.options);
			return new Image(bitmap);
		}
		finally
		{
			if (inputStream != null)
			{
				inputStream.close();
			}
		}
	}

	/**
	 * Make image from resource
	 *
	 * @param resource
	 *            resource URL
	 */
	static public Image try_make(final URL resource)
	{
		try
		{
			final Image image = Image.make(resource);
			return image;
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.Image#getWidth()
	 */
	@Override
	public int getWidth()
	{
		if (this.bitmap == null)
			return 0;
		return this.bitmap.getWidth();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see treebolic.glue.iface.Image#getHeight()
	 */
	@Override
	public int getHeight()
	{
		if (this.bitmap == null)
			return 0;
		return this.bitmap.getHeight();
	}

	// O V E R R I D E S E R I A L I Z A T I O N

	/**
	 * Obtain byte array from image
	 *
	 * @return byte array
	 * @throws IOException
	 */
	public byte[] getByteArray() throws IOException
	{
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		this.bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * Set image from byte array
	 *
	 * @param imageByteArray
	 *            byte array
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void setFromByteArray(final byte[] imageByteArray) throws IOException, ClassNotFoundException
	{
		final Options opt = new Options();
		opt.inDither = true;
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		this.bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length, opt);
	}

	/**
	 * Serialization override
	 *
	 * @param out
	 *            serialized stream
	 * @throws IOException
	 */
	private void writeObject(final ObjectOutputStream out) throws IOException
	{
		final byte[] imageBytes = getByteArray();
		out.writeObject(imageBytes);
	}

	/**
	 * Deserialization override
	 *
	 * @param in
	 *            serialized stream
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		final byte[] imageBytes = (byte[]) in.readObject();
		setFromByteArray(imageBytes);
	}
}
