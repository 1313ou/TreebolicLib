package treebolic.model;

import java.io.IOException;

/**
 * Analysis serializer
 *
 * @author Bernard Bou
 */
public class ModelWriter
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
	public ModelWriter(final String thisArchive)
	{
		this.theArchive = thisArchive;
	}

	/**
	 * Serialize
	 *
	 * @param thisModel
	 *            analysis
	 * @throws IOException
	 */
	public void serialize(final Model thisModel) throws IOException
	{
		ZipSerializer.serializeZip(this.theArchive, "model", thisModel); //$NON-NLS-1$
	}
}
