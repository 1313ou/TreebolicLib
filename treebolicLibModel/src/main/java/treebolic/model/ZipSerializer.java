package treebolic.model;

import android.support.annotation.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
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
	 * @param archive archive
	 * @param entry   the zipfile entry
	 * @param object  object to serialize
	 * @throws IOException io exception
	 */
	static public void serializeZip(@NonNull final String archive, @SuppressWarnings("SameParameterValue") final String entry, final Object object) throws IOException
	{
		final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(archive, false));
		final ZipEntry ze = new ZipEntry(entry);
		zos.putNextEntry(ze);
		final ObjectOutput oos = new ObjectOutputStream(zos);
		oos.writeObject(object);
		zos.closeEntry();
		zos.close();
	}
}
