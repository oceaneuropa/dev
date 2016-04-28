package com.osgi.example1.util;

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
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DirUtil {

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
	public InputStream getZipInputStream(final File directory) throws IOException {
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

}
