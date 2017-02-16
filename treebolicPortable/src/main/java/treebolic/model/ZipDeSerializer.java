/**
 * ZipDeSerializer.java
 *
 * @author Bernard Bou
 */
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
public class ZipDeSerializer
{
	/**
	 * Deserialize from archive
	 *
	 * @param thisArchive archive name
	 * @param thisName    (will be the zipfile entry)
	 * @return deserialized object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	static public Object deserializeZip(final String thisArchive, final String thisName) throws IOException, ClassNotFoundException
	{
		ZipFile thisZipFile = null;
		InputStream thisInputStream = null;
		ObjectInputStream thisOutputStream = null;
		try
		{
			thisZipFile = new ZipFile(thisArchive);
			final ZipEntry thisZipEntry = thisZipFile.getEntry(thisName);
			if (thisZipEntry == null)
			{
				throw new IOException("zip entry not found " + thisName); //$NON-NLS-1$
			}

			thisInputStream = thisZipFile.getInputStream(thisZipEntry);
			thisOutputStream = new ObjectInputStream(thisInputStream);

			return thisOutputStream.readObject();
		}
		finally
		{
			if (thisOutputStream != null)
			{
				try
				{
					thisOutputStream.close();
				}
				catch (IOException ignored)
				{
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
				}
			}
		}
	}
}
