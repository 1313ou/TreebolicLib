/*
 * Copyright (c) 2019-2023. Bernard Bou
 */

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Image
 *
 * @author Bernard Bou
 */
public class Image implements treebolic.glue.iface.Image, Serializable
{
	/**
	 * Bitmap (not serialized)
	 */
	@Nullable
	transient public Bitmap bitmap;

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
	 * Constructor
	 *
	 * @param bitmap0 android bitmap
	 */
	@SuppressWarnings("WeakerAccess")
	public Image(@Nullable final Bitmap bitmap0)
	{
		this.bitmap = bitmap0;
	}

	/**
	 * Make image from resource
	 *
	 * @param resource resource URL
	 */
	public Image(@Nullable final URL resource)
	{
		this(make(resource));
	}

	/**
	 * Make bitmap from resource
	 *
	 * @param resource resource URL
	 * @return bitmap or null
	 * @noinspection WeakerAccess
	 */
	@Nullable
	static public Bitmap make(@Nullable final URL resource)
	{
		if (resource != null)
		{
			try (InputStream inputStream = resource.openStream())
			{
				// return BitmapFactory.decodeStream(inputStream);
				return BitmapFactory.decodeStream(inputStream, null, Image.options);
			}
			catch (@NonNull final IOException ignored)
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
	@Nullable
	@SuppressWarnings("WeakerAccess")
	public byte[] getByteArray()
	{
		if (this.bitmap == null)
		{
			return null;
		}
		@NonNull final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		this.bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * Set image from byte array
	 *
	 * @param imageByteArray byte array
	 */
	@SuppressWarnings({"WeakerAccess"})
	public void setFromByteArray(@Nullable final byte[] imageByteArray)
	{
		@NonNull final Options opt = new Options();
		opt.inDither = true;
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		this.bitmap = imageByteArray == null ? null : BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length, opt);
	}

	/**
	 * Serialization override
	 *
	 * @param out serialized stream
	 * @throws IOException io exception
	 */
	private void writeObject(@NonNull final ObjectOutputStream out) throws IOException
	{
		@Nullable final byte[] imageBytes = getByteArray();
		out.writeObject(imageBytes);
	}

	/**
	 * Deserialization override
	 *
	 * @param in serialized stream
	 * @throws IOException io exception
	 *                     //@throws ClassNotFoundException class not found exception
	 */
	private void readObject(@NonNull final ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		final byte[] imageBytes = (byte[]) in.readObject();
		setFromByteArray(imageBytes);
	}
}
