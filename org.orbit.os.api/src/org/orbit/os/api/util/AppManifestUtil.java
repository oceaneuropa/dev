package org.orbit.os.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.orbit.os.api.Constants;
import org.orbit.os.api.apps.ProgramManifest;
import org.origin.common.io.IOUtil;
import org.osgi.framework.Bundle;

public class AppManifestUtil {

	/**
	 * Load AppManifest from OSGi bundle of an application.
	 * 
	 * 
	 * @param bundle
	 * @return
	 */
	public static ProgramManifest getAppManifest(Bundle bundle) {
		ProgramManifest appManifest = null;
		if (bundle != null) {
			URL url = null;
			try {
				url = bundle.getResource(Constants.APP_MANIFEST_FULLPATH);

			} catch (Exception e) {
				System.err.println(AppManifestUtil.class.getSimpleName() + ".getAppManifest() " + e.getClass().getName() + "|" + e.getMessage());
			}

			if (url != null) {
				InputStream input = null;
				try {
					input = url.openStream();
					if (input != null) {
						appManifest = AppManifestUtil.loadManifes(input);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtil.closeQuietly(input, true);
				}
			}
		}
		return appManifest;
	}

	/**
	 * Load AppManifest from app folder.
	 * 
	 * @param appFolder
	 * @return
	 */
	public static ProgramManifest getManifestFromAppFolder(File appFolder) {
		ProgramManifest appManifest = null;
		File appManifestFile = new File(appFolder, Constants.APP_MANIFEST_FULLPATH);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(appManifestFile);
			appManifest = AppManifestUtil.loadManifes(fis);
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			IOUtil.closeQuietly(fis, true);
		}
		return appManifest;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static ProgramManifest loadManifes(File file) {
		ProgramManifest manifest = null;
		ManifestReader reader = new ManifestReader();
		try {
			manifest = reader.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return manifest;
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static ProgramManifest loadManifes(Path path) {
		ProgramManifest manifest = null;
		ManifestReader reader = new ManifestReader();
		InputStream input = null;
		try {
			input = Files.newInputStream(path);
			manifest = reader.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input, true);
		}
		return manifest;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static ProgramManifest loadManifes(InputStream input) {
		ProgramManifest manifest = null;
		ManifestReader reader = new ManifestReader();
		try {
			manifest = reader.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return manifest;
	}

	/**
	 * 
	 * @param manifest
	 * @param file
	 */
	public static void saveManifest(ProgramManifest manifest, File file) {
		ManifestWriter writer = new ManifestWriter();
		try {
			writer.write(manifest, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param manifest
	 * @param file
	 * @throws IOException
	 */
	public static void saveManifest(ProgramManifest manifest, Path manifestPath) throws IOException {
		Path parentPath = manifestPath.getParent();
		if (!Files.exists(parentPath)) {
			Files.createDirectories(parentPath);
		}

		Files.deleteIfExists(manifestPath);

		Files.createFile(manifestPath);

		ManifestWriter writer = new ManifestWriter();
		OutputStream output = null;
		try {
			output = Files.newOutputStream(manifestPath);
			writer.write(manifest, output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(output, true);
		}
	}

	/**
	 * 
	 * @param manifest
	 * @param ouput
	 */
	public static void saveManifest(ProgramManifest manifest, OutputStream ouput) {
		ManifestWriter writer = new ManifestWriter();
		try {
			writer.write(manifest, ouput);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
