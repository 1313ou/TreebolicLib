package treebolic.model;

import java.io.IOException;

/**
 * Analysis serializer
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class ModelWriter
{
	/**
	 * Archive file
	 */
	private final String archive;

	/**
	 * Constructor
	 *
	 * @param archive archive file
	 */
	public ModelWriter(final String archive)
	{
		this.archive = archive;
	}

	/**
	 * Serialize
	 *
	 * @param model model
	 * @throws IOException io exception
	 */
	public void serialize(final Model model) throws IOException
	{
		ZipSerializer.serializeZip(this.archive, "model", model);
	}
}
