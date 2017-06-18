package org.orbit.fs.connector.cli;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.fs.api.FileRef;
import org.orbit.fs.api.FileSystem;
import org.orbit.fs.connector.FileSystemConfigurationImpl;
import org.orbit.fs.connector.FileSystemImpl;
import org.orbit.fs.connector.ws.FileSystemWSClientHelper;
import org.origin.common.io.ZipUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 *
 */
public class FileSystemCommand {

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> registration;
	protected FileSystem fs;

	/**
	 * 
	 * @param bundleContext
	 */
	public FileSystemCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("FileSystemCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "fs");
		props.put("osgi.command.function", new String[] { "fslogin", "fslroots", "fslfiles", "fsupload", "fsdownload", "fsdelete" });
		this.registration = bundleContext.registerService(FileSystemCommand.class.getName(), this, props);
	}

	public void stop() {
		System.out.println("FileSystemCommand.stop()");

		if (this.registration != null) {
			this.registration.unregister();
			this.registration = null;
		}
	}

	/**
	 * Login in file system.
	 * 
	 * @param url
	 *            e.g. http://127.0.0.1:9090
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	@Descriptor("Login file system")
	public void fslogin( //
			@Descriptor("url") @Parameter(absentValue = "", names = { "-url", "--url" }) String url, //
			@Descriptor("username") @Parameter(absentValue = "admin", names = { "-u", "--username" }) String username, //
			@Descriptor("password") @Parameter(absentValue = "", names = { "-p", "--password" }) String password //
	) throws Exception {
		FileSystemConfigurationImpl config = new FileSystemConfigurationImpl(url, username, password);
		this.fs = FileSystemImpl.newInstance(config);
		if (this.fs != null) {
			System.out.println("login to " + url + " successfully.");
		}
	}

	/**
	 * List root files in the file system.
	 * 
	 * @param recursive
	 * @throws Exception
	 */
	@Descriptor("List root files in the file system")
	public void fslroots( //
			// Options
			@Descriptor("Show files in sub-directories recursively") @Parameter(names = { "-r", "--recursive" }, absentValue = "false", presentValue = "true") boolean recursive //
	) throws Exception {
		if (fs == null) {
			printOutLoginMessage();
			return;
		}

		try {
			FileRef[] files = FileRef.listRoots(fs);
			if (recursive) {
				for (FileRef file : files) {
					FileSystemWSClientHelper.INSTANCE.walkFolders(fs, file, 0);
				}
			} else {
				for (FileRef file : files) {
					System.out.println(file.getName() + " (" + file.getPath() + ")");
					// System.out.println(file.getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * List files in the file system.
	 * 
	 * @param recursive
	 *            whether to list files in sub-directories recursively
	 * @param pathString
	 *            parent path in the file system
	 * @throws Exception
	 */
	@Descriptor("List files in the file system")
	public void fslfiles( //
			// Options
			@Descriptor("List files in sub-directories recursively") @Parameter(names = { "-r", "--recursive" }, absentValue = "false", presentValue = "true") boolean recursive, //
			// Parameters
			@Descriptor("Parent directory path") @Parameter(absentValue = "", names = { "-p", "--path" }) String pathString //
	) throws Exception {
		if (fs == null) {
			printOutLoginMessage();
			return;
		}

		if (pathString == null) {
			fslroots(recursive);
			return;
		}

		// FileRef fileRef = FileRefImpl.newInstance(fs, pathString);
		FileRef fileRef = fs.getFile(pathString);
		if (fileRef.path().isRoot()) {
			fslroots(recursive);
			return;
		}
		if (!fileRef.exists()) {
			System.out.println("Path '" + fileRef.getPath() + "' does not exist.");
			return;
		}

		if (!fileRef.isDirectory()) {
			System.out.println("Path '" + fileRef.getPath() + "' exists but is not a directory.");
			return;
		}

		try {
			FileRef[] files = FileRef.listFiles(fileRef);
			if (recursive) {
				for (FileRef file : files) {
					FileSystemWSClientHelper.INSTANCE.walkFolders(fs, file, 0);
				}
			} else {
				for (FileRef file : files) {
					System.out.println(file.getName() + " (" + file.getPath() + ")");
					// System.out.println(file.getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Upload file or directory from local file system to the file system.
	 * 
	 * fslogin -url http://127.0.0.1:9090
	 * 
	 * fslfiles -r -p 'Users'
	 * 
	 * fsdelete -p '/Users/oceaneuropa/Downloads/apache/myfolder.zip'
	 * 
	 * fsupload -s '/Users/oceaneuropa/Downloads/apache/myfolder' -d '/Users/oceaneuropa/Downloads/apache/' -z
	 * 
	 * fsdownload -s '/Users/oceaneuropa/Downloads/apache/myfolder.zip' -d '/Users/oceaneuropa/Downloads/test_target'
	 * 
	 * @param localPathString local file or directory path
	 * 
	 * @param fsPathString file system path
	 * 
	 * @param includeSourceDir
	 * 
	 * @throws Exception
	 */
	@Descriptor("Upload file or directory to the file system")
	public void fsupload( //
			// Parameters
			@Descriptor("Local path") @Parameter(absentValue = "", names = { "-s", "--src" }) String localPathString, //
			@Descriptor("File system path") @Parameter(absentValue = "", names = { "-d", "--dest" }) String fsPathString, //

			// Options
			@Descriptor("When uploading a directory, whether to include the source directory.") @Parameter(absentValue = "true", names = { "-includesourcedir", "--includesourcedir" }) boolean includeSourceDir, //
			@Descriptor("zip the file or dir") @Parameter(names = { "-z", "--zip" }, absentValue = "false", presentValue = "true") boolean zipIt //
	) throws Exception {
		if (fs == null) {
			printOutLoginMessage();
			return;
		}

		localPathString = normalizeLocalPath(localPathString);
		if (localPathString == null || localPathString.isEmpty()) {
			System.out.println("Source path cannot be empty.");
			return;
		}

		if (fsPathString == null || fsPathString.isEmpty()) {
			System.out.println("Target path cannot be empty.");
			return;
		}

		File localFile = new File(localPathString);
		// FileRef destFile = FileRefImpl.newInstance(fs, fsPathString);
		FileRef destFile = fs.getFile(fsPathString);

		if (!localFile.exists()) {
			System.out.println("Source '" + localFile.getAbsolutePath() + "' does not exist.");
			return;
		}

		if (destFile.path().isEmpty()) {
			System.out.println("Target path cannot be empty.");
			return;
		}

		boolean succeed = false;

		if (zipIt) {
			// zip the file or dir on the fly and upload the input stream of the zip
			InputStream input = ZipUtil.getZipInputStream(localFile);

			boolean isDestDir = false;
			if (destFile.path().isRoot() || destFile.isDirectory()) {
				isDestDir = true;
			} else if (!destFile.exists()) {
				// target path doesn't exist --- check last segment of the path to decide whether it is a dir or not.
				String lastSegment = destFile.path().getLastSegment();
				// if dest file name doesn't have file extension, the dest path is considered as dir.
				if (!lastSegment.contains(".")) {
					isDestDir = true;
				}
			}
			if (isDestDir) {
				String zipFileName = localFile.getName() + ".zip";
				// destFile = FileRefImpl.newInstance(fs, destFile, zipFileName);
				destFile = fs.getFile(destFile, zipFileName);
			}

			// upload input stream file to fs file
			succeed = fs.uploadInputStreamToFsFile(input, destFile);

			System.out.println("'" + localFile.getAbsolutePath() + "' is uploaded to '" + destFile.getPath() + "' " + (succeed ? "successfully" : "unsuccessfully"));

		} else {
			if (localFile.isFile()) {
				boolean uploadToDirectory = false;
				if (destFile.path().isRoot()) {
					uploadToDirectory = true;
				}
				if (!destFile.exists()) {
					// target path doesn't exist --- check last segment of the path to decide whether it is a dir or not.
					String lastSegment = destFile.path().getLastSegment();
					// Note that if source file name doesn't have file extension and source file name matches the dest path last segment, the dest
					// path is
					// considered as file.
					if (!lastSegment.contains(".") && !localFile.getName().equals(lastSegment)) {
						uploadToDirectory = true;
					}
				} else {
					if (destFile.isDirectory()) {
						uploadToDirectory = true;
					}
				}

				if (uploadToDirectory) {
					// upload local file to fs directory
					succeed = fs.uploadFileToFsDirectory(localFile, destFile);
				} else {
					// upload local file to fs file
					succeed = fs.uploadFileToFsFile(localFile, destFile);
				}
			} else {
				// fs dest path must be a directory
				if (destFile.exists() && !destFile.isDirectory()) {
					// upload local directory to fs file --- wrong
					System.out.println("Target path '" + destFile.getPath() + "' exists but is not a directory.");
					return;
				}

				// upload local directory to fs directory
				succeed = fs.uploadDirectoryToFsDirectory(localFile, destFile, includeSourceDir);
			}

			System.out.println("'" + localFile.getAbsolutePath() + "' is uploaded to '" + destFile.getPath() + "' " + (succeed ? "successfully" : "unsuccessfully"));
		}
	}

	/**
	 * Download file or directory from the file system to local file system.
	 * 
	 * @param fsPathString
	 * @throws Exception
	 */
	@Descriptor("Download file or directory from the file system to local file system")
	public void fsdownload( //
			// Parameters
			@Descriptor("File system path") @Parameter(absentValue = "", names = { "-s", "--src" }) String fsPathString, //
			@Descriptor("Local path") @Parameter(absentValue = "", names = { "-d", "--dest" }) String localPathString, //
			@Descriptor("When uploading a directory, whether to include the source directory.") @Parameter(absentValue = "true", names = { "-includesourcedir", "--includesourcedir" }) boolean includeSourceDir //
	) throws Exception {
		if (fsPathString == null || fsPathString.isEmpty()) {
			System.out.println("Source path cannot be empty.");
			return;
		}

		localPathString = normalizeLocalPath(localPathString);
		if (localPathString == null || localPathString.isEmpty()) {
			System.out.println("Target path cannot be empty.");
			return;
		}

		// FileRef fileRef = FileRefImpl.newInstance(fs, fsPathString);
		FileRef fileRef = fs.getFile(fsPathString);
		File localFile = new File(localPathString);

		if (!fileRef.exists()) {
			System.out.println("Source path '" + fileRef.getPath() + "' does not exist.");
			return;
		}

		boolean succeed = false;

		boolean downloadToFile = localFile.isFile();
		if (downloadToFile) {
			// download to local file
			if (fileRef.isDirectory()) {
				// download fs directory to local file --- wrong
				System.out.println("Source path '" + fileRef.getPath() + "' is a directory which cannot be downloaded to a file.");
				return;

			} else {
				// download fs file to local file
				succeed = fs.downloadFsFileToFile(fileRef, localFile);
			}
		} else {
			// download to local directory
			if (fileRef.isDirectory()) {
				// download fs directory to local directory
				succeed = fs.downloadFsDirectoryToDirectory(fileRef, localFile, includeSourceDir);
			} else {
				// download fs file to local directory
				succeed = fs.downloadFsFileToDirectory(fileRef, localFile);
			}
		}

		System.out.println("'" + fileRef.getPath() + "' is downloaded to '" + localFile.getAbsolutePath() + "' " + (succeed ? "successfully" : "unsuccessfully"));
	}

	/**
	 * Delete a file or a directory from the file system.
	 * 
	 * @param pathString
	 * @throws Exception
	 */
	public void fsdelete( //
			// Parameters
			@Descriptor("Parent directory path") @Parameter(absentValue = "", names = { "-p", "--path" }) String fsPathString //
	) throws Exception {
		FileRef fileRef = fs.getFile(fsPathString);
		if (fileRef.path().isRoot()) {
			System.out.println("Root path cannot be deleted.");
			return;
		}
		if (fileRef.path().isEmpty()) {
			System.out.println("Path cannot be empty.");
			return;
		}

		boolean succeed = fileRef.delete();
		System.out.println("Path '" + fileRef.getPath() + "' is deleted " + (succeed ? "successfully" : "unsuccessfully"));
	}

	/**
	 * Replace "\" in the local file path with "/".
	 * 
	 * @param localPathString
	 * @return
	 */
	protected String normalizeLocalPath(String localPathString) {
		if (localPathString != null) {
			if (localPathString.contains("\\")) {
				localPathString = localPathString.replaceAll("\\", "/");
			}
		}
		return localPathString;
	}

	protected void printOutLoginMessage() {
		System.out.println("Please login the file system first.");
		System.out.println("Usage: login -url <url> [-u <username>] [-p <password>]");
		System.out.println("For example:");
		System.out.println("login -url http://127.0.0.1:9090");
		System.out.println("login -url http://127.0.0.1:9090 -u admin -p 123");
	}

}
