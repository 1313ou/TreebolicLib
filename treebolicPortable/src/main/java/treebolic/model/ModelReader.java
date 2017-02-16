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
	 * @param thisArchive
	 *            archive file
	 */
	public ModelReader(final String thisArchive)
	{
		this.theArchive = thisArchive;
	}

	/**
	 * Deserialize
	 *
	 * @return model
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Model deserialize() throws IOException, ClassNotFoundException
	{
		final Model thisModel = (Model) ZipDeSerializer.deserializeZip(this.theArchive, "model"); //$NON-NLS-1$
		return thisModel;
	}

	/**
	 * Deserialize
	 *
	 * @return model
	 */
	public Model deserializeGuarded()
	{
		try
		{
			return deserialize();
		}
		catch (final ClassNotFoundException e)
		{
			//
		}
		catch (final IOException e)
		{
			//
		}
		return null;
	}
}
