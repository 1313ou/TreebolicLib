package treebolic.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Analysis deserializer
 *
 * @author Bernard Bou
 */
public class ModelReader
{
	/**
	 * Archive file
	 */
	private final String theArchive;

	/**
	 * Constructor
	 *
	 * @param thisArchive archive file
	 */
	public ModelReader(final String thisArchive)
	{
		this.theArchive = thisArchive;
	}

	/**
	 * Deserialize
	 *
	 * @return model
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	@NonNull
	public Model deserialize() throws IOException, ClassNotFoundException
	{
		return (Model) ZipDeSerializer.deserializeZip(this.theArchive, "model");
	}

	/**
	 * Deserialize
	 *
	 * @return model
	 */
	@Nullable
	public Model deserializeGuarded()
	{
		try
		{
			return deserialize();
		}
		catch (@NonNull final ClassNotFoundException ignored)
		{
			//
		}
		catch (@NonNull final IOException ignored)
		{
			//
		}
		return null;
	}
}
