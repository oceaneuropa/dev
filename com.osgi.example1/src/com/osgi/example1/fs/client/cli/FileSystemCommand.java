package com.osgi.example1.fs.client.cli;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.osgi.example1.fs.client.api.FileRef;
import com.osgi.example1.fs.client.api.FileSystem;
import com.osgi.example1.fs.client.api.FileSystemConfiguration;
import com.osgi.example1.fs.client.ws.FileSystemUtil;

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
		props.put("osgi.command.function", new String[] { "login", "lroots", "lfiles", "download", "upload", "del" });
		this.registration = bundleContext.registerService(FileSystemCommand.class.getName(), this, props);
	}

	public void stop() {
		System.out.println("FileSystemCommand.stop()");

		if (this.registration != null) {
			this.registration.unregister();
			this.registration = null;
		}
	}

	@Descriptor("Login file system")
	public void login( //
			@Descriptor("url") @Parameter(absentValue = "", names = { "-url", "--url" }) String url, //
			@Descriptor("username") @Parameter(absentValue = "admin", names = { "-u", "--username" }) String username, //
			@Descriptor("password") @Parameter(absentValue = "", names = { "-p", "--password" }) String password //
	) throws Exception {
		FileSystemConfiguration config = new FileSystemConfiguration(url, username, password);
		this.fs = FileSystem.newInstance(config);
		if (this.fs != null) {
			System.out.println("login to " + url + " successfully.");
		}
	}

	@Descriptor("List root files")
	public void lroots( //
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
					FileSystemUtil.walkFolders(fs, file, 0);
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

	@Descriptor("List files")
	public void lfiles( //
			// Options
			@Descriptor("Show files in sub-directories recursively") @Parameter(names = { "-r", "--recursive" }, absentValue = "false", presentValue = "true") boolean recursive, //
			// Parameters
			@Descriptor("Parent directory path") @Parameter(absentValue = "", names = { "-p", "--path" }) String pathString //
	) throws Exception {
		if (fs == null) {
			printOutLoginMessage();
			return;
		}

		if (pathString == null) {
			lroots(recursive);
			return;
		}

		FileRef fileRef = FileRef.newInstance(fs, pathString);
		if (fileRef.path().isRoot()) {
			lroots(recursive);
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
					FileSystemUtil.walkFolders(fs, file, 0);
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

	protected void printOutLoginMessage() {
		System.out.println("Please login the file system first.");
		System.out.println("Usage: login -url <url> [-u <username>] [-p <password>]");
		System.out.println("For example:");
		System.out.println("login -url http://127.0.0.1:9090");
		System.out.println("login -url http://127.0.0.1:9090 -u admin -p 123");
	}

}
