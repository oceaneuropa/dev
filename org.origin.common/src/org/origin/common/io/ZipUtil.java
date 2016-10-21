package org.origin.common.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

public class ZipUtil {

	// ------------------------------------------------------------------------------------
	// Zip
	// ------------------------------------------------------------------------------------
	/**
	 * Jar a directory on the fly and returns input stream of the jar.
	 * 
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	public static InputStream getZipInputStream(final File directory) throws IOException {
		final PipedOutputStream pos = new PipedOutputStream();
		final PipedInputStream pis = new PipedInputStream(pos);

		new Thread() {
			@Override
			public void run() {
				try {
					jarDir(directory, pos);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtil.closeQuietly(pos, true);
				}
			}
		}.start();

		return pis;
	}

	/**
	 * Jar up a directory to a zip file.
	 */
	public static File jarDir(File directory, File zipFile) throws IOException {
		jarDir(directory, new BufferedOutputStream(new FileOutputStream(zipFile)));
		return zipFile;
	}

	/**
	 * Jar up a directory to output stream.
	 * 
	 * @param directory
	 * @param os
	 * @throws IOException
	 */
	public static void jarDir(File directory, OutputStream os) throws IOException {
		// create a ZipOutputStream to zip the data to
		JarOutputStream jarOs = null;
		try {
			jarOs = new JarOutputStream(os);
			jarOs.setLevel(Deflater.NO_COMPRESSION);

			String path = "";
			File manifestFile = new File(directory, JarFile.MANIFEST_NAME);

			if (manifestFile.exists()) {
				byte[] readBuffer = new byte[8192];

				FileInputStream fis = new FileInputStream(manifestFile);
				try {
					ZipEntry anEntry = new ZipEntry(JarFile.MANIFEST_NAME);
					jarOs.putNextEntry(anEntry);

					int bytesIn = fis.read(readBuffer);
					while (bytesIn != -1) {
						jarOs.write(readBuffer, 0, bytesIn);
						bytesIn = fis.read(readBuffer);
					}

				} finally {
					IOUtil.closeQuietly(fis, true);
				}
				jarOs.closeEntry();
			}

			zipDir(directory, jarOs, path, Collections.singleton(JarFile.MANIFEST_NAME));

		} finally {
			IOUtil.closeQuietly(jarOs, true);
		}
	}

	/**
	 * Zip up a directory path
	 */
	protected static void zipDir(File directory, ZipOutputStream zos, String path, Set<String> exclusions) throws IOException {
		// get a listing of the directory content
		File[] files = directory.listFiles();
		assert files != null;

		byte[] readBuffer = new byte[8192];
		int bytesIn;

		// loop through and zip the files
		for (File file : files) {
			if (file.isDirectory()) {
				String prefix = path + file.getName() + "/";
				zos.putNextEntry(new ZipEntry(prefix));

				zipDir(file, zos, prefix, exclusions);
				continue;
			}

			String entry = path + file.getName();
			if (!exclusions.contains(entry)) {
				FileInputStream fis = new FileInputStream(file);
				try {
					ZipEntry anEntry = new ZipEntry(entry);
					zos.putNextEntry(anEntry);
					bytesIn = fis.read(readBuffer);
					while (bytesIn != -1) {
						zos.write(readBuffer, 0, bytesIn);
						bytesIn = fis.read(readBuffer);
					}
				} finally {
					IOUtil.closeQuietly(fis, true);
				}
			}
		}
	}

	// ------------------------------------------------------------------------------------
	// UnZip
	// ------------------------------------------------------------------------------------
	/**
	 * Given a File input it will unzip the file in a the unzip directory passed as the second parameter
	 * 
	 * @see org.apache.hadoop.fs.FileUtil
	 * 
	 * @param inFile
	 *            The zip file as input
	 * @param unzipDir
	 *            The unzip directory where to unzip the zip file.
	 * @throws IOException
	 */
	public static void unZip(File inFile, File unzipDir) throws IOException {
		ZipFile zipFile = new ZipFile(inFile);
		try {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (!entry.isDirectory()) {
					InputStream in = zipFile.getInputStream(entry);
					try {
						File file = new File(unzipDir, entry.getName());
						if (!file.getParentFile().mkdirs()) {
							if (!file.getParentFile().isDirectory()) {
								throw new IOException("Mkdirs failed to create " + file.getParentFile().toString());
							}
						}
						OutputStream out = new FileOutputStream(file);
						try {
							byte[] buffer = new byte[8192];
							int i;
							while ((i = in.read(buffer)) != -1) {
								out.write(buffer, 0, i);
							}
						} finally {
							out.close();
						}
					} finally {
						in.close();
					}
				}
			}
		} finally {
			zipFile.close();
		}
	}

	/**
	 * Given a Tar File as input it will untar the file in a the untar directory passed as the second parameter
	 * 
	 * This utility will untar ".tar" files and ".tar.gz","tgz" files.
	 * 
	 * @param inFile
	 *            The tar file as input.
	 * @param untarDir
	 *            The untar directory where to untar the tar file.
	 * @throws IOException
	 */
	public static void unTar(File inFile, File untarDir) throws IOException {
		if (!untarDir.mkdirs()) {
			if (!untarDir.isDirectory()) {
				throw new IOException("Mkdirs failed to create " + untarDir);
			}
		}
		boolean gzipped = inFile.toString().endsWith("gz");
		unTarUsingJava(inFile, untarDir, gzipped);
	}

	private static void unTarUsingJava(File inFile, File untarDir, boolean gzipped) throws IOException {
		InputStream inputStream = null;
		TarArchiveInputStream tis = null;
		try {
			if (gzipped) {
				inputStream = new BufferedInputStream(new GZIPInputStream(new FileInputStream(inFile)));
			} else {
				inputStream = new BufferedInputStream(new FileInputStream(inFile));
			}

			tis = new TarArchiveInputStream(inputStream);

			for (TarArchiveEntry entry = tis.getNextTarEntry(); entry != null;) {
				unpackEntries(tis, entry, untarDir);
				entry = tis.getNextTarEntry();
			}
		} finally {
			IOUtil.closeQuietly(true, tis, inputStream);
		}
	}

	private static void unpackEntries(TarArchiveInputStream tis, TarArchiveEntry entry, File outputDir) throws IOException {
		if (entry.isDirectory()) {
			File subDir = new File(outputDir, entry.getName());
			if (!subDir.mkdirs() && !subDir.isDirectory()) {
				throw new IOException("Mkdirs failed to create tar internal dir " + outputDir);
			}

			for (TarArchiveEntry e : entry.getDirectoryEntries()) {
				unpackEntries(tis, e, subDir);
			}

			return;
		}

		File outputFile = new File(outputDir, entry.getName());
		if (!outputFile.getParentFile().exists()) {
			if (!outputFile.getParentFile().mkdirs()) {
				throw new IOException("Mkdirs failed to create tar internal dir " + outputDir);
			}
		}

		int count;
		byte data[] = new byte[2048];
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

		while ((count = tis.read(data)) != -1) {
			outputStream.write(data, 0, count);
		}

		outputStream.flush();
		outputStream.close();
	}

}
