/**
 * Assignment 1
 * Submitted by: 
 * Matan Chibotero. 	ID# 204076962
 * Liel Levi 	ID# 307983320
 * Yoni Amikam	ID# 311532774
 */

package base;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface compressor {
	abstract public void Compress(String[] input_names, String[] output_names)
			throws FileNotFoundException, IOException;

	abstract public void Decompress(String[] input_names, String[] output_names)
			throws FileNotFoundException, IOException;

	abstract public byte[] CompressWithArray(String[] input_names, String[] output_names)
			throws FileNotFoundException, IOException;

	abstract public byte[] DecompressWithArray(String[] input_names, String[] output_names)
			throws FileNotFoundException, IOException;
}