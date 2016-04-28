package com.osgi.example1.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;

public class FileUtil {

	// ------------------------------------------------------------------------------------
	// Copy
	// ------------------------------------------------------------------------------------
	/**
	 * Copy bytes from a File to an OutputStream.
	 *
	 * @param input
	 *            the File to read from
	 * @param output
	 *            the OutputStream to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException
	 *             if the input or output is null
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static long copyFileToOutputStream(File input, OutputStream output) throws IOException {
		return org.apache.commons.io.FileUtils.copyFile(input, output);
	}

	/**
	 * Copies a file to a new location preserving the file date.
	 * 
	 * This method copies the contents of the specified source file to the specified destination file. The directory holding the destination file is
	 * created if it does not exist. If the destination file exists, then this method will overwrite it.
	 * 
	 * Note: This method tries to preserve the file's last modified date/times.
	 *
	 * @param srcFile
	 *            an existing file to copy, must not be null
	 * @param destFile
	 *            the new file, must not be null
	 *
	 * @throws NullPointerException
	 *             if source or destination is null
	 * @throws IOException
	 *             if source or destination is invalid
	 * @throws IOException
	 *             if an IO error occurs during copying
	 * @throws IOException
	 *             if the output file length is not the same as the input file length after the copy completes
	 */
	public static void copyFileToFile(File srcFile, File destFile) throws IOException {
		org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
	}

	/**
	 * Copies bytes from an InputStream source to a file destination. The directories up to destination will be created if they don't already exist.
	 * destination will be overwritten if it already exists. The source stream is closed.
	 *
	 * @param source
	 *            the InputStream to copy bytes from, must not be null.
	 * @param destination
	 *            the non-directory File to write bytes to (possibly overwriting), must not be {@code null}
	 * @throws IOException
	 *             if destination is a directory
	 * @throws IOException
	 *             if destination cannot be written
	 * @throws IOException
	 *             if destination needs creating but can't be
	 * @throws IOException
	 *             if an IO error occurs during copying
	 */
	public static void copyInputStreamToFile(InputStream source, final File destination) throws IOException {
		// this method closes the InputStream (since 2.1)
		// org.apache.commons.io.FileUtils.copyInputStreamToFile(source, destination);

		// this method does not close the InputStream (since 2.5)
		org.apache.commons.io.FileUtils.copyToFile(source, destination);
	}

	/**
	 * Copies a file to a directory preserving the file date.
	 * 
	 * This method copies the contents of the specified source file to a file of the same name in the specified destination directory. The destination
	 * directory is created if it does not exist. If the destination file exists, then this method will overwrite it.
	 * 
	 * Note: This method tries to preserve the file's last modified date/times.
	 *
	 * @param srcFile
	 *            an existing file to copy, must not be null
	 * @param destDir
	 *            the directory to place the copy in, must not be null
	 *
	 * @throws NullPointerException
	 *             if source or destination is null
	 * @throws IOException
	 *             if source or destination is invalid
	 * @throws IOException
	 *             if an IO error occurs during copying
	 */
	public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
		org.apache.commons.io.FileUtils.copyFileToDirectory(srcFile, destDir);
	}

	/**
	 * Copies a directory to within another directory preserving the file dates.
	 * 
	 * This method copies the source directory and all its contents to a directory of the same name in the specified destination directory.
	 * 
	 * The destination directory is created if it does not exist. If the destination directory did exist, then this method merges the source with the
	 * destination, with the source taking precedence.
	 * 
	 * Note: This method tries to preserve the files' last modified date/times.
	 *
	 * @param srcDir
	 *            an existing directory to copy, must not be null
	 * @param destDir
	 *            the directory to place the copy in, must not be null
	 *
	 * @throws NullPointerException
	 *             if source or destination is null
	 * @throws IOException
	 *             if source or destination is invalid
	 * @throws IOException
	 *             if an IO error occurs during copying
	 */
	public static void copyDirectoryToDirectory(final File srcDir, final File destDir) throws IOException {
		org.apache.commons.io.FileUtils.copyDirectoryToDirectory(srcDir, destDir);
	}

	// ------------------------------------------------------------------------------------
	// Checksum
	// ------------------------------------------------------------------------------------
	/**
	 * Compute a checksum for the file or directory that consists of the name, length and the last modified date for a file and its children in case
	 * of a directory.
	 *
	 * @param file
	 *            the file or directory
	 * @return a checksum identifying any change
	 */
	public static long getChecksum(File file) {
		CRC32 crc = new CRC32();
		checksum(file, crc);
		return crc.getValue();
	}

	private static void checksum(File file, CRC32 crc) {
		crc.update(file.getName().getBytes());
		if (file.isFile()) {
			checksum(file.lastModified(), crc);
			checksum(file.length(), crc);

		} else if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children != null) {
				for (File aChildren : children) {
					checksum(aChildren, crc);
				}
			}
		}
	}

	private static void checksum(long l, CRC32 crc) {
		for (int i = 0; i < 8; i++) {
			crc.update((int) (l & 0x000000ff));
			l >>= 8;
		}
	}

	// ------------------------------------------------------------------------------------
	// Hash
	// ------------------------------------------------------------------------------------
	/**
	 * Get hash from a file.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String getHash(File file) throws IOException {
		if (file == null) {
			return null;
		}

		String hash = null;
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);
			if (fis != null) {
				hash = getHash(fis);
			}
		} finally {
			IOUtil.closeQuietly(fis, true);
		}

		if (hash == null) {
			hash = ""; //$NON-NLS-1$
		}
		return hash;
	}

	/**
	 * Get hash from bytes array.
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public String getHash(byte[] bytes) throws IOException {
		String hash = null;
		if (bytes != null) {
			ByteArrayInputStream bis = null;
			try {
				bis = new ByteArrayInputStream(bytes);
				hash = getHash(bis);
			} finally {
				IOUtil.closeQuietly(bis, true);
			}
		}
		if (hash == null) {
			hash = ""; //$NON-NLS-1$
		}
		return hash;
	}

	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public String getHash(InputStream is) throws IOException {
		return org.apache.commons.codec.digest.DigestUtils.shaHex(is);
	}

}
