package org.orbit.component.webconsole.servlet.tier2.appstore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.orbit.component.api.util.AppStoreUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.io.FileUtil;
import org.origin.common.servlet.MessageHelper;

/**
 * Upon receiving file upload submission, parses the request to read upload data and saves the file on disk.
 * 
 * @see org.apache.felix.webconsole.internal.core.BundlesServlet
 */
public class AppUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -6442023711681879436L;

	// location to store file uploaded
	protected static final String UPLOAD_DIRECTORY = "upload";

	// upload settings
	protected static final int MEMORY_THRESHOLD = 10 * 1024 * 1024; // 10MB
	protected static final int MAX_FILE_SIZE = 250 * 1024 * 1024; // 250MB
	protected static final int MAX_REQUEST_SIZE = 260 * 1024 * 1024; // 260MB

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		// String appStoreUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_APP_STORE_URL);
		String appsFolderPath = getServletConfig().getInitParameter(WebConstants.PUBLIC_WEB_CONSOLE_APPS_FOLDER);

		String message = "";

		// 1. Check multipart form
		boolean isMultipartForm = ServletFileUpload.isMultipartContent(request);
		if (!isMultipartForm) {
			message = MessageHelper.INSTANCE.add(message, "Error: Form must has enctype=multipart/form-data.");
		}

		if (isMultipartForm) {
			File appUploadDir = null;
			try {
				// 2. Prepare upload directory
				String platformHome = PlatformSDKActivator.getInstance().getPlatform().getHome();
				String uploadPath = platformHome + File.separator + UPLOAD_DIRECTORY;

				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				// 3. Configure upload settings
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(MEMORY_THRESHOLD); // sets memory threshold - beyond which files are stored in disk
				factory.setRepository(new File(System.getProperty("java.io.tmpdir"))); // sets temporary location to store files

				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setFileSizeMax(MAX_FILE_SIZE);
				upload.setSizeMax(MAX_REQUEST_SIZE);

				// 4. Parse request
				List<FileItem> fileItems = upload.parseRequest(request);
				if (fileItems == null) {
					fileItems = new ArrayList<FileItem>();
				}

				// 5. get id, appId, appVersion
				int id = -1;
				String appId = null;
				String appVersion = null;
				for (FileItem fileItem : fileItems) {
					if (fileItem.isFormField()) {
						String currFieldName = fileItem.getFieldName();
						String currFieldValue = fileItem.getString();
						if ("id".equals(currFieldName)) {
							try {
								id = Integer.parseInt(currFieldValue);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if ("appId".equals(currFieldName)) {
							appId = currFieldValue;
						} else if ("appVersion".equals(currFieldName)) {
							appVersion = currFieldValue;
						}
					}
				}
				if (id == -1) {
					message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
				}
				if (appId == null || appId.isEmpty()) {
					message = MessageHelper.INSTANCE.add(message, "'appId' parameter is not set.");
				}
				if (appVersion == null || appVersion.isEmpty()) {
					message = MessageHelper.INSTANCE.add(message, "'appVersion' parameter is not set.");
				}

				if (id != -1 && (appId != null && !appId.isEmpty()) && (appVersion != null && !appVersion.isEmpty())) {
					// 6. Prepare app upload directory
					appUploadDir = new File(uploadDir, appId + "_" + appVersion);
					if (!appUploadDir.exists()) {
						appUploadDir.mkdirs();
					}

					// 7. Upload file to local folder (of the web server)
					List<File> localFiles = new ArrayList<File>();
					for (FileItem fileItem : fileItems) {
						if (!fileItem.isFormField()) {
							String fileName = fileItem.getName();
							try {
								File localFile = new File(appUploadDir, fileName);
								fileItem.write(localFile);
								localFiles.add(localFile);

								// message = MessageHelper.INSTANCE.add(message, "File '" + fileName + "' is uploaded to web server.");

							} catch (Exception e) {
								message = MessageHelper.INSTANCE.add(message, "File '" + fileName + "' is not uploaded to web server. " + e.getMessage());
							}
						}
					}

					// 8. Unzip the app jar file. Copy files in the <app_jar>/WEB-INF folder to /WEB-INF/apps/<appId>_<appVersion>/
					if (appsFolderPath != null && !localFiles.isEmpty()) {
						File appsFolder = new File(appsFolderPath);
						if (appsFolder.isDirectory()) {
							provisionAppsFolder(appId, appVersion, appsFolder, localFiles, false);
						}
					}

					// 9. Upload file to app store
					String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);
					boolean succeed = AppStoreUtil.uploadAppFile(accessToken, id, appId, appVersion, localFiles);
					if (succeed) {
						message = MessageHelper.INSTANCE.add(message, "App file is uploaded to app store.");
					} else {
						message = MessageHelper.INSTANCE.add(message, "App file is not uploaded to app store.");
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();

			} finally {
				// 9. Clean up
				if (appUploadDir != null && appUploadDir.exists()) {
					try {
						// appDir.delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/appstore/apps");
	}

	/**
	 * e.g. http://localhost:8080/orbit/webconsole/public/apps/org.orbit.app.browser_1.0.0/images/browser_01_48x48.png
	 * 
	 * e.g. http://localhost:8080/orbit/webconsole/public/apps/org.orbit.app.browser_1.0.0/images/browser_01_64x64.png
	 * 
	 * e.g. http://localhost:8080/orbit/webconsole/public/apps/org.orbit.app.browser_1.0.0/images/browser_01_128x128.png
	 * 
	 * 
	 * @param appId
	 * @param appVersion
	 * @param appsFolder
	 * @param localFiles
	 * @param clean
	 * @throws IOException
	 * 
	 * @see com.google.gwt.user.tools.WebAppCreator
	 * @see https://stackoverflow.com/questions/36890005/zipentry-to-file
	 * @see https://www.tutorialspoint.com/how-to-delete-folder-and-sub-folders-using-java
	 * 
	 */
	protected void provisionAppsFolder(String appId, String appVersion, File appsFolder, List<File> localFiles, boolean clean) throws IOException {
		System.out.println(getClass().getSimpleName() + ".provisionAppsFolder()");
		System.out.println("    appId = " + appId);
		System.out.println("    appVersion = " + appVersion);
		System.out.println("    appsFolder = " + appsFolder.getAbsolutePath());

		File appFolder = new File(appsFolder, appId + "_" + appVersion);

		if (clean) {
			try {
				FileUtil.deleteDirectory(appFolder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!appFolder.exists()) {
			appFolder.mkdirs();
		}

		for (File localFile : localFiles) {
			ZipFile zipFile = null;
			try {
				zipFile = new ZipFile(localFile);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (zipFile != null) {
				Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
				while (zipEntries.hasMoreElements()) {
					ZipEntry zipEntry = zipEntries.nextElement();

					String entryName = zipEntry.getName();
					System.out.println("        ZipEntry: " + entryName);

					if (!entryName.equals("WEB-INF/") && entryName.startsWith("WEB-INF/")) {
						String filePath = entryName.substring("WEB-INF/".length());

						File currFile = new File(appFolder, filePath);

						if (zipEntry.isDirectory()) {
							if (!currFile.exists()) {
								currFile.mkdirs();
							}
							System.out.println("            unzip to folder: " + currFile.getAbsolutePath());

						} else {
							if (currFile.exists()) {
								currFile.delete();
							}

							InputStream inputStream = null;
							try {
								inputStream = zipFile.getInputStream(zipEntry);
								if (inputStream != null) {
									Files.copy(inputStream, currFile.toPath());
									System.out.println("            unzip to file: " + currFile.getAbsolutePath());
								}
							} finally {
								if (inputStream != null) {
									inputStream.close();
								}
							}
						}

					} else if ("META-INF/manifest.json".equals(entryName) || "META-INF/MANIFEST.MF".equals(entryName)) {
						String filePath = entryName.substring("META-INF/".length());

						File file = new File(appFolder, filePath);
						if (file.exists()) {
							file.delete();
						}

						InputStream inputStream = null;
						try {
							inputStream = zipFile.getInputStream(zipEntry);
							if (inputStream != null) {
								Files.copy(inputStream, file.toPath());
								System.out.println("            unzip to file: " + file.getAbsolutePath());
							}
						} finally {
							if (inputStream != null) {
								inputStream.close();
							}
						}
					}
				}
			}
		}
	}

}
