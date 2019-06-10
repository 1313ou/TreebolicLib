package treebolic.glue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
	 * @param bitmap0 android bitmap
	 */
	@SuppressWarnings("WeakerAccess")
	public Image(final Bitmap bitmap0)
	{
		this.bitmap = bitmap0;
	}

	/**
	 * Bitmap factory options
	 */
	@SuppressWarnings("WeakerAccess")
	static final Options options = new Options();

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
	 * @param resource resource URL
	 */
	static public Image make(@NonNull final URL resource) throws IOException
	{
		try (InputStream inputStream = resource.openStream())
		{
			// final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			final Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, Image.options);
			return new Image(bitmap);
		}
	}

	/**
	 * Make image from resource
	 *
	 * @param resource resource URL
	 */
	@Nullable
	static public Image try_make(@Nullable final URL resource)
	{
		if (resource != null)
		{
			try
			{
				return Image.make(resource);
			}
			catch (@NonNull final Exception ignored)
			{
			}
		}
		return null;
	}

	@Override
	public int getWidth()
	{
		if (this.bitmap == null)
		{
			return 0;
		}
		return this.bitmap.getWidth();
	}

	@Override
	public int getHeight()
	{
		if (this.bitmap == null)
		{
			return 0;
		}
		return this.bitmap.getHeight();
	}

	// O V E R R I D E S E R I A L I Z A T I O N

	/**
	 * Obtain byte array from image
	 *
	 * @return byte array
	 */
	@SuppressWarnings("WeakerAccess")
	public byte[] getByteArray()
	{
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		this.bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * Set image from byte array
	 *
	 * @param imageByteArray byte array
	 */
	@SuppressWarnings("WeakerAccess")
	public void setFromByteArray(@NonNull final byte[] imageByteArray)
	{
		final Options opt = new Options();
		opt.inDither = true;
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		this.bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length, opt);
	}

	/**
	 * Serialization override
	 *
	 * @param out serialized stream
	 * @throws IOException io exception
	 */
	private void writeObject(@NonNull final ObjectOutputStream out) throws IOException
	{
		final byte[] imageBytes = getByteArray();
		out.writeObject(imageBytes);
	}

	/**
	 * Deserialization override
	 *
	 * @param in serialized stream
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	private void readObject(@NonNull final ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		final byte[] imageBytes = (byte[]) in.readObject();
		setFromByteArray(imageBytes);
	}
}
