/*
 * Copyright (c) 2022. Bernard Bou
 */

package treebolic.glue.iface.component;

/**
 * Function to convert character sequences into a string
 */
public interface Converter
{
	/**
	 * Convert array of character sequences to string
	 *
	 * @param strs character sequences
	 * @return string
	 */
	String convert(CharSequence[] strs);
}
