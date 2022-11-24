/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.model;

import java.io.IOException;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;

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
	private final String archive;

	/**
	 * Constructor
	 *
	 * @param archive archive file
	 */
	public ModelReader(final String archive)
	{
		this.archive = archive;
	}

	/**
	 * Deserialize
	 *
	 * @return model
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	@NonNull
	public Model deserialize() throws IOException, ClassNotFoundException
	{
		return (Model) ZipDeSerializer.deserializeZip(this.archive, "model");
	}

	/**
	 * Deserialize
	 *
	 * @return model
	 */
	@Nullable
	public Model deserializeGuarded()
	{
		try
		{
			return deserialize();
		}
		catch (@NonNull final ClassNotFoundException | IOException ignored)
		{
			//
		}
		return null;
	}
}
