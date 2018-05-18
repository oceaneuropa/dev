package org.origin.common.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;

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
	 * This method copies the contents of the specified source file to the specified destination file. The directory holding the destination file is created if
	 * it does not exist. If the destination file exists, then this method will overwrite it.
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
	 * Copies bytes array to a file destination. The directories up to destination will be created if they don't already exist. destination will be overwritten
	 * if it already exists. The source stream is closed.
	 * 
	 * @param bytes
	 * @param destination
	 * @throws IOException
	 */
	public static void copyBytesToFile(byte[] bytes, final File destination) throws IOException {
		InputStream is = null;
		try {
			is = IOUtil.toInputStream(bytes);
			if (is != null) {
				copyInputStreamToFile(is, destination);
			}
		} finally {
			IOUtil.closeQuietly(is, true);
		}
	}

	/**
	 * Copies bytes from an InputStream source to a file destination. The directories up to destination will be created if they don't already exist. destination
	 * will be overwritten if it already exists. The source stream is closed.
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
	 * This method copies the contents of the specified source file to a file of the same name in the specified destination directory. The destination directory
	 * is created if it does not exist. If the destination file exists, then this method will overwrite it.
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
	// Create tmp file. Replace file.
	// ------------------------------------------------------------------------------------
	/**
	 * Create a tmp file for a base file.
	 * 
	 * @see org.apache.hadoop.fs.FileUtil
	 * 
	 * @param basefile
	 *            the base file of the tmp
	 * @param prefix
	 *            file name prefix of tmp
	 * @param isDeleteOnExit
	 *            if true, the tmp will be deleted when the VM exits
	 * @return a newly created tmp file
	 * @exception IOException
	 *                If a tmp file cannot created
	 * @see java.io.File#createTempFile(String, String, File)
	 * @see java.io.File#deleteOnExit()
	 */
	public static File createLocalTempFile(final File basefile, final String prefix, final boolean isDeleteOnExit) throws IOException {
		File tmp = File.createTempFile(prefix + basefile.getName(), "", basefile.getParentFile());
		if (isDeleteOnExit) {
			tmp.deleteOnExit();
		}
		return tmp;
	}

	/**
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteDirectory(File dir) throws IOException {
		FileUtils.deleteDirectory(dir);
		return true;
	}

	/**
	 * Move the src file to the name specified by target.
	 * 
	 * @see org.apache.hadoop.fs.FileUtil
	 * 
	 * @param src
	 *            the source file
	 * @param target
	 *            the target file
	 * @exception IOException
	 *                If this operation fails
	 */
	public static void replaceFile(File src, File target) throws IOException {
		/*
		 * renameTo() has two limitations on Windows platform. src.renameTo(target) fails if 1) If target already exists OR 2) If target is already open for
		 * reading/writing.
		 */
		if (!src.renameTo(target)) {
			int retries = 5;
			while (target.exists() && !target.delete() && retries-- >= 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new IOException("replaceFile interrupted.");
				}
			}
			if (!src.renameTo(target)) {
				throw new IOException("Unable to rename " + src + " to " + target);
			}
		}
	}

	// ------------------------------------------------------------------------------------
	// Checksum
	// ------------------------------------------------------------------------------------
	/**
	 * Compute a checksum for the file or directory that consists of the name, length and the last modified date for a file and its children in case of a
	 * directory.
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

	/**
	 * Computes the checksum of a file using the CRC32 checksum routine. The value of the checksum is returned.
	 *
	 * @see org.apache.commons.io.FileUtils
	 * 
	 * @param file
	 *            the file to checksum, must not be {@code null}
	 * @return the checksum value
	 * @throws NullPointerException
	 *             if the file or checksum is {@code null}
	 * @throws IllegalArgumentException
	 *             if the file is a directory
	 * @throws IOException
	 *             if an IO error occurs reading the file
	 */
	public static long checksumCRC32(File file) throws IOException {
		final CRC32 crc = new CRC32();
		checksum(file, crc);
		return crc.getValue();
	}

	/**
	 * Computes the checksum of a file using the specified checksum object. Multiple files may be checked using one <code>Checksum</code> instance if desired
	 * simply by reusing the same checksum object. For example:
	 * 
	 * <pre>
	 * long csum = FileUtil.checksum(file, new CRC32()).getValue();
	 * </pre>
	 *
	 * @see org.apache.commons.io.FileUtils
	 *
	 * @param file
	 *            the file to checksum, must not be {@code null}
	 * @param checksum
	 *            the checksum object to be used, must not be {@code null}
	 * @return the checksum specified, updated with the content of the file
	 * @throws NullPointerException
	 *             if the file or checksum is {@code null}
	 * @throws IllegalArgumentException
	 *             if the file is a directory
	 * @throws IOException
	 *             if an IO error occurs reading the file
	 */
	public static Checksum checksum(final File file, final Checksum checksum) throws IOException {
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Checksums can't be computed on directories");
		}
		InputStream in = null;
		try {
			in = new CheckedInputStream(new FileInputStream(file), checksum);
			IOUtils.copy(in, new NullOutputStream());
		} finally {
			IOUtils.closeQuietly(in);
		}
		return checksum;
	}

	// ------------------------------------------------------------------------------------
	// Write to file
	// ------------------------------------------------------------------------------------
	/**
	 * 
	 * @param file
	 * @param lines
	 * @param append
	 * @throws IOException
	 */
	public static void writeLines(File file, Collection<?> lines, boolean append) throws IOException {
		org.apache.commons.io.FileUtils.writeLines(file, lines, append);
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
	public static String getHash(File file) throws IOException {
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
	public static String getHash(byte[] bytes) throws IOException {
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
	public static String getHash(InputStream is) throws IOException {
		return org.apache.commons.codec.digest.DigestUtils.shaHex(is);
	}

	/**
	 * Gets the extension of a filename.
	 * <p>
	 * This method returns the textual part of the filename after the last dot. There must be no directory separator after the dot.
	 * 
	 * <pre>
	 * foo.txt      --&gt; "txt"
	 * a/b/c.jpg    --&gt; "jpg"
	 * a/b.txt/c    --&gt; ""
	 * a/b/c        --&gt; ""
	 * </pre>
	 * <p>
	 * The output will be the same irrespective of the machine that the code is running on.
	 *
	 * @param filename
	 *            the filename to retrieve the extension of.
	 * @return the extension of the file or an empty string if none exists or {@code null} if the filename is {@code null}.
	 */
	public static String getExtension(String filename) {
		return FilenameUtils.getExtension(filename);
	}

	/**
	 * Gets the name minus the path from a full filename.
	 * <p>
	 * This method will handle a file in either Unix or Windows format. The text after the last forward or backslash is returned.
	 * 
	 * <pre>
	 * a/b/c.txt --&gt; c.txt
	 * a.txt     --&gt; a.txt
	 * a/b/c     --&gt; c
	 * a/b/c/    --&gt; ""
	 * </pre>
	 * <p>
	 * The output will be the same irrespective of the machine that the code is running on.
	 *
	 * @param filename
	 *            the filename to query, null returns null
	 * @return the name of the file without the path, or an empty string if none exists. Null bytes inside string will be removed
	 */
	public static String getName(String filename) {
		return FilenameUtils.getName(filename);
	}

}
