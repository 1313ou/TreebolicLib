/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import treebolic.annotations.NonNull;

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
	static public void serializeZip(@NonNull final String archive, @NonNull @SuppressWarnings("SameParameterValue") final String entry, final Object object) throws IOException
	{
		@NonNull final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(archive, false));
		@NonNull final ZipEntry ze = new ZipEntry(entry);
		zos.putNextEntry(ze);
		@NonNull final ObjectOutput oos = new ObjectOutputStream(zos);
		oos.writeObject(object);
		zos.closeEntry();
		zos.close();
	}
}
