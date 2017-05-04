package treebolic.model;

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
	public Model deserialize() throws IOException, ClassNotFoundException
	{
		return (Model) ZipDeSerializer.deserializeZip(this.theArchive, "model");
	}

	/**
	 * Deserialize
	 *
	 * @return model
	 */
	public Model deserializeGuarded()
	{
		//noinspection TryWithIdenticalCatches
		try
		{
			return deserialize();
		}
		catch (final ClassNotFoundException ignored)
		{
			//
		}
		catch (final IOException ignored)
		{
			//
		}
		return null;
	}
}
