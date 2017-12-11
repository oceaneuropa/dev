package org.orbit.os.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.orbit.os.api.Constants;
import org.orbit.os.api.apps.ProgramManifest;
import org.origin.common.io.IOUtil;
import org.origin.common.osgi.BundleUtil;

public class ProgramUtil {

	protected static boolean debug = true;

	// -----------------------------------------------------------------------------
	// For app archive file
	// -----------------------------------------------------------------------------

	/**
	 * 
	 * @param appArchivePath
	 * @return
	 */
	public static ProgramManifest extractAppManifest(Path appArchivePath) {
		ProgramManifest appManifest = null;

		InputStream fis = null;
		ZipInputStream zis = null;
		try {
			fis = Files.newInputStream(appArchivePath);
			zis = new ZipInputStream(fis);

			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				try {
					String fileName = zipEntry.getName();
					// System.out.println(fileName);
					if (Constants.APP_MANIFEST_FULLPATH.equals(fileName)) {
						appManifest = AppManifestUtil.loadManifes(zis);
						break;
					}
				} finally {
					zis.closeEntry();
				}
				zipEntry = zis.getNextEntry();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			IOUtil.closeQuietly(zis, true);
			IOUtil.closeQuietly(fis, true);
		}

		return appManifest;
	}

	/**
	 * Extract AppManifest model from an app archive file.
	 * 
	 * @param appArchiveFile
	 * @return
	 */
	public static ProgramManifest extractProgramManifest(File appArchiveFile) {
		ProgramManifest appManifest = null;

		FileInputStream fis = null;
		ZipInputStream zis = null;
		try {
			fis = new FileInputStream(appArchiveFile);
			zis = new ZipInputStream(fis);

			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				try {
					String fileName = zipEntry.getName();
					// System.out.println(fileName);
					if (Constants.APP_MANIFEST_FULLPATH.equals(fileName)) {
						appManifest = AppManifestUtil.loadManifes(zis);
						break;
					}
				} finally {
					zis.closeEntry();
				}
				zipEntry = zis.getNextEntry();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			IOUtil.closeQuietly(zis, true);
			IOUtil.closeQuietly(fis, true);
		}

		return appManifest;
	}

	/**
	 * Extract an app archive file into apps folder.
	 * 
	 * @param appsFolder
	 * @param appArchiveFile
	 * @return
	 * @throws IOException
	 */
	public static boolean extractToAppsFolder(File appsFolder, File appArchiveFile) throws IOException {
		ProgramManifest manifest = extractProgramManifest(appArchiveFile);
		if (manifest == null) {
			System.err.println("The '" + appArchiveFile.getName() + "' file does not contain manifest.json file.");
			return false;
		}

		String appId = manifest.getId();
		String appVersion = manifest.getVersion();
		if (appId == null || appId.isEmpty()) {
			System.err.println("App id is not specified in manifest.json.");
			return false;
		}
		if (appVersion == null || appVersion.isEmpty()) {
			System.err.println("App version is not specified in manifest.json.");
			return false;
		}

		// get app folder
		File appFolder = getAppFolder(appsFolder, appId, appVersion);
		return extractToAppFolder(appFolder, appArchiveFile);
	}

	/**
	 * Extract an app archive file into an app folder.
	 * 
	 * @param appsFolder
	 * @param appArchiveFile
	 * @return
	 * @throws IOException
	 */
	public static boolean extractToAppFolder(File appFolder, File appArchiveFile) throws IOException {
		if (!appFolder.exists()) {
			appFolder.mkdirs();
		}

		byte[] buffer = new byte[1024];

		// get the zip file content
		ZipInputStream zis = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(appArchiveFile);
			zis = new ZipInputStream(fis);

			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				String fileName = zipEntry.getName();

				boolean ignore = false;
				if (fileName.startsWith("__MACOSX")) {
					ignore = true;
				}

				if (ignore) {
					zis.closeEntry();
					zipEntry = zis.getNextEntry();
					continue;
				}

				try {
					File newFile = new File(appFolder + File.separator + fileName);
					if (zipEntry.isDirectory()) {
						if (!newFile.exists()) {
							newFile.mkdirs();
						}
						// if (debug) {
						// System.out.println(newFile.getAbsolutePath() + " directory is unzipped.");
						// }

					} else {
						FileOutputStream fos = null;
						try {
							if (!newFile.getParentFile().exists()) {
								newFile.getParentFile().mkdirs();
							}

							fos = new FileOutputStream(newFile);
							int len;
							while ((len = zis.read(buffer)) > 0) {
								fos.write(buffer, 0, len);
							}

							// if (debug) {
							// System.out.println(newFile.getAbsolutePath() + " file is unzipped.");
							// }

						} catch (IOException e) {
							if (debug) {
								System.err.println("Exception occurs when unzipping file '" + newFile.getAbsolutePath() + "': " + e.getMessage() + ".");
							}
							e.printStackTrace();
							throw e;

						} finally {
							IOUtil.closeQuietly(fos, true);
						}
					}

				} finally {
					zis.closeEntry();
				}

				zipEntry = zis.getNextEntry();
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;

		} finally {
			IOUtil.closeQuietly(zis, true);
			IOUtil.closeQuietly(fis, true);
		}

		return false;
	}

	/**
	 * 
	 * @param appsFolder
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	public static File getAppFolder(File appsFolder, String appId, String appVersion) {
		String appFolderName = deriveAppFolderName(appId, appVersion);
		return new File(appsFolder, appFolderName);
	}

	/**
	 * Derive app folder name from app id and app version.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	public static String deriveAppFolderName(String appId, String appVersion) {
		if (appId == null || appId.isEmpty()) {
			throw new RuntimeException("App id is empty.");
		}
		if (appVersion == null || appVersion.isEmpty()) {
			throw new RuntimeException("App version is empty.");
		}
		return appId + "_" + appVersion;
	}

	/**
	 * Derive app file name from app id and app version.
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	public static String deriveAppFileName(String appId, String appVersion) {
		if (appId == null || appId.isEmpty()) {
			throw new RuntimeException("App id is empty.");
		}
		if (appVersion == null || appVersion.isEmpty()) {
			throw new RuntimeException("App version is empty.");
		}
		return appId + "_" + appVersion + ".app";
	}

	// -----------------------------------------------------------------------------
	// For app folder
	// -----------------------------------------------------------------------------
	public static boolean isAppFolder(File appFolder) {
		ProgramManifest appManifest = AppManifestUtil.getManifestFromAppFolder(appFolder);
		return (appManifest != null) ? true : false;
	}

	/**
	 * 
	 * @param appFolder
	 * @return
	 */
	public static File[] getAppBundleFiles(File appFolder) {
		ProgramManifest appManifest = AppManifestUtil.getManifestFromAppFolder(appFolder);
		return getAppBundleFiles(appFolder, appManifest);
	}

	/**
	 * 
	 * @param appFolder
	 * @param appManifest
	 * @return
	 */
	public static File[] getAppBundleFiles(File appFolder, ProgramManifest appManifest) {
		List<File> bundleFiles = new ArrayList<File>();
		String[] classPaths = appManifest.getClassPaths();
		if (classPaths != null) {
			for (String classPath : classPaths) {
				File bundleFile = new File(appFolder, classPath);
				if (BundleUtil.isBundleFile(bundleFile)) {
					bundleFiles.add(bundleFile);
				}
			}
		}
		return bundleFiles.toArray(new File[bundleFiles.size()]);
	}

}
