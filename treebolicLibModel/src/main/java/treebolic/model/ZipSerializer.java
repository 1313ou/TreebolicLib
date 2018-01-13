package treebolic.model;

import android.support.annotation.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZipSerializer
 *
 * @author Bernard Bou
 */
@SuppressWarnings("WeakerAccess")
public class ZipSerializer
{
	/**
	 * Append serialization to archive
	 *
	 * @param thisArchive archive
	 * @param thisName    name (will be the zipfile entry)
	 * @param thisObject  object to serialize
	 * @throws IOException io exception
	 */
	static public void serializeZip(@NonNull final String thisArchive, @SuppressWarnings("SameParameterValue") final String thisName, final Object thisObject) throws IOException
	{
		final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(thisArchive, false));
		final ZipEntry ze = new ZipEntry(thisName);
		zos.putNextEntry(ze);
		final ObjectOutputStream oos = new ObjectOutputStream(zos);
		oos.writeObject(thisObject);
		zos.closeEntry();
		zos.close();
	}
}
