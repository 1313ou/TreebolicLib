package treebolic.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ZipDeSerializer.java
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class ZipDeSerializer
{
	/**
	 * Deserialize from archive
	 *
	 * @param archive archive name
	 * @param entry   the zipfile entry
	 * @return deserialized object
	 * @throws IOException            io exception
	 * //@throws ClassNotFoundException class not found exception
	 */
	static public Object deserializeZip(final String archive, @SuppressWarnings("SameParameterValue") final String entry) throws IOException, ClassNotFoundException
	{
		ZipFile zipFile = null;
		InputStream inputStream = null;
		ObjectInputStream objectInputStream = null;
		try
		{
			zipFile = new ZipFile(archive);
			final ZipEntry zipEntry = zipFile.getEntry(entry);
			if (zipEntry == null)
			{
				throw new IOException("zip entry not found " + entry);
			}

			inputStream = zipFile.getInputStream(zipEntry);
			objectInputStream = new ObjectInputStream(inputStream);

			return objectInputStream.readObject();
		}
		finally
		{
			if (objectInputStream != null)
			{
				try
				{
					objectInputStream.close();
				}
				catch (IOException ignored)
				{
					//
				}
			}
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException ignored)
				{
					//
				}
			}
			if (zipFile != null)
			{
				try
				{
					zipFile.close();
				}
				catch (IOException ignored)
				{
					//
				}
			}
		}
	}
}
