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
	 * @param thisArchive archive name
	 * @param thisName    (will be the zipfile entry)
	 * @return deserialized object
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	static public Object deserializeZip(final String thisArchive, @SuppressWarnings("SameParameterValue") final String thisName) throws IOException, ClassNotFoundException
	{
		ZipFile thisZipFile = null;
		InputStream thisInputStream = null;
		ObjectInputStream thisObjectInputStream = null;
		try
		{
			thisZipFile = new ZipFile(thisArchive);
			final ZipEntry thisZipEntry = thisZipFile.getEntry(thisName);
			if (thisZipEntry == null)
			{
				throw new IOException("zip entry not found " + thisName);
			}

			thisInputStream = thisZipFile.getInputStream(thisZipEntry);
			thisObjectInputStream = new ObjectInputStream(thisInputStream);

			return thisObjectInputStream.readObject();
		}
		finally
		{
			if (thisObjectInputStream != null)
			{
				try
				{
					thisObjectInputStream.close();
				}
				catch (IOException ignored)
				{
					//
				}
			}
			if (thisInputStream != null)
			{
				try
				{
					thisInputStream.close();
				}
				catch (IOException ignored)
				{
					//
				}
			}
			if (thisZipFile != null)
			{
				try
				{
					thisZipFile.close();
				}
				catch (IOException ignored)
				{
					//
				}
			}
		}
	}
}
